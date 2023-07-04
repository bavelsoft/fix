package com.bavelsoft.fix;

import java.util.Map;
import java.util.Collection;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChildOrderRepo<F> {
	@Inject
	protected OrderRepo<F> orderRepo;
	@Inject
	protected OrderRepo parentOrderRepo;
	@Inject
	protected IdGenerator idgen = new IdGenerator();
	@Inject
	protected RequestRepo requestRepo;
	@Inject
	protected Map<Order<?>, Collection<ChildOrder<F>>> map; //TODO or should we subclass the parent too?
	@Inject
	protected Supplier<Collection> collectionFactory; //TODO if not need pooled implementation

	private final ParentCanceller parentCanceller = new ParentCanceller();

	class ParentCanceller {
		public void accept(ChildOrder<?> order) {
			Order<?> parent = order.parent;
			if (getChildWorkingQty(parent) == 0) {
				parent.cancel();
				parentOrderRepo.removeIfClosed(parent);
				//TODO release from map
			}
		}
	}

	public void cancelFamily(Order<?> parent) {
		for (ChildOrder<?> child : map.get(parent)) {
			requestRepo.requestCancel(child, idgen.getClOrdID());
			child.parentCanceller = parentCanceller;
		}
	}

	public Order<F> requestNew(Order<?> parent, F fields, long orderQty, String clOrdID) {
		if (orderQty+getChildWorkingQty(parent) > parent.getOrderQty()) {
			throw new RuntimeException("attempt to overslice");
		}

		//TODO generify OrderRepo properly, or do we not want to encourage subclassing Order?
		ChildOrder<F> order = (ChildOrder<F>)orderRepo.requestNew(fields, orderQty, clOrdID);
		order.setParent(parent);
		map.get(parent).add(order);

		return order;
	}

	public long getChildWorkingQty(Order<?> parent) {
		long childQty = 0;
		if (!map.containsKey(parent)) { //TODO does this make sense with pooling?
			map.put(parent, collectionFactory.get());
		}

		for (Order<?> child : map.get(parent)) {
			childQty += child.getWorkingQty();
		}
		return childQty;
	}
}

