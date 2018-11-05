package co.ppk;

import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static co.ppk.util.SessionUtil.getLocale;

public interface Template {

    VelocityTemplateEngine instance = new VelocityTemplateEngine();

    String LOAD_FORM = "/templates/load-form.vm";
    String ERROR_PAGE = "/templates/error-page.vm";
    String RESPONSE_PAGE = "/templates/success-page.vm";

    static String render(Request request, String templatePath, ModelEntry... entries) {
        String pathInfo = request.pathInfo();
        Session session = request.session();

        session.removeAttribute("errorMessage");
        Locale locale = getLocale(session);
        HashMap<String, Object> model = new HashMap<String, Object>() {{
            put("lang", locale);
            put("texts", ResourceBundle.getBundle("texts", locale));
            put("pathInfo", pathInfo);
        }};

        Arrays.asList(entries).forEach(e -> model.put(e.getKey(), e.getValue()));

        return instance.render(new ModelAndView(model, templatePath));
    }

}