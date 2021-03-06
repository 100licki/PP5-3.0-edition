package pl.project.voucherstore.sales;

import pl.project.voucherstore.productcatalog.ProductCatalogConfiguration;
import pl.project.voucherstore.productcatalog.ProductCatalogFacade;
import pl.project.voucherstore.sales.basket.InMemoryBasketStorage;
import pl.project.voucherstore.sales.offer.OfferMaker;
import pl.project.voucherstore.sales.ordering.InMemoryReservationRepository;
import pl.project.voucherstore.sales.ordering.ReservationRepository;
import pl.project.voucherstore.sales.payment.InMemoryPaymentGateway;
import pl.project.voucherstore.sales.payment.PaymentGateway;
import pl.project.voucherstore.sales.productd.ProductDetails;

import java.math.BigDecimal;
import java.util.UUID;

public class SalesTestCase {

    protected ProductCatalogFacade productCatalog;
    protected InMemoryBasketStorage basketStorage;
    protected Inventory alwaysExistsInventory;
    protected CurrentCustomerContext currentCustomerContext;
    protected String customerId;
    protected OfferMaker offerMaker;
    protected PaymentGateway paymentGateway;
    protected ReservationRepository reservationRepository;

    protected CurrentCustomerContext thereIsCurrentCustomerContext() {
        return () -> customerId;
    }

    protected Inventory thereIsInventory() {
        return productId -> true;
    }

    protected InMemoryBasketStorage thereIsBasketStore() {
        return new InMemoryBasketStorage();
    }

    protected ProductCatalogFacade thereIsProductCatalog() {
        return new ProductCatalogConfiguration().productCatalogFacade();
    }

    protected PaymentGateway thereIsInMemoryPaymentGateway() {
        return new InMemoryPaymentGateway();
    }

    protected String thereIsProductAvailable() {
        var id = productCatalog.createProduct();
        productCatalog.applyPrice(id, BigDecimal.valueOf(10));
        productCatalog.updateDetails(id, "lego", "http://picture");

        return id;
    }

    protected SalesFacade thereIsSalesModule() {
        return new SalesFacade(
                basketStorage,
                productCatalog,
                currentCustomerContext,
                alwaysExistsInventory,
                offerMaker,
                paymentGateway,
                reservationRepository
        );
    }

    protected OfferMaker thereIsOfferMaker(ProductCatalogFacade productCatalogFacade) {
        return new OfferMaker(productId -> {
            var product = productCatalogFacade.getById(productId);

            return new ProductDetails(product.getId(), product.getDescription(), product.getPrice());
        });
    }

    protected String thereIsCustomerWhoIsDoingSomeShopping() {
        return UUID.randomUUID().toString();
    }

    protected ReservationRepository thereIsInMemoryReservationRepository() {
        return new InMemoryReservationRepository();
    }
}
