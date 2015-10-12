package ssm;

/**
 * This class provides setup constants for initializing the application that are
 * NOT language dependent.
 *
 * @author McKilla Gorilla & Krishna
 */
public class StartupConstants {

    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    public static String UI_PROPERTIES_FILE_NAME = "properties_EN.xml";
    public static String PATH_DATA = "./data/";
    public static String PATH_SLIDE_SHOWS = PATH_DATA + "slide_shows/";
    public static String PATH_IMAGES = "./images/";
    public static String PATH_ICONS = PATH_IMAGES + "icons/";
    public static String PATH_SLIDE_SHOW_IMAGES = PATH_IMAGES + "slide_show_images/";
    public static String PATH_CSS = "ssm/style/";
    public static String STYLE_SHEET_UI = PATH_CSS + "SlideShowMakerStyle.css";

    // HERE ARE THE LANGUAGE INDEPENDENT GUI ICONS
    public static String ICON_NEW_SLIDE_SHOW = "New.png";
    public static String ICON_LOAD_SLIDE_SHOW = "Load.png";
    public static String ICON_SAVE_SLIDE_SHOW = "Save.png";
    public static String ICON_VIEW_SLIDE_SHOW = "View.png";
    public static String ICON_EXIT = "Exit.png";
    public static String ICON_ADD_SLIDE = "Add.png";
    public static String ICON_REMOVE_SLIDE = "Remove.png";
    public static String ICON_MOVE_UP = "MoveUp.png";
    public static String ICON_MOVE_DOWN = "MoveDown.png";
    public static String ICON_PREVIOUS = "Previous.png";
    public static String ICON_NEXT = "Next.png";

    // @todo
    public static String DEFAULT_SLIDE_IMAGE = "DefaultStartSlide.png";
    public static int DEFAULT_THUMBNAIL_WIDTH = 200;
    public static int DEFAULT_SLIDE_SHOW_HEIGHT = 500;

    // CSS STYLE SHEET CLASSES
    public static String CSS_CLASS_VERTICAL_TOOLBAR_BUTTON = "vertical_toolbar_button";
    public static String CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON = "horizontal_toolbar_button";
    public static String CSS_CLASS_SLIDE_SHOW_EDIT_VBOX = "slide_show_edit_vbox";
    public static String CSS_CLASS_SLIDE_EDIT_VIEW = "slide_edit_view";
    public static String CSS_CLASS_LANGUAGE_PROMPT = "language_prompt_vbox";
    public static String CSS_CLASS_LEFT_TOOLBAR = "left_toolbar";
    public static String CSS_CLASS_TOP_TOOLBAR = "top_toolbar";
    public static String CSS_CLASS_SSM_PANE = "ssm_pane";
    public static String CSS_CLASS_SSM_SLIDE_EDITOR_PANE = "ssm_slide_editor_pane";
    public static String CSS_CLASS_SSM_SCROLL_PANE = "ssm_scroll_pane";
    public static String CSS_CLASS_SSM_TEXT_FIELD = "ssm_text_field";
    public static String CSS_CLASS_SSM_LABEL = "ssm_label";
    public static String CSS_CLASS_SSM_EXIT_BUTTONS = "ssm_exit_buttons";
    public static String CSS_EXIT_LABEL="ssm_exit_label";
    public static String CSS_EXIT_HBOX="ssm_exit_hbox";    
    public static String CSS_EXIT_VBOX="ssm_exit_vbox";    

    // UI LABELS
    public static String LABEL_SLIDE_SHOW_TITLE = "slide_show_title";

}
