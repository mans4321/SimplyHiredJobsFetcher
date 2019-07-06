package simplyhired.model.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.AsyncResult;
import simplyhired.model.JobScorer;
import simplyhired.model.job.JobDescription;
import simplyhired.model.job.JobsList;

public class JobDescriptionScorer {

	private int threadCount;

	private volatile int threadsCompleted;

	private WorkerThread[] workers; // the threads that compute the image

	private BlockingQueue<JobDescription> taskQueue;

	private TreeSet<JobDescription> sortedJob;

	private JobScorer jobScorer;

	private volatile boolean done;

	public JobDescriptionScorer(BlockingQueue<JobDescription> taskQueue, int threadCount) {
		this.taskQueue = taskQueue;
		this.threadCount = threadCount;
		sortedJob = new TreeSet<JobDescription>(Collections.reverseOrder());
		done = false;
	}

	public Future<JobsList> score(JobScorer jobScorer) {
		this.jobScorer = jobScorer;
		//TODO to be refactor initAndRunWorkerThread && getJobListWhenFinished
		initAndRunWorkerThread();
		return getJobListWhenFinished();
	}

	private Future<JobsList> getJobListWhenFinished() {
		while (!done){}
		JobsList jobsList = new JobsList(new ArrayList<JobDescription>(sortedJob));
		return new AsyncResult<>(jobsList);
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
					JobDescription task = taskQueue.take();
					if (task.getJobTitle() == null) {
						workCompleted();
						break;
					}
					new ScorerTask(task).run();
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
		} else if (threadsCompleted == threadCount) {
			workers = null;
			done = true;
		}
	}

	private void informOtherThreads() {
		for (int i = 0; i < threadCount - 1; i++) // inform other workers
			taskQueue.add(new JobDescription());
	}

	private class ScorerTask implements Runnable {
		JobDescription jobDes;

		ScorerTask(JobDescription jobDes) {
			this.jobDes = jobDes;
		}

		@Override
		public void run() {
			double score = jobScorer.getScore(jobDes.getJobDes());
			jobDes.setScore(score);
			addJob(jobDes);
		}
	}

	synchronized private void addJob(JobDescription jobDes) {
		sortedJob.add(jobDes);
	}
}