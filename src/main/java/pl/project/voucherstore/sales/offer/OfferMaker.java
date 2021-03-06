package pl.project.voucherstore.sales.offer;

import pl.project.voucherstore.sales.basket.BasketItem;
import pl.project.voucherstore.sales.productd.ProductDetails;
import pl.project.voucherstore.sales.productd.ProductDetailsProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OfferMaker {

    ProductDetailsProvider productDetailsProvider;

    public OfferMaker(ProductDetailsProvider productDetailsProvider) {
        this.productDetailsProvider = productDetailsProvider;
    }

    public Offer calculateOffer(List<BasketItem> basketItems) {
        List<OrderLine> orderItems = basketItems.stream()
                .map(this::createOrderLine)
                .collect(Collectors.toUnmodifiableList());

        return new Offer(orderItems, calculateTotal(orderItems));
    }

    private OrderLine createOrderLine(BasketItem basketItem) {
        ProductDetails details = productDetailsProvider.getByProductId(basketItem.getProductId());

        return new OrderLine(
                basketItem.getProductId(),
                details.getDescription(),
                details.getUnitPrice(),
                basketItem.getQuantity());
    }

    private BigDecimal calculateTotal(List<OrderLine> orderItems) {
        return orderItems.stream()
                .map(orderLine -> orderLine.getUnitPrice().multiply(new BigDecimal(orderLine.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
