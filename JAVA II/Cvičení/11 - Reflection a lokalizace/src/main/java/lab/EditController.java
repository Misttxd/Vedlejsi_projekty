package lab;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lab.score.Score;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class EditController {

    private static Logger log = LogManager.getLogger(EditController.class);

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private Label txtTitle;

    @FXML
    private GridPane content;

    private Object data;

    private App app;

    private Map<String, TextField> nameToTextField = new HashMap<>();
    private final ResourceBundle messages = ResourceBundle.getBundle("msg");

    @Setter
    private Stage stage;

    @Setter
    private MenuController menuController;

    @FXML
    void btnOkAction(ActionEvent event) {
        if (data != null) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass(), Object.class);
                for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                    MyEdit edit = annotationFor(property);
                    TextField textField = nameToTextField.get(property.getName());
                    if (isVisible(edit) && isEditable(edit, property) && textField != null) {
                        property.getWriteMethod().invoke(data, convertValue(textField.getText(),
                            property.getPropertyType()));
                    }
                }
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException
                     | NoSuchFieldException e) {
                log.error("Cannot update edited object.", e);
            }
        }

        if (data instanceof Score score) {
            menuController.updateData(score);
        }
        stage.hide();
    }

    @FXML
    void btnCancelAction(ActionEvent event) {
        stage.hide();
    }

    @FXML
    void initialize() {
        log.info("Screen initialized.");
    }

    public void setObjectToEdit(Object data) {
        this.data = data;
        log.info("data set {}", data);
        content.getChildren().clear();
        nameToTextField.clear();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass(), Object.class);
            int row = 0;
            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                MyEdit edit = annotationFor(property);
                if (isVisible(edit) && property.getReadMethod() != null) {
                    Object value = property.getReadMethod().invoke(data);
                    addDialogRow(row++, property.getName(), translate(property.getName()),
                        value == null ? "" : value.toString(), isEditable(edit, property));
                }
            }
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException
                 | NoSuchFieldException e) {
            log.error("Cannot create edit dialog.", e);
        }
    }

    private void addDialogRow(int rowNumber, String name, String descriptionName, String stringValue,
                              boolean editable) {
        Label label = new Label(descriptionName);
        TextField textField = new TextField(stringValue);
        textField.setEditable(editable);
        nameToTextField.put(name, textField);
        content.addRow(rowNumber, label, textField);
        GridPane.setHalignment(label, HPos.RIGHT);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object convertValue(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (Enum.class.isAssignableFrom(type)) {
            return Enum.valueOf((Class<? extends Enum>) type.asSubclass(Enum.class), value);
        }
        return value;
    }

    private String translate(String name) {
        try {
            return messages.getString(name);
        } catch (MissingResourceException e) {
            return name;
        }
    }

    private MyEdit annotationFor(PropertyDescriptor property) throws NoSuchFieldException {
        Field field = data.getClass().getDeclaredField(property.getName());
        return field.getAnnotation(MyEdit.class);
    }

    private boolean isVisible(MyEdit edit) {
        return edit != null && edit.visible();
    }

    private boolean isEditable(MyEdit edit, PropertyDescriptor property) {
        return !edit.readOnly() && property.getWriteMethod() != null;
    }

}
