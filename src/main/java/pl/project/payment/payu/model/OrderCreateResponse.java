package pl.project.payment.payu.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateResponse {
    private String redirectUri;
    private String orderId;
    private String extOrderId;
    /*
    {
   "status":{
      "statusCode":"SUCCESS",
   },
       "redirectUri":"{url_do_przekierowania_na_stronę_podsumowania_płatności}",
       "orderId":"WZHF5FFDRJ140731GUEST000P01",
       "extOrderId":"{twój_identyfikator_zamówienia}",
    }
     */
}
