package com.bavelsoft.fix;

import java.util.LinkedList;
import com.bavelsoft.fix.OrdStatus;

public class Order {
        private Object fields;
	private String orderID;
        private double avgPx;
        private long orderQty, cumQty, leavesQty, pendingOrderQty;
	private OrdStatus terminalOrdStatus, pendingOrdStatus;
	private LinkedList<Request> requests = new LinkedList<>();

        void fill(long qty, double price) {
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
                    terminalOrdStatus != null ? terminalOrdStatus :
                     pendingOrdStatus != null ? pendingOrdStatus :
                           cumQty >= orderQty ? OrdStatus.Filled :
                                   cumQty > 0 ? OrdStatus.PartiallyFilled
                                              : OrdStatus.New;
        }

	public void updateWithRequest(Request request) {
		if (request.isPending()) {
			requests.add(request);
			//TODO observers
		} else {
			requests.remove(request);
		}
		pendingOrdStatus = requests.isEmpty() ? null : requests.getLast().getPendingOrdStatus();
		pendingOrderQty = requests.stream().mapToLong(x->x.getPendingOrderQty()).max().orElse(0);
	}

        public void done() {
                leavesQty = 0;
                terminalOrdStatus = OrdStatus.DoneForDay;
        }

        public void reject() {
                leavesQty = 0;
                terminalOrdStatus = OrdStatus.Rejected;
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
