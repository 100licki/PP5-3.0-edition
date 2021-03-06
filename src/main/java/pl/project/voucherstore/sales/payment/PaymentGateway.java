package pl.project.voucherstore.sales.payment;

import pl.project.payment.payu.exceptions.PayUException;
import pl.project.voucherstore.sales.ordering.Reservation;

public interface PaymentGateway {
    PaymentDetails registerFor(Reservation reservation) throws PayUException;

    boolean isTrusted(PaymentUpdateStatusRequest updateStatusRequest);
}
