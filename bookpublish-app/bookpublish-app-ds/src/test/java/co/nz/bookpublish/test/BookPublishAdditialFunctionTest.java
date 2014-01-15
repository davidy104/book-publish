package co.nz.bookpublish.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.bookpublish.config.ApplicationConfiguration;
import co.nz.bookpublish.data.WorkflowDto;
import co.nz.bookpublish.ds.WorkflowDS;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfiguration.class})
public class BookPublishAdditialFunctionTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookPublishAdditialFunctionTest.class);

	@Resource
	private WorkflowDS workflowDs;

	@Test
	public void testInitialWorkflow() throws Exception {
		String mainflowDefinitionKey = "publishBookTransMainflow";
		String subflowDefinitionKey = "publishBookTransSubflow";
		String category = "bookPublish";

		WorkflowDto workflow = workflowDs.getWorkflowByNameAndCategory(
				mainflowDefinitionKey, category);
		LOGGER.info("get mainflow:{} ", workflow);

		workflow = workflowDs.getWorkflowByNameAndCategory(subflowDefinitionKey, category);
		LOGGER.info("get subflow:{} ", workflow);
	}



}
