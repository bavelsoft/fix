package com.bavelsoft.fix.order;

import java.util.LinkedList;
import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;

public class PendingRequests {
	private LinkedList<Request> pendingRequests = new LinkedList<>();
       	private Order order;

	PendingRequests(Order order) { this.order = order; }

	void add(Request request) {
		pendingRequests.add(request);
		updateOrderWith(request);
		request.addObserver();
	}

	void remove(Request request) {
		pendingRequests.remove(request);
		updateOrderWith(pendingRequests.isEmpty() ? null : pendingRequests.getLast());
	}

	private void updateOrderWith(Request request) {
		order.pendingOrdStatus = request == null ? null : request.getPendingOrdStatus();
		order.pendingOrderQty = request == null ? 0 : request.getOrderQty();
	}
}
