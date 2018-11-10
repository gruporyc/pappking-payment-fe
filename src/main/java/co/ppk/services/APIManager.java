package co.ppk.services;

import co.ppk.dto.BalanceDto;
import co.ppk.dto.Bank;
import co.ppk.dto.LoadDto;
import co.ppk.dto.LoadRequestDto;

public interface APIManager {


    String[] getCustomerTypes();

    String[] getDocumentTypes();

    String[] getCountries();

    String[] getPaymentMethod();

    String[] getCreditCards();

    Bank[] getBanks();

    BalanceDto getCustomerBalance(String customerId);

    LoadDto performLoad(LoadRequestDto loadRequest);
}
