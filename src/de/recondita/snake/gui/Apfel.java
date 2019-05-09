package de.recondita.snake.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Apfel extends ImageView {

	private final int x;
	private final int y;
	private Pane p;
	private int wert;
	private Main main;
	private int typ;

	public Apfel(int x, int y, Pane p, Main main) {
		this.x = x;
		this.y = y;
		this.p = p;
		this.main = main;
		wert = (int) (Math.random() * 1000);
		if (wert < 600) {
			wert = 1;
			typ = 1;
			setImage(new Image("de/recondita/snake/gui/apfel.png"));
		} else if (wert < 700) {
			wert = 1;
			typ = 5;
			setImage(new Image("de/recondita/snake/gui/rainbow.png"));
		} else if (wert < 720) {
			wert = -1;
			typ = 1;
			setImage(new Image("de/recondita/snake/gui/kuchen.png"));
		} else if (wert < 780) {
			wert = 3;
			typ = 2;
			setImage(new Image("de/recondita/snake/gui/bier.gif"));
		} else if (wert < 810) {
			wert = 0;
			typ = 3;
			setImage(new Image("de/recondita/snake/gui/geschenk.png"));
		} else if (wert < 850) {
			wert = 0;
			typ = 4;
			setImage(new Image("de/recondita/snake/gui/pille.png"));
		} else if (wert < 900) {
			wert = 0;
			typ = 1;
			setImage(new Image("de/recondita/snake/gui/brokkoli.png"));
		} else {
			wert = 2;
			typ = 1;
			setImage(new Image("de/recondita/snake/gui/bratwurst.png"));
		}
		p.getChildren().add(this);
		xProperty().bind(p.widthProperty().divide(main.getX()).multiply(x).subtract(fitWidthProperty().divide(2)));
		yProperty().bind(p.heightProperty().divide(main.getY()).multiply(y).subtract(fitHeightProperty().divide(2)));
		fitWidthProperty().bind(p.widthProperty().divide(main.getX()));
		fitHeightProperty().bind(p.heightProperty().divide(main.getY()));
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.millis(0), new KeyValue(this.rotateProperty(), getRotate() - 20)), new KeyFrame(
						Duration.millis(Main.GESCHWINDIGKEIT), new KeyValue(this.rotateProperty(), getRotate() + 20)));
		timeline.setAutoReverse(true);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public int getPositionX() {
		return x;
	}

	public int getPositionY() {
		return y;
	}

	public void iss() {
		p.getChildren().remove(this);
		main.getItem().remove(this);
		if (typ == 2) {
			main.setBetrunken(true);
			BoxBlur bb = new BoxBlur();
			p.setEffect(bb);
			Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(0), new KeyValue(bb.heightProperty(), 0)),
					new KeyFrame(Duration.millis(0), new KeyValue(bb.widthProperty(), 0)),
					new KeyFrame(Duration.millis(0), new KeyValue(bb.iterationsProperty(), 0)),
					new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT * 15), new KeyValue(bb.heightProperty(), 10)),
					new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT * 15), new KeyValue(bb.widthProperty(), 10)),
					new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT * 15),
							new KeyValue(bb.iterationsProperty(), 10)));
			timeline.setAutoReverse(true);
			timeline.setCycleCount(2);
			timeline.play();
			timeline.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					p.setEffect(null);
					main.setBetrunken(false);
				}
			});
		} else if (typ == 3) {
			for (int i = 0; i < 5; i++) {
				main.generiereTempApfel();
			}
		} else if (typ == 4) {
			ArrayList<Apfel> temp = new ArrayList<Apfel>();
			for (Apfel a : main.getItem()) {
				if (a instanceof SonderApfel) {
					temp.add(a);
				}
			}
			for (Apfel a : temp) {
				a.iss();
			}
			Bloom bloom = new Bloom();
			p.setEffect(bloom);
			Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.millis(0), new KeyValue(bloom.thresholdProperty(), 1.0)), new KeyFrame(
							Duration.millis(Main.GESCHWINDIGKEIT * 50), new KeyValue(bloom.thresholdProperty(), 0.01)));
			timeline.setAutoReverse(true);
			timeline.setCycleCount(2);
			timeline.play();
			timeline.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					p.setEffect(null);
				}
			});
		} else if (typ == 5) {
			 ColorAdjust colorAdjust = new ColorAdjust();
			 p.setEffect(colorAdjust);
			 
			 Timeline timeline = new Timeline();
			 timeline.setCycleCount(5);
			 timeline.setAutoReverse(true);
			 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1500), new KeyValue(colorAdjust.hueProperty(), 1)));
			 timeline.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					p.setEffect(null);
				}
			});
			 timeline.play();
			 
			 
		}
		neu();
	}

	public void neu() {
		main.generiereApfel();
	}

	public int getWert() {
		return wert;
	}

}
