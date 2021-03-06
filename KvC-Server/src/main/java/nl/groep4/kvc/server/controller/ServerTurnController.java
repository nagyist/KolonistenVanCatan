package nl.groep4.kvc.server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.groep4.kvc.common.enumeration.BuildingType;
import nl.groep4.kvc.common.enumeration.Direction;
import nl.groep4.kvc.common.enumeration.GameState;
import nl.groep4.kvc.common.enumeration.Point;
import nl.groep4.kvc.common.enumeration.TurnState;
import nl.groep4.kvc.common.interfaces.Player;
import nl.groep4.kvc.common.interfaces.UpdateMap;
import nl.groep4.kvc.common.map.Building;
import nl.groep4.kvc.common.map.Street;
import nl.groep4.kvc.common.map.Tile;
import nl.groep4.kvc.common.map.TileResource;
import nl.groep4.kvc.common.util.CollectionUtil;
import nl.groep4.kvc.common.util.Scheduler;

/**
 * Controls the turns of the players.
 * 
 * @author Tim
 * @version 1.0
 */
public class ServerTurnController {

    private ServerKolonistenVanCatan controller;

    /**
     * Controls turns.
     * 
     * @param serverKolonistenVanCatan
     *            Current server.
     */
    public ServerTurnController(ServerKolonistenVanCatan serverKolonistenVanCatan) {
	this.controller = serverKolonistenVanCatan;
    }

    /**
     * Goes to next turn.
     */
    public void nextTurn() {
	if (controller.newTurn() >= controller.getPlayers().size() - 1) {
	    controller.resetTurn();
	    controller.nextRound();
	    if (controller.getState() == GameState.INIT && controller.getRound() == 1) {
		controller.setState(GameState.IN_GAME);
	    }
	}
	fixButtons();
	switch (controller.getState()) {
	case END:
	    endGame();
	    break;
	case INIT:
	    initTurnBuilding();
	    break;
	case IN_GAME:
	    onTurn();
	    break;
	}
	controller.updateTurn();
    }

    /**
     * Checks when player can build building.
     * 
     * @param building
     *            Building which containst street.
     */
    public void initTurnStreet(Building building) {
	try {
	    if (controller.getState() != GameState.INIT) {
		return;
	    }
	    Player pl = controller.getTurn();
	    System.out.printf("Initial turn for %s\n", pl.getUsername());
	    if (controller.getRound() >= 0) {
		for (Tile tile : building.getConnectedTiles()) {
		    if (tile instanceof TileResource) {
			TileResource resource = (TileResource) tile;
			pl.giveResource(resource.getResource());
		    }
		}
	    }
	    Set<Street> availbleStreets = new HashSet<>();
	    for (Tile tile : building.getConnectedTiles()) {
		for (Point point : Point.values()) {
		    if (building.equals(tile.getBuilding(point))) {
			availbleStreets
				.add(tile.getStreet(CollectionUtil.getInRange(Direction.values(), point.ordinal())));
			availbleStreets.add(
				tile.getStreet(CollectionUtil.getInRange(Direction.values(), point.ordinal() + 1)));
		    }
		}
	    }
	    controller.updateState(TurnState.BUILDING_STREET);
	    controller.updateResources();
	    controller.highlightStreets(pl, availbleStreets);
	    pl.addRemainingStreets(1);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Turn of player to place village.
     */
    public void initTurnBuilding() {
	try {
	    if (controller.getState() != GameState.INIT) {
		return;
	    }
	    Player pl = controller.getTurn();
	    pl.getUpdateable(UpdateMap.class).blockActions();
	    controller.highlightBuildings(controller.getTurn(), BuildingType.VILLAGE);
	    pl.addRemainingVillages(1);
	} catch (NullPointerException npe) {
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * When it's turn of a player dices will be rolled and round will be
     * updated.
     */
    public void onTurn() {
	try {
	    if (controller.getState() != GameState.IN_GAME) {
		return;
	    }
	    System.out.println(controller.getTurn().getUsername() + "'s turn is now.");
	    controller.openDicePane();
	    controller.updateRound();
	    controller.updateState(TurnState.THROWING_DICE);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Ends the game.
     */
    public void endGame() {
	System.out.println("La fin.");
    }

    /**
     * Fixes the button.
     */
    public void fixButtons() {
	try {
	    List<Runnable> runs = new ArrayList<>();
	    for (Player player : controller.getPlayersOrded()) {
		if (player.equals(controller.getTurn())) {
		    continue;
		}
		runs.add(() -> {
		    try {
			player.getUpdateable(UpdateMap.class).blockActions();
		    } catch (NullPointerException npe) {
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		});
	    }
	    Scheduler.runAsyncdSync(runs);
	    switch (controller.getState()) {
	    case END:
		break;
	    case INIT:
		break;
	    case IN_GAME:
		controller.getTurn().getUpdateable(UpdateMap.class).unblockActions();
		break;
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
