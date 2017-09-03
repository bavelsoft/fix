package com.bavelsoft.fix.order;

import java.util.LinkedList;
import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;

public abstract class Request {
        private String clOrdID;
        private Order order;

        protected void onAccept() {}
        protected void onReject() {}
	protected long getOrderQty() { return 0; }
        protected abstract OrdStatus getPendingOrdStatus();
	protected abstract ExecType getPendingExecType();
	protected abstract ExecType getAcceptedExecType();
	protected abstract void addObserver();

        public Request(Order order, String clOrdID) {
                this.order = order;
                this.clOrdID = clOrdID;
        }

        public void accept() {
		order.getPendingRequests().remove(this);
                onAccept();
        }

        public void reject() {
		order.getPendingRequests().remove(this);
                onReject();
        }

	public Order getOrder() {
		return order;
	}
}
