package co.ppk.util;

import co.ppk.Template;
import org.springframework.web.client.HttpClientErrorException;
import spark.Request;
import spark.Response;
import spark.Route;

import static co.ppk.ModelEntry.withModel;
import static co.ppk.Template.render;
import static co.ppk.util.SessionUtil.Attributes.LANGUAGE;

public class Filters {

    public static void captureLanguage(Request request, Response response) {
        String language = request.queryParams(LANGUAGE);
        if (language != null) {
            request.session().attribute(LANGUAGE, language);
            response.redirect(request.pathInfo());
        }
    }

    public static void checkNotFound(Request request, Response response) {
        if (!request.pathInfo().equals("/login")) {
            return;
        }
    }
}