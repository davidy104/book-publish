package co.nz.bookpublish.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISH_ADMINISTRATOR")
public class PublishAdministratorModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ADMIN_ID")
	private Long adminId;

	@Column(name = "name")
	private String name;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "publishAdminReviewerPk.publishAdministrator")
	private Set<PublishAdminReviewerModel> adminReviewers;

	public void addPublishAdminReviewer(
			PublishAdminReviewerModel publishAdminReviewer) {
		if (adminReviewers == null) {
			adminReviewers = new HashSet<PublishAdminReviewerModel>();
		}
		adminReviewers.add(publishAdminReviewer);
	}

	public Set<PublishAdminReviewerModel> getAdminReviewers() {
		return adminReviewers;
	}

	public void setAdminReviewers(Set<PublishAdminReviewerModel> adminReviewers) {
		this.adminReviewers = adminReviewers;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public static Builder getBuilder(String name) {
		return new Builder(name);
	}

	public static Builder getBuilder(String name, Date createDate) {
		return new Builder(name, createDate);
	}

	public static class Builder {

		private PublishAdministratorModel built;

		public Builder(String name, Date createDate) {
			built = new PublishAdministratorModel();
			built.name = name;
			built.createDate = createDate;
		}

		public Builder(String name) {
			built = new PublishAdministratorModel();
			built.name = name;
		}

		public PublishAdministratorModel build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("adminId", adminId).append("createDate", createDate)
				.append("name", name).toString();
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.name, ((PublishAdministratorModel) obj).name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.name).toHashCode();
	}
}
