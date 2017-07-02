package com.bavelsoft.fix;

import java.util.Date;
import javax.inject.Inject;

/*
 * either pass everything through the entity
 * or observe changes to the entity, and combine with session scoped object
 * or have something that calls both the entities and the publisher
 */

public class ExecutingPublisher {
	@Inject ExecutionReportMessageFactory factory;

	//pessimistic clordid

	public void publishOrderCancelReject(
			Request request,
			CxlRejReason reason,
			Date transactTime,
			String text) {
		//TODO
	}

	//accepts, reject order
	public void publishExecutionReport(Order order, ExecType e) {
		ExecutionReport report = factory.create();
		report.setExecID(generateExecID());
		report.setExecType(e);
	}

	public void publishExecutionReport(Execution execution, ExecType e) {
		ExecutionReport report = factory.create();
		report.setExecID(generateExecID());
		report.setExecType(e);
	}

	//how do we get the exectype? request methods...

	private String generateExecID() { return null; }
}

class ExecutionReportMessageFactory {
	ExecutionReport create() {
		return new ExecutionReport();
	}
}

enum ExecType {};

enum CxlRejReason {};

class ExecutionReport {
	void setExecID(String execID) { }
	void setExecType(ExecType execID) { }
}
