package nl.groep4.kvc.client.view.scene;

import javafx.scene.Scene;
import nl.groep4.kvc.client.controller.Controller;

/**
 * Interface for making code have some guideline
 * 
 * @version 1.0
 * @author Tim
 **/
public interface SceneHolder {

    public Scene getScene();

    /**
     * Update the configuration of sceneHolder
     */
    public void updateConfig();

    public void registerController(Controller controller);
}
