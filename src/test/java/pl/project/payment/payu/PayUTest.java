package pl.project.payment.payu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import pl.project.payment.payu.exceptions.PayUException;
import pl.project.payment.payu.http.NetHttpClientPayuHttp;
import pl.project.payment.payu.model.Buyer;
import pl.project.payment.payu.model.OrderCreateRequest;
import pl.project.payment.payu.model.OrderCreateResponse;
import pl.project.payment.payu.model.Product;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class PayUTest {
    @Test
    public void itAllowsToRegisterPayment() throws PayUException {
        //Arrange
        var payu = thereIsPayU();
        var mySystemOrderId = UUID.randomUUID().toString();
        var exampleOrderCreateRequest = thereIsExampleOrderCreateRequest(mySystemOrderId);

        //Act
        OrderCreateResponse r = payu.handle(exampleOrderCreateRequest);

        //Assert
        assertThat(r.getExtOrderId()).isEqualTo(mySystemOrderId);
        assertThat(r.getRedirectUri()).isNotNull();
        assertThat(r.getOrderId()).isNotNull();
    }

    @Test
    public void itCalculateSignatureBasedOnSecondKey() {
        var payu = thereIsPayU();
        var exampleOrderAsString = "sample_order_confirmation";
        var expectedSignature = "0A5E5BA858C1A7CBDE3326DBACB8FA8A";

        assertThat(payu.isTrusted(exampleOrderAsString, expectedSignature)).isTrue();
    }

    @Test
    public void itVerifySignatureBasedOnNotificationJson() throws JsonProcessingException {
        var payu = thereIsPayU();
        var orderId = "1234567890";
        var exampleOrder = thereIsExampleOrderCreateRequest(orderId);
        var exampleOrderAsString = new ObjectMapper().writeValueAsString(exampleOrder);
        var expectedSignature = "42701F59FDE1FBD9D88ED7BECF0A7EBF";
        var invalidSignature = "0A5E5BA858C1A7CBDE3326DBACB8FA8A";

        assertThat(payu.isTrusted(exampleOrderAsString, invalidSignature)).isFalse();
        assertThat(payu.isTrusted(exampleOrderAsString, expectedSignature)).isTrue();
    }

    private OrderCreateRequest thereIsExampleOrderCreateRequest(String mySystemOrderId) {
        return OrderCreateRequest.builder()
                .notifyUrl("https://your.eshop.com/notify")
                .customerIp("127.0.0.1")
                .description("RTV market")
                .currencyCode("PLN")
                .totalAmount(21000)
                .extOrderId(mySystemOrderId)
                .buyer(Buyer.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .language("pl")
                        .phone("654111654")
                        .build())
                .products(Arrays.asList(
                        new Product("Wireless Mouse for Laptop", 15000, 1),
                        new Product("Battery AAA", 1000, 2)
                ))
                .build();
    }

    private PayU thereIsPayU() {
        return new PayU(
                PayUApiCredentials.sandbox(),
                new NetHttpClientPayuHttp()
        );
    }
}
