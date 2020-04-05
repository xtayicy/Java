package harry.thread.test;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;

/**
 * 
 * @author Harry
 *
 */
public class SineWaveAnimator extends JFrame implements Runnable{
	private int frame = 0;
	
	public SineWaveAnimator() {
		setTitle("Sine Wave Animator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,200);
		setVisible(true);
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new SineWaveAnimator());
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		while(true){
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			frame++;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		g.clearRect(0, 0, bounds.width, bounds.height);
		int h = bounds.height / 2;
		for (int x = 0; x < bounds.width; x++) {
			int y1 = (int) (Math.sin((x - frame) * 0.09) * h + 1.0);
			int y2 = (int) (Math.sin((x + frame) * 0.01) * h + 1.0);
			g.drawLine(x, y1, x, y2);
		}
	}
}
