package nl.groep4.kvc.client.view.elements;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import nl.groep4.kvc.client.util.SoundUtil;

public class LobbyButton extends TexturedButton {

	private static final Image BUTTON_IMAGE = new Image("img/etc/button.png");
	private static final Image BUTTON_HOVER_IMAGE = new Image("img/etc/button_hover.png");
	private static final Image BUTTON_PRESSED_IMAGE = new Image("img/etc/button_pressed.png");

	public LobbyButton() {
		super();
	}

	public LobbyButton(String text) {
		super(text);
	}

	@Override
	public Image getTexture() {
		return BUTTON_IMAGE;
	}

	@Override
	public Image getHoverTexture() {
		return BUTTON_HOVER_IMAGE;
	}

	@Override
	public Image getClickTexture() {
		return BUTTON_PRESSED_IMAGE;
	}

	@Override
	public void onClick(MouseEvent mce) {
		SoundUtil.playSound("sound/clicksound.wav");
	}

}
