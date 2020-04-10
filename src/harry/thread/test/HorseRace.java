package harry.thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

import javax.print.attribute.standard.NumberUp;

/**
 * 
 * @author Harry
 *
 */
public class HorseRace {
	private static final int NUMBER_OF_HORSE = 12;
	private static final int INIT_PARTIES = 1;
	private static final Phaser manager = new Phaser(INIT_PARTIES);
	
	public static void main(String[] args) {
		Thread raceMonitor = new Thread(new RaceMonitor());
		raceMonitor.setDaemon(true);
		raceMonitor.start();
		manageRace();
	}
	
	private static void manageRace() {
		List<Horse> horses = new ArrayList<Horse>();
		for (int i = 0; i < NUMBER_OF_HORSE; i++) {
			horses.add(new Horse());
		}
		
		runRace(horses);
	}
	
	private static void runRace(List<Horse> horses) {
		System.out.println("Assign all horses, then start race");
		for (Horse horse : horses) {
			String dev = horse.toString();
			manager.register();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep((new Random()).nextInt(1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println(dev + ",please await all horses");
					manager.arriveAndAwaitAdvance();
					horse.run();
				}
			}).start();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("All arrived at starting gate, start race");
		manager.arriveAndDeregister();
	}

	private static class Horse implements Runnable{
		private static final AtomicInteger idSource = new AtomicInteger();
		private final int id = idSource.getAndIncrement();
		
		@Override
		public void run() {
			System.out.println(toString() + ":" + " running");
		}
		
		@Override
		public String toString() {
			return "Horse $" + id;
		}
	}

	private static class RaceMonitor implements Runnable{
		@Override
		public void run() {
			while(true){
				System.out.println("Number of horses ready to run: " + HorseRace.manager.getArrivedParties());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
