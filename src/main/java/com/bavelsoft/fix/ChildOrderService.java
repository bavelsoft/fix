package com.bavelsoft.fix;

import java.util.Map;
import java.util.Collection;
import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChildOrderService {
	private final IdGenerator idgen;
	private final RequestRepo requestRepo;
	private final OrderRepo parentOrderRepo;
	private final ChildByParentRepo children;

	final ParentCanceller parentCanceller = new ParentCanceller();

	public ChildOrderService(IdGenerator idgen, RequestRepo requestRepo, OrderRepo parentOrderRepo, ChildByParentRepo children) {
		this.idgen = idgen;
		this.requestRepo = requestRepo;
		this.parentOrderRepo = parentOrderRepo;
		this.children = children;
	}

	class ParentCanceller {
		public void accept(ChildOrder order) {
			Order parent = order.getParent();
			if (getChildWorkingQty(parent) == 0) {
				parent.cancel();
				parentOrderRepo.removeIfClosed(parent);
			}
		}
	}

	public void cancelFamily(Order<?> parent) {
		for (ChildOrder child : children.get(parent)) {
			requestRepo.requestCancel(child, idgen.getClOrdID());
			child.parentCanceller = parentCanceller;
		}
	}

	public long getChildWorkingQty(Order<?> parent) {
		long childQty = 0;
		for (ChildOrder child : children.get(parent)) {
			childQty += child.getWorkingQty();
		}
		return childQty;
	}
}

