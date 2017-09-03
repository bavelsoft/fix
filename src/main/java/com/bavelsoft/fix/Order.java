package com.bavelsoft.fix;

import java.util.List;
import java.util.ArrayList;

public class Order {
        private Object fields;
	private String orderID;
        private double avgPx;
        private long orderQty, cumQty;
	long pendingOrderQty, leavesQty;
        OrdStatus terminalOrdStatus, pendingOrdStatus;
	Request.List requests = new Request.List(this);

        public void fill(long qty, double price) {
		double totalValue = qty * price + this.cumQty * this.avgPx;
                this.avgPx = totalValue / (this.cumQty + qty);
                this.cumQty += qty;
                this.leavesQty -= qty;
        }

        public void replace(Object newFields, long newOrderQty) {
		newOrderQty = Math.max(newOrderQty, cumQty);
		this.leavesQty += newOrderQty - orderQty;
                this.orderQty = newOrderQty;
                this.fields = newFields;
        }

        public void cancel() {
                cancel(leavesQty);
        }

        public void cancel(long qtyChange) {
                leavesQty -= qtyChange;
		if (leavesQty <= 0)
			terminalOrdStatus = OrdStatus.Canceled;
        }

        public OrdStatus getOrdStatus(Order order) {
		return
                 pendingOrdStatus != null  ? pendingOrdStatus :
                 terminalOrdStatus != null ? terminalOrdStatus :
                        cumQty >= orderQty ? OrdStatus.Filled :
                                cumQty > 0 ? OrdStatus.PartiallyFilled
                                           : OrdStatus.New;
        }

        public void done() {
                leavesQty = 0;
                terminalOrdStatus = OrdStatus.DoneForDay;
        }

	public Request.List getPendingRequests() {
		return requests;
	}

	public OrdStatus getTerminalStatus() {
		return terminalOrdStatus;
	}

	public long getOrderQty() {
		return orderQty;
	}

	public long getCumQty() {
		return cumQty;
	}

	public long getLeavesQty() {
		return leavesQty;
	}

	public String getOrderID() {
		return orderID;
	}

	public double getAvgPx() {
		return avgPx;
	}
}
