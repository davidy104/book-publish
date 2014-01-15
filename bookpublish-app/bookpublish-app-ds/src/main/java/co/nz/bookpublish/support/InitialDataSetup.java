package co.nz.bookpublish.support;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.nz.bookpublish.data.PublishAdministratorDto;
import co.nz.bookpublish.data.PublishReviewerDto;
import co.nz.bookpublish.data.UserDto;
import co.nz.bookpublish.data.WorkflowDto;
import co.nz.bookpublish.ds.PublishAdministratorDS;
import co.nz.bookpublish.ds.PublishReviewerDS;
import co.nz.bookpublish.ds.UserDS;
import co.nz.bookpublish.ds.WorkflowDS;

public class InitialDataSetup {

	private WorkflowDS workflowDs;

	private PublishAdministratorDS publishAdministratorDs;

	private PublishReviewerDS publishReviewerDs;

	private UserDS userDs;

	public InitialDataSetup(WorkflowDS workflowDs,
			PublishAdministratorDS publishAdministratorDs,
			PublishReviewerDS publishReviewerDs, UserDS userDs) {
		this.workflowDs = workflowDs;
		this.publishAdministratorDs = publishAdministratorDs;
		this.publishReviewerDs = publishReviewerDs;
		this.userDs = userDs;
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InitialDataSetup.class);

	public void initialize() throws Exception {
		LOGGER.info("initialize start:{} ");
		WorkflowDto workflow = workflowDs.deployWorkflow(
				"publishBookTransMainflow", "bookPublish",
				"bpmn/publishBook-mainflow.bpmn20.xml");
		LOGGER.info("deploy main process, workflow:{} ", workflow);

		workflow = workflowDs.deployWorkflow("publishBookTransSubflow",
				"bookPublish", "bpmn/publishBook-subflow.bpmn20.xml");
		LOGGER.info("deploy sub process, workflow:{} ", workflow);

		// init user
		UserDto davidUser = userDs.createUser("david", "123456");
		UserDto bradUser = userDs.createUser("brad", "123456");
		UserDto johnUser = userDs.createUser("john", "123456");
		// create admin user, just for operation
		userDs.createUser("admin", "123456");

		// init admin department
		PublishAdministratorDto publishAdministratorDto = PublishAdministratorDto
				.getBuilder("PublishAdminManager").build();
		publishAdministratorDto = publishAdministratorDs
				.createPublishAdministrator(publishAdministratorDto);
		Long publishAdminId = publishAdministratorDto.getAdminId();
		Set<Long> selectedAdminIds = new HashSet<Long>();
		selectedAdminIds.add(publishAdminId);

		// init reviewer
		PublishReviewerDto publishReviewer = PublishReviewerDto.getBuilder(
				"david", "yuan", "", "david.yuan124@gmail.com").build();
		publishReviewer.setUser(davidUser);
		publishReviewerDs.createPublishReviewer(publishReviewer,
				selectedAdminIds);

		publishReviewer = PublishReviewerDto.getBuilder("brad", "wu", "",
				"david.yuan124@gmail.com").build();
		publishReviewer.setUser(bradUser);
		publishReviewerDs.createPublishReviewer(publishReviewer,
				selectedAdminIds);

		publishReviewer = PublishReviewerDto.getBuilder("john", "ni", "",
				"david.yuan124@gmail.com").build();
		publishReviewer.setUser(johnUser);
		publishReviewerDs.createPublishReviewer(publishReviewer,
				selectedAdminIds);

		LOGGER.info("initialize end:{} ");
	}
}
