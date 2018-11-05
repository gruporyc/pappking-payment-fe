package co.ppk.handlers;

import co.ppk.Template;
import co.ppk.dto.*;
import co.ppk.enums.*;
import co.ppk.services.APIManager;
import co.ppk.services.impl.APIManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import spark.Route;
import spark.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;

import static co.ppk.ModelEntry.withModel;
import static co.ppk.Template.render;

//@Component
public class LoadHandler {

    @Autowired
    APIManager apiManager;

    private final static Logger LOGGER = LogManager.getLogger(APIManagerImpl.class);

    public LoadHandler(APIManager apiManager) {
        this.apiManager = apiManager;
    }

//    public Route support() {
//        return (req, resp) -> render(req, Template.SUPPORT,
//                withModel("title", "Support"),
//                withModel("section", "support"));
//    }

    public Route viewLoadForm() {
        return (req, resp) -> {
            try{

                String customerId = req.params(":customerId");
                BalanceDto customerBalance = apiManager.getCustomerBalance(customerId);
                if(Objects.isNull(customerBalance.getId()) || customerBalance.getId().isEmpty()) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
                }

                String[] customerTypes = apiManager.getCustomerTypes();
                String[] documentTypes = apiManager.getDocumentTypes();
                String[] countries = apiManager.getCountries();
                String[] paymentMethod = apiManager.getPaymentMethod();
                String[] creditCards = apiManager.getCreditCards();
                Bank[] banks = apiManager.getBanks();

                return render(req, Template.LOAD_FORM,
                        withModel("title", "Home"),
                        withModel("section", "home"),
                        withModel("customerTypes", customerTypes),
                        withModel("documentTypes", documentTypes),
                        withModel("countries", countries),
                        withModel("paymentMethod", paymentMethod),
                        withModel("creditCards", creditCards),
                        withModel("banks", banks),
                        withModel("customerId", customerId),
                        withModel("error", false));
            } catch (HttpClientErrorException ex) {
                LOGGER.error(ex);
                return render(req, Template.ERROR_PAGE,
                        withModel("error", true),
                        withModel("errorCode", ex.getStatusCode().value()));
            }
        };
    }

    public Route performLoad() {
        return (req, resp) -> {
            String ret = "";
            try{
                String customerId = req.params(":customerId");
                String personType = req.queryParams("personType");

                String name = (personType.equals(PersonType.LEGAL.name())) ? req.queryParams("companyName") : req.queryParams("firstName");

                PersonDto person = new PersonDto();
                person.setAddress1(req.queryParams("address"));
                person.setCity(req.queryParams("city"));
                person.setCountry(Country.valueOf(req.queryParams("country")));
                person.setEmail(req.queryParams("email"));
                person.setId(customerId);
                person.setIdentification(req.queryParams("id"));
                person.setName(name);
                person.setPhone(req.queryParams("phone"));
                person.setPostalCode(req.queryParams("zip"));
                person.setState(req.queryParams("state"));

                CreditCardDto creditCard = new CreditCardDto();
                if(req.queryParams("paymentMethod").equals(PaymentMethod.CREDIT.name())) {
                    creditCard.setCountry(Country.valueOf(req.queryParams("country")));
                    creditCard.setExpiration(req.queryParams("cc-expiration"));
                    creditCard.setInstallments(Integer.valueOf(req.queryParams("cc-installments")));
                    creditCard.setNumber(req.queryParams("cc-number"));
                    creditCard.setSecurityCode(Integer.valueOf(req.queryParams("cc-cvv")));
                    creditCard.setType(CreditCardType.valueOf(req.queryParams("cc-type")));
                }

                LoadRequestDto loadRequest = new LoadRequestDto();
                if(req.queryParams("paymentMethod").equals(PaymentMethod.PSE.name())) {
                    loadRequest.setDni(req.queryParams("id"));
                    loadRequest.setFinancialInstituteCode(req.queryParams("bank"));
                }


                loadRequest.setPersonType(PersonType.valueOf(req.queryParams("personType")));
                loadRequest.setAmount(Double.valueOf(req.queryParams("amount")));
                /*TODO: Remove hardcoded currency to make it dynamic */
                loadRequest.setCurrency(Currency.COP);
                loadRequest.setCreditCard(creditCard);
                loadRequest.setBuyer(person);
                /*TODO: Translate description based on selected language*/
                loadRequest.setDescription("Recarga de saldo pappking para el usuario: " + customerId);
                loadRequest.setMethod(PaymentMethod.valueOf(req.queryParams("paymentMethod")));
                loadRequest.setDocumentType(DocumentType.valueOf(req.queryParams("documentType")));


                LoadDto response = apiManager.performLoad(loadRequest);

                if (response.getStatus().equals(Status.APPROVED.name())) {
                    LOGGER.info("Transaction " + response.getStatus() + " for customer: " + customerId);
                    ret = render(req, Template.RESPONSE_PAGE,
                            withModel("error", false),
                            withModel("status", response.getStatus()));
                } else if (response.getStatus().equals(Status.PENDING.name())) {
                    if(loadRequest.getMethod().equals(PaymentMethod.PSE)) {
                        resp.redirect(response.getBankUrl());
                    } else {
                        resp.redirect(response.getReceiptUrl());
                    }
                } else {
                    LOGGER.error("Transaction was " + response.getStatus() +
                            " with description " + response.getResponseCode() + ". Order ID: " + response.getOrderId() +
                            " Transaction ID: " + response.getTransactionId() +
                            " Trazability code: " + response.getTrazabilityCode());

                    String[] customerTypes = apiManager.getCustomerTypes();
                    String[] documentTypes = apiManager.getDocumentTypes();
                    String[] countries = apiManager.getCountries();
                    String[] paymentMethod = apiManager.getPaymentMethod();
                    String[] creditCards = apiManager.getCreditCards();
                    Bank[] banks = apiManager.getBanks();
                    ret = render(req, Template.LOAD_FORM,
                            withModel("title", "Home"),
                            withModel("section", "home"),
                            withModel("customerTypes", customerTypes),
                            withModel("documentTypes", documentTypes),
                            withModel("countries", countries),
                            withModel("paymentMethod", paymentMethod),
                            withModel("creditCards", creditCards),
                            withModel("banks", banks),
                            withModel("customerId", customerId),
                            withModel("error", true));
                }

            } catch (HttpClientErrorException ex) {
                LOGGER.error(ex);
                ret = render(req, Template.ERROR_PAGE,
                        withModel("error", true),
                        withModel("errorCode", ex.getStatusCode().value()));
            }
            return ret;
        };
    }
}