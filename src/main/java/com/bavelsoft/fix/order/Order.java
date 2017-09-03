package com.bavelsoft.fix.order;

import java.util.List;
import java.util.ArrayList;
import com.bavelsoft.fix.OrdStatus;

public class Order {
        private Object fields;
	private String orderID;
        private double avgPx;
        private long orderQty, cumQty, leavesQty;
	private OrdStatus terminalOrdStatus;
	private PendingRequests pendingRequests = new PendingRequests(this);
	long pendingOrderQty;
        OrdStatus pendingOrdStatus;

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

        public void reject() {
                leavesQty = 0;
                terminalOrdStatus = OrdStatus.Rejected;
        }

	public PendingRequests getPendingRequests() {
		return pendingRequests;
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
