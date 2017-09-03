package com.bavelsoft.fix;

import java.util.LinkedList;

public abstract class Request {
        private String clOrdID;
        private Order order;

        protected void onAccept() {}
        protected void onReject() {}
        protected abstract OrdStatus getPendingOrdStatus();
	protected abstract ExecType getPendingExecType();
	protected abstract ExecType getAcceptedExecType();
	protected abstract void addObserver();

        public Request(Order order, String clOrdID) {
                this.order = order;
                this.clOrdID = clOrdID;
        }

        public void accept() {
		order.requests.remove(this);
                onAccept();
        }

        public void reject() {
		order.requests.remove(this);
                onReject();
        }

	public static class List {
		LinkedList<Request> requests = new LinkedList<>();
	
		void add(Request request) {
			requests.add(request);
			request.updateAsLast();
			request.addObserver();
		}

		void remove(Request request) {
			requests.remove(request);
			requests.getLast().updateAsLast();
		}
	}

	void updateAsLast() {
		order.pendingOrdStatus = getPendingOrdStatus();
	}

	public Order getOrder() {
		return order;
	}
}
