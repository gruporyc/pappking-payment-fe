package co.ppk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * Basic functions for PropertyManager integration
 *
 * @author jmunoz
 * 
 * @version 1.0
 */
public class PropertyManager{

    private static Properties instance = null;
    public PropertyManager() {

    }

    public Properties getInstance() {
        if (instance == null) {
            Properties prop = new Properties();
            InputStream input = null;
            try {
                input = getClass().getResourceAsStream("/application.properties");
                prop.load(input);
                instance = prop;
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
}
