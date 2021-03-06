package pl.project.payment.payu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import pl.project.payment.payu.exceptions.PayUException;
import pl.project.payment.payu.http.PayuHttp;
import pl.project.payment.payu.model.AccessTokenResponse;
import pl.project.payment.payu.model.OrderCreateRequest;
import pl.project.payment.payu.model.OrderCreateResponse;

import java.net.http.HttpResponse;
import java.util.Map;

public class PayU {
    public static final int HTTP_FORBIDDEN = 401;
    private final PayUApiCredentials configuration;
    private final PayuHttp http;
    private final ObjectMapper om;
    private AccessTokenResponse token;

    public PayU(PayUApiCredentials configuration, PayuHttp netHttpClientPayuHttp) {
        this.configuration = configuration;
        this.http = netHttpClientPayuHttp;
        this.om = new ObjectMapper();
        this.om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.token = new AccessTokenResponse();
    }

    public OrderCreateResponse handle(OrderCreateRequest orderCreateRequest) throws PayUException {
        orderCreateRequest.setMerchantPosId(configuration.getPosId());
        orderCreateRequest.setNotifyUrl(configuration.getNotifyUrl());

        HttpResponse<String> response = handlePost(orderCreateRequest);

        if (response.statusCode() == HTTP_FORBIDDEN) {
            this.authorize();
            response = handlePost(orderCreateRequest);
        }

        return readOrderCreateResponseFromString(response.body());
    }

    private void authorize() throws PayUException {
        try {
            var body = String.format(
                    "grant_type=client_credentials&client_id=%s&client_secret=%s",
                    configuration.getClientId(),
                    configuration.getClientSecret());

            HttpResponse<String> response = http.post(
                    getUrl("/pl/standard/user/oauth/authorize"),
                    body,
                    Map.of(
                            "content-type", "application/x-www-form-urlencoded"
                    )
            );

            this.token = om.readValue(response.body(), AccessTokenResponse.class);
        } catch (JsonProcessingException e) {
            throw new PayUException(e);
        }

    }

    private HttpResponse<String> handlePost(OrderCreateRequest orderCreateRequest) throws PayUException { ;
        HttpResponse<String> response = http.post(
                getUrl("/api/v2_1/orders"),
                valueAsString(orderCreateRequest),
                Map.of(
                        "content-type", "application/json",
                        "Authorization", String.format("Bearer %s", token.getAccessToken())
                )
        );
        return response;
    }

    private OrderCreateResponse readOrderCreateResponseFromString(String valueAsString) throws PayUException {
        try {
            OrderCreateResponse orderCreateResponse = om.readValue(valueAsString, OrderCreateResponse.class);
            return  orderCreateResponse;
        } catch (JsonProcessingException e) {
            throw new PayUException(e);
        }
    }

    private String valueAsString(OrderCreateRequest orderCreateRequest) throws PayUException {
        try {
            return om.writeValueAsString(orderCreateRequest);
        } catch (JsonProcessingException e) {
            throw new PayUException(e);
        }
    }

    private String getUrl(String uri) {
        return String.format("%s%s", configuration.getBaseUrl(), uri);
    }

    public boolean isTrusted(String confirmationAsJson, String signature) {
        var toBeHashed = confirmationAsJson + configuration.getSecondKey();
        var md5Hash = DigestUtils.md5Hex(toBeHashed).toUpperCase();

        return md5Hash.equals(signature.toUpperCase());
    }
}
