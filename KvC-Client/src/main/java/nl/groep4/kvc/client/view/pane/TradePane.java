package nl.groep4.kvc.client.view.pane;

import java.rmi.RemoteException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.SceneUtil;
import nl.groep4.kvc.client.util.TranslationManager;
import nl.groep4.kvc.client.view.ViewMaster;
import nl.groep4.kvc.client.view.elements.KvCText;
import nl.groep4.kvc.client.view.elements.MenuButton;
import nl.groep4.kvc.client.view.elements.TradeEntry;
import nl.groep4.kvc.client.view.scene.SceneMap;
import nl.groep4.kvc.common.interfaces.Trade;
import nl.groep4.kvc.common.interfaces.UpdateTrade;

/**
 * Generates trade pane.
 * 
 * @author Matthijs
 * @version 1.2
 */
public class TradePane implements PaneHolder, UpdateTrade {

    private SceneMap sceneMap;
    private MenuButton plaats = new MenuButton(425, 500, TranslationManager.translate("trade.button.place"));
    private MenuButton terug = new MenuButton(425, 500, TranslationManager.translate("trade.button.back"));

    private Text wheat = new KvCText(TranslationManager.translate("trade.text.wheat")).addShadow();
    private Text wood = new KvCText(TranslationManager.translate("trade.text.wood")).addShadow();
    private Text wool = new KvCText(TranslationManager.translate("trade.text.wool")).addShadow();
    private Text stone = new KvCText(TranslationManager.translate("trade.text.brick")).addShadow();
    private Text ore = new KvCText(TranslationManager.translate("trade.text.ore")).addShadow();

    private VBox entries = new VBox(10);

    /**
     * Sets up the TradePane.
     * 
     * @param sceneMap
     *            The view of the map.
     */
    public TradePane(SceneMap sceneMap) {
	this.sceneMap = sceneMap;
    }

    @Override
    public Pane getPane() {
	HBox hboxScrollpane = new HBox();
	VBox vboxButtons = new VBox();
	VBox vbox = new VBox();
	HBox buttons = new HBox();
	HBox tb = new HBox();
	HBox testbox = new HBox();
	StackPane stackpane = new StackPane();
	ScrollPane scrollpane = new ScrollPane();

	scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
	scrollpane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	scrollpane.setMinWidth(500);
	scrollpane.setMaxWidth(500);
	scrollpane.setMinHeight(300);
	scrollpane.setMaxHeight(300);
	scrollpane.getStylesheets().add("/assets/stylesheet.css");
	scrollpane.setContent(entries);
	hboxScrollpane.setAlignment(Pos.CENTER);
	tb.setAlignment(Pos.BOTTOM_CENTER);

	VBox vboxWheat = new VBox();
	VBox vboxWood = new VBox();
	VBox vboxWool = new VBox();
	VBox vboxStone = new VBox();
	VBox vboxOre = new VBox();

	HBox hboxResources = new HBox();

	vboxWheat.getChildren().addAll(wheat);
	vboxWood.getChildren().addAll(wood);
	vboxWool.getChildren().addAll(wool);
	vboxStone.getChildren().addAll(stone);
	vboxOre.getChildren().addAll(ore);

	hboxResources.setAlignment(Pos.CENTER);
	hboxResources.setPadding(new Insets(200, 0, 0, 0));
	hboxResources.setSpacing(20);

	plaats.setFont(ViewMaster.FONT);
	terug.setFont(ViewMaster.FONT);

	stackpane.setAlignment(Pos.CENTER);

	buttons.getChildren().addAll(terug, plaats);
	vboxButtons.getChildren().addAll(buttons);
	tb.getChildren().addAll(buttons);

	hboxScrollpane.getChildren().addAll(scrollpane);
	hboxResources.getChildren().addAll(vboxWood, vboxStone, vboxWool, vboxWheat, vboxOre);

	vboxButtons.getChildren().addAll(tb);

	vbox.getChildren().addAll(hboxResources, hboxScrollpane, vboxButtons);
	stackpane.getChildren().addAll(SceneUtil.getGamePane(), vbox, testbox);

	terug.setOnMouseClicked(klick -> onBackClick());
	plaats.setOnMouseClicked(klick -> onPlaceClick());

	return stackpane;
    }

    /**
     * When there is a click on the button, the tradepane will be closed.
     */
    private void onBackClick() {
	sceneMap.closeOverlay();
    }

    /**
     * When there is a click on the button, the PlaceTradePane will be opened.
     */
    private void onPlaceClick() {
	sceneMap.openPlaceTradePane();
    }

    @Override
    public void updateConfig() {
	terug.updateText(TranslationManager.translate("trade.button.back"));
	plaats.updateText(TranslationManager.translate("trade.button.place"));
	wheat.setText(TranslationManager.translate("trade.text.wheat"));
	wood.setText(TranslationManager.translate("trade.text.wood"));
	wool.setText(TranslationManager.translate("trade.text.wool"));
	stone.setText(TranslationManager.translate("trade.text.brick"));
	ore.setText(TranslationManager.translate("trade.text.ore"));
    }

    @Override
    public void updateTrades(List<Trade> allTrades) throws RemoteException {
	entries.getChildren().clear();
	for (Trade trade : allTrades) {
	    entries.getChildren().add(new TradeEntry(trade, sceneMap).getPane());
	}
    }
}