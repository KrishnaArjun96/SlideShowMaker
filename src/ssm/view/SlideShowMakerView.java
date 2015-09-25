package ssm.view;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.LanguagePropertyType.TOOLTIP_ADD_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_EXIT;
import static ssm.LanguagePropertyType.TOOLTIP_LOAD_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_MOVE_DOWN;
import static ssm.LanguagePropertyType.TOOLTIP_MOVE_UP;
import static ssm.LanguagePropertyType.TOOLTIP_NEW_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_NEXT_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_PREVIOUS_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_REMOVE_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_SAVE_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_VIEW_SLIDE_SHOW;
import static ssm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_SHOW_EDIT_VBOX;
import static ssm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_BUTTON;
import static ssm.StartupConstants.ICON_ADD_SLIDE;
import static ssm.StartupConstants.ICON_EXIT;
import static ssm.StartupConstants.ICON_LOAD_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_MOVE_DOWN;
import static ssm.StartupConstants.ICON_MOVE_UP;
import static ssm.StartupConstants.ICON_NEW_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_NEXT;
import static ssm.StartupConstants.ICON_PREVIOUS;
import static ssm.StartupConstants.ICON_REMOVE_SLIDE;
import static ssm.StartupConstants.ICON_SAVE_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_VIEW_SLIDE_SHOW;
import static ssm.StartupConstants.PATH_ICONS;
import static ssm.StartupConstants.STYLE_SHEET_UI;
import ssm.controller.FileController;
import ssm.controller.SlideShowEditController;
import ssm.model.Slide;
import ssm.model.SlideShowModel;
import ssm.error.ErrorHandler;
import ssm.file.SlideShowFileManager;
import static ssm.file.SlideShowFileManager.SLASH;

/**
 * This class provides the User Interface for this application, providing
 * controls and the entry points for creating, loading, saving, editing, and
 * viewing slide shows.
 *
 * @author McKilla Gorilla & _____________
 */
public class SlideShowMakerView {

    // THIS IS THE MAIN APPLICATION UI WINDOW AND ITS SCENE GRAPH
    Stage primaryStage;
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane ssmPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newSlideShowButton;
    Button loadSlideShowButton;
    Button saveSlideShowButton;
    Button viewSlideShowButton;
    Button exitButton;

    // WORKSPACE
    HBox workspace;

    int indexOfSlide;
    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    VBox slideEditToolbar;
    Button addSlideButton;
    Button removeSlideButton;
    Button upSlideButton;
    Button downSlideButton;

    // AND THIS WILL GO IN THE CENTER
    ScrollPane slidesEditorScrollPane;
    VBox slidesEditorPane;

    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    SlideShowModel slideShow;

    // THIS IS FOR SAVING AND LOADING SLIDE SHOWS
    SlideShowFileManager fileManager;

    // THIS CLASS WILL HANDLE ALL ERRORS FOR THIS PROGRAM
    private ErrorHandler errorHandler;

    // THIS CONTROLLER WILL ROUTE THE PROPER RESPONSES
    // ASSOCIATED WITH THE FILE TOOLBAR
    private FileController fileController;

    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private SlideShowEditController editController;

    //To enter the title of the slide show (MY WORK)
    public static TextField title;

    //Button for the slideshow presentation
    HBox slideShowControls;
    Button previousSlideButton;
    Button nextSlideButton;

    //For SlideShow variables
    Slide slide;
    String imagePath;
    File file;
    URL fileURL;
    Image slideImage;
    ImageView image;
    /**
     * Default constructor, it initializes the GUI for use, but does not yet
     * load all the language-dependent controls, that needs to be done via the
     * startUI method after the user has selected a language.
     */
    public SlideShowMakerView(SlideShowFileManager initFileManager) {
        // FIRST HOLD ONTO THE FILE MANAGER
        fileManager = initFileManager;

        // MAKE THE DATA MANAGING MODEL
        slideShow = new SlideShowModel(this);

        // WE'LL USE THIS ERROR HANDLER WHEN SOMETHING GOES WRONG
        errorHandler = new ErrorHandler(this);
    }

    // ACCESSOR METHODS
    public SlideShowModel getSlideShow() {
        return slideShow;
    }

    public Stage getWindow() {
        return primaryStage;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Initializes the UI controls and gets it rolling.
     *
     * @param initPrimaryStage The window for this application.
     *
     * @param windowTitle The title for this window.
     */
    public void startUI(Stage initPrimaryStage, String windowTitle) {
        // THE TOOLBAR ALONG THE NORTH
        initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
        // TO THE WINDOW YET
        initWorkspace();

        // NOW SETUP THE EVENT HANDLERS
        initEventHandlers();

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        // KEEP THE WINDOW FOR LATER
        primaryStage = initPrimaryStage;
        //MY WORK
        primaryStage.getIcons().add(new Image("file:./images/icons/Icon.png"));
        initWindow(windowTitle);
    }

    // UI SETUP HELPER METHODS
    private void initWorkspace() {
        // FIRST THE WORKSPACE ITSELF, WHICH WILL CONTAIN TWO REGIONS
        workspace = new HBox();

        // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
        slideEditToolbar = new VBox();
        slideEditToolbar.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDIT_VBOX);
        addSlideButton = this.initChildButton(slideEditToolbar, ICON_ADD_SLIDE, TOOLTIP_ADD_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        removeSlideButton = this.initChildButton(slideEditToolbar, ICON_REMOVE_SLIDE, TOOLTIP_REMOVE_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        upSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_UP, TOOLTIP_MOVE_UP, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);

        downSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_DOWN, TOOLTIP_MOVE_DOWN, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        // AND THIS WILL GO IN THE CENTER
        slidesEditorPane = new VBox();
        slidesEditorScrollPane = new ScrollPane(slidesEditorPane);

        //Adding a text field to enter the title of the slide show (MY WORK)
        Label promptTitle = new Label("Title:");
        title = new TextField();
        title.setPromptText("ENTER TITLE");

        // NOW PUT THESE TWO IN THE WORKSPACE
        workspace.getChildren().add(slideEditToolbar);
        workspace.getChildren().addAll(promptTitle, title);
        workspace.getChildren().add(slidesEditorScrollPane);

    }

    private void initEventHandlers() {
        // FIRST THE FILE CONTROLS
        fileController = new FileController(this, fileManager);
        newSlideShowButton.setOnAction(e -> {
            fileController.handleNewSlideShowRequest();
            upSlideButton.setDisable(true);
            downSlideButton.setDisable(true);
            removeSlideButton.setDisable(true);
        });
        loadSlideShowButton.setOnAction(e -> {
            fileController.handleLoadSlideShowRequest();
        });
        saveSlideShowButton.setOnAction(e -> {
            fileController.handleSaveSlideShowRequest();
        });
        exitButton.setOnAction(e -> {
            Stage exit = new Stage();
            exit.getIcons().add(new Image("file:./images/icons/Icon.png"));
            exit.setTitle("Exit?");
            VBox exitPane = new VBox(50);
            exitPane.setPadding(new Insets(10,10,10,10));
            Label question = new Label("Are you sure you want to quit?");
            HBox YN = new HBox();
            YN.setSpacing(10);
            Button yes = new Button("Yes");
            yes.setOnAction(e1->fileController.handleExitRequest());
            Button no=new Button("No");
            no.setOnAction(e2-> exit.close());
            YN.setPadding(new Insets(10,10,10,10));
            YN.getChildren().addAll(yes,no);
            exitPane.getChildren().addAll(question,YN);
            Scene climax=new Scene(exitPane,250,220);
            exit.setScene(climax);
            exit.show();

        });
        viewSlideShowButton.setOnAction(e -> {
            try {
                Stage p = new Stage();
                p.getIcons().add(new Image("file:./images/icons/Icon.png"));
                BorderPane mainPane = new BorderPane();
                mainPane.setPadding(new Insets(12, 12, 12, 12));
                //Top part of the pane(Add the caption)
                //middle part of the pane(Add the image)
                //Bottom part of the pane(Add the buttons)
                slideShowControls = new HBox();
                mainPane.setBottom(slideShowControls);

                previousSlideButton = this.initChildButton(slideShowControls, ICON_PREVIOUS, TOOLTIP_PREVIOUS_SLIDE, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);

                nextSlideButton = this.initChildButton(slideShowControls, ICON_NEXT, TOOLTIP_NEXT_SLIDE, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);

                indexOfSlide = 0;

                slide = slideShow.getSlides().get(indexOfSlide);
                imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
                file = new File(imagePath);
                fileURL = file.toURI().toURL();
                slideImage = new Image(fileURL.toExternalForm());
                image = new ImageView(slideImage);

                mainPane.setCenter(image);
                mainPane.setTop(new Label(slide.getCaption()));
                Scene slideShowScene = new Scene(mainPane, 800, 600);
                nextSlideButton.setOnAction(e1 -> {
                    try {
                        indexOfSlide++;
                        indexOfSlide = indexOfSlide % slideShow.getSlides().size();
                        slide = slideShow.getSlides().get(indexOfSlide);
                        imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
                        file = new File(imagePath);
                        fileURL = file.toURI().toURL();
                        slideImage = new Image(fileURL.toExternalForm());
                        image = new ImageView(slideImage);
                        mainPane.setCenter(image);
                        mainPane.setTop(new Label(slide.getCaption()));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SlideShowMakerView.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });

                previousSlideButton.setOnAction(e1 -> {
                    try {
                        indexOfSlide--;
                        indexOfSlide = (int) Math.abs(indexOfSlide % slideShow.getSlides().size());
                        slide = slideShow.getSlides().get(indexOfSlide);
                        imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
                        file = new File(imagePath);
                        fileURL = file.toURI().toURL();
                        slideImage = new Image(fileURL.toExternalForm());
                        image = new ImageView(slideImage);
                        mainPane.setCenter(image);
                        mainPane.setTop(new Label(slide.getCaption()));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(SlideShowMakerView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                p.setScene(slideShowScene);
                p.setTitle(slideShow.getTitle());
                p.setFullScreen(true);
                p.show();
            } catch (MalformedURLException ex) {
                Logger.getLogger(SlideShowMakerView.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        // THEN THE SLIDE SHOW EDIT CONTROLS
        editController = new SlideShowEditController(this);

        addSlideButton.setOnAction(e -> {
            editController.processAddSlideRequest();
            saveSlideShowButton.setDisable(false);
            removeSlideButton.setDisable(false);
            upSlideButton.setDisable(false);
            downSlideButton.setDisable(false);
        });

        removeSlideButton.setOnAction(e -> {
            editController.processRemoveSlideRequest();
            saveSlideShowButton.setDisable(false);
        });

        upSlideButton.setOnAction(e -> {
            editController.processMoveSlideUpRequest();
            saveSlideShowButton.setDisable(false);
        });

        downSlideButton.setOnAction(e -> {
            editController.processMoveSlideDownRequest();
            saveSlideShowButton.setDisable(false);
        });

    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolbarPane = new FlowPane();

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        newSlideShowButton = initChildButton(fileToolbarPane, ICON_NEW_SLIDE_SHOW, TOOLTIP_NEW_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        loadSlideShowButton = initChildButton(fileToolbarPane, ICON_LOAD_SLIDE_SHOW, TOOLTIP_LOAD_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        saveSlideShowButton = initChildButton(fileToolbarPane, ICON_SAVE_SLIDE_SHOW, TOOLTIP_SAVE_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
        viewSlideShowButton = initChildButton(fileToolbarPane, ICON_VIEW_SLIDE_SHOW, TOOLTIP_VIEW_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
        exitButton = initChildButton(fileToolbarPane, ICON_EXIT, TOOLTIP_EXIT, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
    }

    private void initWindow(String windowTitle) {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(windowTitle);

        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // SETUP THE UI, NOTE WE'LL ADD THE WORKSPACE LATER
        ssmPane = new BorderPane();
        ssmPane.setTop(fileToolbarPane);
        primaryScene = new Scene(ssmPane);

        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
        // WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
        primaryScene.getStylesheets().add(STYLE_SHEET_UI);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    /**
     * This helps initialize buttons in a toolbar, constructing a custom button
     * with a customly provided icon and tooltip, adding it to the provided
     * toolbar pane, and then returning it.
     */
    public Button initChildButton(
            Pane toolbar,
            String iconFileName,
            LanguagePropertyType tooltip,
            String cssClass,
            boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_ICONS + iconFileName;
        Image buttonImage = new Image(imagePath);
        Button button = new Button();
        button.getStyleClass().add(cssClass);
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
        return button;
    }

    /**
     * Updates the enabled/disabled status of all toolbar buttons.
     *
     * @param saved
     */
    public void updateToolbarControls(boolean saved) {
        // FIRST MAKE SURE THE WORKSPACE IS THERE
        ssmPane.setCenter(workspace);

        // NEXT ENABLE/DISABLE BUTTONS AS NEEDED IN THE FILE TOOLBAR
        saveSlideShowButton.setDisable(saved);
        viewSlideShowButton.setDisable(false);

        // AND THE SLIDESHOW EDIT TOOLBAR
        addSlideButton.setDisable(false);
        removeSlideButton.setDisable(false);
        upSlideButton.setDisable(false);
        downSlideButton.setDisable(false);
    }

    /**
     * Uses the slide show data to reload all the components for slide editing.
     *
     * @param slideShowToLoad SLide show being reloaded.
     */
    public void reloadSlideShowPane(SlideShowModel slideShowToLoad) {
        slidesEditorPane.getChildren().clear();
        for (Slide slide : slideShowToLoad.getSlides()) {
            SlideEditView slideEditor = new SlideEditView(slide, slideShow);

            slidesEditorPane.getChildren().add(slideEditor);
        }
    }
}
