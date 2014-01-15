package co.nz.bookpublish.wf;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.converter.PublishTransConverter;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.repository.PublishTransRepository;
/**
 *
 * @author david
 *
 */
@Component("publishReviewTaskListener")
public class PublishReviewTaskListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishReviewTaskListener.class);

	@Resource
	private PublishTransConverter publishTransConverter;

	@Resource
	private PublishTransRepository publishTransRepository;

	public void execute(ExecutionListenerExecution execution) throws Exception {
		LOGGER.info("PublishReviewTaskListener start:{} ");
		LOGGER.info("eventName:{} ", execution.getEventName());
		PublishTransDto publishTransDto = null;
		boolean indenpendentProcess = true;
		ExecutionEntity parentExecutionEntity = null;

		indenpendentProcess = (Boolean) execution
				.getVariable("indenpendentProcess");

		if (indenpendentProcess) {
			// it is indenpendentProcess
			publishTransDto = (PublishTransDto) execution
					.getVariable("publishTransBean");
		} else {
			// it act as a subprocess, so try to get it from
			// parentProcess
			String activitiProcessDefinitionId = execution
					.getProcessDefinitionId();
			String activitiProcesssInstanceId = execution
					.getProcessInstanceId();
			LOGGER.info("activitiProcessDefinitionId:{}",
					activitiProcessDefinitionId);
			LOGGER.info("activitiProcesssInstanceId:{}",
					activitiProcesssInstanceId);
			parentExecutionEntity = ((ExecutionEntity) execution)
					.getSuperExecution();
			publishTransDto = (PublishTransDto) parentExecutionEntity
					.getVariable("publishTrans");
			// activitiProcessDefinitionId and
			// activitiProcesssInstanceId are subprocess's
			publishTransDto
					.setActiviteProcessDefinitionId(activitiProcessDefinitionId);
			publishTransDto
					.setActiviteProcesssInstanceId(activitiProcesssInstanceId);
		}

		// if review process started as subprocess, it need bizKey, generally it
		// can be done in
		// its start eventListener,
		if (StringUtils.isEmpty(execution.getProcessBusinessKey())) {
			ExecutionEntity executionEntity = (ExecutionEntity) execution;
			executionEntity.setBusinessKey(publishTransDto.getPublishTransNo());
		}
		// in general,executionId is same as processInstanceId,but
		// subprocess has its own executionId
		publishTransDto.setExecutionId(execution.getId());
		publishTransDto.setStatus("pendingReview");

		if (!indenpendentProcess) {
			// overwrite it in parentProcess.
			parentExecutionEntity.setVariable("publishTrans", publishTransDto);
		}
		LOGGER.info("PublishReviewTaskListener end:{} ", publishTransDto);
	}

}
