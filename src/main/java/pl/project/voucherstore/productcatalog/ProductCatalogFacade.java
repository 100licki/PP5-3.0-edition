package pl.project.voucherstore.productcatalog;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductCatalogFacade {
    ProductsStorage productsStorage;

    public ProductCatalogFacade(ProductsStorage productsStorage) {
        this.productsStorage = productsStorage;
    }

    public String createProduct() {
        Product newProduct = new Product(UUID.randomUUID());
        productsStorage.save(newProduct);

        return newProduct.getId();
    }

    public boolean isExistsById(String productId) {
        return productsStorage.loadById(productId).isPresent();
    }

    public Product getById(String productId) {
        return getProductOrException(productId);
    }

    public void updateDetails(String productId, String productDesc, String productPicture) {
        Product loaded = getProductOrException(productId);

        loaded.setDescription(productDesc);
        loaded.setPicture(productPicture);

        productsStorage.save(loaded);
    }

    public void applyPrice(String productId, BigDecimal valueOf) {
        Product loaded = getProductOrException(productId);

        loaded.setPrice(valueOf);

        productsStorage.save(loaded);
    }

    public List<Product> getAvailableProducts() {
        return productsStorage.allPublished();
    }

    private Product getProductOrException(String productId) {
        return productsStorage.loadById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("There is no product with id: %s", productId)));
    }

    @Transactional
    public void emptyCatalog() {
        productsStorage.clear();
    }
}
