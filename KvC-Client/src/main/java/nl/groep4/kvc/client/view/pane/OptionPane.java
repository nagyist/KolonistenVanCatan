package nl.groep4.kvc.client.view.pane;

import java.rmi.RemoteException;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.SceneUtil;
import nl.groep4.kvc.client.util.TranslationManager;
import nl.groep4.kvc.client.view.ViewMaster;
import nl.groep4.kvc.client.view.elements.MenuButton;
import nl.groep4.kvc.client.view.elements.SettingsButton;
import nl.groep4.kvc.client.view.scene.SceneMap;

public class OptionPane implements PaneHolder {

    // TODO: OptionPane needs work
    private SceneMap map;

    private Text tilte;
    private MenuButton save;
    private MenuButton pause;
    private MenuButton rules;
    private MenuButton exit;
    private MenuButton back;

    public OptionPane(SceneMap map) {
	this.map = map;
    }

    @Override
    public Pane getPane() {
	StackPane layers = new StackPane();
	VBox buttons = new VBox(8);
	tilte = new Text(TranslationManager.translate("game.menu.title"));
	save = new MenuButton(TranslationManager.translate("game.menu.save"));
	pause = new MenuButton(TranslationManager.translate("game.menu.pause"));
	rules = new MenuButton(TranslationManager.translate("game.menu.rules"));
	exit = new MenuButton(TranslationManager.translate("game.menu.exit"));
	back = new MenuButton(TranslationManager.translate("game.menu.back"));
	save.setFont(ViewMaster.FONT);
	pause.setFont(ViewMaster.FONT);
	rules.setFont(ViewMaster.FONT);
	exit.setFont(ViewMaster.FONT);
	back.setFont(ViewMaster.FONT);

	save.setOnMouseClicked(klick -> onSaveClick());
	pause.setOnMouseClicked(klick -> onPauseClick());
	rules.setOnMouseClicked(klick -> onRulesClick());
	exit.setOnMouseClicked(klick -> onExitClick());
	back.setOnMouseClicked(klick -> onBackClick());

	buttons.getChildren().addAll(SettingsButton.getButton(null, 0, 0), save, pause, rules, exit, back);
	layers.getChildren().addAll(SceneUtil.getSettingsPane(), buttons);
	return layers;
    }

    private void onExitClick() {
    }

    private void onBackClick() {
	try {
	    map.closeOverlay();
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    private void onRulesClick() {
	try {
	    map.openRulesPane();
	} catch (RemoteException ex) {
	    ex.printStackTrace();
	}
    }

    private void onPauseClick() {
    }

    private void onSaveClick() {
    }

    @Override
    public void updateTranslation() {
	tilte.setText(TranslationManager.translate("game.menu.title"));
	save.updateText(TranslationManager.translate("game.menu.save"));
	pause.updateText(TranslationManager.translate("game.menu.pause"));
	rules.updateText(TranslationManager.translate("game.menu.rules"));
	exit.updateText(TranslationManager.translate("game.menu.exit"));
	back.updateText(TranslationManager.translate("game.menu.back"));
    }

}
