package co.ppk.services.impl;

import co.ppk.dto.BalanceDto;
import co.ppk.dto.Bank;
import co.ppk.dto.LoadDto;
import co.ppk.dto.LoadRequestDto;
import co.ppk.services.APIManager;
import co.ppk.util.PropertyManager;
import co.ppk.util.RestTemplateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Properties;


@Component
public class APIManagerImpl implements APIManager {

	@Autowired
	private RestTemplateHelper client;
	
	private Properties pm;

    /*TODO: For MS_PAYMENTS_GET_BANKS remove hardcodded country,, make it dynamic*/

	public APIManagerImpl(RestTemplateHelper client, PropertyManager pm) {
		this.client = client;
        this.pm = pm.getInstance();
	}

    @Override
    public String[] getCustomerTypes() {
        ResponseEntity<String[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_CUSTOMER_TYPES"), String[].class);
        return response.getBody();
    }

    @Override
    public String[] getDocumentTypes() {
        ResponseEntity<String[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_DOCUMENT_TYPES"), String[].class);
        return response.getBody();
    }

    @Override
    public String[] getCountries() {
        ResponseEntity<String[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_COUNTRIES"), String[].class);
        return response.getBody();
    }

    @Override
    public String[] getPaymentMethod() {
        ResponseEntity<String[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_PAYMENT_METHOD"), String[].class);
        return response.getBody();
    }

    @Override
    public String[] getCreditCards() {
        ResponseEntity<String[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_CREDIT_CARDS"), String[].class);
        return response.getBody();
    }

    @Override
    public Bank[] getBanks() {
        ResponseEntity<Bank[]> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_BANKS") + "/co", Bank[].class);
        return response.getBody();
    }

    @Override
    public BalanceDto getCustomerBalance(String customerId) {
	    ResponseEntity<BalanceDto> response = client.processRequestGet(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_CUSTOMER_BALANCE") + "/" + customerId, BalanceDto.class);
        return response.getBody();
    }

    @Override
    public LoadDto performLoad(LoadRequestDto loadRequest) {
        HashMap<String, Object> load = new HashMap<String, Object>() {{
            put("amount", loadRequest.getAmount());
            put("currency", loadRequest.getCurrency());
            put("buyer", new HashMap<String, Object>() {{
                put("id", loadRequest.getBuyer().getId());
                put("name", loadRequest.getBuyer().getName());
                put("email", loadRequest.getBuyer().getEmail());
                put("phone", loadRequest.getBuyer().getPhone());
                put("identification", loadRequest.getBuyer().getIdentification());
                put("address_1", loadRequest.getBuyer().getAddress1());
                put("address_2", loadRequest.getBuyer().getAddress2());
                put("city", loadRequest.getBuyer().getCity());
                put("state", loadRequest.getBuyer().getState());
                put("country", loadRequest.getBuyer().getCountry());
                put("postal_code", loadRequest.getBuyer().getPostalCode());
            }});
            put("description", loadRequest.getDescription());
            put("method", loadRequest.getMethod());
            put("credit_card", new HashMap<String, Object>(){{
                put("number", loadRequest.getCreditCard().getNumber());
                put("expiration", loadRequest.getCreditCard().getExpiration());
                put("security_code", loadRequest.getCreditCard().getSecurityCode());
                put("type", loadRequest.getCreditCard().getType());
                put("installments", loadRequest.getCreditCard().getInstallments());
                put("country", loadRequest.getCreditCard().getCountry());
            }});
            put("financial_institute_code", loadRequest.getFinancialInstituteCode());
            put("person_type", loadRequest.getPersonType());
            put("dni", loadRequest.getDni());
            put("document_type", loadRequest.getDocumentType());
        }};
        ResponseEntity<LoadDto> response = client.processRequestPostObject(
                pm.getProperty("MS_PAYMENTS_URL") + pm.getProperty("MS_PAYMENTS_LOAD"), load, LoadDto.class);
        return response.getBody();
    }
}
