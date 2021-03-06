package pl.project.voucherstore.sales.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PaymentDetails {
    String reservationId;
    String paymentUrl;
    String paymentId;


}
