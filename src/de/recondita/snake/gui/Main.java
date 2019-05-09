package de.recondita.snake.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private int x = 49;
	private int y = 27;
	private Pane background = new Pane();
	private Schlange schlange;
	public final static int GESCHWINDIGKEIT = 150;
	private Timeline timer;
	private List<Apfel> apfel = Collections.synchronizedList(new ArrayList<Apfel>());
	private int nextRotation = 0;
	private boolean betrunken;
	private BorderPane menu = new BorderPane();
	private Button restart = new Button("Restart");
	private Label pause = new Label();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(background, 1320, 700);

		stage.setScene(scene);
		stage.setTitle("Super Snake2");
		background.setBackground(new Background(
				new BackgroundImage(new Image("de/recondita/snake/gui/boden.jpg"), BackgroundRepeat.REPEAT,
						BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		schlange = new Schlange(background, this);

		timer = new Timeline(new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				schlange.setRotation(nextRotation);
				schlange.schritt();
				if ((int) (Math.random() * 200) < 1) {
					generiereTempApfel();
				}
			}
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();

		generiereApfel();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					nextRotation = 0;
					if (betrunken) {
						nextRotation = 180;
					}
					break;
				case DOWN:
					nextRotation = 180;
					if (betrunken) {
						nextRotation = 0;
					}
					break;
				case LEFT:
					nextRotation = -90;
					if (betrunken) {
						nextRotation = 90;
					}
					break;
				case RIGHT:
					nextRotation = 90;
					if (betrunken) {
						nextRotation = -90;
					}
					break;
				case R:
					resetItems();
					break;
				case P:
					if (!timer.getStatus().equals(Status.PAUSED)) {
						timer.pause();
						background.getChildren().add(menu);
						pause.setText("Spiel pausiert, \n LÃ¤nge: " + schlange.getLaenge());
					} else {
						background.getChildren().remove(menu);
						timer.play();
					}
					break;
				default:
					break;
				}
			}
		});
		stage.show();
		menu.setCenter(pause);
		menu.prefWidthProperty().bind(background.widthProperty());
		menu.prefHeightProperty().bind(background.heightProperty());
		// menu.setStyle("-fx-background-color: green");
		menu.setBackground(new Background(
				new BackgroundImage(new Image("de/recondita/snake/gui/schlange.png"), BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		pause.setStyle("-fx-text-fill: red; -fx-text-size: 36;");
		pause.setFont(Font.font("Cambria", 32));
		restart.setStyle(
				"-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%), linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));  -fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;  -fx-text-fill: white;");
		restart.setLayoutX(((background.getWidth() - restart.getWidth()) / 2) - 20);
		restart.setLayoutY(((background.getHeight() - restart.getHeight()) / 2) + 30);
	}

	public boolean isBetrunken() {
		return betrunken;
	}

	public void setBetrunken(boolean betrunken) {
		this.betrunken = betrunken;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void verloren() {
		timer.stop();
		background.getChildren().removeAll();
		pause.setText("Verloren, Länge: " + schlange.getLaenge());
		background.getChildren().add(menu);
		restart.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					restartGame();
				}
			}
		});
		background.getChildren().add(restart);
	}

	private void restartGame() {
		resetItems();
		schlange.reset();
		nextRotation = 0;
		timer.play();
		background.getChildren().remove(menu);
		background.getChildren().remove(restart);
	}

	public List<Apfel> getItem() {
		return apfel;
	}

	public Timeline getTimeline() {
		return timer;
	}

	private void resetItems() {
		ArrayList<Apfel> temp = new ArrayList<Apfel>();
		for (Apfel a : apfel) {
			temp.add(a);
		}
		for (Apfel a : temp) {
			a.iss();
		}
	}

	public synchronized void generiereApfel() {
		int x = 0;
		int y = 0;
		do {
			x = (int) (Math.random() * this.x - 2) + 2;
			y = (int) (Math.random() * this.y - 2) + 2;
		} while (schlange.istAuf(x, y));
		apfel.add(new Apfel(x, y, background, this));
	}

	public synchronized void generiereTempApfel() {
		int x = 0;
		int y = 0;
		do {
			x = (int) (Math.random() * this.x - 2) + 2;
			y = (int) (Math.random() * this.y - 2) + 2;
		} while (schlange.istAuf(x, y));
		apfel.add(new SonderApfel(x, y, background, this));
	}
}
