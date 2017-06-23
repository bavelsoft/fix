package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.Rejected;
import static com.bavelsoft.fix.OrdStatus.DoneForDay;
import static com.bavelsoft.fix.OrdStatus.Canceled;

public class Order {
        private Object fields;
        private long cumQty, leavesQty, orderID;
        private double avgPx;
        Request lastRequest;
        OrdStatus terminalStatus;

        public void fill(Execution x) {
                avgPx = x.getNewAvgPx(this); //must be before changing cumQty
                cumQty += x.getQty();
                leavesQty -= x.getQty();
        }

        public void request(Request request) {
                request.previousRequest = lastRequest;
                lastRequest = request;
        }

        public void replace(Object fields) {
                this.fields = fields;
        }

        public void cancel() {
                cancel(leavesQty);
        }

        public void cancel(long qtyChange) {
                leavesQty -= qtyChange;
		if (leavesQty <= 0)
			terminalStatus = Canceled;
        }

        public void reject() {
                leavesQty = 0;
                terminalStatus = Rejected;
        }

        public void done() {
                leavesQty = 0;
                terminalStatus = DoneForDay;
        }

	public long getCumQty() {
		return cumQty;
	}

	public double getAvgPx() {
		return avgPx;
	}

	public long getOrderQty() {
		return 0; //TODO should come from fields
	}
}
