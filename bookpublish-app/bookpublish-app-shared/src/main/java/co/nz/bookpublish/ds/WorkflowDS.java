package co.nz.bookpublish.ds;

import co.nz.bookpublish.data.WorkflowDto;

public interface WorkflowDS {

	WorkflowDto deployWorkflow(String name, String category,
			String... classpathResources) throws Exception;

	void undeployWorkflow(Long workflowId) throws Exception;

	WorkflowDto getWorkflowByNameAndCategory(String name, String category)
			throws Exception;

	WorkflowDto getWorkflowById(Long workflowId) throws Exception;

	WorkflowDto getWorkflowDtoByProcessDefinitionKey(
			String processDefinitionKey, String category) throws Exception;
}
