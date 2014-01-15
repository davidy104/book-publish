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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_REVIEW_RECORD")
public class ReviewRecordModel implements Serializable {

	public enum ReviewStatus {
		accept(1), reject(3), pending(5);
		ReviewStatus(int value) {
			this.value = value;
		}

		private final int value;

		public int value() {
			return value;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REVIEW_RECORD_ID")
	private Long reviewRecordId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUBLISH_TRANS_ID", referencedColumnName = "PUBLISH_TRANS_ID")
	private PublishTransModel publishTrans;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REVIEWER_ID", referencedColumnName = "REVIEWER_ID")
	private PublishReviewerModel reviewer;

	@Column(name = "CONTENT")
	private String content;

	@Temporal(value = TemporalType.TIME)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "REVIEW_STATUS")
	private Integer reviewStatus = ReviewStatus.pending.value();

	public Long getReviewRecordId() {
		return reviewRecordId;
	}

	public void setReviewRecordId(Long reviewRecordId) {
		this.reviewRecordId = reviewRecordId;
	}

	public PublishTransModel getPublishTrans() {
		return publishTrans;
	}

	public void setPublishTrans(PublishTransModel publishTrans) {
		this.publishTrans = publishTrans;
	}

	public PublishReviewerModel getReviewer() {
		return reviewer;
	}

	public void setReviewer(PublishReviewerModel reviewer) {
		this.reviewer = reviewer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public static Builder getBuilder(PublishTransModel publishTrans,
			String content, Date createTime, PublishReviewerModel reviewer) {
		return new Builder(publishTrans, content, createTime, reviewer);
	}

	public static Builder getBuilder(String content, Date createTime) {
		return new Builder(content, createTime);
	}

	public static class Builder {

		private ReviewRecordModel built;

		public Builder(PublishTransModel publishTrans, String content,
				Date createTime, PublishReviewerModel reviewer) {
			built = new ReviewRecordModel();
			built.publishTrans = publishTrans;
			built.content = content;
			built.createTime = createTime;
			built.reviewer = reviewer;
		}

		public Builder(String content, Date createTime) {
			built = new ReviewRecordModel();
			built.content = content;
			built.createTime = createTime;

		}

		public ReviewRecordModel build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("reviewRecordId", reviewRecordId)
				.append("content", content).append("createTime", createTime)
				.toString();
	}
}
