package nl.groep4.kvc.server.controller;

import java.rmi.RemoteException;
import java.util.Map.Entry;
import java.util.Set;

import nl.groep4.kvc.common.enumeration.Resource;
import nl.groep4.kvc.common.interfaces.Player;
import nl.groep4.kvc.common.interfaces.UpdateMap;
import nl.groep4.kvc.server.model.ServerCardHolder;
import nl.groep4.kvc.server.model.ServerCosts;

public class ServerShopController {

    private ServerKolonistenVanCatan controller;
    private ServerCardHolder holder = new ServerCardHolder();

    public ServerShopController(ServerKolonistenVanCatan serverKolonistenVanCatan) {
	this.controller = serverKolonistenVanCatan;
    }

    private boolean hasAllResources(Set<Entry<Resource, Integer>> set) {
	boolean hasAllResources = true;
	try {
	    for (Entry<Resource, Integer> resource : set) {
		if (controller.getTurn().getResourceAmount(resource.getKey()) < resource.getValue()) {
		    hasAllResources = false;
		}
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	    return false;
	}
	return hasAllResources;
    }

    private void takeResources(Set<Entry<Resource, Integer>> set) {
	try {
	    for (Entry<Resource, Integer> resource : set) {
		controller.getTurn().takeResource(resource.getKey(), resource.getValue());
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    public void buyStreet() {
	try {
	    Player who = controller.getTurn();
	    if (hasAllResources(ServerCosts.STREET_COSTS.entrySet())) {
		takeResources(ServerCosts.STREET_COSTS.entrySet());
		controller.buildStreetModus(1);
	    } else {
		who.getUpdateable(UpdateMap.class).popup("noresources");
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    public void buyVillage() {
	try {
	    Player who = controller.getTurn();
	    if (hasAllResources(ServerCosts.VILLAGE_COSTS.entrySet())) {
		takeResources(ServerCosts.VILLAGE_COSTS.entrySet());
		controller.buildVillageModus(1);
	    } else {
		who.getUpdateable(UpdateMap.class).popup("noresources");
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    public void buyCity() {
	try {
	    Player who = controller.getTurn();
	    if (hasAllResources(ServerCosts.CITY_COSTS.entrySet())) {
		takeResources(ServerCosts.CITY_COSTS.entrySet());
		controller.buildCityModus(1);
	    } else {
		who.getUpdateable(UpdateMap.class).popup("noresources");
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    public void buyCard() {
	try {
	    Player who = controller.getTurn();
	    if (holder.hasCards()) {
		if (hasAllResources(ServerCosts.CITY_COSTS.entrySet())) {
		    takeResources(ServerCosts.CITY_COSTS.entrySet());
		    who.addCard(holder.drawCard());
		    UpdateMap view = who.getUpdateable(UpdateMap.class);
		    view.closeOverlay();
		    controller.updateCards();
		    ;
		} else {
		    who.getUpdateable(UpdateMap.class).popup("noresources");
		}
	    } else {
		who.getUpdateable(UpdateMap.class).popup("nocards");
	    }
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

}
