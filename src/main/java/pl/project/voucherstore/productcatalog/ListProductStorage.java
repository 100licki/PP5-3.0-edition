package pl.project.voucherstore.productcatalog;

import java.util.List;
import java.util.Optional;

public class ListProductStorage implements ProductsStorage {
    @Override
    public List<Product> allPublished() {
        return null;
    }

    @Override
    public Optional<Product> loadById(String productId) {
        return Optional.empty();
    }

    @Override
    public void save(Product newProduct) {

    }

    @Override
    public void clear() {

    }
}
