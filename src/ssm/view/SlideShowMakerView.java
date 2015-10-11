package ssm.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import static java.lang.System.out;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
import ssm.SlideShowMaker;
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
 * @author McKilla Gorilla & Krishna
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
    private boolean saved = false;
    Label showCap;
    VBox imgCap;
    Label showTitle;

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
        slideEditToolbar.setSpacing(10);
        slideEditToolbar.setPadding(new Insets(5, 5, 5, 5));
        slideEditToolbar.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDIT_VBOX);
        addSlideButton = this.initChildButton(slideEditToolbar, ICON_ADD_SLIDE, TOOLTIP_ADD_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        removeSlideButton = this.initChildButton(slideEditToolbar, ICON_REMOVE_SLIDE, TOOLTIP_REMOVE_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        upSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_UP, TOOLTIP_MOVE_UP, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);

        downSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_DOWN, TOOLTIP_MOVE_DOWN, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        // AND THIS WILL GO IN THE CENTER
        slidesEditorPane = new VBox();
        slidesEditorPane.setSpacing(10);
        slidesEditorScrollPane = new ScrollPane(slidesEditorPane);

        Label promptTitle;
        //Adding a text field to enter the title of the slide show (MY WORK)
        if (SlideShowMaker.lang.getValue().equals("English")) {
            promptTitle = new Label("Title:");
            promptTitle.setFont(Font.font("Harrington", 20));
            title = new TextField();
            title.setFont(Font.font("Harrington", 14));
            title.setPromptText("ENTER TITLE");
        } else {
            promptTitle = new Label("título:");
            title = new TextField();
            title.setPromptText("entrar título");
        }

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
            reloadSlideShowPane(slideShow);
            title.setText("");
            upSlideButton.setDisable(true);
            downSlideButton.setDisable(true);
            removeSlideButton.setDisable(true);
        });

        loadSlideShowButton.setOnAction(e -> {
            saved = true;
            fileController.handleLoadSlideShowRequest();
            if (slideShow.getSlides().size() > 1) {
                upSlideButton.setDisable(false);
                downSlideButton.setDisable(false);
            } //IF SIZE >= 1
            //SET REMOVESLIDE DISABLE FALSE;
            else if (slideShow.getSlides().size() <= 1) {
                upSlideButton.setDisable(true);
                downSlideButton.setDisable(true);
            } else if (slideShow.getSlides().size() == 0) {
                removeSlideButton.setDisable(true);
            }
        });

        saveSlideShowButton.setOnAction(e -> {
            fileController.handleSaveSlideShowRequest();
            saved = true;
        });

        exitButton.setOnAction(e -> {
            Stage exit = new Stage();
            exit.getIcons().add(new Image("file:./images/icons/Icon.png"));
            if (SlideShowMaker.lang.getValue().equals("English")) {
                exit.setTitle("Exit?");
            } else {
                exit.setTitle("Salida?");
            }
            VBox exitPane = new VBox(50);
            exitPane.setStyle("-fx-background-color: rgb(255,225,78);");
            exitPane.setPadding(new Insets(10, 10, 10, 10));
            String labText = "";
            if (SlideShowMaker.lang.getValue().equals("English")) {
                labText = "Do you want to save before you quit?";
            } else {
                labText = "Quieres guardar antes de salir ?";
            }
            Label question = new Label(labText);
            question.setFont(Font.font("Harrington", 16));
            HBox YN = new HBox();
            YN.setSpacing(10);
            Button yes = new Button();
            yes.setFont(Font.font("Harrington", 16));
            if (SlideShowMaker.lang.getValue().equals("English")) {
                yes.setText("Yes");
            } else {
                yes.setText("Sí");
            }
            yes.setOnAction(e1 -> {
                fileController.handleSaveSlideShowRequest();
                exit.close();
                primaryStage.close();
            });
            yes.setDisable(saved);
            Button no = new Button("No");
            no.setFont(Font.font("Harrington", 16));
            no.setOnAction(e2 -> {
                exit.close();
                primaryStage.close();
            });
            Button cancel = new Button("Cancel");
            cancel.setFont(Font.font("Harrington", 16));
            if (SlideShowMaker.lang.getValue().equals("English")) {
                cancel.setText("Cancel");

            } else {
                cancel.setText("Cancelar");
            }
            cancel.setOnAction(e3 -> exit.close());
            YN.setPadding(new Insets(10, 10, 10, 10));
            YN.getChildren().addAll(yes, no, cancel);
            exitPane.getChildren().addAll(question, YN);
            Scene climax = new Scene(exitPane, 300, 250);
            exit.setScene(climax);
            exit.show();

        });

        //The slideshow web view is generated here
        viewSlideShowButton.setOnAction(e -> {

            File Sites = new File("sites");
            Sites.mkdir();

            File Directory = new File("sites/" + slideShow.getTitle());
            if (Directory.exists()) {
                Directory.delete();
            }
            Directory.mkdir();

            try {
                File file = new File("sites/" + slideShow.getTitle() + "/index.html");
                file.createNewFile();
                Writer writer = new BufferedWriter(new FileWriter(file));
                writer.write("<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "    <head>\n"
                        + "        <title>" + slideShow.getTitle() + "</title>\n"
                        + "        <meta charset=\"UTF-8\">\n"
                        + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"CSS/slideshow.css\">\n"
                        + "        <script src=\"JS/slideshow.js\" type=\"text/javascript\"></script>\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>" + slideShow.getTitle() + "</h1>\n"
                        + "        <div align=\"center\" color=\"purple\">\n"
                        + "         <img src=\"img/" + slideShow.getSlides().get(0).getImageFileName() + "\" id=\"slideShow\" alt=\"Image doesn't exist\" style=\"width:454px;height:458px;\"> \n "
                        + "            <br>\n"
                        + "        </div>\n"
                        + "        <div id=\"Captions\">\n"
                        + "            <label id=\"caption\">" + slideShow.getSlides().get(0).getCaption() + "</label>\n"
                        + "            <br>\n"
                        + "            <button onClick=\"prevSlide()\"><img src=\"img/Previous.png\" alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\" > </button>\n"
                        + "            <button id=\"play\" onClick=\"playSlide()\"> <img src=\"img/play.png\" alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\"> </button>\n"
                        + "            <button id=\"pause\" onClick=\"pause()\"> <img src=\"img/pause.png\"  alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\"> </button>\n"
                        + "            <button onClick=\"nextSlide()\"><img src=\"img/Next.png\" alt=\"Image doesn't exist\" style=\"width:20px;height:20px;\"></button>\n"
                        + "        </div>\n"
                        + "    </body>\n"
                        + "</html>");
                writer.close();

                File cssDirectory = new File("sites/" + slideShow.getTitle() + "/CSS");
                cssDirectory.mkdir();

                File cssFile = new File("sites/" + slideShow.getTitle() + "/CSS/slideshow.css");
                Writer writerCSS = new BufferedWriter(new FileWriter(cssFile));
                writerCSS.write("body {\n"
                        + "}\n"
                        + "\n"
                        + "h1 {\n"
                        + "    font-family: \"Harrington\";\n"
                        + "    text-align: left;\n"
                        + "    background-color:#CC0000;\n"
                        + "    font-size: 40px;"
                        + "    text-shadow: 0 0 3px #FF0000, 0 0 5px #0000FF;"
                        + "}\n"
                        + "\n"
                        + "#Captions {\n"
                        + "    text-align: center;\n"
                        + "    background-color: navy;\n"
                        + "}"
                        + "p {\n"
                        + "    font-family: \"Harrington\";\n"
                        + "    font-size: 20px;\n"
                        + "}");
                writerCSS.close();

                File imgDirectory = new File("sites/" + slideShow.getTitle() + "/img");
                imgDirectory.mkdir();
                Files.copy(Paths.get("./images/icons/play.png"),Paths.get("sites/" + slideShow.getTitle() + "/img/play.png/"), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Paths.get("./images/icons/pause.png"),Paths.get("sites/" + slideShow.getTitle() + "/img/pause.png"), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Paths.get("./images/icons/Next.png"),Paths.get("sites/" + slideShow.getTitle() + "/img/Next.png"), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Paths.get("./images/icons/Previous.png"),Paths.get("sites/" + slideShow.getTitle() + "/img/Previous.png"), StandardCopyOption.REPLACE_EXISTING);                
                String source = "";
                String dest = "";
                for (int i = 0; i < slideShow.getSlides().size(); i++) {
                    source = slideShow.getSlides().get(i).getImagePath() + SLASH + slideShow.getSlides().get(i).getImageFileName();
                    dest = "sites/" + slideShow.getTitle() + "/img/" + slideShow.getSlides().get(i).getImageFileName();
                    Files.copy(Paths.get(source), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
                }

                File jsDirectory = new File("sites/" + slideShow.getTitle() + "/JS");
                jsDirectory.mkdir();

                File jsFile = new File("sites/" + slideShow.getTitle() + "/JS/slideshow.js");
                Writer writerJS = new BufferedWriter(new FileWriter(jsFile));
                writerJS.write("var imgs=new Array();\n"
                        + "var caps =new Array();\n"
                        + "var indexOfSlide=1;\n"
                        + "var timeOfSlide = 2000;\n"
                        + "var slideShowTimer;\n");
                for (int i = 0; i < slideShow.getSlides().size(); i++) {
                    writerJS.write("imgs[" + (i + 1) + "]='img/" + slideShow.getSlides().get(i).getImageFileName() + "\';\n");
                    writerJS.write("caps[" + (i + 1) + "]=\"" + slideShow.getSlides().get(i).getCaption() + "\";\n");
                }
                writerJS.write("function nextSlide(){\n"
                        + "    indexOfSlide++;\n"
                        + "    if(indexOfSlide>" + (slideShow.getSlides().size()) + ")\n"
                        + "        indexOfSlide=1;\n"
                        + "    document.images.slideShow.src=imgs[indexOfSlide];\n"
                        + "    document.getElementById(\"caption\").innerHTML=caps[indexOfSlide]; \n"
                        + "}\n\n");
                writerJS.write("function prevSlide(){\n"
                        + "    indexOfSlide--;\n"
                        + "    if(indexOfSlide<1)\n"
                        + "        indexOfSlide=" + slideShow.getSlides().size() + ";\n"
                        + "    document.images.slideShow.src=imgs[indexOfSlide];\n"
                        + "    document.getElementById(\"caption\").innerHTML=caps[indexOfSlide];\n"
                        + "}\n\n"
                        //  + "function changeToPause(){\n"
                        //  + "    document.getElementById(\"play\").innerHTML = \"pause\";\n"
                        //  + "    document.getElementById(\"play\").onClick = changeToPlay();\n"
                        //  + "    document.getElementById(\"play\").id = 'pause';\n"
                        //  + "    playSlide();\n"
                        //  + "}\n"
                        + "\n"
                        + "function playSlide() {\n"
                        + "     var recur=\"playSlide()\";\n "
                        + "     slideShowTimer=setTimeout(recur,timeOfSlide);\n"
                        + "     nextSlide();"
                        + "\n"
                        + "}\n"
                        + "\n"
                        //   + "function changeToPlay(){\n"
                        //   + "    document.getElementById(\"pause\").innerHTML = \"play\";\n"
                        //   + "    document.getElementById(\"pause\").onClick = changeToPause();\n"
                        //   + "    document.getElementById(\"pause\").id = 'play';\n"
                        //   + "    pause();\n"
                        //   + "}"
                        + "\n"
                        + "function pause(){\n"
                        + "    clearTimeout(slideShowTimer);\n"
                        + "}");
                writerJS.close();

                WebView webView = new WebView();
                webView.getEngine().load(file.toURI().toString());
                Stage blah = new Stage();
                blah.getIcons().add(new Image("file:./images/icons/Icon.png"));
                blah.setTitle(slideShow.getTitle());
                Scene blahScene = new Scene(webView, 700, 600);
                blah.setScene(blahScene);
                blah.show();
            } catch (IOException ex) {
                System.out.println("Exception");
            }
        });

        // THEN THE SLIDE SHOW EDIT CONTROLS
        editController = new SlideShowEditController(this);

        addSlideButton.setOnAction(e -> {
            editController.processAddSlideRequest();
            saveSlideShowButton.setDisable(false);
            saved = false;

            if (slideShow.getSlides().size() > 1) {
                upSlideButton.setDisable(false);
                downSlideButton.setDisable(false);
                removeSlideButton.setDisable(false);
            } //IF SIZE >= 1
            //SET REMOVESLIDE DISABLE FALSE;
            else if (slideShow.getSlides().size() == 1) {
                upSlideButton.setDisable(true);
                downSlideButton.setDisable(true);
                removeSlideButton.setDisable(false);
            } else if (slideShow.getSlides().size() == 0) {
                removeSlideButton.setDisable(true);
            }
        });

        removeSlideButton.setOnAction(e -> {
            editController.processRemoveSlideRequest();
            saveSlideShowButton.setDisable(false);
            updateToolbarControls(saved);
            saved = false;

            //SET REMOVESLIDE DISABLE FALSE;
            if (slideShow.getSlides().size() == 1) {
                upSlideButton.setDisable(true);
                downSlideButton.setDisable(true);
            } else if (slideShow.getSlides().size() == 0) {
                upSlideButton.setDisable(true);
                downSlideButton.setDisable(true);
                removeSlideButton.setDisable(true);
            }

        });

        upSlideButton.setOnAction(e -> {
            editController.processMoveSlideUpRequest();
            saveSlideShowButton.setDisable(false);
            saved = false;
        });

        downSlideButton.setOnAction(e -> {
            editController.processMoveSlideDownRequest();
            saveSlideShowButton.setDisable(false);
            saved = false;
        });

    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolbarPane = new FlowPane();
        fileToolbarPane.setHgap(5);
        fileToolbarPane.setPadding(new Insets(5, 5, 5, 5));

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
        ssmPane.setStyle("-fx-background-color: rgb(255,225,78);");
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
