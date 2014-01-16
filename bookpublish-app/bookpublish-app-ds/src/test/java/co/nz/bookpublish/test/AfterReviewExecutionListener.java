package co.nz.bookpublish.test;

import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("afterReviewExecutionListener")
public class AfterReviewExecutionListener {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AfterReviewExecutionListener.class);

	public void execute(ExecutionListenerExecution execution) throws Exception {
		Integer publishTransReviewStatus = (Integer) execution
				.getVariable("publishTransReviewStatus");
		LOGGER.info("publishTransReviewStatus:{} ", publishTransReviewStatus);
	}
}
