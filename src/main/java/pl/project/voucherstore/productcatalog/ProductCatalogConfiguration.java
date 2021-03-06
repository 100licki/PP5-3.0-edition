package pl.project.voucherstore.productcatalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductCatalogConfiguration {

    public ProductCatalogFacade productCatalogFacade() {
        return new ProductCatalogFacade(new HashMapProductsStorage());
    }

    @Bean
    public ProductCatalogFacade fixturesAwareProductCatalogFacade(ProductsStorage productsStorage) {
        ProductCatalogFacade catalogFacade = new ProductCatalogFacade(productsStorage);

        return catalogFacade;
    }
}
