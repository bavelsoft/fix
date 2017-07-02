package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.PendingReplace;

public class ReplaceRequest extends Request {
	private Object fields;
	private long orderQty;

        public ReplaceRequest(Order order, String clOrdID, Object fields, long orderQty) {
                super(order, clOrdID);
                this.fields = fields;
                this.orderQty = orderQty;
        }

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return PendingReplace;
        }

	@Override
        protected void onAccept() {
                getOrder().replace(fields, orderQty);
        }
}


