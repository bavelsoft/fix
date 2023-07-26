package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;

//TODO another generic for things that change not according to replaces? or let them compose/extend us? listeners?

public class Order<F> {
	F fields;
	private long orderID, orderQty, leavesQty, cumQty;
	private double avgPx;
	private OrdStatus terminalOrdStatus;
	final RequestNew newRequest = new RequestNew(this);
	final RequestReplace replaceRequest = new RequestReplace(this);
	final RequestCancel cancelRequest = new RequestCancel(this);

	public Order<F> init(F newFields, long orderID, long requestedOrderQty) {
		this.fields = newFields;
		this.orderID = orderID;
		this.orderQty = requestedOrderQty;
		this.leavesQty = requestedOrderQty;
		this.cumQty = 0;
		this.avgPx = 0;
		this.terminalOrdStatus = null;
		resetRequests();
		return this;
	}

	private void resetRequests() {
		replaceRequest.reset();
		cancelRequest.reset();
		newRequest.reset();
	}

	public void fill(long qty, double px) {
		double totalValue = qty * px + cumQty * avgPx;
		avgPx = totalValue / (qty + cumQty);
		cumQty += qty;
		leavesQty -= qty;
		if (leavesQty <= 0) {
			terminate(OrdStatus.Filled);
		}
	}

	public void replace(F newFields, long requestedOrderQty) {
		long newOrderQty = Math.max(requestedOrderQty, cumQty);
		leavesQty += newOrderQty - orderQty;
		orderQty = newOrderQty;
		fields = newFields;
		if (leavesQty <= 0) {
			terminate(OrdStatus.Filled);
		}
	}

	public void cancel() {
		cancel(leavesQty);
	}

	public void cancel(long qtyChange) {
		leavesQty -= qtyChange;
		if (leavesQty <= 0) {
			terminate(OrdStatus.Canceled);
		}
	}

	public OrdStatus getOrdStatus() {
		return
			terminalOrdStatus != null  ? terminalOrdStatus :
			cancelRequest.isPending()  ? cancelRequest.getStatus() :
			replaceRequest.isPending() ? replaceRequest.getStatus() :
			cumQty > 0                 ? OrdStatus.PartiallyFilled :
			                             newRequest.getStatus();
	}

	public void done() {
		terminate(OrdStatus.DoneForDay);
	}

	protected void terminate(OrdStatus status) {
		terminalOrdStatus = status;
		leavesQty = 0;
		resetRequests();
	}

	/**
	 * @return whether the sell-side triggered this call in an expected way
	 */
	public boolean exec(ExecType execType, CharSequence clOrdID) {
		return FixOrderUtil.exec(this, execType, clOrdID);
	}

	public long getOrderID() {
		return orderID;
	}

	public long getOrderQty() {
		return orderQty;
	}

	public long getCumQty() {
		return cumQty;
	}

	public long getLeavesQty() {
		return leavesQty;
	}

	public double getAvgPx() {
		return avgPx;
	}
}
