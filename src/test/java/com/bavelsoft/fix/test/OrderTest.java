package com.bavelsoft.fix.test;
//leaves in a nonstandard package to not exploit any package private methods

import com.bavelsoft.fix.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientFields {
	double price;
}

class ExchangeFields {
	Order parent;
	double price;
}

public class OrderTest {
	private RequestRepo clientRequestRepo = new RequestRepo(new HashMap());
	private RequestRepo exchangeRequestRepo = new RequestRepo(new HashMap());
	private OrderRepo clientOrderRepo;
	private OrderRepo exchangeOrderRepo;
	private IdGenerator idgen = new IdGenerator();

	@BeforeEach
	public void setup() {
		clientOrderRepo = new OrderRepo() {{
			map = new HashMap();
			pool = new OrderPool(new ArrayList(), 10);
			idgen = idgen;
			requestRepo = clientRequestRepo;
		}};

		exchangeOrderRepo = new OrderRepo() {{
			map = new HashMap();
			pool = new OrderPool(new ArrayList(), 10);
			idgen = idgen;
			requestRepo = exchangeRequestRepo;
		}};
	}
	
	@Test
	public void testClientOrder() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		assertEquals(OrdStatus.PendingNew, order.getOrdStatus());
	}

	@Test
	public void testClientAccept() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		clientRequestRepo.get("111_clOrdID_from_client").accept();

		assertEquals(OrdStatus.New, order.getOrdStatus());
	}

	@Test
	public void testClientReject() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		clientRequestRepo.get("111_clOrdID_from_client").reject();

		assertEquals(OrdStatus.Rejected, order.getOrdStatus());
	}

	@Test
	public void testClientReplaceRequest() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		clientRequestRepo.requestReplace(order, "222_clOrdID_from_client");

		assertEquals(OrdStatus.PendingReplace, order.getOrdStatus());
	}

	@Test
	public void testClientCancelRequest() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		clientRequestRepo.requestCancel(order, "333_clOrdID_from_client");

		assertEquals(OrdStatus.PendingCancel, order.getOrdStatus());
	}

	@Test
	public void testClientCancel() {
		Order order = clientOrderRepo.requestNew(new ClientFields(), 100, "111_clOrdID_from_client");

		order.cancel();

		assertEquals(OrdStatus.Canceled, order.getOrdStatus());
	}

	@Test
	public void testExchangeOrder() {
		Order<ExchangeFields> order = exchangeOrderRepo.requestNew(new ExchangeFields(), 100, idgen.getClOrdID());

		assertEquals(OrdStatus.PendingNew, order.getOrdStatus());
		assertEquals(100, order.getWorkingQty());
	}

	@Test
	public void testExchangeCancelRequest() {
		Order<ExchangeFields> order = exchangeOrderRepo.requestNew(new ExchangeFields(), 100, idgen.getClOrdID());

		exchangeRequestRepo.requestCancel(order, idgen.getClOrdID());

		assertEquals(OrdStatus.PendingCancel, order.getOrdStatus());
	}

	@Test
	public void testExchangeReplaceRequest() {
		//TODO
		ExchangeFields fields = new ExchangeFields();
		fields.parent = new Order();
		fields.price = 1;
		Order<ExchangeFields> order = exchangeOrderRepo.requestNew(fields, 100, idgen.getClOrdID());

		//TODO improve this api?
		RequestReplace<ExchangeFields> replaceRequest = exchangeRequestRepo.requestReplace(order, idgen.getClOrdID());
		ExchangeFields newFields = new ExchangeFields();
		newFields.parent = fields.parent;
		newFields.price = 2;
		replaceRequest.pendingOrderQty = 120;
		replaceRequest.pendingFields = newFields;

		assertEquals(120, order.getWorkingQty());
	}

	@Test
	public void testExchangeAccept() {
		Order order = exchangeOrderRepo.requestNew(new ExchangeFields(), 100, "myClOrdID");

		boolean success = exchangeRequestRepo.get("myClOrdID").order.exec(ExecType.New, "myClOrdID");

		assertEquals(true, success);
		assertEquals(OrdStatus.New, order.getOrdStatus());
	}

	@Test
	public void testExchangeReject() {
		Order order = exchangeOrderRepo.requestNew(new ExchangeFields(), 100, "myClOrdID");

		boolean success = exchangeRequestRepo.get("myClOrdID").order.exec(ExecType.Rejected, "myClOrdID");

		assertEquals(true, success);
		assertEquals(OrdStatus.Rejected, order.getOrdStatus());
	}
}

