package co.nz.bookpublish.data.predicates;

import co.nz.bookpublish.data.QPublishAdministratorModel;

import com.mysema.query.types.Predicate;

public class PublishAdministratorPredicates {
	public static Predicate findByAdminName(final String adminName) {
		QPublishAdministratorModel publishAdministratorModel = QPublishAdministratorModel.publishAdministratorModel;
		return publishAdministratorModel.name.eq(adminName);
	}
}
