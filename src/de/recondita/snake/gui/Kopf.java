package de.recondita.snake.gui;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

public class Kopf extends Komponente {

	private Main main;
	private Pane p;
	private Schlange schlange;

	public Kopf(Pane p, Main main, Schlange schlange) {
		super("de/recondita/snake/gui/kopf.png", p, main, new Schwanz(p, main));
		getNachher().setzePosition((int) getPositionX(), (int) getPositionY() + 1);
		this.main = main;
		this.p = p;
		this.schlange = schlange;
	}

	@Override
	public void bewege(double x, double y) {
		super.bewege(x, y);
		if (x <= 0 || y <= 0 || x >= main.getX() || y >= main.getY()) {
			main.verloren();
		}
		if (schlange.istAuf(x, y)) {
			main.verloren();
		}
		ArrayList<Apfel> temp = new ArrayList<Apfel>();
		for (Apfel a : main.getApfel()) {
			if ((int) Math.round(x) == a.getPositionX() && (int) Math.round(y) == a.getPositionY()) {
				temp.add(a);
				for (int i = 0; i < (a.getWert() == -1 ? schlange.getLaenge() / 4 : a.getWert()); i++) {
					verlaengern();
				}
			}
		}
		for (Apfel a : temp) {
			a.iss();
		}
	}

	public void verlaengern() {
		int temp=getNachher().getNextRotation();
		setNachher(new Mittelstueck(p, main, getNachher()));
		getNachher().setRotation(temp);
		p.getChildren().add(getNachher());
	}
}
