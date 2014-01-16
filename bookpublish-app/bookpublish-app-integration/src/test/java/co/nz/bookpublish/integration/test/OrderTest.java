package co.nz.bookpublish.integration.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.bookpublish.config.ApplicationConfiguration;
import co.nz.bookpublish.data.ProcessActivityDto;
import co.nz.bookpublish.integration.order.Order;
import co.nz.bookpublish.wf.ActivitiFacade;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfiguration.class})
public class OrderTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderTest.class);

	@Resource
	private RepositoryService repositoryService;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private ActivitiFacade activitiFacade;

	private String deployId;
	private String definitionId;
	private String definitionKey;

	@Before
	public void initialize() throws Exception {
		deployId = repositoryService.createDeployment()
				.addClasspathResource("order.bpmn20.xml").deploy().getId();

		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		definitionId = processDefinition.getId();
		definitionKey = processDefinition.getKey();
		LOGGER.info("definitionId:{} ", definitionId);
		LOGGER.info("definitionKey:{} ", definitionKey);
	}

	@Test
	public void testOrder() throws Exception {
		String bizKey = "order-001";
		Order order = new Order();
		order.setBookName("Camel in action");
		order.setOrderNo(bizKey);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("order", order);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceById(definitionId, bizKey, variables);
		Thread.sleep(3000);
		order = (Order) activitiFacade.getVariableFromCurrentProcess(
				processInstance.getId(), "order");
		LOGGER.info("after process:{} ", order);
		ProcessActivityDto pendingActivity = activitiFacade
				.getExecutionActivityBasicInfo(bizKey, definitionId,
						processInstance.getId(), true, true);
		LOGGER.info("pendingActivity:{} ", pendingActivity);

	}

}
