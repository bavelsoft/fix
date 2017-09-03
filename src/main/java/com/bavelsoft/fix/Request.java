package com.bavelsoft.fix;

import java.util.LinkedList;

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

	public static class List {
		private LinkedList<Request> pendingRequests = new LinkedList<>();
        	private Order order;

		List(Order order) { this.order = order; }
	
		void add(Request request) {
			pendingRequests.add(request);
			updateOrderWith(request);
			request.addObserver();
		}

		void remove(Request request) {
			pendingRequests.remove(request);
			updateOrderWith(pendingRequests.isEmpty() ? null : pendingRequests.getLast());
		}

		private void updateOrderWith(Request request) {
			order.pendingOrdStatus = request == null ? null : request.getPendingOrdStatus();
			order.pendingOrderQty = request == null ? 0 : request.getOrderQty();
		}
	}

	public Order getOrder() {
		return order;
	}
}
