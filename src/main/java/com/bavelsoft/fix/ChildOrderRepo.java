package com.bavelsoft.fix;

import java.util.Map;
import java.util.Collection;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChildOrderRepo<O extends ChildOrder<F>, F> {
	private final OrderRepo<O, F> orderRepo;
	private final IdGenerator idgen;
	private final ChildByParentRepo children;

	public ChildOrderRepo(OrderRepo<O, F> orderRepo, IdGenerator idgen, ChildByParentRepo children) {
		this.orderRepo = orderRepo;
		this.idgen = idgen;
		this.children = children;
	}

	public O requestNew(Order<?> parent, F fields, long orderQty) {
		O child = orderRepo.requestNew(fields, orderQty, idgen.getClOrdID());
		children.add(parent, child);

		return child;
	}

	//TODO how to make sure parent is not removed until all children are removed?
	public void removeIfClosed(O order) {
		orderRepo.removeIfClosed(order);
		children.removeIfClosed(order);
	}

	public O get(long id) {
		return orderRepo.get(id);
	}
}

