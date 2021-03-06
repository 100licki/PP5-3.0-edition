package pl.project.voucherstore.productcatalog;

import static org.assertj.core.api.Assertions.*;

public class ListProductStorageTest {


    public void itAllowToSoreProduct() {
        Product p = thereIsNewPublishedProduct();
        ProductsStorage storage = new ListProductStorage();

        storage.save(p);

        assertThat(storage.allPublished())
                .hasSize(1)
                .extracting(Product::getId)
                .contains(p.getId());
    }

    private static Product thereIsNewPublishedProduct() {
        return null;
    }
}
