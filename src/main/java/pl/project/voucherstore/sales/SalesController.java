package pl.project.voucherstore.sales;

import org.springframework.web.bind.annotation.*;
import pl.project.payment.payu.exceptions.PayUException;
import pl.project.voucherstore.sales.dto.AcceptOfferRequest;
import pl.project.voucherstore.sales.offer.Offer;
import pl.project.voucherstore.sales.payment.PaymentUpdateStatusRequest;

@RestController
public class SalesController {

    private final SalesFacade sales;

    public SalesController(SalesFacade sales) {
        this.sales = sales;
    }

    @GetMapping("/api/current-offer")
    public Offer currentOffer() {
        return sales.getCurrentOffer();
    }

    @PostMapping("/api/basket/add/{productId}")
    public void addToBasket(@PathVariable String productId) {
        sales.addToBasket(productId);
    }

    @PostMapping("/api/accept-offer")
    public void acceptOffer(@RequestBody AcceptOfferRequest acceptOfferRequest) throws PayUException {
        sales.acceptOffer(acceptOfferRequest.getClientDetails(), acceptOfferRequest.getSeenOffer());
    }

    @PostMapping("/api/payment/status")
    public void updatePaymentStatus(@RequestHeader("OpenPayu-Signature") String signatureHeader, @RequestBody String body) {
        PaymentUpdateStatusRequest updateStatusRequest = PaymentUpdateStatusRequest.of(signatureHeader, body);
        sales.handlePaymentStatusChange(updateStatusRequest);
    }
}
