package co.nz.bookpublish.data;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISH_ADMIN_REVIEWER")
@AssociationOverrides({
		@AssociationOverride(name = "publishAdminReviewerPk.publishAdministrator", joinColumns = @JoinColumn(name = "ADMIN_ID")),
		@AssociationOverride(name = "publishAdminReviewerPk.publishReviewer", joinColumns = @JoinColumn(name = "REVIEWER_ID")) })
public class PublishAdminReviewerModel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ADMIN_REVIEWER_ID")
	private Long adminReviewerId;

	@Embedded
	private PublishAdminReviewerPk publishAdminReviewerPk = new PublishAdminReviewerPk();

	public Long getAdminReviewerId() {
		return adminReviewerId;
	}

	public void setAdminReviewerId(Long adminReviewerId) {
		this.adminReviewerId = adminReviewerId;
	}

	public PublishAdminReviewerPk getPublishAdminReviewerPk() {
		return publishAdminReviewerPk;
	}

	public void setPublishAdminReviewerPk(
			PublishAdminReviewerPk publishAdminReviewerPk) {
		this.publishAdminReviewerPk = publishAdminReviewerPk;
	}

	@Transient
	public PublishAdministratorModel getPublishAdministrator() {
		return getPublishAdminReviewerPk().getPublishAdministrator();
	}

	public void setPublishAdministrator(
			PublishAdministratorModel publishAdministrator) {
		getPublishAdminReviewerPk().setPublishAdministrator(
				publishAdministrator);
	}

	@Transient
	public PublishReviewerModel getPublishReviewer() {
		return getPublishAdminReviewerPk().getPublishReviewer();
	}

	public void setPublishReviewer(PublishReviewerModel publishReviewer) {
		getPublishAdminReviewerPk().setPublishReviewer(publishReviewer);
	}

}
