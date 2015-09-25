package ssm.view;

import java.io.File;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static ssm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import ssm.controller.ImageSelectionController;
import ssm.model.Slide;
import static ssm.file.SlideShowFileManager.SLASH;
import ssm.model.SlideShowModel;

/**
 * This UI component has the controls for editing a single slide in a slide
 * show, including controls for selected the slide image and changing its
 * caption.
 *
 * @author McKilla Gorilla & _____________
 */
public class SlideEditView extends HBox {

    // SLIDE THIS COMPONENT EDITS
    Slide slide;

    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;

    // CONTROLS FOR EDITING THE CAPTION
    VBox captionVBox;
    Label captionLabel;
    TextField captionTextField;

    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;

    /**
     * THis constructor initializes the full UI for this component, using the
     * initSlide data for initializing values./
     *
     * @param initSlide The slide to be edited by this component.
     */
    public SlideEditView(Slide initSlide, SlideShowModel model) {
        // FIRST SELECT THE CSS STYLE CLASS FOR THIS CONTAINER
        this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);

        // KEEP THE SLIDE FOR LATER
        slide = initSlide;

        // MAKE SURE WE ARE DISPLAYING THE PROPER IMAGE
        imageSelectionView = new ImageView();
        updateSlideImage();

        // SETUP THE CAPTION CONTROLS
        captionVBox = new VBox();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        captionLabel = new Label(props.getProperty(LanguagePropertyType.LABEL_CAPTION));
        captionTextField = new TextField();
        captionVBox.getChildren().add(captionLabel);
        captionVBox.getChildren().add(captionTextField);

        //A listener for the caption text field, (MY WORK)
        captionTextField.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                slide.setCaption(newValue);
                //throw new UnsupportedOperationException("Not supported yet.");
                //To change body of generated methods, choose Tools | Templates.
            }

        });

        this.setOnMouseClicked((MouseEvent e) -> {
            // GET CSS OF THE NOT SELECTED SLIDE.
            model.setSelectedSlide(slide);
            this.firstHighlight(this);
        });

        this.imageSelectionView.setOnMouseClicked(e -> {
            this.secondHighlight(this);
        });

        // LAY EVERYTHING OUT INSIDE THIS COMPONENT
        getChildren().add(imageSelectionView);
        getChildren().add(captionVBox);

        // SETUP THE EVENT HANDLERS
        imageController = new ImageSelectionController();

        updateCaption();
    }

    /**
     * This function gets the image for the slide and uses it to update the
     * image displayed.
     */
    public void updateSlideImage() {
        String imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
        File file = new File(imagePath);
        try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            imageSelectionView.setImage(slideImage);

            // AND RESIZE IT
            double scaledWidth = DEFAULT_THUMBNAIL_WIDTH;
            double perc = scaledWidth / slideImage.getWidth();
            double scaledHeight = slideImage.getHeight() * perc;
            imageSelectionView.setFitWidth(scaledWidth);
            imageSelectionView.setFitHeight(scaledHeight);

        } catch (Exception e) {
            // @todo - use Error handler to respond to missing image
        }
    }

    private void updateCaption() {
        try {
            captionTextField.setText(slide.getCaption());
        } catch (Exception e) {
            System.out.println("Update caption failed!!");

        }
    }

    public void firstHighlight(SlideEditView editor) {
        if (!this.slide.isHighlight()) {
            this.setEffect(new SepiaTone(1.0));
            this.slide.setHighlight(true);
        } else {
            this.setEffect(new SepiaTone(0.0));
            this.slide.setHighlight(false);
        }
    }

    public void secondHighlight(SlideEditView editor) {

        imageController.processSelectImage(slide, this);
        editor.setEffect(new SepiaTone(1.0));
    }

}
