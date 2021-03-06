package pl.project.voucherstore.sales.basket;

import org.junit.Test;
import pl.project.voucherstore.productcatalog.Product;
import pl.project.voucherstore.sales.Inventory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class BasketTest {

    public static final String PRODUCT_1 = "lego-8297";
    public static final String PRODUCT_2 = "lego-8298";

    @Test
    public void newlyCreateBasketIsEmpty() {
        Basket basket = Basket.empty();

        assertThat(basket.isEmpty())
                .isTrue();
    }

    @Test
    public void basketWithProductsIsNotEmpty() {
        //Arrange
        Basket basket = Basket.empty();
        Product product = thereIsProduct(PRODUCT_1);
        //Act
        basket.add(product, thereIsAllwaysExistingProductInventory());

        //Assert
        assertThat(basket.isEmpty())
                .isFalse();
    }

    @Test
    public void itShowsProductsCount() {
        //Arrange
        Basket basket = Basket.empty();
        Product product1 = thereIsProduct(PRODUCT_1);
        Product product2 = thereIsProduct(PRODUCT_2);
        //Act
        basket.add(product1, thereIsAllwaysExistingProductInventory());
        basket.add(product2, thereIsAllwaysExistingProductInventory());

        //Assert
        assertThat(basket.getProductsQuantities())
            .isEqualTo(2);
    }

    @Test
    public void itShowsSingleLineWhenSameProductsAddedTwice() {
        //Arrange
        Basket basket = Basket.empty();
        Product product1 = thereIsProduct(PRODUCT_1);
        //Act
        basket.add(product1, thereIsAllwaysExistingProductInventory());
        basket.add(product1, thereIsAllwaysExistingProductInventory());

        //Assert
        assertThat(basket.getProductsQuantities())
                .isEqualTo(1);

        basketContainsProductWithQuantity(basket, product1, 2);
    }

    @Test
    public void itStoreQuantityOfMultipleProducts() {
        //Arrange
        Basket basket = Basket.empty();
        Product product1 = thereIsProduct(PRODUCT_1);
        Product product2 = thereIsProduct(PRODUCT_2);
        //Act
        basket.add(product1, thereIsAllwaysExistingProductInventory());
        basket.add(product1, thereIsAllwaysExistingProductInventory());
        basket.add(product2, thereIsAllwaysExistingProductInventory());

        //Assert
        assertThat(basket.getProductsQuantities())
                .isEqualTo(2);

        basketContainsProductWithQuantity(basket, product1, 2);
        basketContainsProductWithQuantity(basket, product2, 1);
    }

    @Test
    public void itDennyToAddProductWithInventoryStateOf0() {
        Basket basket = Basket.empty();
        Product product1 = thereIsProduct(PRODUCT_1);

        assertThatThrownBy(() -> basket.add(product1, (productId) -> false))
                .hasMessage("Not enough products");

    }

    private Inventory thereIsAllwaysExistingProductInventory() {
        return (productId -> true);
    }

    private void basketContainsProductWithQuantity(Basket basket, Product product1, int expectedQuantity) {
        assertThat(basket.getBasketItems())
            .filteredOn(basketItem -> basketItem.getProductId().equals(product1.getId()))
            .extracting(BasketItem::getQuantity)
            .first()
            .isEqualTo(expectedQuantity)
        ;
    }

    private Product thereIsProduct(String description) {
        var product = new Product(UUID.randomUUID());
        product.setDescription(description);

        return product;
    }
}
