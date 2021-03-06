package nl.groep4.kvc.client.view.pane;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nl.groep4.kvc.client.view.elements.PlayerScore;
import nl.groep4.kvc.client.view.scene.SceneMap;
import nl.groep4.kvc.common.enumeration.Resource;
import nl.groep4.kvc.common.interfaces.Card;
import nl.groep4.kvc.common.interfaces.Player;
import nl.groep4.kvc.common.interfaces.UpdatePlayerOrder;
import nl.groep4.kvc.common.interfaces.UpdateScore;
import nl.groep4.kvc.common.interfaces.UpdateStock;

/**
 * Scorepane that gets updates.
 * 
 * @author Tim, Luc
 * @version 1.1
 */
public class ScorePane implements PaneHolder, UpdateStock, UpdatePlayerOrder, UpdateScore {

    private StackPane scorePane;
    private VBox content;
    private Node first;
    private ImageView banner;
    private ImageView closedBanner;
    private SceneMap view;

    private ArrayList<PlayerScore> scores;

    public ScorePane(SceneMap mapView) {
	this.view = mapView;
	first = new PlayerScore(view, null).getPane();
    }

    public SceneMap getParent() {
	return view;
    }

    @Override
    public Pane getPane() {
	scores = new ArrayList<>();
	scorePane = new StackPane();
	content = new VBox();
	scorePane.getChildren().addAll(getClosedBanner());
	content.setAlignment(Pos.TOP_CENTER);

	scorePane.setOnMouseEntered(e -> hoverIn());
	scorePane.setOnMouseExited(e -> hoverOut());
	scorePane.setAlignment(Pos.TOP_CENTER);
	hoverOut();
	return scorePane;
    }

    /**
     * Opens the banner with player resources by adding children to the Pane.
     * 
     */
    public void hoverIn() {
	scorePane.getChildren().clear();
	VBox stack = new VBox(first, content);
	stack.setAlignment(Pos.TOP_CENTER);
	scorePane.getChildren().addAll(getBanner(), stack);
    }

    /**
     * Closes the banner with player resources by removing children from the
     * Pane.
     * 
     */
    public void hoverOut() {
	scorePane.getChildren().clear();
	VBox stack = new VBox(first);
	stack.setAlignment(Pos.TOP_CENTER);
	scorePane.getChildren().addAll(getClosedBanner(), stack);
	view.getGamepane().highlightPlayer(null);
    }

    /**
     * Gets the banner texture.
     * 
     * @return Image of the banner in the game.
     */
    public Node getBanner() {
	banner = new ImageView("img/game/banner.png");
	banner.setFitHeight(520);
	banner.setFitWidth(220);
	return banner;
    }

    /**
     * Gets the closed banner texture.
     * 
     * @return Image of the closed banner in the game.
     */
    public Node getClosedBanner() {
	closedBanner = new ImageView("img/game/banner_closed.png");
	closedBanner.setFitHeight(120);
	closedBanner.setFitWidth(220);
	return closedBanner;
    }

    @Override
    public void updateConfig() {
    }

    @Override
    public void updateStock(Player pl, Map<Resource, Integer> resources) {
	for (PlayerScore playerScore : scores) {
	    if (pl == null || playerScore.getPlayer().equals(pl)) {
		playerScore.updateResources(resources);
	    }
	}
    }

    @Override
    public void updateStock(Player pl, List<Card> cards) {
	for (PlayerScore playerScore : scores) {
	    if (pl == null || playerScore.getPlayer().equals(pl)) {
		playerScore.updateStock(cards);
	    }
	}
    }

    @Override
    public void updateScore(Player pl, int score) throws RemoteException {
	for (PlayerScore playerScore : scores) {
	    if (pl == null || playerScore.getPlayer().equals(pl)) {
		playerScore.updateScore(score);
	    }
	}
    }

    @Override
    public void updatePlayerOrder(List<Player> order) {
	Platform.runLater(() -> {
	    content.getChildren().clear();
	    if (!order.isEmpty()) {
		PlayerScore ps = new PlayerScore(view, order.get(0));
		scores.add(ps);
		Pane parent = (Pane) first.getParent();
		parent.getChildren().remove(first);
		first = ps.getPane();
		parent.getChildren().add(0, first);
	    }
	    for (int i = 1; i < order.size(); i++) {
		PlayerScore ps = new PlayerScore(view, order.get(i));
		scores.add(ps);
		content.getChildren().add(ps.getPane());
	    }
	});
    }
}
