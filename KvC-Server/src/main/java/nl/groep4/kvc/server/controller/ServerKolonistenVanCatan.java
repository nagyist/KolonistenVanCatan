package nl.groep4.kvc.server.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import nl.groep4.kvc.common.enumeration.BuildingType;
import nl.groep4.kvc.common.enumeration.GameState;
import nl.groep4.kvc.common.enumeration.Resource;
import nl.groep4.kvc.common.enumeration.SelectState;
import nl.groep4.kvc.common.enumeration.TurnState;
import nl.groep4.kvc.common.interfaces.Card;
import nl.groep4.kvc.common.interfaces.KolonistenVanCatan;
import nl.groep4.kvc.common.interfaces.Player;
import nl.groep4.kvc.common.interfaces.Throw;
import nl.groep4.kvc.common.interfaces.Trade;
import nl.groep4.kvc.common.interfaces.UpdateMap;
import nl.groep4.kvc.common.map.Building;
import nl.groep4.kvc.common.map.Coordinate;
import nl.groep4.kvc.common.map.Map;
import nl.groep4.kvc.common.map.Street;
import nl.groep4.kvc.common.util.Scheduler;
import nl.groep4.kvc.server.model.ServerCosts;
import nl.groep4.kvc.server.model.ServerThrow;
import nl.groep4.kvc.server.model.map.ServerMap;
import nl.groep4.kvc.server.util.SaveHelper;

/**
 * Instance of KolonistenVanCatan.
 * 
 * @author Tim
 * @version 1.0
 */
public class ServerKolonistenVanCatan implements KolonistenVanCatan {

    ServerTradeController tradeController;
    ServerTurnController turnController;
    ServerShopController shopController;
    ServerMapController mapController;
    ServerCardController cardController;
    ServerScoreController scoreController;
    ServerSaveController saveController;

    private List<Player> players;
    private List<Trade> trades = new ArrayList<>();
    private Map map = new ServerMap();
    private int endScore = 10;
    private int round = -1;
    private int turn = -1;
    private Throw lastThrow;
    private GameState state;

    /**
     * Gives a list of players.
     * 
     * @param players
     *            List of players on the server.
     */
    public ServerKolonistenVanCatan(List<Player> players) {
	System.out.println("Starting game!");
	this.players = players;
	tradeController = new ServerTradeController(this);
	turnController = new ServerTurnController(this);
	shopController = new ServerShopController(this);
	mapController = new ServerMapController(this);
	cardController = new ServerCardController(this);
	scoreController = new ServerScoreController(getPlayers(), getMap());
	if (players != null) {
	    setPlayers(players);
	    players.sort((pl1, pl2) -> {
		return Integer.compare(pl1.hashCode(), pl2.hashCode());
	    });
	}
	System.out.println("\tRandomized players");
    }

    @Override
    public void start() {
	System.out.println("\tStarted game!");
	if (state == null) {
	    state = GameState.INIT;
	}
	updateCosts();
	nextTurn();
    }

    @Override
    public Map getMap() {
	return map;
    }

    /**
     * Sets the map for the game.
     * 
     * @param map
     *            The map that will be set.
     */
    public void setMap(ServerMap map) {
	this.map = map;
    }

    @Override
    public void createMap() {
	map.createMap();
    }

    @Override
    public GameState getState() {
	return this.state;
    }

    @Override
    public boolean isMovingRover() {
	return mapController.isMovingRover();
    }

    /**
     * Sets the game state.
     * 
     * @param state
     *            State of the game.
     */
    public void setState(GameState state) {
	this.state = state;
    }

    @Override
    public int getRound() {
	return round;
    }

    @Override
    public void nextRound() {
	round++;
    }

    /**
     * Gets new turn.
     * 
     * @return The new turn.
     */
    public int newTurn() {
	return ++turn - 1;
    }

    /**
     * Resets turn.
     */
    public void resetTurn() {
	turn = 0;
    }

    private void updateScores() {
	new ServerScoreController(getPlayers(), getMap()).updateScores();
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		for (Player player : getPlayers()) {
		    try {
			pl.getUpdateable(UpdateMap.class).updateScore(player, player.getPoints());
		    } catch (NullPointerException ex) {
		    } catch (RemoteException ex) {
			ex.printStackTrace();
		    }
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
	for (Player pl : getPlayers()) {
	    try {
		if (pl.getPoints() >= endScore) {
		    runs = new ArrayList<>();
		    setState(GameState.END);
		    runs.add(() -> {
			for (Player player : getPlayers()) {
			    try {
				player.getUpdateable(UpdateMap.class).openEnd(pl);
			    } catch (RemoteException ex) {
				ex.printStackTrace();
			    }
			}
		    });
		    Scheduler.runAsyncdSync(runs);
		}
	    } catch (RemoteException ex) {
		ex.printStackTrace();
	    }
	}
    }

    @Override
    public List<Player> getPlayers() {
	return players;
    }

    /**
     * Sets the players list.
     * 
     * @param playersSave
     *            The list where the players will be set to.
     */
    public void setPlayers(List<Player> playersSave) {
	this.players = playersSave;
    }

    @Override
    public void nextTurn() {
	lastThrow = null;
	updateScores();
	turnController.nextTurn();
    }

    @Override
    public Player getTurn() {
	return getPlayersOrded().get(0);
    }

    @Override
    public List<Player> getPlayersOrded() {
	List<Player> orded = new ArrayList<>();
	for (int i = 0; i < players.size(); i++) {
	    orded.add(players.get((i + turn) % players.size()));
	}
	return orded;
    }

    @Override
    public void placeBuilding(Player newOwner, Coordinate coord, BuildingType type) {
	mapController.placeBuilding(newOwner, coord, type);
    }

    @Override
    public void placeStreet(Player newOwner, Coordinate coord) {
	mapController.placeStreet(newOwner, coord);
    }

    @Override
    public void moveFromRover(Player turn, Coordinate position) throws RemoteException {
	if (getTurn().equals(turn)) {
	    mapController.moveRoverFrom(position);
	}
    }

    @Override
    public void moveToRover(Player turn, Coordinate position) throws RemoteException {
	if (getTurn().equals(turn)) {
	    mapController.moveRoverTo(position);
	}
	updateScores();
	updateResources();
    }

    @Override
    public void throwDices() {
	ServerThrowController diceController = new ServerThrowController(this);
	lastThrow = diceController.getThrow();
	diceController.updateThrow();
    }

    /**
     * Gets last throw.
     */
    public Throw getLastThrow() {
	return this.lastThrow;
    }

    @Override
    public void distribute() {
	mapController.distribute();
    }

    /**
     * Highlights bandit.
     * 
     * @param pl
     *            Turn player to move bandit.
     */
    public void highlightBandit(Player pl) {
	try {
	    pl.setSelectable(SelectState.BANDIT);
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Highlights buildings which are valid to build.
     * 
     * @param pl
     *            Valid places for a player to build.
     * @param type
     *            Type of building to build.
     */
    public void highlightBuildings(Player pl, BuildingType type) {
	highlightBuildings(pl, mapController.getValidBuildingLocations(type), type);
    }

    /**
     * Highlights streets.
     * 
     * @param pl
     *            Valid street location for player to build.
     */
    public void highlightStreet(Player pl) {
	highlightStreets(pl, mapController.getValidStreetLocations());
    }

    /**
     * Highlights type building.
     * 
     * @param pl
     *            Player to build type building.
     * @param buildings
     *            Buildings to highlight.
     * @param type
     *            Type of building to build.
     */
    public void highlightBuildings(Player pl, Collection<Building> buildings, BuildingType type) {
	try {
	    pl.getUpdateable(UpdateMap.class).highlightBuildings(buildings, type);
	    pl.setSelectable(SelectState.BUILDING);
	    updateState(TurnState.BUILDING_BUILDING);
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Highlights streets places.
     * 
     * @param pl
     *            Valid places of player to highlight street locations.
     * @param streets
     *            Street locations to highlight.
     */
    public void highlightStreets(Player pl, Collection<Street> streets) {
	try {
	    pl.getUpdateable(UpdateMap.class).highlightStreets(streets);
	    pl.setSelectable(SelectState.STREET);
	    updateState(TurnState.BUILDING_STREET);
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    @Override
    public void buyStreet() {
	shopController.buyStreet();
    }

    @Override
    public void buyVillage() {
	shopController.buyVillage();
    }

    @Override
    public void buyCity() {
	shopController.buyCity();
    }

    @Override
    public void buyCard() {
	shopController.buyCard();
	updateScores();
    }

    @Override
    public void useCard(Player player, Card card) throws RemoteException {
	if (getTurn().equals(player)) {
	    cardController.useCard(player, card);
	}
	updateScores();
    }

    @Override
    public void trade(UUID tradeKey, Player with) throws RemoteException {
	if (with.equals(getTurn())) {
	    updateState(TurnState.TRADING);
	}
	tradeController.onTrade(tradeController.getTrade(tradeKey), with);
    }

    @Override
    public void addTrade(Player player, java.util.Map<Resource, Integer> request,
	    java.util.Map<Resource, Integer> reward) throws RemoteException {
	if (player.equals(getTurn())) {
	    updateState(TurnState.TRADING);
	}
	tradeController.addTrade(player, request, reward);
	updateTrades();
    }

    @Override
    public void remvoeTrade(UUID key) throws RemoteException {
	tradeController.removeTrade(key);
	updateTrades();
    }

    @Override
    public void reconnect() throws RemoteException {
	updateMap();
	updateResources();
	updateTrades();
	updateRound();
	updateTurn();
	updateTrades();
	updateScores();
	updateCosts();
	updateCards();
    }

    @Override
    public void updateMap() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).setModel(getMap());
		} catch (RemoteException ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
	updateScores();
    }

    /**
     * Updates the resources.
     */
    public void updateResources() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		for (Player player : getPlayers()) {
		    try {
			pl.getUpdateable(UpdateMap.class).updateStock(player, player.getResources());
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates the cards.
     */
    public void updateCards() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		for (Player player : getPlayers()) {
		    try {
			pl.getUpdateable(UpdateMap.class).updateStock(player, player.getCards());
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates the costs of development cards, city, village and street.
     */
    public void updateCosts() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    UpdateMap view = pl.getUpdateable(UpdateMap.class);
		    view.updateCardCosts(ServerCosts.DEVELOPMENT_CARD_COSTS);
		    view.updateCityCosts(ServerCosts.CITY_COSTS);
		    view.updateVillageCosts(ServerCosts.VILLAGE_COSTS);
		    view.updateStreetCosts(ServerCosts.STREET_COSTS);
		} catch (NullPointerException npe) {
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates the trades.
     */
    public void updateTrades() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).updateTrades(getTrades());
		} catch (NullPointerException npe) {
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates the round.
     */
    public void updateRound() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).updateRound(getRound());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates state.
     * 
     * @param state
     *            State of player.
     */
    public void updateState(TurnState state) {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).updateTurn(getTurn(), state);
		} catch (NullPointerException npe) {
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Updates the turn.
     */
    public void updateTurn() {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).updatePlayerOrder(getPlayersOrded());
		} catch (NullPointerException npe) {
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Opens the dice pane.
     */
    public void openDicePane() {
	try {
	    for (Player pl : getPlayersOrded()) {
		if (pl.equals(getTurn())) {
		    continue;
		}
		try {
		    pl.getUpdateable(UpdateMap.class).openDicePane(false);
		} catch (RemoteException ex) {
		    ex.printStackTrace();
		}
	    }
	    getTurn().getUpdateable(UpdateMap.class).openDicePane(true);
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    @Override
    public void openPausePane(Player requester) {
	try {
	    if (!requester.equals(getTurn())) {
		requester.getUpdateable(UpdateMap.class).closeOverlay();
		requester.getUpdateable().popup("noturn");
		return;
	    }
	    for (Player pl : getPlayers()) {
		try {
		    pl.getUpdateable(UpdateMap.class).openPausePane(false);
		} catch (RemoteException ex) {
		    ex.printStackTrace();
		}
	    }
	    requester.getUpdateable(UpdateMap.class).openPausePane(true);
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    @Override
    public void closePausePane() throws RemoteException {
	List<Runnable> runs = new ArrayList<>();
	for (Player pl : getPlayers()) {
	    runs.add(() -> {
		try {
		    pl.getUpdateable(UpdateMap.class).closeOverlay();
		} catch (RemoteException ex) {
		    ex.printStackTrace();
		}
	    });
	}
	Scheduler.runAsyncdSync(runs);
    }

    /**
     * Moves the bandit.
     */
    public void moveBanditModus() {
	mapController.moveBanditModus();
    }

    /**
     * Gets into street modus.
     * 
     * @param streetsToBuild
     *            Places where streets can be build.
     */
    public void buildStreetModus(int streetsToBuild) {
	try {
	    Player who = getTurn();
	    who.addRemainingStreets(streetsToBuild);
	    UpdateMap view = who.getUpdateable(UpdateMap.class);
	    view.closeOverlay();
	    view.setSelectable(SelectState.STREET);
	    view.blockActions();
	    highlightStreet(who);
	    updateResources();
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Gets into village build modus.
     * 
     * @param villageToBuild
     *            Places where villages can be build.
     */
    public void buildVillageModus(int villageToBuild) {
	try {
	    Player who = getTurn();
	    who.addRemainingVillages(villageToBuild);
	    UpdateMap view = who.getUpdateable(UpdateMap.class);
	    view.closeOverlay();
	    highlightBuildings(who, BuildingType.VILLAGE);
	    view.blockActions();
	    updateResources();
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * Gets into city build modus.
     * 
     * @param cityToBuild
     *            Places where city's can be build.
     */
    public void buildCityModus(int cityToBuild) {
	try {
	    Player who = getTurn();
	    who.addRemainingCitys(cityToBuild);
	    UpdateMap view = who.getUpdateable(UpdateMap.class);
	    view.closeOverlay();
	    highlightBuildings(who, BuildingType.CITY);
	    view.blockActions();
	    updateResources();
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    @Override
    public void targetInvention(Player who, Resource resource) throws RemoteException {
	cardController.useInvention(who, resource);
    }

    @Override
    public void targetMonopoly(Player who, Resource resource) throws RemoteException {
	cardController.targetMonopoly(who, resource);
    }

    /**
     * Gets the score when the game should end.
     * 
     * @return The score when it ends.
     */
    public int getEndscore() {
	return endScore;
    }

    /**
     * Sets the score when the game will end.
     * 
     * @param score
     *            The score when the game wil end.
     */
    public void setEndScore(int score) {
	this.endScore = score;
    }

    /**
     * Gets the current turn.
     * 
     * @return The turn the game is in.
     */
    public int getTurnNumber() {
	return turn;
    }

    /**
     * Sets the round where the game is in.
     * 
     * @param round
     *            The round where the game is in.
     */
    public void setRound(int round) {
	this.round = round;
    }

    /**
     * Sets the turn, the number of the player in orderd.
     * 
     * @param turn
     *            The number for the player who is.
     */
    public void setTurn(int turn) {
	this.turn = turn;
    }

    /**
     * Sets the last dice throw for the game.<br>
     * NOTE: Will not trigger the desribute.
     * 
     * @param lastThrow
     *            The last throw.
     */
    public void setThrow(ServerThrow lastThrow) {
	this.lastThrow = lastThrow;
    }

    /**
     * Gets list of trades.
     * 
     * @return The list of trades.
     */
    public List<Trade> getTrades() {
	return trades;
    }

    /**
     * Sets the trades
     * 
     * @param trades
     *            all the trades that there are.
     */
    public void setTrades(List<Trade> trades) {
	this.trades = trades;
    }

    @Override
    public String getSave() {
	return SaveHelper.toSaveFile(this);
    }
}
