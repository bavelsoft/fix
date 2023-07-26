package com.bavelsoft.fix;

import java.util.Map;
import java.util.Collection;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChildByParentRepo {
	private final Map<Order, Collection<ChildOrder>> map;

	public ChildByParentRepo(Map<Order, Collection<ChildOrder>> map, OrderRepo parentOrderRepo, Supplier<Collection> collectionFactory) {
		this.map = map;

		for (Object order : parentOrderRepo.all()) {
			map.put((Order)order, collectionFactory.get());
		}
	}

	public void add(Order parent, ChildOrder child) {
		child.init(parent);
		map.get(parent).add(child);
	}

	public Iterable<ChildOrder> get(Order parent) {
		return map.get(parent);
	}

	public void removeIfClosed(ChildOrder order) {
		if (order.getLeavesQty() == 0) {
			map.get(order.getParent()).remove(order);
		}
	}
}

