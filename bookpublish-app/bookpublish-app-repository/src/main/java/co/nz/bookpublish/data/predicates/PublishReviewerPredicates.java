package co.nz.bookpublish.data.predicates;

import co.nz.bookpublish.data.QPublishReviewerModel;
import co.nz.bookpublish.data.QUserModel;

import com.mysema.query.types.Predicate;

public class PublishReviewerPredicates {
	public static Predicate findByReviewerName(final String firstName,
			final String lastName) {
		QPublishReviewerModel publishReviewer = QPublishReviewerModel.publishReviewerModel;
		return publishReviewer.firstName.eq(firstName).and(
				publishReviewer.lastName.eq(lastName));
	}

	public static Predicate findByReviewerIdentity(final String identity) {
		QPublishReviewerModel publishReviewer = QPublishReviewerModel.publishReviewerModel;
		return publishReviewer.identity.eq(identity);
	}

	public static Predicate findByUserAccount(final String username) {
		QPublishReviewerModel publishReviewer = QPublishReviewerModel.publishReviewerModel;
		QUserModel user = publishReviewer.user;
		if (user != null) {
			return user.username.eq(username);
		}
		return null;
	}
}
