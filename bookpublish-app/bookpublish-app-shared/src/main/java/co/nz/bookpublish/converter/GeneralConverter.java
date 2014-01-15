package co.nz.bookpublish.converter;

import co.nz.bookpublish.ConvertException;

public interface GeneralConverter<M, V> {

	M toDto(V model, Object... loadStrategies) throws ConvertException;

	V toModel(M dto) throws ConvertException;
}
