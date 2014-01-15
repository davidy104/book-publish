package co.nz.bookpublish.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.BookDto;
import co.nz.bookpublish.data.BookModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class BookConverter implements GeneralConverter<BookDto, BookModel> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookConverter.class);

	@Override
	public BookDto toDto(BookModel model, Object... loadStrategies)
			throws ConvertException {
		LOGGER.info("toDto start:{}", model);
		BookDto dto = BookDto.getBuilder(model.getIsbn(), model.getTitle(),
				model.getPrice(), model.getAuthor(), model.getPages()).build();
		if (model.getPublishDate() != null) {
			dto.setPublishDate(GeneralUtils.dateToStr(model.getPublishDate(),
					"yyyy-MM-dd"));
		}
		dto.setBookId(model.getBookId());
		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public BookModel toModel(BookDto dto) throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		BookModel model = BookModel.getBuilder(dto.getTitle(), dto.getIsbn(),
				dto.getPages(), dto.getPrice(), dto.getAuthor()).build();
		if (!StringUtils.isEmpty(dto.getPublishDate())) {
			model.setPublishDate(GeneralUtils.strToDate(dto.getPublishDate(),
					"yyyy-MM-dd"));
		}
		LOGGER.info("toModel end:{}", model);
		return model;
	}

}
