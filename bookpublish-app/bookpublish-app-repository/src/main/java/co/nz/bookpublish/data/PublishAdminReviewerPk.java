package co.nz.bookpublish.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Embeddable
public class PublishAdminReviewerPk implements Serializable {
	@ManyToOne
	private PublishAdministratorModel publishAdministrator;
	@ManyToOne
	private PublishReviewerModel publishReviewer;

	public PublishAdministratorModel getPublishAdministrator() {
		return publishAdministrator;
	}

	public void setPublishAdministrator(
			PublishAdministratorModel publishAdministrator) {
		this.publishAdministrator = publishAdministrator;
	}

	public PublishReviewerModel getPublishReviewer() {
		return publishReviewer;
	}

	public void setPublishReviewer(PublishReviewerModel publishReviewer) {
		this.publishReviewer = publishReviewer;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.publishAdministrator,
						((PublishAdminReviewerPk) obj).publishAdministrator)
				.append(this.publishReviewer,
						((PublishAdminReviewerPk) obj).publishReviewer)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.publishReviewer)
				.append(this.publishAdministrator).toHashCode();
	}
}
