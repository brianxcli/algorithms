package brian.algorithm.AucklandRoadSystem.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadScheduler {
	private static final int TASK_MAX = 10;
	
	private static LoadScheduler self = new LoadScheduler();
	
	private ExecutorService service;
	
	private LoadScheduler() {
		service = Executors.newFixedThreadPool(TASK_MAX);		
	}
	
	public static LoadScheduler instance() {
		return self;
	}
	
	public void execute(Runnable runnable) {
		service.execute(runnable);
	}
}
