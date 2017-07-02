package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.PendingReplace;

public class ReplaceRequest extends Request {
	private Object fields;

        public ReplaceRequest(Order order, Object fields) {
                super(order);
                this.fields = fields;
        }

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return PendingReplace;
        }

	@Override
        protected void onAccept() {
                getOrder().replace(fields);
        }
}


