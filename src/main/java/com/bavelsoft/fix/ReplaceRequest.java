package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.PendingReplace;

public class ReplaceRequest extends Request {
	private Object fields;

        public ReplaceRequest(Order order, Object fields) {
                super(order);
                this.fields = fields;
        }

	@Override
        OrdStatus getPendingOrdStatus() {
                return PendingReplace;
        }

	@Override
        protected void onAccept() {
                order.replace(fields);
        }
}


