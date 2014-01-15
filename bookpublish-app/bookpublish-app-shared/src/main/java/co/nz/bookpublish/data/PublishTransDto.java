package co.nz.bookpublish.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class PublishTransDto implements Serializable {
	private Long publishTransId;
	private String publishTransNo;
	private String createDate;
	private String completeDate;
	private String executionId;

	// dataEntry,pendingReview,pendingDecision,published,rejected
	private String status;
	private BookDto book;

	private String mainProcessInstanceId;
	private String mainProcessDefinitionId;

	private String activiteProcesssInstanceId;
	private String activiteProcessDefinitionId;

	private UserDto operator;
	private UserDto taskOwner;

	private ProcessActivityDto pendingActivity;

	public String getActiviteProcesssInstanceId() {
		return activiteProcesssInstanceId;
	}

	public void setActiviteProcesssInstanceId(String activiteProcesssInstanceId) {
		this.activiteProcesssInstanceId = activiteProcesssInstanceId;
	}

	public String getActiviteProcessDefinitionId() {
		return activiteProcessDefinitionId;
	}

	public void setActiviteProcessDefinitionId(
			String activiteProcessDefinitionId) {
		this.activiteProcessDefinitionId = activiteProcessDefinitionId;
	}

	public Long getPublishTransId() {
		return publishTransId;
	}

	public void setPublishTransId(Long publishTransId) {
		this.publishTransId = publishTransId;
	}

	public String getPublishTransNo() {
		return publishTransNo;
	}

	public void setPublishTransNo(String publishTransNo) {
		this.publishTransNo = publishTransNo;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BookDto getBook() {
		return book;
	}

	public void setBook(BookDto book) {
		this.book = book;
	}

	public UserDto getOperator() {
		return operator;
	}

	public void setOperator(UserDto operator) {
		this.operator = operator;
	}

	public UserDto getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(UserDto taskOwner) {
		this.taskOwner = taskOwner;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getMainProcessInstanceId() {
		return mainProcessInstanceId;
	}

	public void setMainProcessInstanceId(String mainProcessInstanceId) {
		this.mainProcessInstanceId = mainProcessInstanceId;
	}

	public String getMainProcessDefinitionId() {
		return mainProcessDefinitionId;
	}

	public void setMainProcessDefinitionId(String mainProcessDefinitionId) {
		this.mainProcessDefinitionId = mainProcessDefinitionId;
	}

	public ProcessActivityDto getPendingActivity() {
		return pendingActivity;
	}

	public void setPendingActivity(ProcessActivityDto pendingActivity) {
		this.pendingActivity = pendingActivity;
	}

	public static Builder getBuilder(String publishTransNo,
			String mainProcessDefinitionId) {
		return new Builder(publishTransNo, mainProcessDefinitionId);
	}

	public static Builder getBuilder(String publishTransNo, String status,
			String mainProcessDefinitionId) {
		return new Builder(publishTransNo, status, mainProcessDefinitionId);
	}

	public static Builder getBuilder(String publishTransNo, String status,
			String mainProcessInstanceId, String mainProcessDefinitionId,
			String activiteProcesssInstanceId,
			String activiteProcessDefinitionId, String executionId) {
		return new Builder(publishTransNo, status, mainProcessInstanceId,
				mainProcessDefinitionId, activiteProcesssInstanceId,
				activiteProcessDefinitionId, executionId);
	}

	public static class Builder {

		private PublishTransDto built;

		public Builder(String publishTransNo, String status,
				String mainProcessDefinitionId) {
			built = new PublishTransDto();
			built.publishTransNo = publishTransNo;
			built.status = status;
			built.mainProcessDefinitionId = mainProcessDefinitionId;
		}

		public Builder(String publishTransNo, String mainProcessDefinitionId) {
			built = new PublishTransDto();
			built.publishTransNo = publishTransNo;
			built.mainProcessDefinitionId = mainProcessDefinitionId;
		}

		public Builder(String publishTransNo, String status,
				String mainProcessInstanceId, String mainProcessDefinitionId,
				String activiteProcesssInstanceId,
				String activiteProcessDefinitionId, String executionId) {
			built = new PublishTransDto();
			built.publishTransNo = publishTransNo;
			built.status = status;
			built.mainProcessInstanceId = mainProcessInstanceId;
			built.mainProcessDefinitionId = mainProcessDefinitionId;
			built.activiteProcessDefinitionId = activiteProcessDefinitionId;
			built.activiteProcesssInstanceId = activiteProcesssInstanceId;
			built.executionId = executionId;
		}

		public PublishTransDto build() {
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
		return builder.append(this.publishTransNo,
				((PublishTransDto) obj).publishTransNo).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.publishTransNo).toHashCode();
	}
}
