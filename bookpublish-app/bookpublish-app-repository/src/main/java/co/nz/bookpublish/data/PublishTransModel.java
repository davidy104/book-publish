package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISH_TRANSACTION")
public class PublishTransModel implements Serializable {

	public enum PublishStatus {
		dataEntry(1), pendingReview(3), pendingDecision(5), published(7), rejected(
				9);
		PublishStatus(int value) {
			this.value = value;
		}

		private final int value;

		public int value() {
			return value;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PUBLISH_TRANS_ID")
	private Long publishTransId;

	@Column(name = "PUBLISH_TRANS_NO")
	private String publishTransNo;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "COMPLETE_DATE")
	private Date completeDate;

	@Column(name = "STATUS")
	private Integer status = PublishStatus.dataEntry.value();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID")
	private BookModel book;

	@Column(name = "MAIN_PROCESS_INSTANCE_ID")
	private String mainProcessInstanceId;

	@Column(name = "MAIN_PROCESS_DEFINITION_ID")
	private String mainProcessDefinitionId;

	@Column(name = "ACTIVE_PROCESS_INSTANCE_ID")
	private String activiteProcessInstanceId;

	@Column(name = "EXECUTION_ID")
	private String executionId;

	@Column(name = "ACTIVE_PROCESS_DEFINITION_ID")
	private String activiteProcessDefinitionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATOR_ID", referencedColumnName = "USER_ID")
	private UserModel operator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_OWNER_ID", referencedColumnName = "USER_ID")
	private UserModel activiTaskOwner;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BookModel getBook() {
		return book;
	}

	public void setBook(BookModel book) {
		this.book = book;
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

	public String getActiviteProcessInstanceId() {
		return activiteProcessInstanceId;
	}

	public void setActiviteProcessInstanceId(String activiteProcessInstanceId) {
		this.activiteProcessInstanceId = activiteProcessInstanceId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getActiviteProcessDefinitionId() {
		return activiteProcessDefinitionId;
	}

	public void setActiviteProcessDefinitionId(
			String activiteProcessDefinitionId) {
		this.activiteProcessDefinitionId = activiteProcessDefinitionId;
	}

	public UserModel getOperator() {
		return operator;
	}

	public void setOperator(UserModel operator) {
		this.operator = operator;
	}

	public UserModel getActiviTaskOwner() {
		return activiTaskOwner;
	}

	public void setActiviTaskOwner(UserModel activiTaskOwner) {
		this.activiTaskOwner = activiTaskOwner;
	}

	public static Builder getBuilder(String publishTransNo,
			String mainProcessDefinitionId, String mainProcessInstanceId,
			String executionId) {
		return new Builder(publishTransNo, mainProcessDefinitionId,
				mainProcessInstanceId, executionId);
	}

	public static Builder getBuilder(String publishTransNo,
			String mainProcessDefinitionId, Date createDate) {
		return new Builder(publishTransNo, mainProcessDefinitionId, createDate);
	}

	public static Builder getBuilder(String publishTransNo) {
		return new Builder(publishTransNo);
	}

	public static class Builder {

		private PublishTransModel built;

		public Builder(String publishTransNo) {
			built = new PublishTransModel();
			built.publishTransNo = publishTransNo;
		}

		public Builder(String publishTransNo, String mainProcessDefinitionId,
				String mainProcessInstanceId, String executionId) {
			built = new PublishTransModel();
			built.publishTransNo = publishTransNo;
			built.mainProcessDefinitionId = mainProcessDefinitionId;
			built.activiteProcessDefinitionId = mainProcessDefinitionId;
			built.mainProcessInstanceId = mainProcessInstanceId;
			built.activiteProcessInstanceId = mainProcessInstanceId;
			built.executionId = executionId;
		}

		public Builder(String publishTransNo, String mainProcessDefinitionId,
				Date createDate) {
			built = new PublishTransModel();
			built.publishTransNo = publishTransNo;
			built.mainProcessDefinitionId = mainProcessDefinitionId;
			built.activiteProcessDefinitionId = mainProcessDefinitionId;
			built.createDate = createDate;
		}

		public PublishTransModel build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("publishTransId", publishTransId)
				.append("publishTransNo", publishTransNo)
				.append("createDate", createDate)
				.append("completeDate", completeDate)
				.append("status", status)
				.append("mainProcessInstanceId", mainProcessInstanceId)
				.append("mainProcessDefinitionId", mainProcessDefinitionId)
				.append("activiteProcessInstanceId", activiteProcessInstanceId)
				.append("activiteProcessDefinitionId",
						activiteProcessDefinitionId)
				.append("executionId", executionId).toString();
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.publishTransNo,
				((PublishTransModel) obj).publishTransNo).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.publishTransNo).toHashCode();
	}
}
