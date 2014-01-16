package co.nz.bookpublish.integration.order;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements Processor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderProcessor.class);
	@Override
	public void process(Exchange exchange) throws Exception {
		Order order = exchange.getIn().getBody(Order.class);
		LOGGER.info("deliveryOrder start:{} ", order);
		order.setDeliveryDate(new Date());
		LOGGER.info("deliveryOrder end:{} ", order);
//		exchange.setProperty("order", order);

		Map<String, Object> variables = exchange.getProperties();
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			LOGGER.info("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
	}

}
