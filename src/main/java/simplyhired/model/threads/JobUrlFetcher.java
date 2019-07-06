package simplyhired.model.threads;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jsoup.nodes.Document;

import simplyhired.model.PageRequest;
import simplyhired.model.apifetcher.FetchJobsAPI;
import simplyhired.model.apifetcher.SimplyHiredAPI;

public class JobUrlFetcher {

	private final String TASK_FINISHED = "DONE";

	private int threadCount;

	private WorkerThread[] workers; // the threads that compute the image

	private volatile int threadsCompleted; // how many threads have finished?

	private ConcurrentLinkedQueue<Runnable> taskQueue; // holds tasks

	// holds individual job Des url
	private BlockingQueue<String> jobUrlQueue;

	private FetchJobsAPI simplyHiredAPI;

	public JobUrlFetcher(BlockingQueue<String> jobUrlQueue, int threadCount) {
		this.threadCount = threadCount;
		this.jobUrlQueue = jobUrlQueue;
		this.simplyHiredAPI = new SimplyHiredAPI();
	}

	public void fetch(List<String> resultPage) {
		initTaskQueue(resultPage);
		initAndRunWorkerThread();
	}

	private void initTaskQueue(List<String> resultPage) {
		taskQueue = new ConcurrentLinkedQueue<Runnable>();
		for (String url : resultPage)
			taskQueue.add(new UrlFetchingTask(url));
	}

	private void initAndRunWorkerThread() {
		workers = new WorkerThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			workers[i] = new WorkerThread();
			workers[i].start();
		}
	}

	private class WorkerThread extends Thread {
		public void run() {
			while (true) {
				Runnable task = taskQueue.poll();
				if (task == null)
					break;
				task.run();
			}
			threadFinished();
		}
	}

	synchronized private void threadFinished() {
		threadsCompleted++;
		if (threadsCompleted == workers.length) { // all threads have finished
			jobUrlQueue.add(TASK_FINISHED); // used to signal that no more tasks will be added
			workers = null;
		}
	}

	private class UrlFetchingTask implements Runnable {

		private String url;
		private PageRequest pageRequest;

		UrlFetchingTask(String url) {
			this.url = url;
			this.pageRequest = new PageRequest();
		}

		@Override
		public void run() {
			Document page = pageRequest.getPage(url);
			if (Objects.isNull(page))
				return;// couldn't Connect to page
			jobUrlQueue.addAll(simplyHiredAPI.getJobsUrlsOnPage(page));
		}

	}
}
