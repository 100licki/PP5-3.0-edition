package pl.project.voucherstore.productcatalog;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ProductCatalogTest {

    public static final String MY_PRODUCT_DESC = "my fancy product";
    public static final String MY_PRODUCT_PICTURE = "http://my_image.pl/image.jpeg";

    @Test
    public void itAllowCreateProduct() {
        //A
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        //A
        String productId = productCatalog.createProduct();
        //A
        Assert.assertTrue((productCatalog.getById(productId)).getId().equals(productId));
        Assert.assertTrue(productCatalog.isExistsById(productId));
    }

    @Test
    public void itAllowSetProductDescription() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        String productId = productCatalog.createProduct();

        productCatalog.updateDetails(productId, MY_PRODUCT_DESC, MY_PRODUCT_PICTURE);
        Product loadedProduct = productCatalog.getById(productId);

        Assert.assertEquals(MY_PRODUCT_DESC, loadedProduct.getDescription());
        Assert.assertEquals(MY_PRODUCT_PICTURE, loadedProduct.getPicture());
    }

    @Test
    public void itAllowApplyPrice() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        String productId = productCatalog.createProduct();

        productCatalog.applyPrice(productId, BigDecimal.valueOf(20.20));
        Product loadedProduct = productCatalog.getById(productId);

        Assert.assertTrue(BigDecimal.valueOf(20.20).equals(loadedProduct.getPrice()));
    }

    @Test
    public void itAllowsLoadAllProducts() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        String draftProductId = productCatalog.createProduct();
        String productId = productCatalog.createProduct();

        productCatalog.applyPrice(productId, BigDecimal.valueOf(20.20));
        productCatalog.updateDetails(productId, MY_PRODUCT_DESC, MY_PRODUCT_PICTURE);
        List<Product> all = productCatalog.getAvailableProducts();

        assertThat(all)
                .hasSize(1)
                .extracting(Product::getId)
                .contains(productId)
                .doesNotContain(draftProductId);
    }

    @Test
    public void itDenyActionOnProductThatNotExistsV1() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        try {
            productCatalog.applyPrice("notExists", BigDecimal.TEN);
            Assert.fail("should throw exception");
        } catch (ProductNotFoundException e) {
            Assert.assertTrue(true);
        }
    }

    @Test(expected = ProductNotFoundException.class)
    public void itDenyActionOnProductThatNotExistsV2() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();
        productCatalog.applyPrice("notExists", BigDecimal.TEN);
        productCatalog.updateDetails("notExists", "desc", "picture");
    }

    @Test
    public void itDenyActionOnProductThatNotExistsV3() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();

        assertThatThrownBy(() -> productCatalog.applyPrice("notExists", BigDecimal.TEN))
                .hasMessage("There is no product with id: notExists")
        ;
    }

    @Test
    public void exceptionOnLoadingNotExisted() {
        ProductCatalogFacade  productCatalog  = thereIsProductCatalog();

        assertThatThrownBy(() -> productCatalog.applyPrice("notExists", BigDecimal.TEN))
                .hasMessage("There is no product with id: notExists")
        ;

        assertThatThrownBy(() -> productCatalog.getById("notExists"))
                .hasMessage("There is no product with id: notExists")
        ;

        assertThatThrownBy(() -> productCatalog.updateDetails("notExists", "desc", "pic"))
                .hasMessage("There is no product with id: notExists")
        ;
    }

    private static ProductCatalogFacade thereIsProductCatalog() {
        return new ProductCatalogConfiguration().productCatalogFacade();
    }
}
