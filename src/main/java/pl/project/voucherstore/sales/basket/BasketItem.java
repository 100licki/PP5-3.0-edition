package pl.project.voucherstore.sales.basket;

public class BasketItem {
    private String productId;
    private int quantity;

    public BasketItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
