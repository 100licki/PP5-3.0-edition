package pl.project.voucherstore.sales.payment;

import pl.project.payment.payu.PayU;
import pl.project.payment.payu.exceptions.PayUException;
import pl.project.payment.payu.model.Buyer;
import pl.project.payment.payu.model.OrderCreateRequest;
import pl.project.payment.payu.model.OrderCreateResponse;
import pl.project.payment.payu.model.Product;
import pl.project.voucherstore.sales.ordering.Reservation;

import java.util.stream.Collectors;

public class PayUPaymentGateway implements PaymentGateway {
    private final PayU payU;

    public PayUPaymentGateway(PayU payU) {
        this.payU = payU;
    }

    @Override
    public PaymentDetails registerFor(Reservation reservation) throws PaymentException {
        var orderCreateRequest = fromReservation(reservation);
        try {
            OrderCreateResponse response = payU.handle(orderCreateRequest);
            return PaymentDetails.builder()
                    .paymentUrl(response.getRedirectUri())
                    .paymentId(response.getOrderId())
                    .reservationId(reservation.getId())
                    .build();
        } catch (PayUException e) {
            throw new PaymentException(e);
        }
    }

    private OrderCreateRequest fromReservation(Reservation reservation) {
        return OrderCreateRequest.builder()
                .customerIp("127.0.0.1")
                .description("Reservation for items")
                .currencyCode("PLN")
                .totalAmount(reservation.getTotal())
                .extOrderId(reservation.getId())
                .buyer(Buyer.builder()
                        .firstName(reservation.getCustomerFirstname())
                        .lastName(reservation.getCustomerLastname())
                        .email(reservation.getCustomerEmail())
                        .language("pl")
                        .build())
                .products(
                        reservation.getProducts()
                            .stream()
                            .map(reservationItem -> new Product(
                                    reservationItem.getName(),
                                    reservationItem.getUnitPrice(),
                                    reservationItem.getQuantity()))
                            .collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean isTrusted(PaymentUpdateStatusRequest updateStatusRequest) {
        return payU.isTrusted(updateStatusRequest.getBody(), updateStatusRequest.getSignature());
    }
}
