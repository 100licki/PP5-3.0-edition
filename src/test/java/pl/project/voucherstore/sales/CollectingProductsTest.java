package pl.project.voucherstore.sales;

import org.junit.Before;
import org.junit.Test;
import pl.project.voucherstore.sales.basket.Basket;
import pl.project.voucherstore.sales.offer.Offer;
import pl.project.voucherstore.sales.offer.OrderLine;

import java.math.BigDecimal;


import static org.assertj.core.api.Assertions.*;

public class CollectingProductsTest extends SalesTestCase {

    @Before
    public void setUp() {
        productCatalog = thereIsProductCatalog();
        basketStorage = thereIsBasketStore();
        alwaysExistsInventory = thereIsInventory();
        currentCustomerContext = thereIsCurrentCustomerContext();
        offerMaker = thereIsOfferMaker(productCatalog);
    }

    @Test
    public void itAllowAddProductToBasket()
    {
        //Arrange
        SalesFacade sales = thereIsSalesModule();
        String productId1 = thereIsProductAvailable();
        String productId2 = thereIsProductAvailable();
        customerId = thereIsCustomerWhoIsDoingSomeShopping();

        //Act
        sales.addToBasket(productId1);
        sales.addToBasket(productId2);
        //Assert
        thereIsXproductsInCustomersBasket(2, customerId);
    }

    @Test
    public void itAllowAddProductToBasketByMultipleCustomers() {
        //Arrange
        SalesFacade sales = thereIsSalesModule();
        String productId1 = thereIsProductAvailable();
        String productId2 = thereIsProductAvailable();

        customerId = thereIsCustomerWhoIsDoingSomeShopping();
        var customer1 = new String(customerId);
        //Act
        sales.addToBasket(productId1);
        sales.addToBasket(productId2);

        customerId = thereIsCustomerWhoIsDoingSomeShopping();
        var customer2 = new String(customerId);
        sales.addToBasket(productId2);

        //Assert
        thereIsXproductsInCustomersBasket(2, customer1);
        thereIsXproductsInCustomersBasket(1, customer2);
    }

    @Test
    public void itGenerateOfferBasedOnCurrentBasket() {
        //Arrange
        SalesFacade sales = thereIsSalesModule();
        String productId1 = thereIsProductAvailable();
        String productId2 = thereIsProductAvailable();

        customerId = thereIsCustomerWhoIsDoingSomeShopping();
        var customer1 = new String(customerId);

        //Act
        sales.addToBasket(productId1);
        sales.addToBasket(productId1);
        sales.addToBasket(productId2);

        Offer offer = sales
                .getCurrentOffer();

        assertThat(offer.getTotal()).isEqualTo(BigDecimal.valueOf(30));
        assertThat(offer.getOrderItems())
                .hasSize(2);
        assertThat(offer.getOrderItems())
                .filteredOn(orderLine -> orderLine.getProductId().equals(productId1))
                .extracting(OrderLine::getQuantity)
                .first()
                .isEqualTo(2);

    }

    @Test
    public void itGenerateOfferBasedOnCurrentBasketWithSingleProduct() {
        //Arrange
        SalesFacade sales = thereIsSalesModule();
        String productId1 = thereIsProductAvailable();

        customerId = thereIsCustomerWhoIsDoingSomeShopping();

        //Act
        sales.addToBasket(productId1);

        Offer offer = sales
                .getCurrentOffer();

        assertThat(offer.getTotal()).isEqualTo(BigDecimal.valueOf(10));
        assertThat(offer.getOrderItems())
                .hasSize(1);
    }

    private void thereIsXproductsInCustomersBasket(int productsCount, String customerId) {
        Basket basket = basketStorage.loadForCustomer(customerId)
                .orElse(Basket.empty());

        assertThat(basket.getProductsQuantities()).isEqualTo(productsCount);
    }
}
