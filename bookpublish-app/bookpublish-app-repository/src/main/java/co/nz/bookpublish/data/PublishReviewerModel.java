package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISH_REVIEWER")
public class PublishReviewerModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REVIEWER_ID")
	private Long reviewerId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "IDENTITY")
	private String identity;

	@Column(name = "EMAIL")
	private String email;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private UserModel user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewer")
	private List<ReviewRecordModel> reviewRecords;

	@OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy = "publishAdminReviewerPk.publishReviewer")
	private Set<PublishAdminReviewerModel> adminReviewers;

	public void addPublishAdminReviewer(
			PublishAdminReviewerModel publishAdminReviewerModel) {
		if (adminReviewers == null) {
			adminReviewers = new HashSet<PublishAdminReviewerModel>();
		}
		adminReviewers.add(publishAdminReviewerModel);
	}

	public Set<PublishAdminReviewerModel> getAdminReviewers() {
		return adminReviewers;
	}

	public void setAdminReviewers(Set<PublishAdminReviewerModel> adminReviewers) {
		this.adminReviewers = adminReviewers;
	}

	public void addReviewRecord(ReviewRecordModel reviewRecord) {
		if (reviewRecords == null) {
			reviewRecords = new ArrayList<ReviewRecordModel>();
		}
		reviewRecords.add(reviewRecord);
	}

	public List<ReviewRecordModel> getReviewRecords() {
		return reviewRecords;
	}

	public void setReviewRecords(List<ReviewRecordModel> reviewRecords) {
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

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email, Date createDate, UserModel user) {
		return new Builder(firstName, lastName, identity, email, createDate,
				user);
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email, Date createDate) {
		return new Builder(firstName, lastName, identity, email, createDate);
	}

	public static Builder getBuilder(String firstName, String lastName,
			String identity, String email) {
		return new Builder(firstName, lastName, identity, email);
	}

	public static class Builder {

		private PublishReviewerModel built;

		public Builder(String firstName, String lastName, String identity,
				String email, Date createDate, UserModel user) {
			built = new PublishReviewerModel();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
			built.createDate = createDate;
			built.user = user;
		}

		public Builder(String firstName, String lastName, String identity,
				String email, Date createDate) {
			built = new PublishReviewerModel();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
			built.createDate = createDate;
		}

		public Builder(String firstName, String lastName, String identity,
				String email) {
			built = new PublishReviewerModel();
			built.firstName = firstName;
			built.lastName = lastName;
			built.identity = identity;
			built.email = email;
		}

		public PublishReviewerModel build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("reviewerId", reviewerId)
				.append("firstName", firstName).append("lastName", lastName)
				.append("identity", identity).append("email", email)
				.append("createDate", createDate).toString();
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.identity,
				((PublishReviewerModel) obj).identity).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.identity).toHashCode();
	}
}
