package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;
import com.bavelsoft.fix.Request;
import com.bavelsoft.fix.Order;

public class RequestNew extends Request {
	private OrdStatus status;

	public RequestNew(Order<?> order) {
	        super(order);
	}

	@Override
	public void reset() {
		status = OrdStatus.PendingNew;
	}

	@Override
	public void accept() {
		super.reject();
		status = OrdStatus.New;
	}

	@Override
	public void reject() {
		super.reject();
		status = OrdStatus.Rejected;
	}

        protected OrdStatus getStatus() {
                return status;
        }
}
