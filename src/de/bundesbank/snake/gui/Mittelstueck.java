package de.bundesbank.snake.gui;

import javafx.scene.layout.Pane;

public class Mittelstueck extends Komponente {
	public Mittelstueck(Pane p, Main main) {
		super("de/bundesbank/snake/gui/mittelstück.png", p, main, new Schwanz(p, main));
	}

	public Mittelstueck(Pane p, Main main, Komponente k) {
		super("de/bundesbank/snake/gui/mittelstück.png", p, main, k);
	}
}
