package co.nz.bookpublish.test;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.bookpublish.config.ApplicationConfiguration;
import co.nz.bookpublish.data.BookDto;
import co.nz.bookpublish.data.PublishReviewerDto;
import co.nz.bookpublish.data.PublishTransDto;
import co.nz.bookpublish.data.UserDto;
import co.nz.bookpublish.ds.PublishReviewerDS;
import co.nz.bookpublish.ds.PublishTransDS;
import co.nz.bookpublish.ds.UserDS;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfiguration.class})
@Ignore
public class PublishMainTransactionTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishMainTransactionTest.class);

	@Resource
	private PublishTransDS publishTransDs;

	@Resource
	private PublishReviewerDS publishReviewerDs;

	@Resource
	private UserDS userDs;

	private static final String OPERATOR_USER = "admin";

	private UserDto operator;

	private PublishReviewerDto davidReviewer;
	private PublishReviewerDto bradReviewer;

	@Before
	public void initialize() throws Exception {
		operator = userDs.getUserByName(OPERATOR_USER);
		assertNotNull(operator);
		LOGGER.info("login and operator is:{} ", operator);

		davidReviewer = publishReviewerDs.getPublishReviewerByUser("david");
		bradReviewer = publishReviewerDs.getPublishReviewerByUser("brad");
		LOGGER.info("davidReviewer:{} ", davidReviewer);
		LOGGER.info("bradReviewer:{} ", bradReviewer);
	}

	@Test
	public void testStartProcess() throws Exception {
		PublishTransDto publishTransDto = this.mockStartProcess(operator);
		LOGGER.info("after initial process, publishTransDto:{} ",
				publishTransDto);

		Set<PublishTransDto> publishTransList = publishTransDs
				.getAllTransTaskForCurrentUser(operator);
		assertNotNull(publishTransList);
		for (PublishTransDto publishTrans : publishTransList) {
			LOGGER.info("avaliabe task for user[" + operator.getUsername()
					+ "]:{} ", publishTrans);
		}
	}

	@Test
	public void testDataEntry() throws Exception {
		PublishTransDto publishTransDto = this.mockStartProcess(operator);
		LOGGER.info("after initial process, publishTransDto:{} ",
				publishTransDto);
//		publishTransDto = this.mockDataEntryPassAutoReview(
//				publishTransDto.getPublishTransNo(), operator);
		publishTransDto = this.mockDataEntryPassToReject(
				publishTransDto.getPublishTransNo(), operator);
		assertNotNull(publishTransDto);
		LOGGER.info("publishTransDto:{} ", publishTransDto);
	}

	private PublishTransDto mockDataEntryPassAutoReview(String publishTransNo,
			UserDto operator) throws Exception {
		BookDto book = BookDto.getBuilder("isbn-002", "Camel in action",
				new BigDecimal(60.00), "Mike", 500, "2013-10-10").build();
		return publishTransDs.dataEntry(publishTransNo, book, operator);
	}

	private PublishTransDto mockDataEntryPassToManualReview(String publishTransNo,
			UserDto operator) throws Exception {
		BookDto book = BookDto.getBuilder("isbn-003", "Spring in action",
				new BigDecimal(120.00), "Mike", 500, "2013-10-10").build();
		return publishTransDs.dataEntry(publishTransNo, book, operator);
	}

	private PublishTransDto mockDataEntryPassToReject(String publishTransNo,
			UserDto operator) throws Exception {
		BookDto book = BookDto.getBuilder("isbn-003", "Jquery in action",
				new BigDecimal(620.00), "Mike", 500, "2013-10-10").build();
		return publishTransDs.dataEntry(publishTransNo, book, operator);
	}

	private PublishTransDto mockStartProcess(UserDto operator) throws Exception {
		return publishTransDs.startPublishProcess(operator);
	}
}
