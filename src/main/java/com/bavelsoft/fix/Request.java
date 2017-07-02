package com.bavelsoft.fix;

public abstract class Request {
        private String clOrdID;
        private boolean isRejected, isAccepted;
        private Order order;

        protected void onAccept() {}
        protected void onReject() {}
        protected abstract OrdStatus getPendingOrdStatus();

        public Request(Order order) {
                this.order = order;
        }

        public void accept() {
                isAccepted = true;
                onAccept();
        }

        public void reject() {
                isRejected = true;
                onReject();
        }

	public boolean isPending() {
		return !isAccepted && !isRejected;
	}

	protected Order getOrder() {
		return order;
	}
}
