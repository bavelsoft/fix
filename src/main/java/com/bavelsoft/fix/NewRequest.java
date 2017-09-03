package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;
import com.bavelsoft.fix.order.Request;
import com.bavelsoft.fix.order.Order;

public class NewRequest extends Request {
        public NewRequest(Order order, String clOrdID) {
                super(order, clOrdID);
        }

	@Override
        protected void onReject() {
		getOrder().reject();
        }

	@Override
        protected OrdStatus getPendingOrdStatus() {
                return OrdStatus.PendingNew;
        }

	@Override
        protected ExecType getPendingExecType() {
                return ExecType.PendingNew;
        }

	@Override
        protected ExecType getAcceptedExecType() {
                return ExecType.New;
        }

	@Override
        protected void addObserver() {
		//accept if the order is filled
        }
}
