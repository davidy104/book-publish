package co.nz.bookpublish.converter;

import javax.annotation.Resource;

import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.BookModel;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.PublishTransModel;
import co.nz.bookpublish.data.PublishTransModel.PublishStatus;
import co.nz.bookpublish.data.UserModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class PublishTransConverter
		implements
			GeneralConverter<PublishTransDto, PublishTransModel> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishTransConverter.class);

	@Resource
	private UserConverter userConverter;

	@Resource
	private BookConverter bookConverter;

	public enum PublishTransLoadStrategies {
		OPERATOR, OWNER, BOOK, ALL
	}

	@Override
	public PublishTransDto toDto(PublishTransModel model,
			Object... loadStrategies) throws ConvertException {
		LOGGER.info("toDto start:{}", model);
		boolean operatorLoaded = false;
		boolean ownerloaded = false;
		boolean bookloaded = false;
		PublishTransDto dto = PublishTransDto.getBuilder(
				model.getPublishTransNo(), model.getMainProcessDefinitionId())
				.build();
		dto.setExecutionId(model.getExecutionId());
		dto.setMainProcessInstanceId(model.getMainProcessInstanceId());
		dto.setActiviteProcessDefinitionId(model
				.getActiviteProcessDefinitionId());
		dto.setActiviteProcesssInstanceId(model.getActiviteProcessInstanceId());

		dto.setPublishTransId(model.getPublishTransId());
		if (model.getCreateDate() != null) {
			dto.setCreateDate(GeneralUtils.dateToStr(model.getCreateDate(),
					"yyyy-MM-dd"));
		}
		if (model.getCompleteDate() != null) {
			dto.setCompleteDate(GeneralUtils.dateToStr(model.getCompleteDate(),
					"yyyy-MM-dd"));
		}

		Integer status = model.getStatus();
		if (status == PublishStatus.dataEntry.value()) {
			dto.setStatus("dataEntry");
		} else if (status == PublishStatus.pendingDecision.value()) {
			dto.setStatus("pendingDecision");
		} else if (status == PublishStatus.pendingReview.value()) {
			dto.setStatus("pendingReview");
		} else if (status == PublishStatus.published.value()) {
			dto.setStatus("published");
		} else if (status == PublishStatus.rejected.value()) {
			dto.setStatus("rejected");
		}

		if (loadStrategies != null) {
			for (Object tmpLoadStrategy : loadStrategies) {
				PublishTransLoadStrategies loadyStrategy = (PublishTransLoadStrategies) tmpLoadStrategy;

				if (loadyStrategy == PublishTransLoadStrategies.ALL) {
					if (!operatorLoaded) {
						operatorLoaded = this.loadOperator(dto,
								model.getOperator());
					}
					if (!ownerloaded) {
						ownerloaded = this.loadOwner(dto,
								model.getActiviTaskOwner());
					}

					if (!bookloaded) {
						bookloaded = this.loadBook(dto, model.getBook());
					}
					break;
				} else if (loadyStrategy == PublishTransLoadStrategies.BOOK
						&& !bookloaded) {
					bookloaded = this.loadBook(dto, model.getBook());
				} else if (loadyStrategy == PublishTransLoadStrategies.OPERATOR
						&& !operatorLoaded) {
					operatorLoaded = this
							.loadOperator(dto, model.getOperator());
				} else if (loadyStrategy == PublishTransLoadStrategies.OWNER
						&& !ownerloaded) {
					ownerloaded = this.loadOwner(dto,
							model.getActiviTaskOwner());
				}
			}
		}
		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public PublishTransModel toModel(PublishTransDto dto)
			throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		PublishTransModel model = PublishTransModel.getBuilder(
				dto.getPublishTransNo(), dto.getMainProcessDefinitionId(),
				dto.getMainProcessInstanceId(), dto.getExecutionId()).build();

		if (!StringUtils.isEmpty(dto.getStatus())) {
			if (dto.getStatus().equals("dataEntry")) {
				model.setStatus(PublishStatus.dataEntry.value());
			} else if (dto.getStatus().equals("pendingReview")) {
				model.setStatus(PublishStatus.pendingReview.value());
			} else if (dto.getStatus().equals("pendingDecision")) {
				model.setStatus(PublishStatus.pendingDecision.value());
			} else if (dto.getStatus().equals("published")) {
				model.setStatus(PublishStatus.published.value());
			} else if (dto.getStatus().equals("rejected")) {
				model.setStatus(PublishStatus.rejected.value());
			}
		}

		LOGGER.info("toModel end:{}", model);
		return model;
	}

	private boolean loadOwner(PublishTransDto publishTransDto,
			UserModel userModel) throws ConvertException {
		if (userModel != null) {
			publishTransDto.setTaskOwner(userConverter.toDto(userModel));
		}
		return true;
	}

	private boolean loadOperator(PublishTransDto publishTransDto,
			UserModel userModel) throws ConvertException {
		if (userModel != null) {
			publishTransDto.setOperator(userConverter.toDto(userModel));
		}
		return true;
	}

	private boolean loadBook(PublishTransDto publishTransDto,
			BookModel bookModel) throws ConvertException {
		if (bookModel != null) {
			publishTransDto.setBook(bookConverter.toDto(bookModel));
		}
		return true;
	}

}
