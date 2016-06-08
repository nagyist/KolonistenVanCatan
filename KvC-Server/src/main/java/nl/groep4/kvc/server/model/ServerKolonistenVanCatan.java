package nl.groep4.kvc.server.model;

import nl.groep4.kvc.common.interfaces.KolonistenVanCatan;
import nl.groep4.kvc.common.map.Map;
import nl.groep4.kvc.server.model.map.ServerMap;

/**
 * Instance of KolonistenVanCatan
 * 
 * @author Tim
 * @version 1.0
 */
public class ServerKolonistenVanCatan implements KolonistenVanCatan {

    private ServerMap map;
    private int round;

    @Override
    public Map getMap() {
	return map;
    }

    /**
     * Creates the map
     */
    public void createMap() {
	map = new ServerMap();
    }

    @Override
    public int getRound() {
	return round;
    }

    @Override
    public void nextRound() {
	round++;
    }

}
