package pl.project.voucherstore.productcatalog;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ProductsStorage extends Repository<Product, String> {

    @Query("Select p from Product p where p.price is NOT NULL and p.description is NOT NULL")
    List<Product> allPublished();

    @Query("Select p from Product p where p.productID = :productId")
    Optional<Product> loadById(@Param("productId") String productId);

    void save(Product newProduct);

    @Modifying
    @Query("Delete from Product")
    void clear();
}
