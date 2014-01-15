package co.nz.bookpublish.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class ReviewRecordDto implements Serializable {
	private Long reviewRecordId;
	private String publishTransNo;
	private String content;
	private String createTime;
	private PublishReviewerDto reviewer;

	// accept,pending,reject
	private String reviewStatus;

	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Long getReviewRecordId() {
		return reviewRecordId;
	}

	public void setReviewRecordId(Long reviewRecordId) {
		this.reviewRecordId = reviewRecordId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public PublishReviewerDto getReviewer() {
		return reviewer;
	}

	public void setReviewer(PublishReviewerDto reviewer) {
		this.reviewer = reviewer;
	}

	public String getPublishTransNo() {
		return publishTransNo;
	}

	public void setPublishTransNo(String publishTransNo) {
		this.publishTransNo = publishTransNo;
	}

	public static Builder getBuilder(String publishTransNo, String content,
			String createTime, PublishReviewerDto reviewer) {
		return new Builder(publishTransNo, content, createTime, reviewer);
	}

	public static Builder getBuilder(String publishTransNo, String content,
			PublishReviewerDto reviewer) {
		return new Builder(publishTransNo, content, reviewer);
	}

	public static Builder getBuilder(String publishTransNo, String content) {
		return new Builder(publishTransNo, content);
	}

	public static class Builder {

		private ReviewRecordDto built;

		public Builder(String publishTransNo, String content,
				String createTime, PublishReviewerDto reviewer) {
			built = new ReviewRecordDto();
			built.publishTransNo = publishTransNo;
			built.content = content;
			built.createTime = createTime;
			built.reviewer = reviewer;
		}

		public Builder(String publishTransNo, String content,
				PublishReviewerDto reviewer) {
			built = new ReviewRecordDto();
			built.publishTransNo = publishTransNo;
			built.content = content;
			built.reviewer = reviewer;
		}

		public Builder(String publishTransNo, String content) {
			built = new ReviewRecordDto();
			built.publishTransNo = publishTransNo;
			built.content = content;
		}

		public ReviewRecordDto build() {
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
		return builder.append(this.reviewRecordId,
				((ReviewRecordDto) obj).reviewRecordId).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.reviewRecordId).toHashCode();
	}
}
