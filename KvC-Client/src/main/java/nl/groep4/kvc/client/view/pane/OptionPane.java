package nl.groep4.kvc.client.view.pane;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.SceneUtil;
import nl.groep4.kvc.client.util.TranslationManager;
import nl.groep4.kvc.client.view.ViewMaster;
import nl.groep4.kvc.client.view.elements.KvCText;
import nl.groep4.kvc.client.view.elements.MenuButton;
import nl.groep4.kvc.client.view.elements.SettingsButton;
import nl.groep4.kvc.client.view.scene.SceneMap;

public class OptionPane implements PaneHolder {

    private SceneMap map;

    private Text title;
    private MenuButton save;
    private MenuButton pause;
    private MenuButton rules;
    private MenuButton credits;
    private MenuButton exit;
    private MenuButton back;

    public OptionPane(SceneMap map) {
	this.map = map;
    }

    @Override
    public Pane getPane() {
	StackPane layers = new StackPane();
	VBox buttons = new VBox(8);
	buttons.setAlignment(Pos.CENTER);
	title = new KvCText(TranslationManager.translate("game.menu.title"));
	save = new MenuButton(TranslationManager.translate("game.menu.save"));
	pause = new MenuButton(TranslationManager.translate("game.menu.pause"));
	rules = new MenuButton(TranslationManager.translate("game.menu.rules"));
	credits = new MenuButton(TranslationManager.translate("game.menu.credits"));
	exit = new MenuButton(TranslationManager.translate("game.menu.exit"));
	back = new MenuButton(TranslationManager.translate("game.menu.back"));
	title.setFont(ViewMaster.TITLE_FONT);
	save.setFont(ViewMaster.FONT);
	pause.setFont(ViewMaster.FONT);
	rules.setFont(ViewMaster.FONT);
	credits.setFont(ViewMaster.FONT);
	exit.setFont(ViewMaster.FONT);
	back.setFont(ViewMaster.FONT);

	save.setOnMouseClicked(klick -> onSaveClick());
	pause.setOnMouseClicked(klick -> onPauseClick());
	rules.setOnMouseClicked(klick -> onRulesClick());
	credits.setOnMouseClicked(klick -> onCreditsClick());
	exit.setOnMouseClicked(klick -> onExitClick());
	back.setOnMouseClicked(klick -> onBackClick());

	buttons.getChildren().addAll(title, SettingsButton.getButton(null, 0, 0), save, pause, rules, credits, exit,
		back);
	layers.getChildren().addAll(SceneUtil.getSettingsPane(), buttons);
	return layers;
    }

    private void onExitClick() {
	System.exit(0);
    }

    private void onCreditsClick() {
	map.openCreditsPane();
    }

    private void onBackClick() {
	map.closeOverlay();
    }

    private void onRulesClick() {
	map.openRulesPane();
    }

    private void onPauseClick() {
	// FIXME: pause moet zijn controller#pausegame
	map.openPausePane(true);
    }

    private void onSaveClick() {
	map.openSavePane();
    }

    @Override
    public void updateTranslation() {
	title.setText(TranslationManager.translate("game.menu.title"));
	save.updateText(TranslationManager.translate("game.menu.save"));
	pause.updateText(TranslationManager.translate("game.menu.pause"));
	rules.updateText(TranslationManager.translate("game.menu.rules"));
	credits.updateText(TranslationManager.translate("game.menu.credits"));
	exit.updateText(TranslationManager.translate("game.menu.exit"));
	back.updateText(TranslationManager.translate("game.menu.back"));
    }

}
