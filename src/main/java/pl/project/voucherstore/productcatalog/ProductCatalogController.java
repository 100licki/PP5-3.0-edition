package pl.project.voucherstore.productcatalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductCatalogController {

    private final ProductCatalogFacade productCatalog;

    public ProductCatalogController(ProductCatalogFacade productCatalog) {
        this.productCatalog = productCatalog;
    }

    @GetMapping("/api/products")
    public List<Product> listAllProducts() {
        return productCatalog.getAvailableProducts();
    }
}
