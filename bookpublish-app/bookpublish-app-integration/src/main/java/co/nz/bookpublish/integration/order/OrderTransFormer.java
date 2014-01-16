package co.nz.bookpublish.integration.order;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderTransFormer implements Expression {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderTransFormer.class);
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		Map variablesFromProcess = exchange.getIn().getBody(Map.class);
		Order order = (Order) variablesFromProcess.get("order");
		LOGGER.info("after transform order:{} ", order);
		return (T) order;
	}

}
