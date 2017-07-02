package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.PendingNew;

public class NewRequest extends Request {
        public NewRequest(Order order) {
                super(order);
        }

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return PendingNew;
        }

	@Override
        protected void onReject() {
                getOrder().reject();
        }
}


