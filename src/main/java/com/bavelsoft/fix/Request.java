package com.bavelsoft.fix;

public abstract class Request {
        private String clOrdID;
        private boolean isRejected, isAccepted;
        protected Order order;
        protected Request previousRequest;

        Request(Order order) {
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

        public Request getLastPending() {
                Request request = this;
                while (request != null && (request.isRejected || request.isAccepted))
                        request = request.previousRequest;
                return request;
        }

        protected void onAccept() {
	}

        protected void onReject() {
	}

        abstract OrdStatus getPendingOrdStatus();
}


