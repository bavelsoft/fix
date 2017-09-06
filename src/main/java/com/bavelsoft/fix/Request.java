package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;

public abstract class Request {
        private String clOrdID;
        private Order order;
	private boolean isPending = true;

        protected void onAccept() {}
        protected void onReject() {}
	public long getPendingOrderQty() { return 0; }
        protected abstract OrdStatus getPendingOrdStatus();
	protected abstract ExecType getPendingExecType();
	protected abstract ExecType getAcceptedExecType();
        protected abstract void addObserver();

        public Request(Order order, String clOrdID) {
                this.order = order;
                this.clOrdID = clOrdID;
        }

        public void accept() {
		isPending = false;
                onAccept();
        }

        public void reject() {
		isPending = false;
                onReject();
        }

	public boolean isPending() {
		return isPending;
	}

	public Order getOrder() {
		return order;
	}
}
