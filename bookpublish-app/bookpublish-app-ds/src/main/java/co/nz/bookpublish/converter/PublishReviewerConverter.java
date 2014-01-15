package co.nz.bookpublish.converter;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.PublishReviewerDto;
import co.nz.bookpublish.data.PublishReviewerLoadStrategies;
import co.nz.bookpublish.data.PublishReviewerModel;
import co.nz.bookpublish.data.ReviewRecordModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class PublishReviewerConverter
		implements
			GeneralConverter<PublishReviewerDto, PublishReviewerModel> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishReviewerConverter.class);

	@Resource
	private UserConverter userConverter;

	@Resource
	private ReviewRecordConverter reviewRecordConverter;

	@Override
	public PublishReviewerDto toDto(PublishReviewerModel model,
			Object... loadStrategies) throws ConvertException {
		LOGGER.info("toDto start:{}", model);
		PublishReviewerDto dto = PublishReviewerDto.getBuilder(
				model.getFirstName(), model.getLastName(), model.getIdentity(),
				model.getEmail()).build();
		if (model.getCreateDate() != null) {
			dto.setCreateDate(GeneralUtils.dateToStr(model.getCreateDate(),
					"yyyy-MM-dd"));
		}

		dto.setReviewerId(model.getReviewerId());

		if (loadStrategies != null) {
			for (Object tempLoadStrategy : loadStrategies) {
				PublishReviewerLoadStrategies loadStrategy = (PublishReviewerLoadStrategies) tempLoadStrategy;
				if (loadStrategy == PublishReviewerLoadStrategies.USER
						&& dto.getUser() == null && model.getUser() != null) {
					dto.setUser(userConverter.toDto(model.getUser()));
				} else if (loadStrategy == PublishReviewerLoadStrategies.RECORDS
						&& dto.getReviewRecords() == null
						&& model.getReviewRecords() != null) {
					for (ReviewRecordModel reviewRecordModel : model
							.getReviewRecords()) {
						dto.addReviewRecord(reviewRecordConverter
								.toDto(reviewRecordModel));
					}
				}
			}
		}
		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public PublishReviewerModel toModel(PublishReviewerDto dto)
			throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		PublishReviewerModel model = PublishReviewerModel.getBuilder(
				dto.getFirstName(), dto.getLastName(), dto.getIdentity(),
				dto.getEmail()).build();
		if (!StringUtils.isEmpty(dto.getCreateDate())) {
			model.setCreateDate(GeneralUtils.strToDate(dto.getCreateDate(),
					"yyyy-MM-dd"));
		}
		LOGGER.info("toModel end:{}", model);
		return model;
	}

}
