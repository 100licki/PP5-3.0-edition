package pl.project.voucherstore.sales.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentUpdateStatusRequest {
    String signature;
    String algorithm;
    String body;

    public static PaymentUpdateStatusRequest of(String signatureHeader, String body) {
        return new PaymentUpdateStatusRequest(
                signatureHeader.split(";")[1].replace("signature=", ""),
                signatureHeader.split(";")[2].replace("algorithm=", ""),
                body
        );
    }
}
