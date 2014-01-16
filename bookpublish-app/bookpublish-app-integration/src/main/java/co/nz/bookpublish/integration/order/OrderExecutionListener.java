package co.nz.bookpublish.integration.order;

import java.util.Map;

import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("orderExecutionListener")
public class OrderExecutionListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderExecutionListener.class);

	public void execute(ExecutionListenerExecution execution) throws Exception {
		Order order = (Order) execution.getVariable("order");
		LOGGER.info("get order after camel route:{}", order);

		Map<String, Object> variables = execution.getVariables();
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			LOGGER.info("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
	}
}
