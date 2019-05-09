package de.recondita.snake.gui;

import javafx.scene.layout.Pane;

public class Schlange {
	private Kopf kopf;

	public Schlange(Pane background, Main main) {
		kopf = new Kopf(background, main, this);
		background.getChildren().add(kopf);
		kopf.setzePosition((int) (main.getX() / 2), (int) (main.getY() / 2));
	}

	public void setRotation(int r) {
		kopf.setRotation(r);
	}
	
	public void reset() {
		kopf.reset();
	}

	public void schritt() {
		double x = kopf.getPositionX();
		double y = kopf.getPositionY();
		switch (kopf.getRichtung()) {
		case (-90):
		case (270):
			x--;
			break;
		case (0):
			y--;
			break;
		case (90):
			x++;
			break;
		case (180):
		case (-180):
			y++;
			break;
		default:
			break;
		}
		kopf.bewege(x, y);
	}

	public int getLaenge() {
		int ret = 0;
		for (Komponente temp = kopf; temp != null; temp = temp.getNachher()) {
			ret++;
		}
		return ret;
	}

	public boolean istAuf(int x, int y) {
		for (Komponente temp = kopf; temp != null; temp = temp.getNachher()) {
			if ((int) Math.round(temp.getPositionX()) == x && (int) Math.round(temp.getPositionY()) == y) {
				return true;
			}
		}
		return false;
	}

	public boolean istAuf(double x, double y) {
		double seed = 0.3;
		for (Komponente temp = kopf.getNachher(); temp != null; temp = temp.getNachher()) {
			if (temp.getPositionX() < x + seed && temp.getPositionX() > x - seed && temp.getPositionY() < y + seed
					&& temp.getPositionY() > y - seed) {
				return true;
			}
		}
		return false;
	}
}
