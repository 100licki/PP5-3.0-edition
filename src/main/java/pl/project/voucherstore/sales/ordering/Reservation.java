package pl.project.voucherstore.sales.ordering;

import pl.project.voucherstore.sales.ClientDetails;
import pl.project.voucherstore.sales.offer.Offer;
import pl.project.voucherstore.sales.payment.PaymentDetails;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Reservation {
    @Id
    private String id;

    @Embedded
    private ClientDetails clientDetails;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationItem> items;

    private boolean isPaid;
    private Integer total;

    private String paymentUrl;
    private String paymentId;

    public Reservation(String id, ClientDetails clientDetails, List<ReservationItem> items, BigDecimal total) {
        this.id = id;
        this.clientDetails = clientDetails;
        this.items = items;
        this.isPaid = false;
        this.total = total.intValue() * 100;
    }

    public static Reservation of(Offer offer, ClientDetails clientDetails) {
        List<ReservationItem> items =
                offer.getOrderItems().stream()
                .map(item -> new ReservationItem(item.getProductId(), item.getDescription(), item.getUnitPrice(), item.getQuantity()))
                .collect(Collectors.toList());
        return new Reservation(UUID.randomUUID().toString(), clientDetails, items, offer.getTotal());
    }

    public String getId() {
        return id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public boolean isPending() {
        return !isPaid;
    }

    public boolean isCompleated() {
        return isPaid;
    }

    public void fillPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentId = paymentDetails.getPaymentId();
        this.paymentUrl = paymentDetails.getPaymentUrl();
    }

    public List<ReservationItem> getProducts() {
        return Collections.emptyList();
    }

    public Integer getTotal() {
        return total;
    }

    public String getCustomerFirstname() {
        return clientDetails.getFirstname();
    }

    public String getCustomerLastname() {
        return clientDetails.getLastname();
    }

    public String getCustomerEmail() {
        return clientDetails.getEmail();
    }
}
