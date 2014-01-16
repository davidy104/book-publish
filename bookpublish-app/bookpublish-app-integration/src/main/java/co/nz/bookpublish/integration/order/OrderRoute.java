package co.nz.bookpublish.integration.order;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderRoute extends RouteBuilder {

	@Resource
	private OrderProcessor orderProcessor;

	@Resource
	private OrderDeliveryService orderDeliveryService;

	@Resource
	private OrderTransFormer orderTransFormer;

	@Override
	public void configure() throws Exception {

		from("activiti:orderProcess:camelTask?copyVariablesToBodyAsMap=true")
				.to("log:input")
				.wireTap("direct:integration")
				.executorServiceRef("genericThreadPool");

		from("direct:integration").transform(orderTransFormer)
				.log("get order data ${body}")
//				.bean(orderDeliveryService, "deliveryOrder")
				.process(orderProcessor)
				.log("after delivery ${body}")
				.wireTap("direct:receiveQueue")
				.executorServiceRef("genericThreadPool");

		from("direct:receiveQueue")
				.to("log:input")
				.to("activiti:orderProcess:receiveTask");
	}

}
