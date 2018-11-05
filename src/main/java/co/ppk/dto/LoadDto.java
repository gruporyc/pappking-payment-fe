package co.ppk.dto;

public class LoadDto {

    private  String id;
    private  String customerId;
    private  String clientId;
    private  double amount;
    private  String currency;
    private  String payerName;
    private  String payerCardLastDigits;
    private  String method;
    private  String orderId;
    private  String transactionId;
    private  String status;
    private  String networkCode;
    private  String networkMessage;
    private  String trazabilityCode;
    private  String responseCode;
    private  String bankUrl;
    private  String receiptUrl;
    private  String country;
    private  String createdAt;
    private  String updatedAt;


    public LoadDto() {
        super();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getClientId() {
        return clientId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayerName() {
        return payerName;
    }

    public String getPayerCardLastDigits() {
        return payerCardLastDigits;
    }

    public String getMethod() {
        return method;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getNetworkCode() {
        return networkCode;
    }

    public String getNetworkMessage() {
        return networkMessage;
    }

    public String getTrazabilityCode() {
        return trazabilityCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getBankUrl() {
        return bankUrl;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public void setPayerCardLastDigits(String payerCardLastDigits) {
        this.payerCardLastDigits = payerCardLastDigits;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public void setNetworkMessage(String networkMessage) {
        this.networkMessage = networkMessage;
    }

    public void setTrazabilityCode(String trazabilityCode) {
        this.trazabilityCode = trazabilityCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "LoadDto{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", payerName='" + payerName + '\'' +
                ", payerCardLastDigits='" + payerCardLastDigits + '\'' +
                ", method='" + method + '\'' +
                ", orderId='" + orderId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", status='" + status + '\'' +
                ", networkCode='" + networkCode + '\'' +
                ", networkMessage='" + networkMessage + '\'' +
                ", trazabilityCode='" + trazabilityCode + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", bankUrl='" + bankUrl + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                ", country='" + country + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
