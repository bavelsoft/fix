package com.bavelsoft.fix;

import java.util.Map;
import javax.inject.Inject;

public class OrderRepo<O extends Order<F>, F> {
	private final Map<Long, O> map; //TODO garbage!
	private final SimplePool<O> pool;
	private final IdGenerator idgen;
	private final RequestRepo requestRepo;

	public OrderRepo(Map<Long, O> map, SimplePool<O> pool, IdGenerator idgen, RequestRepo requestRepo) {
		this.map = map;
		this.pool = pool;
		this.idgen = idgen;
		this.requestRepo = requestRepo;
	}

	public O requestNew(F fields, long orderQty, String clOrdID) {
		long orderID = idgen.getOrderID();
		O order = pool.acquire();
		order.init(fields, orderID, orderQty);
		map.put(orderID, order);
		requestRepo.request(order.newRequest, clOrdID);
		return order;
	}

	public void removeIfClosed(O order) {
		if (order.getLeavesQty() == 0) {
			map.remove(order.getOrderID());
			requestRepo.remove(order.newRequest);
			requestRepo.remove(order.replaceRequest);
			requestRepo.remove(order.cancelRequest);
			pool.release(order);
		}
	}

	public O get(long id) {
		return map.get(id);
	}

	public Iterable<O> all() {
		return pool.pool;
	}
}

