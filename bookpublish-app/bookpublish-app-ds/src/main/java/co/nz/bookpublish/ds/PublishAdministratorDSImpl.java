package co.nz.bookpublish.ds;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.nz.bookpublish.DuplicatedException;
import co.nz.bookpublish.NotFoundException;
import co.nz.bookpublish.converter.PublishAdministratorConverter;
import co.nz.bookpublish.data.PublishAdministratorDto;
import co.nz.bookpublish.data.PublishAdministratorModel;
import co.nz.bookpublish.data.repository.PublishAdministratorRepository;

import static co.nz.bookpublish.data.predicates.PublishAdministratorPredicates.findByAdminName;

@Service
@Transactional(value = "localTxManager", readOnly = true)
public class PublishAdministratorDSImpl implements PublishAdministratorDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishAdministratorDSImpl.class);

	@Resource
	private PublishAdministratorConverter publishAdministratorConverter;

	@Resource
	private PublishAdministratorRepository publishAdministratorRepository;

	@Resource
	private IdentityService identityService;

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public PublishAdministratorDto createPublishAdministrator(
			PublishAdministratorDto publishAdministrator) throws Exception {
		LOGGER.info("createPublishAdministrator start:{}", publishAdministrator);
		PublishAdministratorDto added = null;
		String adminName = publishAdministrator.getName();
		PublishAdministratorModel model = publishAdministratorRepository
				.findOne(findByAdminName(adminName));
		if (model != null) {
			throw new DuplicatedException(
					"PublishAdministrator already existed with name["
							+ adminName + "]");
		}

		model = publishAdministratorConverter.toModel(publishAdministrator);
		model.setCreateDate(new Date());
		model = publishAdministratorRepository.save(model);

		Group group = identityService.newGroup(adminName);
		group.setName(adminName);
		group.setType("");
		identityService.saveGroup(group);

		added = publishAdministratorConverter.toDto(model);
		LOGGER.info("createPublishAdministrator end:{}", added);
		return added;
	}

	@Override
	public PublishAdministratorDto getPublishAdministratorById(Long adminId,
			boolean loadReviewers) throws Exception {
		LOGGER.info("getPublishAdministratorById start:{}", adminId);
		PublishAdministratorDto found = null;

		PublishAdministratorModel model = publishAdministratorRepository
				.findOne(adminId);
		if (model == null) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ adminId + "]");
		}
		String foundAdminName = model.getName();
		if (identityService.createGroupQuery().groupId(foundAdminName).count() <= 0) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ foundAdminName + "]");
		}
		found = publishAdministratorConverter.toDto(model, loadReviewers);
		LOGGER.info("getPublishAdministratorById end:{}", found);
		return found;
	}

	@Override
	public PublishAdministratorDto getPublishAdministratorByName(
			String adminName, boolean loadReviewers) throws Exception {
		LOGGER.info("getPublishAdministratorById start:{}", adminName);
		PublishAdministratorDto found = null;

		PublishAdministratorModel model = publishAdministratorRepository
				.findOne(findByAdminName(adminName));
		if (model == null) {
			throw new NotFoundException(
					"PublishAdministrator not found by adminName[" + adminName
							+ "]");
		}

		if (identityService.createGroupQuery().groupId(adminName).count() <= 0) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ adminName + "]");
		}
		found = publishAdministratorConverter.toDto(model, loadReviewers);
		LOGGER.info("getPublishAdministratorById end:{}", found);
		return found;
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public void updatePublishAdministratorName(Long adminId, String name)
			throws Exception {
		LOGGER.info("updatePublishAdministratorName start:{}", adminId);
		PublishAdministratorModel model = publishAdministratorRepository
				.findOne(adminId);
		if (model == null) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ adminId + "]");
		}
		model.setName(name);
		LOGGER.info("updatePublishAdministratorName end:{}", model);
	}

	@Override
	@Transactional(value = "localTxManager", readOnly = false)
	public void deletePublishAdministrator(Long adminId) throws Exception {
		LOGGER.info("deletePublishAdministrator start:{}", adminId);
		PublishAdministratorModel model = publishAdministratorRepository
				.findOne(adminId);
		if (model == null) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ adminId + "]");
		}
		String foundAdminName = model.getName();
		if (identityService.createGroupQuery().groupId(foundAdminName).count() <= 0) {
			throw new NotFoundException("PublishAdministrator not found by id["
					+ foundAdminName + "]");
		}

		if (identityService.createUserQuery().memberOfGroup(foundAdminName)
				.count() > 0) {
			// still has members
			throw new Exception(
					"PublishAdministrator can not be deleted as it still has members");
		}

		publishAdministratorRepository.delete(model);
		identityService.deleteGroup(foundAdminName);
		LOGGER.info("deletePublishAdministrator end:{}");
	}

	@Override
	public Set<PublishAdministratorDto> getAllPublishAdministrators()
			throws Exception {
		LOGGER.info("getAllPublishAdministrators start:{}");
		Set<PublishAdministratorDto> foundSet = null;
		List<PublishAdministratorModel> foundList = publishAdministratorRepository
				.findAll();
		if (foundList != null) {
			foundSet = new HashSet<PublishAdministratorDto>();
			for (PublishAdministratorModel publishAdministratorModel : foundList) {
				foundSet.add(publishAdministratorConverter
						.toDto(publishAdministratorModel));
			}
		}
		LOGGER.info("getAllPublishAdministrators end:{}");
		return foundSet;
	}

}
