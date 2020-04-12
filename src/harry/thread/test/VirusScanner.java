package harry.thread.test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author Harry
 *
 */
public class VirusScanner {
	private static JFrame appFrame;
	private static JLabel statusString;
	private static int scanNumber = 0;
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

	public static void main(String[] args) {
		appFrame = new JFrame();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		appFrame.setSize(400, 70);
		appFrame.setLocation(dimension.width / 2 - appFrame.getWidth() / 2,
				dimension.height / 2 - appFrame.getWidth() / 2);
		statusString = new JLabel();
		appFrame.add(statusString);
		appFrame.setVisible(false);
		scanDisk();
	}

	private static void scanDisk() {
		Runnable scanner = new Runnable() {

			@Override
			public void run() {
				try {
					appFrame.setVisible(true);
					scanNumber++;
					Calendar cal = Calendar.getInstance();
					DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);
					statusString.setText(" scan " + scanNumber + " started at " + df.format(cal.getTime()));
					Thread.sleep(1000 + new Random().nextInt(10000));
					appFrame.setVisible(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		ScheduledFuture<?> scanManager = scheduler.scheduleAtFixedRate(scanner, 1, 15, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable(){

			@Override
			public void run() {
				scanManager.cancel(true);
				scheduler.shutdown();
				appFrame.dispose();
			}}, 60, TimeUnit.SECONDS);
	}
}
