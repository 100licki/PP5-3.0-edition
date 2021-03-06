package pl.project.voucherstore.productcatalog;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    private String productID;
    private String description;
    private String picture;
    private BigDecimal price;

    public Product(UUID productId) {
        this.productID = productId.toString();
    }

    public String getId() {
        return productID;
    }
}
