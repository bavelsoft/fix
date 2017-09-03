package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;

public class ReplaceRequest extends Request {
	private Object fields;
	private long orderQty;

        public ReplaceRequest(Order order, String clOrdID, Object fields, long orderQty) {
                super(order, clOrdID);
                this.fields = fields;
                this.orderQty = orderQty;
        }

	@Override
        protected void onAccept() {
                getOrder().replace(fields, orderQty);
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
	public long getOrderQty() {
		return orderQty;
	}
}
