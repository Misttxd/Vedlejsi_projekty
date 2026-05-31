package lab;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("lab.messages", Locale.getDefault());

    private I18n() {
    }

    public static ResourceBundle getBundle() {
        return BUNDLE;
    }

    public static String text(String key) {
        return BUNDLE.getString(key);
    }
}
