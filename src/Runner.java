import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Runner {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Power Grid");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Interface panel = new Interface();
				frame.add(panel);
				frame.setSize(panel.getWidth(), panel.getHeight());
				try {
					frame.setIconImage(ImageIO.read(new File("assets/images/icon.png")));
				} catch (IOException e) {
					System.out.println("Could not load icon!");
				}

				// frame.pack();
				// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				// frame.setUndecorated(true);

				try {
					frame.setIconImage(ImageIO.read(new File("assets/images/icon.png")));
				} catch (IOException e) {
					System.out.println("Could not load icon!");
				}
				frame.setVisible(true);
			}
		});
	}
}
