package co.nz.bookpublish.ds;

import static co.nz.bookpublish.data.predicates.BookPredicates.findByBookTitle;
import static co.nz.bookpublish.data.predicates.PublishTransPredicates.findByProcessExecutionIds;
import static co.nz.bookpublish.data.predicates.PublishTransPredicates.findByPublishTransNo;
import static co.nz.bookpublish.data.predicates.WorkflowPredicates.findByProcessDefinitionKeyAndCategory;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.IteratorUtils;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.nz.bookpublish.DuplicatedException;
import co.nz.bookpublish.NotFoundException;
import co.nz.bookpublish.converter.BookConverter;
import co.nz.bookpublish.converter.PublishTransConverter;
import co.nz.bookpublish.converter.PublishTransConverter.PublishTransLoadStrategies;
import co.nz.bookpublish.converter.ReviewRecordConverter;
import co.nz.bookpublish.converter.UserConverter;
import co.nz.bookpublish.data.BookDto;
import co.nz.bookpublish.data.BookModel;
import co.nz.bookpublish.data.ProcessActivityDto;
import co.nz.bookpublish.data.PublishReviewerDto;
import co.nz.bookpublish.data.PublishReviewerModel;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.PublishTransModel;
import co.nz.bookpublish.data.PublishTransModel.PublishStatus;
import co.nz.bookpublish.data.ReviewRecordDto;
import co.nz.bookpublish.data.ReviewRecordModel;
import co.nz.bookpublish.data.UserDto;
import co.nz.bookpublish.data.UserModel;
import co.nz.bookpublish.data.WorkflowModel;
import co.nz.bookpublish.data.repository.BookRepository;
import co.nz.bookpublish.data.repository.PublishReviewerRepository;
import co.nz.bookpublish.data.repository.PublishTransRepository;
import co.nz.bookpublish.data.repository.ReviewRecordRepository;
import co.nz.bookpublish.data.repository.UserRepository;
import co.nz.bookpublish.data.repository.WorkflowRepository;
import co.nz.bookpublish.ds.PublishTransSupport.PendingActivityBuildOperation;
import co.nz.bookpublish.wf.ActivitiFacade;
@Service
public class PublishTransDSImpl implements PublishTransDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishTransDSImpl.class);

	@Resource
	private PublishTransSupport publishTransSupport;

	@Resource
	private ActivitiFacade activitiFacade;

	@Resource
	private PublishTransConverter publishTransConverter;

	@Resource
	private UserConverter userConverter;

	@Resource
	private BookConverter bookConverter;

	@Resource
	private UserRepository userRepository;

	@Resource
	private BookRepository bookRepository;

	@Resource
	private PublishReviewerRepository publishReviewerRepository;

	@Resource
	private PublishTransRepository publishTransRepository;

	@Resource
	private ReviewRecordConverter reviewRecordConverter;

	@Resource
	private ReviewRecordRepository reviewRecordRepository;

	@Resource
	private TaskService taskService;

	@Resource
	private WorkflowRepository workflowRepository;

	public static final String PROCESS_DEFINITION_KEY = "publishBookTransMainflow";
	public static final String CATEGORY = "bookPublish";
	public static final String PUBLISH_TRANSACTION = "publishTrans";

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishTransDto startPublishProcess(UserDto operator)
			throws Exception {
		LOGGER.info("startPublishProcess start:{}", operator);
		PublishTransDto publishTrans = null;
		PublishTransModel publishTransModel = null;
		// get associated process definition
		WorkflowModel foundWorkflowModel = workflowRepository
				.findOne(findByProcessDefinitionKeyAndCategory(
						PROCESS_DEFINITION_KEY, CATEGORY));
		if (foundWorkflowModel == null) {
			throw new NotFoundException(
					"Process definition not found by processDefinitionKey["
							+ PROCESS_DEFINITION_KEY + "]");
		}

		// built PublishTrans object for process and persist it
		String processDefinitionId = foundWorkflowModel
				.getProcessDefinitionId();
		String publishTransNo = UUID.randomUUID().toString();

		publishTransModel = PublishTransModel.getBuilder(publishTransNo,
				processDefinitionId, new Date()).build();

		UserModel userModel = userRepository.findOne(operator.getUserId());
		if (userModel == null) {
			throw new NotFoundException("Operator not found");
		}
		publishTransModel.setOperator(userModel);
		ProcessInstance processInstance = activitiFacade.startProcessInstance(
				publishTransNo, processDefinitionId, null);
		publishTransModel.setActiviteProcessInstanceId(processInstance.getId());
		publishTransModel.setMainProcessInstanceId(processInstance.getId());
		publishTransModel.setStatus(PublishStatus.dataEntry.value());
		publishTransModel = publishTransRepository.save(publishTransModel);

		publishTrans = publishTransConverter.toDto(publishTransModel,
				PublishTransLoadStrategies.ALL);

		// get PublishTransModel as variable from process when it is paused
		publishTransSupport.postProcessForFlow(publishTrans, publishTransModel,
				operator.getUsername(), PendingActivityBuildOperation.ALL);

		LOGGER.info("startPublishProcess end:{}", publishTrans);
		return publishTrans;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishTransDto dataEntry(String publishTransNo, BookDto book,
			UserDto operator) throws Exception {
		LOGGER.info("dataEntry start:{}", publishTransNo);
		PublishTransDto publishTransDto = null;
		String username = operator.getUsername();
		Map<String, Object> variableMap = null;

		// Check book if already existed, published or undering process
		BookModel bookModel = bookRepository.findOne(findByBookTitle(book
				.getTitle()));
		if (bookModel != null) {
			throw new DuplicatedException("book[" + book.getTitle()
					+ "] already published or is under publish process.");
		}
		bookModel = bookConverter.toModel(book);
		bookModel = bookRepository.save(bookModel);
		PublishTransModel publishTransModel = publishTransRepository
				.findOne(findByPublishTransNo(publishTransNo));
		if (publishTransModel == null) {
			throw new NotFoundException(
					"PublishTrans not found by publishTransNo["
							+ publishTransNo + "]");
		}
		publishTransModel.setBook(bookModel);
		publishTransDto = publishTransConverter.toDto(publishTransModel,
				PublishTransLoadStrategies.ALL);

		// dataEntry don't need taskowner, but operator to process it,
		// operator had already been assigned thru executionListener
		// see@{PublishBookTransitionListener}
		Task pendingTask = activitiFacade.getActiviteTask(publishTransNo,
				publishTransModel.getActiviteProcessDefinitionId());
		if (pendingTask == null) {
			throw new NotFoundException("dataEntry task not found");
		}
		String taskName = pendingTask.getName();
		if (!activitiFacade.checkIfUserHasRightForGivenTask(publishTransNo,
				taskName, username)) {
			throw new Exception("User["
					+ publishTransModel.getOperator().getUsername()
					+ "] has no right to process " + taskName + "");
		}

		// complete task
		variableMap = new HashMap<String, Object>();
		variableMap.put(PUBLISH_TRANSACTION, publishTransDto);
		taskService.complete(pendingTask.getId(), variableMap);

		// as per process flow, after dataentry, it could be rejected by
		// autoreview
		// or it go to manual review or manual decision
		publishTransDto = publishTransSupport.postProcessForFlow(publishTransDto,
				publishTransModel, operator.getUsername(),
				PendingActivityBuildOperation.ALL);

		LOGGER.info("dataEntry end:{}", publishTransDto);
		return publishTransDto;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishTransDto claimReviewTask(String publishTransNo,
			UserDto currentLoginUser) throws Exception {
		LOGGER.info("claimReviewTask start:{}", publishTransNo);
		PublishTransDto publishTransDto = null;
		String activeProcessDefinitionId = null;
		PublishTransModel publishTransModel = publishTransRepository
				.findOne(findByPublishTransNo(publishTransNo));
		if (publishTransModel == null) {
			throw new NotFoundException(
					"PublishTrans not found by publishTransNo["
							+ publishTransNo + "]");
		}
		activeProcessDefinitionId = publishTransModel
				.getActiviteProcessDefinitionId();

		Task task = activitiFacade.getActiviteTask(publishTransNo,
				activeProcessDefinitionId);
		if (task == null || !task.getName().equals("Manual Review")) {
			throw new NotFoundException("Task[Manual Review] not found");
		}
		String taskId = task.getId();
		String taskName = task.getName();
		LOGGER.info("taskId:{}", taskId);
		LOGGER.info("taskName:{}", taskName);

		if (!activitiFacade.checkIfUserHasRightForGivenTask(publishTransNo,
				taskName, currentLoginUser.getUsername())) {
			throw new Exception("User[" + currentLoginUser.getUsername()
					+ "] has no right for [" + taskName + "]");
		}

		UserModel taskOwnerModel = userConverter.toModel(currentLoginUser);
		publishTransModel.setActiviTaskOwner(taskOwnerModel);

		publishTransDto = publishTransConverter.toDto(publishTransModel);
		taskService.setAssignee(taskId, currentLoginUser.getUsername());
		taskService.claim(taskId, currentLoginUser.getUsername());
		LOGGER.info("claimReviewTask end:{}", publishTransDto);
		return publishTransDto;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishTransDto manualReview(String publishTransNo,
			ReviewRecordDto reviewRecord) throws Exception {
		LOGGER.info("manualReview start:{}", publishTransNo);
		LOGGER.info("reviewRecord:{}", reviewRecord);
		PublishTransDto publishTransDto = null;
		String activeProcessDefinitionId = null;
		Map<String, Object> variableMap = null;
		// 0--accept; 2--reject
		Integer bookReviewOk = 0;
		// find trans info
		PublishTransModel publishTransModel = publishTransRepository
				.findOne(findByPublishTransNo(publishTransNo));
		if (publishTransModel == null) {
			throw new NotFoundException(
					"PublishTrans not found by publishTransNo["
							+ publishTransNo + "]");
		}
		activeProcessDefinitionId = publishTransModel
				.getActiviteProcessDefinitionId();

		PublishReviewerDto reviewer = reviewRecord.getReviewer();
		UserDto reviewerUser = reviewer.getUser();
		UserModel ownerModel = publishTransModel.getActiviTaskOwner();

		if (ownerModel == null
				|| !ownerModel.getUsername().equals(reviewerUser.getUsername())) {
			throw new Exception("task owner is incorrect");
		}

		publishTransDto = publishTransConverter.toDto(publishTransModel,
				PublishTransLoadStrategies.ALL);

		Task task = activitiFacade.getActiviteTask(publishTransNo,
				activeProcessDefinitionId);
		if (reviewRecord.getReviewStatus().equals("reject")) {
			bookReviewOk = 2;
		}
		// review is subflow, it has its own variable for publishTrans, it is
		// 'publishTransBean'
		variableMap = new HashMap<String, Object>();
		variableMap.put("bookReviewOk", bookReviewOk);
		variableMap.put("publishTransBean", publishTransDto);
		taskService.complete(task.getId(), variableMap);

		// after review, it could be end by reject or go to manual decision task
		publishTransSupport.postProcessForFlow(publishTransDto,
				publishTransModel, publishTransModel.getOperator()
						.getUsername(), PendingActivityBuildOperation.ALL);

		// persist review record for publishTrans
		PublishReviewerModel reviewerModel = publishReviewerRepository
				.findOne(reviewer.getReviewerId());
		ReviewRecordModel reviewRecordModel = reviewRecordConverter
				.toModel(reviewRecord);
		reviewRecordModel.setReviewer(reviewerModel);
		reviewRecordModel.setPublishTrans(publishTransModel);
		reviewRecordModel = reviewRecordRepository.save(reviewRecordModel);

		LOGGER.info("manualReview end:{}", publishTransDto);
		publishTransModel.setActiviTaskOwner(null);
		publishTransDto.setTaskOwner(null);
		LOGGER.info("manualReview end:{}", publishTransDto);
		return publishTransDto;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishTransDto publishDecision(String publishTransNo,
			UserDto operator, Integer decisionOperation) throws Exception {
		LOGGER.info("publishDecision start:{} ", publishTransNo);
		LOGGER.info("decisionOperation:{} ", decisionOperation);
		Map<String, Object> variableMap = null;
		String activeProcessDefinitionId = null;
		PublishTransDto publishTransDto = null;
		PublishTransModel publishTransModel = publishTransRepository
				.findOne(findByPublishTransNo(publishTransNo));
		if (publishTransModel == null) {
			throw new NotFoundException(
					"PublishTrans not found by publishTransNo["
							+ publishTransNo + "]");
		}

		if (!operator.getUsername().equals(
				publishTransModel.getOperator().getUsername())) {
			throw new Exception("User[" + operator.getUsername()
					+ "] do not have right for decision task");
		}

		activeProcessDefinitionId = publishTransModel
				.getActiviteProcessDefinitionId();

		publishTransDto = publishTransConverter.toDto(publishTransModel,
				PublishTransLoadStrategies.ALL);

		Task pendingTask = activitiFacade.getActiviteTask(publishTransNo,
				activeProcessDefinitionId);
		if (pendingTask == null
				|| !pendingTask.getName().equals("Publish Decision")) {
			throw new NotFoundException("Task[Publish Decision] not found");
		}
		variableMap = new HashMap<String, Object>();
		variableMap.put(PUBLISH_TRANSACTION, publishTransDto);
		variableMap.put("decision", decisionOperation);
		taskService.complete(pendingTask.getId(), variableMap);
		publishTransSupport.postProcessForFlow(publishTransDto,
				publishTransModel, operator.getUsername());

		LOGGER.info("publishDecision end:{} ", publishTransDto);
		return publishTransDto;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(value = "localTxManager", readOnly = true)
	public Set<PublishTransDto> getAllTransTaskForCurrentUser(
			UserDto currentLoginUser) throws Exception {
		LOGGER.info("getAllTransTaskForCurrentUser start:{} ", currentLoginUser);
		Set<PublishTransDto> publishTransSet = null;
		List<PublishTransModel> modelList = null;
		List<Task> taskList = activitiFacade
				.getAllTasksForUser(currentLoginUser.getUsername());
		if (taskList != null) {
			LOGGER.info("tasks size:{}", taskList.size());
			Set<String> taskExecutionIds = new HashSet<String>();
			for (Task task : taskList) {
				taskExecutionIds.add(task.getExecutionId());
			}

			Iterable<PublishTransModel> resultIterable = publishTransRepository
					.findAll(findByProcessExecutionIds(taskExecutionIds));
			if (resultIterable != null) {
				publishTransSet = new HashSet<PublishTransDto>();
				modelList = IteratorUtils.toList(resultIterable.iterator());
				for (PublishTransModel model : modelList) {
					publishTransSet.add(publishTransConverter.toDto(model));
				}
			}
		}
		LOGGER.info("getAllTransTaskForCurrentUser end:{} ");
		return publishTransSet;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = true)
	public ProcessActivityDto getPendingActivity(String publishTransNo)
			throws Exception {
		LOGGER.info("getPendingActivity start:{} ", publishTransNo);
		ProcessActivityDto processActivityDto = null;
		PublishTransModel publishTransModel = publishTransRepository
				.findOne(findByPublishTransNo(publishTransNo));
		if (publishTransModel == null) {
			throw new NotFoundException(
					"PublishTrans not found by publishTransNo["
							+ publishTransNo + "]");
		}
		processActivityDto = this.doBuildPendingActivity(
				publishTransModel.getPublishTransNo(),
				publishTransModel.getActiviteProcessDefinitionId(),
				publishTransModel.getActiviteProcessInstanceId(), true, true);
		LOGGER.info("getPendingActivity end:{} ", processActivityDto);
		return processActivityDto;
	}

	private ProcessActivityDto doBuildPendingActivity(String bizKey,
			String processDefinitionId, String processInstanceId,
			boolean buildIncomingAndOutgoingActivities,
			boolean buildTaskDetailsIfActivityIsUserTask) {
		LOGGER.info("doBuildPendingActivity start:{}");
		ProcessActivityDto pendingActivity = activitiFacade
				.getExecutionActivityBasicInfo(bizKey, processDefinitionId,
						processInstanceId, true, true);
		LOGGER.info("pending activity:{}", pendingActivity);
		if (buildTaskDetailsIfActivityIsUserTask) {
			if (pendingActivity.getType().equalsIgnoreCase("userTask")) {
				Task task = activitiFacade.getActiveTaskByNameAndBizKey(
						pendingActivity.getName(), bizKey);
				String taskDefinitionKey = task.getTaskDefinitionKey();
				if (!StringUtils.isEmpty(task.getAssignee())) {
					pendingActivity.setAssignee(task.getAssignee());
				} else {
					Map<String, Set<String>> candidatasInfo = activitiFacade
							.getActiviteTaskCandidateAssignmentInfo(
									taskDefinitionKey, processDefinitionId);
					if (candidatasInfo != null) {
						pendingActivity.setCandidateUsers(candidatasInfo
								.get(ActivitiFacade.CANDIDATE_USERS));
						pendingActivity.setCandidateGroups(candidatasInfo
								.get(ActivitiFacade.CANDIDATE_GROUPS));
					}

				}
			}
		}
		return pendingActivity;
	}

}
