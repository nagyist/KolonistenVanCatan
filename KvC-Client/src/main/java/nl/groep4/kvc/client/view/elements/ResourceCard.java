package nl.groep4.kvc.client.view.elements;

import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.TranslationManager;
import nl.groep4.kvc.client.view.ViewMaster;
import nl.groep4.kvc.common.enumeration.Resource;

/**
 * Sets images and names for resources in stock. Also update stock.
 * 
 * @author Luc
 * @version 1.1
 */
public class ResourceCard {

    private ImageView cardPlank;
    private ImageView oreCard;
    private ImageView stoneCard;
    private ImageView wheatCard;
    private ImageView woodCard;
    private ImageView woolCard;

    private Text amntWood;
    private Text amntOre;
    private Text amntWool;
    private Text amntWheat;
    private Text amntStone;
    private Text nameWood;
    private Text nameOre;
    private Text nameWool;
    private Text nameWheat;
    private Text nameStone;

    /**
     * Gets wood name.
     * 
     * @return Name of wood.
     */
    public Text getWoodName() {
	nameWood = new KvCText(TranslationManager.translate("game.resourcename.wood"));
	return nameWood;
    };

    /**
     * Gets stone name.
     * 
     * @return Name of stone.
     */
    public Text getStoneName() {
	nameStone = new KvCText(TranslationManager.translate("game.resourcename.stone"));
	return nameStone;
    };

    /**
     * Gets ore name.
     * 
     * @return Name of ore.
     */
    public Text getOreName() {
	nameOre = new KvCText(TranslationManager.translate("game.resourcename.ore"));
	return nameOre;
    };

    /**
     * Gets wool name.
     * 
     * @return Name of wool.
     */
    public Text getWoolName() {
	nameWool = new KvCText(TranslationManager.translate("game.resourcename.wool"));
	return nameWool;
    };

    /**
     * Gets wheat name.
     * 
     * @return Name of wheat.
     */
    public Text getWheatName() {
	nameWheat = new KvCText(TranslationManager.translate("game.resourcename.wheat"));
	return nameWheat;
    };

    /**
     * Gets wood text.
     * 
     * @return Text of wood.
     */
    public Text getWoodText() {
	amntWood = new KvCText("0");
	amntWood.setFont(ViewMaster.TITLE_FONT);
	return amntWood;
    }

    /**
     * Gets ore text.
     * 
     * @return Text of ore.
     */
    public Text getOreText() {
	amntOre = new KvCText("0");
	amntOre.setFont(ViewMaster.TITLE_FONT);
	return amntOre;
    }

    /**
     * Gets wool text.
     * 
     * @return Gext of wool.
     */
    public Text getWoolText() {
	amntWool = new KvCText("0");
	amntWool.setFont(ViewMaster.TITLE_FONT);
	return amntWool;
    }

    /**
     * Gets wheat text.
     * 
     * @return Text of wheat.
     */
    public Text getWheatText() {
	amntWheat = new KvCText("0");
	amntWheat.setFont(ViewMaster.TITLE_FONT);
	return amntWheat;
    }

    /**
     * Gets stone text.
     * 
     * @return Text of stone.
     */
    public Text getStoneText() {
	amntStone = new KvCText("0");
	amntStone.setFont(ViewMaster.TITLE_FONT);
	return amntStone;
    }

    /**
     * Gets image of cardPlank.
     * 
     * @return The image of cardPlank.
     */
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
     * Gets the resource for the stone card.
     * 
     * @return Image of the stone card.
     */
    public Node getStoneCard() {
	stoneCard = new ImageView("img/cards/kaart_steen.png");
	stoneCard.setFitHeight(170);
	stoneCard.setFitWidth(113.5);
	return stoneCard;
    }

    /**
     * Gets the resource for the wool card.
     * 
     * @return Image of the wool card.
     */
    public Node getWoolCard() {
	woolCard = new ImageView("img/cards/kaart_schaap.png");
	woolCard.setFitHeight(170);
	woolCard.setFitWidth(113.5);
	return woolCard;
    }

    /**
     * Gets the resource for the wheat card.
     * 
     * @return Image of the wheat card.
     */
    public Node getWheatCard() {
	wheatCard = new ImageView("img/cards/kaart_graan.png");
	wheatCard.setFitHeight(170);
	wheatCard.setFitWidth(113.5);
	return wheatCard;
    }

    /**
     * Gets the resource for the ore card.
     * 
     * @return Image of the ore card.
     */
    public Node getOreCard() {
	oreCard = new ImageView("img/cards/kaart_erts.png");
	oreCard.setFitHeight(170);
	oreCard.setFitWidth(113.5);
	return oreCard;
    }

    /**
     * Updates translation of resources.
     * 
     */
    public void updateTranslation() {
	nameOre.setText(TranslationManager.translate("game.resourcename.ore"));
	nameWood.setText(TranslationManager.translate("game.resourcename.wood"));
	nameStone.setText(TranslationManager.translate("game.resourcename.stone"));
	nameWheat.setText(TranslationManager.translate("game.resourcename.wheat"));
	nameWool.setText(TranslationManager.translate("game.resourcename.wool"));

    }

    /**
     * Updates stock with all resources.
     * 
     * @param resources
     *            Resources that will be updated.
     */
    public void updateStock(Map<Resource, Integer> resources) {
	for (Entry<Resource, Integer> resource : resources.entrySet()) {
	    Resource res = resource.getKey();
	    String amnt = resource.getValue().toString();
	    switch (res) {
	    case BRICK:
		amntStone.setText(amnt);
		break;
	    case ORE:
		amntOre.setText(amnt);
		break;
	    case WHEAT:
		amntWheat.setText(amnt);
		break;
	    case WOOD:
		amntWood.setText(amnt);
		break;
	    case WOOL:
		amntWool.setText(amnt);
		break;
	    }
	}
    }

}
