package co.nz.bookpublish.ds;

import java.util.Set;

import co.nz.bookpublish.data.PublishReviewerDto;
import co.nz.bookpublish.data.PublishReviewerLoadStrategies;

public interface PublishReviewerDS {
	PublishReviewerDto createPublishReviewer(
			PublishReviewerDto publishReviewer, Set<Long> selectedAdminIds)
			throws Exception;

	PublishReviewerDto getPublishReviewerById(Long reviewerId,
			PublishReviewerLoadStrategies... loadStrategies) throws Exception;

	PublishReviewerDto getPublishReviewerByIdentity(String identity,
			PublishReviewerLoadStrategies... loadStrategies) throws Exception;

	PublishReviewerDto getPublishReviewerByUser(String username)
			throws Exception;

	void deletePublishReviewer(Long reviewerId) throws Exception;

	void updatePublishReviewer(Long reviewerId,
			PublishReviewerDto publishReviewer) throws Exception;

	void updatePublishAdministratorForReviewer(Long reviewerId,
			Set<Long> selectedAdminIds) throws Exception;

}
