package co.nz.bookpublish.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.nz.bookpublish.ConvertException;
import co.nz.bookpublish.data.WorkflowDto;
import co.nz.bookpublish.data.WorkflowModel;
import co.nz.bookpublish.utils.GeneralUtils;

@Component
public class WorkflowConverter
		implements
			GeneralConverter<WorkflowDto, WorkflowModel> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WorkflowConverter.class);

	@Override
	public WorkflowDto toDto(WorkflowModel model, Object... loadStrategies)
			throws ConvertException {
		LOGGER.info("toDto start:{}", model);
		WorkflowDto dto = WorkflowDto.getBuilder(model.getName(),
				model.getCategory(), model.getDeployId(),
				model.getProcessDefinitionKey(),
				model.getProcessDefinitionId(),
				GeneralUtils.dateToStr(model.getCreateTime())).build();
		dto.setWfId(model.getWfId());
		LOGGER.info("toDto end:{}", dto);
		return dto;
	}

	@Override
	public WorkflowModel toModel(WorkflowDto dto) throws ConvertException {
		LOGGER.info("toModel start:{}", dto);
		WorkflowModel model = WorkflowModel.getBuilder(dto.getName(),
				dto.getCategory()).build();
		if (!StringUtils.isEmpty(dto.getCreateTime())) {
			model.setCreateTime(GeneralUtils.strToDate(dto.getCreateTime()));
		}

		LOGGER.info("toModel end:{}", model);
		return model;
	}

}
