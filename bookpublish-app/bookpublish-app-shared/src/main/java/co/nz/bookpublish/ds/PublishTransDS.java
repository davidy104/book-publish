package co.nz.bookpublish.ds;

import java.util.Set;

import co.nz.bookpublish.data.BookDto;
import co.nz.bookpublish.data.ProcessActivityDto;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.ReviewRecordDto;
import co.nz.bookpublish.data.UserDto;

public interface PublishTransDS {

	PublishTransDto startPublishProcess(UserDto operator) throws Exception;

	ProcessActivityDto getPendingActivity(String publishTransNo)
			throws Exception;

	PublishTransDto dataEntry(String publishTransNo, BookDto book,
			UserDto operator) throws Exception;

	PublishTransDto claimReviewTask(String publishTransNo,
			UserDto currentLoginUser) throws Exception;

	PublishTransDto manualReview(String publishTransNo,
			ReviewRecordDto reviewRecord) throws Exception;

	PublishTransDto publishDecision(String publishTransNo, UserDto operator,
			Integer decisionOperation) throws Exception;

	Set<PublishTransDto> getAllTransTaskForCurrentUser(UserDto currentLoginUser)
			throws Exception;

}
