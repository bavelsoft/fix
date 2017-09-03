package com.bavelsoft.fix.request;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;
import com.bavelsoft.fix.order.Request;
import com.bavelsoft.fix.order.Order;

public class ReplaceRequest extends Request {
	private Object fields;
	private long pendingOrderQty;

        public ReplaceRequest(Order order, String clOrdID, Object fields, long pendingOrderQty) {
                super(order, clOrdID);
                this.fields = fields;
                this.pendingOrderQty = pendingOrderQty;
        }

	@Override
        protected void onAccept() {
                getOrder().replace(fields, pendingOrderQty);
        }

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return OrdStatus.PendingReplace;
        }

	@Override
        protected ExecType getPendingExecType() {
                return ExecType.PendingReplace;
        }

	@Override
        protected ExecType getAcceptedExecType() {
                return ExecType.Replaced;
        }

	@Override
        protected void addObserver() {
		//reject if the order is cancelled, filled
        }

	@Override
	public long getPendingOrderQty() {
		return pendingOrderQty;
	}
}
