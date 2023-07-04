package com.bavelsoft.fix;

import com.bavelsoft.fix.OrdStatus;
import com.bavelsoft.fix.ExecType;

public abstract class Request {
        public final Order<?> order;

        private CharSequence clOrdID;
	private boolean isPending;

        public Request(Order<?> order) {
                this.order = order;
		reset();
        }

	public void reset() {
		isPending = false;
	}

	public Request init(CharSequence clOrdID) {
		isPending = true;
		this.clOrdID = clOrdID;
		return this;
	}

        public void reject() {
		isPending = false;
        }

        public void accept() {
		isPending = false;
        }

	public boolean isPending() {
		return isPending;
	}

	public CharSequence getClOrdID() {
		return clOrdID;
	}
}
