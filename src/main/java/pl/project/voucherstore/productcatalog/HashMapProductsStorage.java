package pl.project.voucherstore.productcatalog;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HashMapProductsStorage implements ProductsStorage {
    Map<String, Product> products;

    public HashMapProductsStorage() {
        this.products = new ConcurrentHashMap<>();
    }

    @Override
    public List<Product> allPublished() {
        return products.values()
                .stream()
                .filter(p -> p.getDescription() != null)
                .filter(p -> p.getPrice() != null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> loadById(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public void save(Product newProduct) {
        products.put(newProduct.getId(), newProduct);
    }

    @Override
    public void clear() {
        products.clear();
    }
}
