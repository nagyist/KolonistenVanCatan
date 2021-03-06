package nl.groep4.kvc.client.view.elements;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nl.groep4.kvc.client.util.ConfirmDialog;

/**
 * Button class what makes a combination of images looks like a real button.
 * 
 * @author Tim
 * @version 1.1
 * 
 **/
public abstract class TexturedButton extends StackPane {

    protected Text label;
    protected List<Runnable> clickHandlers = new ArrayList<>();

    /**
     * Generates button design.
     * 
     */
    public TexturedButton() {
	ImageView background = new ImageView(getTexture());
	label = new KvCText().addShadow();
	setOnMouseEntered(mouseEnter -> background.setImage(getHoverTexture()));
	setOnMouseExited(mouseLeave -> background.setImage(getTexture()));
	setOnMousePressed(mousePressed -> {
	    background.setImage(getClickTexture());
	    if (ConfirmDialog.confirm("button")) {
		for (Runnable click : clickHandlers) {
		    click.run();
		}
	    }
	});
	setOnMouseReleased(mouseRelease -> background.setImage(getHoverTexture()));
	getChildren().addAll(background, label);
	setHeight(background.getFitHeight());
	setWidth(background.getFitWidth());
    }

    /**
     * Updates the text of the button.
     * 
     * @param text
     *            Text of the button.
     */
    public TexturedButton(String text) {
	this();
	updateText(text);
    }

    /**
     * Sets the font of the label.
     * 
     * 
     * @param font
     *            Font of the label.
     */
    public void setFont(Font font) {
	label.setFont(font);
    }

    /**
     * Updates the text of the label.
     * 
     * @param text
     *            Text of the label.
     */
    public void updateText(String text) {
	this.label.setText(text);
    }

    /**
     * Gets the text of the label.
     * 
     * @return Text of the label.
     */
    public String getText() {
	return this.label.getText();
    }

    /**
     * Generates a shadow effect with color black.
     * 
     * @return The shadow effect.
     */
    public static DropShadow getShadowEffect() {
	DropShadow shadow = new DropShadow();
	shadow.setOffsetX(3);
	shadow.setOffsetY(3);
	shadow.setColor(Color.BLACK);
	return shadow;
    }

    /**
     * Registers the clicks in a List.
     * 
     * @param click
     *            Click that gets registers.
     */
    public void registerClick(Runnable click) {
	clickHandlers.add(click);
    }

    /**
     * Sets the button on disabled. You can't click the button anymore.
     */
    public void setDisabled() {
	setMouseTransparent(true);
    }

    /**
     * Sets the button on enabled. You can click the button.
     */
    public void setEnabled() {
	setMouseTransparent(false);
    }

    /**
     * Gets the image of the button.
     * 
     * @return The texture image.
     */
    public abstract Image getTexture();

    /**
     * Gets the image of the button when hovered over.
     * 
     * @return The image of the button when hovered over.
     */
    public abstract Image getHoverTexture();

    /**
     * Gets the image of the button when clicked on.
     * 
     * @return The image of the button when clicked on.
     */
    public abstract Image getClickTexture();
}
