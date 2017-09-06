package com.bavelsoft.fix;

import com.bavelsoft.fix.Order;

public class Execution {
	private Order order;
        private long qty;
        private double price;

        public Execution(Order order, long qty, double price) {
		this.order = order;
                this.qty = qty;
                this.price = price;
        }

        public void cancel() {
                order.fill(-qty, price);
        }

        public void correct(long qty, double price) {
                cancel();
                this.qty = qty;
                this.price = price;
                order.fill(qty, price);
        }

	public Order getOrder() {
		return order;
	}

	public long getQty() {
		return qty;
	}

	public double getPrice() {
		return price;
	}
}
