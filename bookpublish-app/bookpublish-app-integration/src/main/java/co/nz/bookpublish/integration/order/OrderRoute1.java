package co.nz.bookpublish.integration.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderRoute1 extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:mockws").to("log:input")
				.wireTap("direct:orderReceiveQueue")
				.executorServiceRef("genericThreadPool");

		from("direct:orderReceiveQueue").setBody(method(Helper.class))
				.to("log:input").to("activiti:orderProcess1:receiveTask");
	}

	public static final class Helper {
		private static final Logger LOGGER = LoggerFactory
				.getLogger(Helper.class);
		@Handler
		public Map<String, Object> getProcessVariables(Exchange exchange) {
			Map<String, Object> properties = exchange.getProperties();
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				LOGGER.info("Key : " + entry.getKey() + " Value : "
						+ entry.getValue());
			}
			Order order = exchange.getIn().getBody(Order.class);
			LOGGER.info("order in Helper :{} ", order);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("order", order);
			return variables;
		}
	}

}
