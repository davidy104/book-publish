package co.nz.bookpublish.converter;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.PublishAdminReviewerModel;
import co.nz.bookpublish.data.PublishAdministratorDto;
import co.nz.bookpublish.data.PublishAdministratorModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class PublishAdministratorConverter
		implements
			GeneralConverter<PublishAdministratorDto, PublishAdministratorModel> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishAdministratorConverter.class);

	@Resource
	private PublishReviewerConverter publishReviewerConverter;

	@Override
	public PublishAdministratorDto toDto(PublishAdministratorModel model,
			Object... loadStrategies) throws ConvertException {
		LOGGER.info("toDto start:{}", model);

		PublishAdministratorDto dto = PublishAdministratorDto.getBuilder(
				model.getName()).build();
		if (model.getCreateDate() != null) {
			dto.setCreateDate(GeneralUtils.dateToStr(model.getCreateDate(),
					"yyyy-MM-dd"));
		}
		dto.setAdminId(model.getAdminId());
		if (loadStrategies != null && loadStrategies.length == 1) {
			boolean loadReviewer = (Boolean) loadStrategies[0];
			if (loadReviewer && model.getAdminReviewers() != null) {
				for (PublishAdminReviewerModel publishAdminReviewerModel : model
						.getAdminReviewers()) {
					dto.addPublishReviewer(publishReviewerConverter
							.toDto(publishAdminReviewerModel
									.getPublishReviewer()));
				}
			}
		}

		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public PublishAdministratorModel toModel(PublishAdministratorDto dto)
			throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		PublishAdministratorModel model = PublishAdministratorModel.getBuilder(
				dto.getName()).build();
		if (!StringUtils.isEmpty(dto.getCreateDate())) {
			model.setCreateDate(GeneralUtils.strToDate(dto.getCreateDate(),
					"yyyy-MM--dd"));
		}
		LOGGER.info("toModel end:{}", model);
		return model;
	}
}
