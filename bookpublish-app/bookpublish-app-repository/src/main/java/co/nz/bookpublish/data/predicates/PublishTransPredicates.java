package co.nz.bookpublish.data.predicates;

import java.util.Set;

import co.nz.bookpublish.data.QPublishTransModel;

import com.mysema.query.types.Predicate;

public class PublishTransPredicates {
	public static Predicate findByPublishTransNo(final String publishTransNo) {
		QPublishTransModel publishTrans = QPublishTransModel.publishTransModel;
		return publishTrans.publishTransNo.eq(publishTransNo);
	}

	public static Predicate findByProcessExecutionIds(
			final Set<String> executionIds) {
		QPublishTransModel publishTrans = QPublishTransModel.publishTransModel;
		return publishTrans.executionId.in(executionIds);
	}
}
