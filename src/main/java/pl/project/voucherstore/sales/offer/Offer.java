package pl.project.voucherstore.sales.offer;

import java.math.BigDecimal;
import java.util.List;

public class Offer {
    private final List<OrderLine> orderItems;
    private final BigDecimal total;
    private final Integer productsCount;

    public Offer(List<OrderLine> orderItems, BigDecimal total) {
        this.orderItems = orderItems;
        this.total = total;
        this.productsCount = orderItems.size();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<OrderLine> getOrderItems() {
        return orderItems;
    }

    public Integer getProductsCount() {
        return productsCount;
    }

    public boolean isSameTotal(Offer seenOffer) {
        return seenOffer.getTotal().equals(total);
    }
}
