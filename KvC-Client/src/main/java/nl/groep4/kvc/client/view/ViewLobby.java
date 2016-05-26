package nl.groep4.kvc.client.view;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.groep4.kvc.client.view.elements.Button;

public class ViewLobby extends Application {

	public static final Font FONT = new Font("Impact", 22);

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Starting lobby");

		// Build multiple layers for the design
		Pane layers = new StackPane();
		Pane theGrid = new Pane();

		// Add all user interactables
		Button join = new Button("Join");
		join.setLayoutX(425);
		join.setLayoutY(500);

		theGrid.getChildren().add(join);

		// Build the lobby
		layers.getChildren().addAll(getBackground(), getForeground(), getBrazier(), theGrid);
		Scene scene = new Scene(layers);
		scene.setCursor(new ImageCursor(new Image("img/etc/cursor.png")));
		primaryStage.setScene(scene);
		primaryStage.setTitle("Kolonisten van Catan: Online");
		primaryStage.setResizable(false);
		primaryStage.show();
		System.out.println("Showing lobby");
	}

	private Node getBackground() {
		return new ImageView("img/lobby/menu_background.png");
	}

	private Node getForeground() {
		return new ImageView("img/lobby/menu_foreground.png");
	}

	private Node getBrazier() {
		return new ImageView("img/lobby/brazier.gif");
	}

}
