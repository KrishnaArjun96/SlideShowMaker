package ssm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import ssm.view.SlideShowMakerView;

/**
 * This class manages all the data associated with a slideshow.
 *
 * @author McKilla Gorilla & Krishna
 */
public class SlideShowModel {

    SlideShowMakerView ui;
    String title;
    ObservableList<Slide> slides;
    Slide selectedSlide;
    public static int number=0;

    public SlideShowModel(SlideShowMakerView initUI) {
        ui = initUI;
        slides = FXCollections.observableArrayList();
        reset();
    }

    // ACCESSOR METHODS
    public boolean isSlideSelected() {
        return selectedSlide != null;
    }

    public ObservableList<Slide> getSlides() {
        return slides;
    }

    public Slide getSelectedSlide() {
        return selectedSlide;
    }

    public String getTitle() {
        return title;
    }

    // MUTATOR METHODS
    public void setSelectedSlide(Slide initSelectedSlide) {
        selectedSlide = initSelectedSlide;
    }

    public void setTitle(String initTitle) {
        title = initTitle;
    }

    // SERVICE METHODS
    /**
     * Resets the slide show to have no slides and a default title.
     */
    public void reset() {
        slides.clear();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        title = props.getProperty(LanguagePropertyType.DEFAULT_SLIDE_SHOW_TITLE);
        selectedSlide = null;
    }

    /**
     * Adds a slide to the slide show with the parameter settings.
     *
     * @param initImageFileName File name of the slide image to add.
     * @param initImagePath File path for the slide image to add.
     * @param initCaption Caption of the slide
     */
    public void addSlide(String initImageFileName,
            String initImagePath, String initCaption) {
        Slide slideToAdd = new Slide(initImageFileName, initImagePath, initCaption);
        slides.add(slideToAdd);
        selectedSlide = slideToAdd;
        ui.reloadSlideShowPane(this);
    }

    public void RemoveSlide() {

        slides.remove(selectedSlide);
        ui.reloadSlideShowPane(this);
    }

    public void MoveUp() {
        int i = slides.indexOf(selectedSlide) - 1;
        if (i < 0) 
            return ;
        
        Slide s = slides.get(i);
        slides.set(i, selectedSlide);
        slides.set(i + 1, s);

        ui.reloadSlideShowPane(this);
    }

    public void MoveDown() {
        int i = slides.indexOf(selectedSlide) + 1;
        if(i>slides.size())
            return;
        Slide s = slides.get(i);
        slides.set(i, selectedSlide);
        slides.set(i - 1, s);
        ui.reloadSlideShowPane(this);
    }

}
