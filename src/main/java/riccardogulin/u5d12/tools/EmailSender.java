package riccardogulin.u5d12.tools;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import riccardogulin.u5d12.entities.User;

@Component
public class EmailSender {
    private String domain;
    private String apiKey;

    public EmailSender(@Value("${mailgun.domain}") String domain,
                       @Value("${mailgun.apiKey}") String apiKey) {
        this.domain = domain;
        this.apiKey = apiKey;
    }

    public void sendRegistration(User recipient) {

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domain + "/messages")
                .basicAuth("api", apiKey)
                .queryString("from", "riccardo.gulin@gmail.com")
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Benvenuto sulla piattaforma")
                .queryString("text", "Ciao " + recipient.getName() + ", la tua registrazione è andata a buon fine")
                .asJson();
        
        System.out.println(response.getBody()); // Consiglio questo log per ispezionare la risposta e poter debuggare più
        // facilmente


    }

    public void sendBilingEmail(User recipient) {
    }

    public void sendInvoiceEmail(User recipient) {
    }
}
