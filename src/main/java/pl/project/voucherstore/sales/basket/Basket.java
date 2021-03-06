package pl.project.voucherstore.sales.basket;

import pl.project.voucherstore.productcatalog.Product;
import pl.project.voucherstore.sales.Inventory;
import pl.project.voucherstore.sales.exceptions.NotEnoughQuantityException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Basket {
    private final HashMap<String, Product> products;
    private final HashMap<String, Integer> productsQuantities;

    public Basket() {
        products = new HashMap<>();
        productsQuantities = new HashMap<>();
    }

    public static Basket empty() {
        return new Basket();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void add(Product product, Inventory inventory) {

        if (!isAvailable(product.getProductID(), inventory)) {
            throw new NotEnoughQuantityException();
        }

        if (!isInBasket(product)) {
            putIntoBasket(product);
        } else {
            increaseProductQuantity(product);
        }
    }

    private boolean isAvailable(String productId, Inventory inventory) {
        return inventory.isAvailable(productId);
    }

    public int getProductsQuantities() {
        return products.size();
    }

    public List<BasketItem> getBasketItems() {
        return productsQuantities
                .entrySet()
                .stream()
                .map(es -> new BasketItem(es.getKey(), es.getValue()))
                .collect(Collectors.toUnmodifiableList());
    }

    private void putIntoBasket(Product product) {
        products.put(product.getId(), product);
        productsQuantities.put(product.getId(), 1);
    }

    private void increaseProductQuantity(Product product) {
        productsQuantities.put(product.getId(), productsQuantities.get(product.getId()) + 1);
    }

    private boolean isInBasket(Product product) {
        return productsQuantities.containsKey(product.getId());
    }
}
