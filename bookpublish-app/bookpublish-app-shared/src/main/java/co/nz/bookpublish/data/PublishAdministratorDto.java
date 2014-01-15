package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class PublishAdministratorDto implements Serializable {

	private Long adminId;
	private String name;
	private String createDate;

	private Set<PublishReviewerDto> reviewers;

	public void addPublishReviewer(PublishReviewerDto reviewer) {
		if (reviewers == null) {
			reviewers = new HashSet<PublishReviewerDto>();
		}
		reviewers.add(reviewer);
	}

	public Set<PublishReviewerDto> getReviewers() {
		return reviewers;
	}

	public void setReviewers(Set<PublishReviewerDto> reviewers) {
		this.reviewers = reviewers;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public static Builder getBuilder(String name, String createDate) {
		return new Builder(name, createDate);
	}

	public static Builder getBuilder(String name) {
		return new Builder(name);
	}

	public static class Builder {

		private PublishAdministratorDto built;

		public Builder(String name, String createDate) {
			built = new PublishAdministratorDto();
			built.name = name;
			built.createDate = createDate;
		}

		public Builder(String name) {
			built = new PublishAdministratorDto();
			built.name = name;
		}

		public PublishAdministratorDto build() {
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
		return builder.append(this.name, ((PublishAdministratorDto) obj).name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.name).toHashCode();
	}
}
