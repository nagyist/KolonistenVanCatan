package nl.groep4.kvc.client;

import javafx.application.Application;
import nl.groep4.kvc.client.view.ViewMaster;

/**
 * Starts the client
 * 
 * @version 1.0
 * @author Tim
 */
public class ClientStarter {

    /**
     * Launches the application
     * 
     * @param args
     *            contains the supplied command-line arguments
     */
    public static void main(String[] args) throws Exception {
	Application.launch(ViewMaster.class, args);
    }

}
