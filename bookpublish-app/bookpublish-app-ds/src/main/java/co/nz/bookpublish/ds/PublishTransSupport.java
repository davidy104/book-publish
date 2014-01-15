package co.nz.bookpublish.ds;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.converter.PublishTransConverter;
import co.nz.bookpublish.data.ProcessActivityDto;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.PublishTransModel;
import co.nz.bookpublish.data.PublishTransModel.PublishStatus;
import co.nz.bookpublish.data.repository.PublishTransRepository;
import co.nz.bookpublish.wf.ActivitiFacade;

@Component
public class PublishTransSupport {
	@Resource
	private ActivitiFacade activitiFacade;

	@Resource
	private PublishTransConverter publishTransConverter;

	@Resource
	private PublishTransRepository publishTransRepository;

	@Resource
	private TaskService taskService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishTransSupport.class);

	public enum PendingActivityBuildOperation {
		ACTIVITY_INCOMING, ACTIVITY_OUTGOING, TASK_DETAILS, ALL
	}

	public void mergeDtoToModelAftProcess(PublishTransDto dto,
			PublishTransModel model) {
		model.setActiviteProcessDefinitionId(dto
				.getActiviteProcessDefinitionId());
		model.setActiviteProcessInstanceId(dto.getActiviteProcesssInstanceId());
		model.setExecutionId(dto.getExecutionId());
		if (dto.getStatus().equals("dataEntry")) {
			model.setStatus(PublishStatus.dataEntry.value());
		} else if (dto.getStatus().equals("pendingReview")) {
			model.setStatus(PublishStatus.pendingReview.value());
		} else if (dto.getStatus().equals("pendingDecision")) {
			model.setStatus(PublishStatus.pendingDecision.value());
		} else if (dto.getStatus().equals("published")) {
			model.setStatus(PublishStatus.published.value());
			model.setCompleteDate(new Date());
		} else if (dto.getStatus().equals("rejected")) {
			model.setStatus(PublishStatus.rejected.value());
			model.setCompleteDate(new Date());
		}
	}

	public PublishTransDto postProcessForFlow(PublishTransDto publishTransDto,
			PublishTransModel publishTransModel, String operatorName,
			PendingActivityBuildOperation... loadPendingActivityOperations)
			throws Exception {

		LOGGER.info("postProcessForFlow start:{} ", publishTransDto);
		String publishTransNo = publishTransDto.getPublishTransNo();
		String mainProcessDefinitionId = publishTransDto
				.getMainProcessDefinitionId();
		String mainProcessInstanceId = publishTransDto
				.getMainProcessInstanceId();

		if (activitiFacade.ifProcessFinishted(publishTransNo,
				mainProcessDefinitionId)) {
			LOGGER.info("process stopped..");
			ProcessActivityDto endActivity = activitiFacade.getLastActivity(
					mainProcessInstanceId, mainProcessDefinitionId);
			LOGGER.info("end activity:{} ", endActivity);

			if (endActivity.getActivityId().equals("rejectedEnd")) {
				publishTransDto.setStatus("rejected");
			} else if (endActivity.getActivityId().equals("theEnd")) {
				publishTransDto.setStatus("published");
			}

		} else {
			LOGGER.info("process pending ");
			boolean loadincoming = false;
			boolean loadoutgoing = false;
			boolean loadTaskDetails = false;

			Object variable = activitiFacade.getVariableFromCurrentProcess(
					mainProcessInstanceId, "publishTrans");
			if (variable != null) {
				publishTransDto = (PublishTransDto) variable;
				LOGGER.info("get publishTransDto from flow:{} ",
						publishTransDto);
			}

			if (loadPendingActivityOperations != null
					&& loadPendingActivityOperations.length > 0) {
				for (PendingActivityBuildOperation loadPendingActivityOperation : loadPendingActivityOperations) {
					if (loadPendingActivityOperation == PendingActivityBuildOperation.ALL) {
						loadincoming = true;
						loadoutgoing = true;
						loadTaskDetails = true;
						break;
					} else if (loadPendingActivityOperation == PendingActivityBuildOperation.TASK_DETAILS) {
						loadTaskDetails = true;
					} else if (loadPendingActivityOperation == PendingActivityBuildOperation.ACTIVITY_INCOMING) {
						loadincoming = true;
					} else if (loadPendingActivityOperation == PendingActivityBuildOperation.ACTIVITY_OUTGOING) {
						loadoutgoing = true;
					}
				}
			}

			ProcessActivityDto pendingActivity = activitiFacade
					.getExecutionActivityBasicInfo(publishTransNo,
							publishTransDto.getActiviteProcessDefinitionId(),
							publishTransDto.getActiviteProcesssInstanceId(),
							loadincoming, loadoutgoing);
			LOGGER.info("pending activity:{}", pendingActivity);

			Task pendingTask = this.assignOperatorToTaskAndBuildTaskInfo(
					pendingActivity, publishTransNo,
					publishTransDto.getActiviteProcessDefinitionId(),
					operatorName, loadTaskDetails);
			if (pendingTask != null) {
				// we use task executionId to load all avaliable publishTrans
				// for given user
				publishTransDto.setExecutionId(pendingTask.getExecutionId());
			}

			publishTransDto.setPendingActivity(pendingActivity);
		}

		this.mergeDtoToModelAftProcess(publishTransDto, publishTransModel);
		LOGGER.info("after merge publishTransModel:{} ", publishTransModel);
		LOGGER.info("postProcessForFlow end:{} ", publishTransDto);
		return publishTransDto;
	}
	public Task assignOperatorToTaskAndBuildTaskInfo(
			ProcessActivityDto pendingActivity, String bizKey,
			String processDefinitionId, String operatorName,
			boolean loadTaskDetails) {
		Task pendingTask = null;
		if (pendingActivity.getType().equals("userTask")) {
			pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
					pendingActivity.getName(), bizKey);
			if (pendingActivity.getName().equals("Data Entry")
					|| pendingActivity.getName().equals("Publish Decision")) {
				// for these 2 tasks, we assign operator as task owner
				LOGGER.info("assign operator[" + operatorName + "] to task["
						+ pendingTask.getName() + "]");
				pendingTask.setAssignee(operatorName);
				taskService.claim(pendingTask.getId(), operatorName);
				pendingActivity.setAssignee(operatorName);
			} else if (loadTaskDetails) {
				String taskDefinitionKey = pendingTask.getTaskDefinitionKey();
				if (!StringUtils.isEmpty(pendingTask.getAssignee())) {
					pendingActivity.setAssignee(pendingTask.getAssignee());
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
		return pendingTask;
	}

}
