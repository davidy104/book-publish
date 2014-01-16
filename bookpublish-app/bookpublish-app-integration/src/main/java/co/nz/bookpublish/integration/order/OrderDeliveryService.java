package co.nz.bookpublish.integration.order;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderDeliveryService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderRoute.class);

	public Order deliveryOrder(Order order) throws Exception {
		LOGGER.info("deliveryOrder start:{} ", order);
		order.setDeliveryDate(new Date());
		LOGGER.info("deliveryOrder end:{} ", order);
		return order;
	}
}
