package co.nz.bookpublish.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.UserDto;
import co.nz.bookpublish.data.UserModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class UserConverter implements GeneralConverter<UserDto, UserModel> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserConverter.class);

	@Override
	public UserDto toDto(UserModel model, Object... loadStrategies)
			throws ConvertException {
		LOGGER.info("toDto start:{}", model);
		UserDto dto = UserDto.getBuilder(model.getUsername(),
				model.getPassword()).build();
		if (model.getCreateTime() != null) {
			dto.setCreateTime(GeneralUtils.dateToStr(model.getCreateTime()));
		}
		dto.setUserId(model.getUserId());
		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public UserModel toModel(UserDto dto) throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		UserModel model = UserModel.getBuilder(dto.getUsername(),
				dto.getPassword()).build();
		if (!StringUtils.isEmpty(dto.getCreateTime())) {
			model.setCreateTime(GeneralUtils.strToDate(dto.getCreateTime()));
		}

		if (dto.getUserId() != null) {
			model.setUserId(dto.getUserId());
		}
		LOGGER.info("toModel end:{}", model);
		return model;
	}

}
