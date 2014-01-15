package co.nz.bookpublish.ds;

import java.util.Set;

import co.nz.bookpublish.data.PublishAdministratorDto;

public interface PublishAdministratorDS {

	PublishAdministratorDto createPublishAdministrator(
			PublishAdministratorDto publishAdministrator) throws Exception;

	PublishAdministratorDto getPublishAdministratorById(Long adminId,
			boolean loadReviewers) throws Exception;

	PublishAdministratorDto getPublishAdministratorByName(String adminName,
			boolean loadReviewers) throws Exception;

	void updatePublishAdministratorName(Long adminId, String name)
			throws Exception;

	void deletePublishAdministrator(Long adminId) throws Exception;

	Set<PublishAdministratorDto> getAllPublishAdministrators() throws Exception;
}
