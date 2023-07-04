package com.bavelsoft.fix;

import java.util.Map;
import javax.inject.Inject;

public class OrderRepo<F> {
	@Inject
	protected Map<Long, Order<F>> map;
	@Inject
	protected SimplePool<Order<F>> pool;
	@Inject
	protected IdGenerator idgen = new IdGenerator();
	@Inject
	protected RequestRepo requestRepo;

	public Order<F> requestNew(F fields, long orderQty, String clOrdID) {
		long orderID = idgen.getOrderID();
		Order<F> order = pool.acquire();
		map.put(orderID, order.init(fields, orderID, orderQty));
		requestRepo.request(order.newRequest, clOrdID); //TODO this is convenient, but is it "right"? could imagine orders without newRequests :)
		return order;
	}

	public void removeIfClosed(Order<F> order) {
		if (order.getLeavesQty() == 0) {
			map.remove(order.getOrderID());
			requestRepo.remove(order.newRequest);
			requestRepo.remove(order.replaceRequest);
			requestRepo.remove(order.cancelRequest);
			pool.release(order);
		}
	}
}

