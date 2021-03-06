package nl.groep4.kvc.server.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import nl.groep4.kvc.common.interfaces.Player;
import nl.groep4.kvc.common.util.Scheduler;

/**
 * The class will delete players views.
 * 
 * version: 1.0
 * 
 * @author Tim
 *
 */

public class ConnectionUtil {

    /**
     * Deletes the players views who are not in the game anymore.
     * 
     * @param players
     *            De players who were in the game
     * @param onRemove
     *            De list with players who are deleted
     * @throws RemoteException
     *             Any remotely invoked method.
     */

    public static void cleanup(List<Player> players, Consumer<Player> onRemove) throws RemoteException {
	Iterator<Player> playerIT = new ArrayList<>(players).iterator();
	List<Runnable> runs = new ArrayList<>();
	while (playerIT.hasNext()) {
	    Player pl = playerIT.next();
	    runs.add(() -> {
		try {
		    pl.getUpdateable().testConnection();
		} catch (RemoteException ex) {
		    try {
			playerIT.remove();
			onRemove.accept(pl);
			System.out.printf("Player %s has been removed by disconnecting.\n", pl.getUsername());
		    } catch (Exception subex) {
			System.err.println(subex);
		    }
		} catch (NullPointerException npe) {
		    try {
			System.out.printf("No updateable for %s\n", pl.getUsername());
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    });
	    Scheduler.runAsyncdSync(runs);
	}
    }

}
