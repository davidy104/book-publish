package co.nz.bookpublish.data.predicates;

import co.nz.bookpublish.data.QUserModel;

import com.mysema.query.types.Predicate;

public class UserPredicates {
	public static Predicate findByUsername(final String username) {
		QUserModel user = QUserModel.userModel;
		return user.username.eq(username);
	}

	public static Predicate findByUsernameAndPassword(final String username,
			final String password) {
		QUserModel user = QUserModel.userModel;
		return user.username.eq(username).and(user.password.eq(password));
	}
}
