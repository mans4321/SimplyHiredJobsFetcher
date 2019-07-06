package simplyhired.model.threads;

import java.util.Map.Entry;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.jsoup.nodes.Document;

import simplyhired.model.PageRequest;
import simplyhired.model.apifetcher.FetchJobsAPI;
import simplyhired.model.apifetcher.SimplyHiredAPI;
import simplyhired.model.job.JobDescription;

public class JobDescriptionFetcher {

	private final String TASK_FINISHED = "DONE";

	private int threadCount;

	private volatile int threadsCompleted;

	private WorkerThread[] workers; // the threads that compute the image

	// holds individual job Des url
	private BlockingQueue<String> taskQueue;

	private BlockingQueue<JobDescription> jobDesQueue;

	private FetchJobsAPI simplyHiredAPI;

	public JobDescriptionFetcher(BlockingQueue<String> taskQueue,
								 BlockingQueue<JobDescription> jobDesQueue, int threadCount) {

		this.taskQueue = taskQueue;
		this.jobDesQueue = jobDesQueue;
		this.threadCount = threadCount;
		simplyHiredAPI = new SimplyHiredAPI();
	}

	public void fetch() {
		initAndRunWorkerThread();
	}

	private void initAndRunWorkerThread() {
		workers = new WorkerThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			workers[i] = new WorkerThread(i);
			workers[i].start();
		}
	}

	private class WorkerThread extends Thread {
		private int index;

		WorkerThread(int index) {
			this.index = index;
		}

		public void run() {
			try {
				while (true) {
					String url = taskQueue.take();
					if (url.equalsIgnoreCase(TASK_FINISHED)) {
						workCompleted();
						break;
					}
					new DescriptionFetchingTask(url).run();
				}
			} catch (InterruptedException e) {
				workers[index] = new WorkerThread(index);
				workers[index].start();
			}
		}
	}

	synchronized private void workCompleted() {
		threadsCompleted++;

		if (threadsCompleted == 1) {
			informOtherThreads();
		}else if(threadsCompleted == threadCount){
			jobDesQueue.add(new JobDescription());//job done
			workers = null;
		}
	}

	private void informOtherThreads() {
		for (int i = 0; i < threadCount - 1; i++) // inform other workers
			taskQueue.add(TASK_FINISHED);
	}

	private class DescriptionFetchingTask implements Runnable {

		private String url;
		private PageRequest pageRequest;

		DescriptionFetchingTask(String url) {
			this.url = url;
			this.pageRequest = new PageRequest();
		}

		@Override
		public void run() {
			Document page = pageRequest.getPage(url);
			if (Objects.isNull(page))
				return;// couldn't Connect to page
			addJobDesToQueue(page, url);
		}

		private void addJobDesToQueue(Document page, String url) {
			JobDescription jobDes = simplyHiredAPI.extractJobDescriptionOnPage(page);
			jobDes.setUrl(url);
			jobDesQueue.add(jobDes);

		}
	}
}