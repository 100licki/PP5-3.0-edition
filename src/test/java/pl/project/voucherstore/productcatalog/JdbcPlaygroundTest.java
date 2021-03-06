package pl.project.voucherstore.productcatalog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcPlaygroundTest {
    //Integration Test
    public static final String PRODUCT_ID = "f1f21a3d-d205-465f-8da7-29c0bf5a5d59";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate.execute("DROP TABLE product_catalog__products IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE `product_catalog__products` (" +
                "`id` varchar(255) NOT NULL," +
                "`description` varchar(255)," +
                "`picture` varchar(150)," +
                "`price` DECIMAL(12,2)," +
                "PRIMARY KEY (id)" +
        ");");
    }

    @Test
    public void playground() {
        int results = jdbcTemplate.queryForObject("select count(*) from `product_catalog__products`", Integer.class);
        assertThat(results).isEqualTo(0);
    }

    @Test
    public void playgroundInsert() {
        jdbcTemplate.execute("INSERT INTO `product_catalog__products` (id, description, picture, price) " +
                "VALUES " +
                "('p1', 'p1 description', 'p1 picture', 20.25)," +
                "('p2', 'p2 description', 'p2 picture', 25.25)" +
                "; ");

        int results = jdbcTemplate.queryForObject("select count(*) from `product_catalog__products`", Integer.class);
        assertThat(results).isEqualTo(2);
    }

    @Test
    public void selectPlayground() {
        jdbcTemplate.execute("INSERT INTO `product_catalog__products` (id, description, picture, price) " +
                "VALUES " +
                "('f1f21a3d-d205-465f-8da7-29c0bf5a5d59', 'p1 description', 'p1 picture', 20.25)," +
                "('f1f21a3d-d205-465f-8da7-29c0bf5a5d60', 'p2 description', 'p2 picture', 25.25)" +
                "; ");

        var query = "Select * FROM `product_catalog__products` where id = ?";

        Product product = jdbcTemplate.queryForObject(query, new Object[]{PRODUCT_ID}, new ProductRowMapper());

        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
    }

    @Test
    public void selectPlaygroundMapperVialambda() {
        jdbcTemplate.execute("INSERT INTO `product_catalog__products` (id, description, picture, price) " +
                "VALUES " +
                "('f1f21a3d-d205-465f-8da7-29c0bf5a5d59', 'p1 description', 'p1 picture', 20.25)," +
                "('f1f21a3d-d205-465f-8da7-29c0bf5a5d60', 'p2 description', 'p2 picture', 25.25)" +
                "; ");

        var query = "Select * FROM `product_catalog__products` where id = ?";

        Product product = jdbcTemplate.queryForObject(query, new Object[]{PRODUCT_ID}, (rs, i) -> {
            Product p = new Product(UUID.fromString(rs.getString("id")));
            p.setDescription(rs.getString("description"));
            return p;
        });

        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
    }

    @Test
    public void addProduct() {
        Product product = new Product(UUID.randomUUID());
        jdbcTemplate.update("INSERT INTO `product_catalog__products` (id, description, picture, price) values " +
                "(?,?,?,?)", product.getId(), product.getDescription(), product.getPicture(), product.getPrice()
        );

        var query = "Select * FROM `product_catalog__products` where id = ?";

        Product loaded = jdbcTemplate.queryForObject(query, new Object[]{product.getId()}, (rs, i) -> {
            Product p = new Product(UUID.fromString(rs.getString("id")));
            p.setDescription(rs.getString("description"));
            return p;
        });

        assertThat(loaded.getId()).isEqualTo(product.getId());
    }

    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = new Product(UUID.fromString(resultSet.getString("id")));
            product.setDescription(resultSet.getString("description"));

            return product;
        }
    }
}
