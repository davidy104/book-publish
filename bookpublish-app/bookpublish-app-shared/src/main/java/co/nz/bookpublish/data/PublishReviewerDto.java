package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class PublishReviewerDto implements Serializable {

	private Long reviewerId;
	private String firstName;
	private String lastName;
	private String identity;
	private String email;
	private String createDate;
	private UserDto user;
	private Set<ReviewRecordDto> reviewRecords;

	public void addReviewRecord(ReviewRecordDto reviewRecord) {
		if (reviewRecords == null) {
			reviewRecords = new HashSet<ReviewRecordDto>();
		}
		reviewRecords.add(reviewRecord);
	}

	public Set<ReviewRecordDto> getReviewRecords() {
		return reviewRecords;
	}

	public void setReviewRecords(Set<ReviewRecordDto> reviewRecords) {
		this.reviewRecords = reviewRecords;
	}

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public UserDto getUser() {
		return user;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email, String createDate, UserDto user) {
		return new Builder(firstName, lastName, identity, email, createDate,
				user);
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email, String createDate) {
		return new Builder(firstName, lastName, identity, email, createDate);
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email) {
		return new Builder(firstName, lastName, identity, email);
	}

	public static class Builder {

		private PublishReviewerDto built;

		public Builder(String firstName, String lastName, String identity,
				String email, String createDate, UserDto user) {
			built = new PublishReviewerDto();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
			built.createDate = createDate;
			built.user = user;
		}

		public Builder(String firstName, String lastName, String identity,
				String email, String createDate) {
			built = new PublishReviewerDto();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
			built.createDate = createDate;
		}

		public Builder(String firstName, String lastName, String identity,
				String email) {
			built = new PublishReviewerDto();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
		}

		public PublishReviewerDto build() {
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
		return builder.append(this.identity,
				((PublishReviewerDto) obj).identity).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.identity).toHashCode();
	}
}
