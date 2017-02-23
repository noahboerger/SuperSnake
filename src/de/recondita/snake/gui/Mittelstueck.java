package de.recondita.snake.gui;

import javafx.scene.layout.Pane;

public class Mittelstueck extends Komponente {
	public Mittelstueck(Pane p, Main main) {
		super("de/recondita/snake/gui/mittelstueck.png", p, main, new Schwanz(p, main));
	}

	public Mittelstueck(Pane p, Main main, Komponente k) {
		super("de/recondita/snake/gui/mittelstueck.png", p, main, k);
	}
}
