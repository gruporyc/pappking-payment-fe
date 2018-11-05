package co.ppk;

import co.ppk.handlers.ExceptionHandler;
import co.ppk.handlers.LoadHandler;
import co.ppk.services.impl.APIManagerImpl;
import co.ppk.util.Filters;
import co.ppk.util.RestTemplateHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import spark.Service;

import static co.ppk.ModelEntry.withModel;
import static co.ppk.Template.render;
import static spark.Spark.exception;
import static spark.Spark.notFound;

public class Main {

    public static void main(String[] args) {
        setupLoadsFe(Service.ignite(), 5004);
    }

    private static void setupLoadsFe(Service http, int port) {
        http.staticFiles.location("/public");

        LoadHandler loadHandler = new LoadHandler(new APIManagerImpl(new RestTemplateHelper()));

        ExceptionHandler exceptionHandler = new ExceptionHandler();

        http.port(port);
        notFound((req, res) -> {
            res.type("text/html");
            String render = render(req, Template.ERROR_PAGE,
                    withModel("error", true),
                    withModel("errorCode", 404));
            return render;
        });

            http.before("*", Filters::captureLanguage);
            http.before("*", Filters::checkNotFound);

            http.path("/payments/customer", () -> {
                http.get("/:customerId", loadHandler.viewLoadForm());
                http.post("/:customerId", loadHandler.performLoad());
            });
    }

}