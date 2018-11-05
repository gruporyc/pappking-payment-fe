package co.ppk.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class StandardResponse<T> {

    private boolean successful;
    private String message;
    private T data;

    private StandardResponse(boolean successful) {
        this.successful = successful;
    }

    private StandardResponse(T data) {
        this(true);
        this.data = data;
    }

    private StandardResponse(String message) {
        this(false);
        this.message = message;
    }

    public static <T> StandardResponse<T> successful() {
        return new StandardResponse<>(true);
    }

    public static <T> StandardResponse<T> successful(T data) {
        return new StandardResponse<>(data);
    }

    public static <T> StandardResponse failed(String message, Locale locale) {
        return new StandardResponse(
                message != null ? ResourceBundle.getBundle("texts", locale).getString("error.message." + message) : message);
    }

    public static <T> StandardResponse failedKeyFieldsExists(String message, Locale locale) {
        String response = "";
        for (String item : message.split(",")) {
            response = response.concat(ResourceBundle.getBundle("texts", locale).getString("error.message." + item.toLowerCase() + ".exists"));
        }
        return new StandardResponse(ResourceBundle.getBundle("texts", locale).getString("error.message.standard") + response);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
