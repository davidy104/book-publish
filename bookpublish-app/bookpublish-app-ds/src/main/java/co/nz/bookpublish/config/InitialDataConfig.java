package co.nz.bookpublish.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.bookpublish.ds.PublishAdministratorDS;
import co.nz.bookpublish.ds.PublishReviewerDS;
import co.nz.bookpublish.ds.UserDS;
import co.nz.bookpublish.ds.WorkflowDS;
import co.nz.bookpublish.support.InitialDataSetup;

@Configuration
public class InitialDataConfig {
	@Resource
	private WorkflowDS workflowDs;

	@Resource
	private PublishAdministratorDS publishAdministratorDs;

	@Resource
	private PublishReviewerDS publishReviewerDs;

	@Resource
	private UserDS userDs;

	@Bean(initMethod = "initialize")
	public InitialDataSetup setupData() {
		return new InitialDataSetup(workflowDs, publishAdministratorDs,
				publishReviewerDs, userDs);
	}
}
