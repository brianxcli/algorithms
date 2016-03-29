package brian.algorithm.AucklandRoadSystem;

import javax.swing.SwingUtilities;

import brian.algorithm.AucklandRoadSystem.ui.MapWindow;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MapWindow();
			}			
		});
	}
}