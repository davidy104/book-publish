package co.nz.bookpublish.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class WorkflowDto implements Serializable {
	private Long wfId;
	private String name;
	private String category;
	private String deployId;
	private String processDefinitionKey;
	private String processDefinitionId;
	private String createTime;

	public Long getWfId() {
		return wfId;
	}

	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public static Builder getBuilder(String name, String category,
			String deployId, String processDefinitionKey,
			String processDefinitionId, String createTime) {
		return new Builder(name, category, deployId, processDefinitionKey,
				processDefinitionId, createTime);
	}

	public static Builder getBuilder(String name, String category,
			String deployId, String processDefinitionId) {
		return new Builder(name, category, deployId, processDefinitionId);
	}

	public static class Builder {

		private WorkflowDto built;

		public Builder(String name, String category, String deployId,
				String processDefinitionKey, String processDefinitionId,
				String createTime) {
			built = new WorkflowDto();
			built.name = name;
			built.category = category;
			built.processDefinitionId = processDefinitionId;
			built.processDefinitionKey = processDefinitionKey;
			built.deployId = deployId;
			built.createTime = createTime;
		}

		public Builder(String name, String category, String deployId,
				String processDefinitionId) {
			built = new WorkflowDto();
			built.name = name;
			built.category = category;
			built.processDefinitionId = processDefinitionId;
			built.deployId = deployId;
		}

		public WorkflowDto build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.name, ((WorkflowDto) obj).name)
				.append(this.category, ((WorkflowDto) obj).category).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.name).append(this.category).toHashCode();
	}
}
