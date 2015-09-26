package ssm.error;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import ssm.SlideShowMaker;
import ssm.view.SlideShowMakerView;

/**
 * This class provides error messages to the user when the occur. Note that
 * error messages should be retrieved from language-dependent XML files and
 * should have custom messages that are different depending on the type of error
 * so as to be informative concerning what went wrong.
 *
 * @author McKilla Gorilla & Krishna
 */
public class ErrorHandler {

    // APP UI
    private SlideShowMakerView ui;

    // KEEP THE APP UI FOR LATER
    public ErrorHandler(SlideShowMakerView initUI) {
        ui = initUI;
    }

    /**
     * This method provides all error feedback. It gets the feedback text, which
     * changes depending on the type of error, and presents it to the user in a
     * dialog box.
     *
     * @param errorType Identifies the type of error that happened, which allows
     * us to get and display different text for different errors.
     */
    public void processError(LanguagePropertyType errorType, String errorDialogTitle, String errorDialogMessage) {
        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String errorFeedbackText = props.getProperty(errorType);

        // POP OPEN A DIALOG TO DISPLAY TO THE USER
        // @todo
        Stage errorDialog = new Stage();
        errorDialog.getIcons().add(new Image("file:./images/icons/Icon.png"));
        VBox errorPane = new VBox(50);
        Label errorMessage = new Label(errorFeedbackText);
        //  Label testLabel = new Label(errorDialogMessage);
        Button ok = new Button("Ok");
        ok.setOnAction(e -> errorDialog.close());
        errorPane.getChildren().addAll(errorMessage, ok);
        Scene errorScene = new Scene(errorPane, 250, 250);
        errorPane.setPadding(new Insets(12, 12, 12, 12));
        errorDialog.setScene(errorScene);
        if (SlideShowMaker.lang.getValue().equals("English")) {
            errorDialog.setTitle(errorDialogTitle);
        } else {
            errorDialog.setTitle("Ha habido un error");
        }

        errorDialog.show();

    }
}
