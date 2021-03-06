package pl.project.voucherstore.sales;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.project.payment.payu.PayU;
import pl.project.payment.payu.PayUApiCredentials;
import pl.project.payment.payu.http.NetHttpClientPayuHttp;
import pl.project.voucherstore.productcatalog.ProductCatalogFacade;
import pl.project.voucherstore.sales.basket.InMemoryBasketStorage;
import pl.project.voucherstore.sales.offer.OfferMaker;
import pl.project.voucherstore.sales.ordering.ReservationRepository;
import pl.project.voucherstore.sales.payment.PayUPaymentGateway;
import pl.project.voucherstore.sales.payment.PaymentGateway;
import pl.project.voucherstore.sales.productd.ProductCatalogProductDetailsProvider;
import pl.project.voucherstore.sales.productd.ProductDetailsProvider;

import java.util.UUID;

@Configuration
public class SalesConfiguration {

    @Bean
    SalesFacade salesFacade(ProductCatalogFacade productCatalogFacade, OfferMaker offerMaker, PaymentGateway paymentGateway, ReservationRepository reservationRepository) {
        var alwaysSameCustomer = UUID.randomUUID().toString();

        return new SalesFacade(
                new InMemoryBasketStorage(),
                productCatalogFacade,
                () -> alwaysSameCustomer,
                (productId) -> true,
                offerMaker,
                paymentGateway,
                reservationRepository
        );
    }

    @Bean
    PaymentGateway payUPaymentGateway(PayU payU) {
        return new PayUPaymentGateway(payU);
    }

    @Bean
    PayU payU() {
        return new PayU(
            PayUApiCredentials.sandbox(),
            new NetHttpClientPayuHttp()
        );
    }

    @Bean
    OfferMaker offerMaker(ProductDetailsProvider productDetailsProvider) {
        return new OfferMaker(productDetailsProvider);
    }

    @Bean
    ProductDetailsProvider productDetailsProvider(ProductCatalogFacade productCatalogFacade) {
        return new ProductCatalogProductDetailsProvider(productCatalogFacade);
    }
}
