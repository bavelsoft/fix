package com.bavelsoft.fix;

import java.util.function.Consumer;

public class ChildOrder<F> extends Order<F> {
	Order<?> parent;
	ChildOrderRepo.ParentCanceller parentCanceller;

	//must be called with init!
	public void setParent(Order<?> parent) {
		this.parent = parent;
		this.parentCanceller = null;

	}

	public void fill(long qty, double px) {
		super.fill(qty, px);
		parent.fill(qty, px);
	}


	protected void terminate(OrdStatus status) {
		super.terminate(status);
		if (parentCanceller != null) {
			parentCanceller.accept(this);
		}
	}
}
