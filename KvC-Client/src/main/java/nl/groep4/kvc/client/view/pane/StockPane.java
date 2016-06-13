package nl.groep4.kvc.client.view.pane;

import java.util.EnumMap;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.SceneUtil;
import nl.groep4.kvc.client.view.ViewMaster;
import nl.groep4.kvc.client.view.elements.MenuButton;
import nl.groep4.kvc.common.enumeration.Resource;
import nl.groep4.kvc.common.interfaces.Card;
import nl.groep4.kvc.common.interfaces.UpdateStock;

public class StockPane implements PaneHolder, UpdateStock {
    // TODO StockPaneAfmaken
    private ImageView oreCard;
    private ImageView stoneCard;
    private ImageView wheatCard;
    private ImageView woodCard;
    private ImageView woolCard;
    private ImageView cardPlank;

    StackPane cardPane;
    Pane allThings;
    HBox resCards;
    HBox devCards;
    Text amntWood;
    Text amntOre;
    Text amntStone;
    Text amntWheat;
    Text amntWool;
    MenuButton showCards;

    @Override
    public Pane getPane() {
	showCards = new MenuButton(200, 230, "Hide");
	showCards.setFont(ViewMaster.FONT);
	showCards.setAlignment(Pos.BOTTOM_CENTER);

	resCards = new HBox();
	devCards = new HBox();
	cardPane = new StackPane();
	allThings = new Pane();

	resCards.getChildren().addAll(getOreCard(), getWoodCard(), getWoolCard(), getStoneCard(), getWheatCard());
	resCards.setAlignment(Pos.CENTER);

	cardPane.getChildren().addAll(getCardPlank(), resCards, devCards, showCards);
	showCards.registerClick(() -> SceneUtil.fadeOut(cardPane));
	return cardPane;
    }

    public Node getCardPlank() {
	if (cardPlank == null) {
	    cardPlank = new ImageView("img/game/kaart_plank.png");
	}
	return cardPlank;
    }

    /**
     * Gets the resource for the wood card
     * 
     * @return image of the wood card
     */
    public Node getWoodCard() {
	woodCard = new ImageView("img/cards/kaart_hout.png");
	woodCard.setFitHeight(170);
	woodCard.setFitWidth(113.5);
	return woodCard;
    }

    /**
     * Gets the resource for the stone card
     * 
     * @return image of the stone card
     */
    public Node getStoneCard() {
	stoneCard = new ImageView("img/cards/kaart_steen.png");
	stoneCard.setFitHeight(170);
	stoneCard.setFitWidth(113.5);
	return stoneCard;
    }

    /**
     * Gets the resource for the wool card
     * 
     * @return image of the wool card
     */
    public Node getWoolCard() {
	woolCard = new ImageView("img/cards/kaart_schaap.png");
	woolCard.setFitHeight(170);
	woolCard.setFitWidth(113.5);
	return woolCard;
    }

    /**
     * Gets the resource for the wheat card
     * 
     * @return image of the wheat card
     */
    public Node getWheatCard() {
	wheatCard = new ImageView("img/cards/kaart_graan.png");
	wheatCard.setFitHeight(170);
	wheatCard.setFitWidth(113.5);
	return wheatCard;
    }

    /**
     * Gets the resource for the ore card
     * 
     * @return image of the ore card
     */
    public Node getOreCard() {
	oreCard = new ImageView("img/cards/kaart_erts.png");
	oreCard.setFitHeight(170);
	oreCard.setFitWidth(113.5);
	return oreCard;
    }

    @Override
    public void updateTranslation() {
	// TODO Auto-generated method stub

    }

    @Override
    public void updateStock(EnumMap<Resource, Integer> resources) {
	// TODO Auto-generated method stub

    }

    @Override
    public void updateStock(List<Card> cards) {
	// TODO Auto-generated method stub

    }

}
