package co.nz.bookpublish.converter;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.ReviewRecordDto;
import co.nz.bookpublish.data.ReviewRecordModel;
import co.nz.bookpublish.data.ReviewRecordModel.ReviewStatus;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class ReviewRecordConverter
		implements
			GeneralConverter<ReviewRecordDto, ReviewRecordModel> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReviewRecordConverter.class);

	@Resource
	private PublishReviewerConverter publishReviewerConverter;

	@Override
	public ReviewRecordDto toDto(ReviewRecordModel model,
			Object... loadStrategies) throws ConvertException {
		LOGGER.info("toDto start:{} ", model);
		ReviewRecordDto dto = ReviewRecordDto
				.getBuilder(model.getPublishTrans().getPublishTransNo(),
						model.getContent()).build();
		dto.setReviewRecordId(model.getReviewRecordId());

		Integer reviewStatus = model.getReviewStatus();
		if (reviewStatus == ReviewStatus.accept.value()) {
			dto.setReviewStatus("accept");
		} else if (reviewStatus == ReviewStatus.reject.value()) {
			dto.setReviewStatus("reject");
		} else if (reviewStatus == ReviewStatus.pending.value()) {
			dto.setReviewStatus("pending");
		}

		if (model.getCreateTime() != null) {
			dto.setCreateTime(GeneralUtils.dateToStr(model.getCreateTime()));
		}

		if (loadStrategies != null) {
			boolean loadReviewer = (Boolean) loadStrategies[0];
			if (loadReviewer && model.getReviewer() != null) {
				dto.setReviewer(publishReviewerConverter.toDto(model
						.getReviewer()));
			}
		}

		LOGGER.info("toDto end:{} ", dto);
		return dto;
	}

	@Override
	public ReviewRecordModel toModel(ReviewRecordDto dto)
			throws ConvertException {
		LOGGER.info("toModel start:{} ", dto);
		ReviewRecordModel model = ReviewRecordModel.getBuilder(
				dto.getContent(), new Date()).build();
		if (!StringUtils.isEmpty(dto.getCreateTime())) {
			model.setCreateTime(GeneralUtils.strToDate(dto.getCreateTime()));
		}

		if (!StringUtils.isEmpty(dto.getReviewStatus())) {
			if (dto.getReviewStatus().equals("accept")) {
				model.setReviewStatus(ReviewStatus.accept.value());
			} else if (dto.getReviewStatus().equals("reject")) {
				model.setReviewStatus(ReviewStatus.reject.value());
			} else if (dto.getReviewStatus().equals("pending")) {
				model.setReviewStatus(ReviewStatus.pending.value());
			}
		}

		LOGGER.info("toModel end:{} ", model);
		return model;
	}

}
