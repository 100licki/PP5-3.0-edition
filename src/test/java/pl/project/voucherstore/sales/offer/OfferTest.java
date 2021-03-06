package pl.project.voucherstore.sales.offer;

import org.junit.Test;
import pl.project.voucherstore.sales.basket.BasketItem;
import pl.project.voucherstore.sales.productd.ProductDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.*;


public class OfferTest {
    @Test
    public void itCreateOfferBasedOnBasketItems() {
        List<BasketItem> basketItems = Arrays.asList(
                new BasketItem("prod1", 2),
                new BasketItem("prod2", 1)
        );
        OfferMaker offerMaker = thereIsOfferMaker();

        Offer offer = offerMaker.calculateOffer(basketItems);

        assertThat(offer.getTotal())
                .isEqualTo(BigDecimal.valueOf(30));
    }

    @Test
    public void itCreateOfferBasedOnSingleBasketItems() {
        List<BasketItem> basketItems = Collections.singletonList(
                new BasketItem("prod2", 1)
        );
        OfferMaker offerMaker = thereIsOfferMaker();

        Offer offer = offerMaker.calculateOffer(basketItems);

        assertThat(offer.getTotal())
                .isEqualTo(BigDecimal.valueOf(10));
    }

    private OfferMaker thereIsOfferMaker() {
        return new OfferMaker(productId -> new ProductDetails(productId, String.format("%s-desc", productId), BigDecimal.valueOf(10)));
    }
}
