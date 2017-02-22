package de.bundesbank.snake.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Komponente extends ImageView {

	private SimpleDoubleProperty x = new SimpleDoubleProperty();
	private SimpleDoubleProperty y = new SimpleDoubleProperty();
	// private boolean fertig = true;
	//private Main main;
	private int rotate = 0;
	private Komponente nachher;
	private int nextRotation = 0;

	public Komponente(String pfad, Pane p, Main main, Komponente nachher) {
		super(new Image(pfad));
		this.nachher = nachher;
		setCache(true);
		//this.main = main;
		xProperty().bind(p.widthProperty().divide(main.getX()).multiply(x).subtract(fitWidthProperty().divide(2)));
		yProperty().bind(p.heightProperty().divide(main.getY()).multiply(y).subtract(fitHeightProperty().divide(2)));
		fitWidthProperty().bind(p.widthProperty().divide(main.getX()));
		fitHeightProperty().bind(p.heightProperty().divide(main.getY()));
		if (nachher == null)
		{
			// x.bind(vorher.getPropX().add(vorher.rotateProperty().divide(90)));
			// y.bind(vorher.getPropY().subtract(vorher.rotateProperty().divide(90).subtract(1)));
			// rotateProperty().bind(vorher.rotateProperty());
		} else {
			try {
				p.getChildren().add(nachher);
			} catch (Exception e) {
			}
			rotateProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
					setRotate(getRotate() % 360);
					if (getRotate() < 0)
						setRotate(getRotate() + 360);
					if (getRotate() == 360)
						setRotate(0);
				}
			});
		}
	}

	public void setRotation(int r) {
		RotateTransition ft = new RotateTransition(
				Duration.millis((this instanceof Kopf) ? Main.GESCHWINDIGKEIT / 2 : Main.GESCHWINDIGKEIT), this);
		// fertig = false;
		double t = (r - getRotate()) % 360;
		t = t > 200 || t < -200 ? t / Math.abs(t) * (Math.abs(t) - 360)
				: (t < 200 && t > 100 || t < -100 && t > -200) ? 0 : t;
		ft.setByAngle(t);
		rotate = t < 1 && t > -1 ? rotate : r;
		ft.play();

	}

	public void setzePosition(int x, int y) {
		this.x.set(x);
		this.y.set(y);
	}

	public void bewege(double x, double y) {
		if (!(this instanceof Kopf)) {
			setRotation(nextRotation);
		}
		if ((int) Math.round(this.x.get() + this.y.get()) == 0) {
			this.x.set(x);
			this.y.set(y);
			if (nachher != null) {
				nachher.bewege(this.x.get(), this.y.get());
				nachher.setRotate(rotate);
			}
		} else {
			Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT), new KeyValue(this.x, x)),
					new KeyFrame(Duration.millis(Main.GESCHWINDIGKEIT), new KeyValue(this.y, y)));
			timeline.play();
			if (nachher != null)
				nachher.bewege(Komponente.this.x.get(), Komponente.this.y.get());
			timeline.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (nachher != null)
						nachher.setNextRotation(rotate);
				}
			});
		}
	}

	public int getRichtung() {
		return rotate;
	}

	public void setNextRotation(int rotation) {
		nextRotation = rotation;
	}
	
	public int getNextRotation() {
		return nextRotation;
	}

	public double getPositionX() {
		return x.get();
	}

	public double getPositionY() {
		return y.get();
	}

	public Komponente getNachher() {
		return nachher;
	}

	public void setNachher(Komponente n) {
		nachher = n;
	}
}
