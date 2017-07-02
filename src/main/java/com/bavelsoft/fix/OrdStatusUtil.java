package com.bavelsoft.fix;

import static com.bavelsoft.fix.OrdStatus.New;
import static com.bavelsoft.fix.OrdStatus.PartiallyFilled;
import static com.bavelsoft.fix.OrdStatus.Filled;

public class OrdStatusUtil {
        public static OrdStatus getOrdStatus(Order order) {
                Request pendingRequest = order.getLastPendingRequest();
                if (order.getTerminalStatus() != null)
                        return order.getTerminalStatus();
                else if (order.getCumQty() >= order.getOrderQty())
                        return Filled;
                else if (pendingRequest != null)
                        return pendingRequest.getPendingOrdStatus();
                else if (order.getCumQty() > 0)
                        return PartiallyFilled;
                else
                        return New;
        }
}
