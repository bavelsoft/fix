package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.PendingCancel;

public class CancelRequest extends Request {
	public CancelRequest(Order order, String clOrdID) {
		super(order, clOrdID);
	}

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return PendingCancel;
        }

	@Override
        protected void onAccept() {
                getOrder().cancel();
        }
}

