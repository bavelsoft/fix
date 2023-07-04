package com.bavelsoft.fix;

import java.util.List;

public class OrderPool<F> {
	private final List<Order<F>> pool;

	//search for Field Declaration, Type Parameter, Usage in extends/implements clauses of the class and its interfaces 
	//make sure all of those (except this class) are updated before we call release, or check them in turn
	//

	public OrderPool(List<Order<F>> poolList, int size) {
		this.pool = poolList;
		for (int i=0; i<size; i++) {
			pool.add(new Order());
		}
	}

	public Order<F> getOrder() {
		return pool.remove(pool.size()-1);
	}

	public void release(Order<F> order) {
		pool.add(order);
	}
}
