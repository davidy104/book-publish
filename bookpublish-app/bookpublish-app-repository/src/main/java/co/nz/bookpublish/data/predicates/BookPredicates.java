package co.nz.bookpublish.data.predicates;

import co.nz.bookpublish.data.QBookModel;

import com.mysema.query.types.Predicate;

public class BookPredicates {
	public static Predicate findByBookTitle(final String booktitle) {
		QBookModel bookModel = QBookModel.bookModel;
		return bookModel.title.eq(booktitle);
	}
}
