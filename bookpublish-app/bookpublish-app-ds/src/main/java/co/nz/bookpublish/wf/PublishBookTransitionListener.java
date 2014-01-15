package co.nz.bookpublish.wf;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.converter.PublishTransConverter;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.repository.PublishTransRepository;

@Component("publishBookTransitionListener")
public class PublishBookTransitionListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishBookTransitionListener.class);

	@Resource
	private PublishTransConverter publishTransConverter;

	@Resource
	private PublishTransRepository publishTransRepository;

	public void execute(ExecutionListenerExecution execution) throws Exception {
		LOGGER.info("PublishUserTaskListener start:{} ");
		PublishTransDto publishTransDto = (PublishTransDto) execution
				.getVariable("publishTrans");

		String processInstanceId = execution.getProcessInstanceId();
		String processDefinitionId = execution.getProcessDefinitionId();
		String executionId = execution.getId();

		LOGGER.info("publishTrans:{} ", publishTransDto);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		LOGGER.info("processDefinitionId:{} ", processDefinitionId);

		PvmTransition transition = (PvmTransition) execution.getEventSource();
		PvmActivity nextActivity = transition.getDestination();
		String activityType = (String) nextActivity.getProperty("type");
		String activityName = (String) nextActivity.getProperty("name");
		String activityId = nextActivity.getId();

		LOGGER.info("nextActivity id:{}", activityId);
		LOGGER.info("nextActivity name:{}", activityName);
		LOGGER.info("nextActivity type:{}", activityType);

		if (activityType.equals("userTask")) {
			publishTransDto.setExecutionId(executionId);
			if (activityId.equals("dataEntry")) {
				publishTransDto.setStatus("dataEntry");
				publishTransDto
						.setActiviteProcesssInstanceId(processInstanceId);

			} else if (activityId.equals("publishDecision")) {
				publishTransDto.setStatus("pendingDecision");
				publishTransDto
						.setActiviteProcessDefinitionId(processDefinitionId);
				publishTransDto
						.setActiviteProcesssInstanceId(processInstanceId);

			}
			LOGGER.info("after update publishTrans:{} ",
					publishTransDto.getStatus());
		} else if (activityType.equals("callActivity")) {
			// indicate subflow is not indenpendent process, it is subprocess
			// for current process
			execution.setVariable("publishReviewProcessIsIndependent", false);
		}
		// else if (activityType.equals("end")) {
		// publishTransModel.setCompleteDate(new Date());
		// if (activityId.equals("theEnd")) {
		// publishTransModel.setStatus(PublishStatus.published.value());
		// } else if (activityId.equals("rejectedEnd")) {
		// publishTransModel.setStatus(PublishStatus.rejected.value());
		// }
		// }
	}

}
