package simplyhired.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import simplyhired.model.JobScorer;
import simplyhired.model.apifetcher.FetchJobsAPI;
import simplyhired.model.apifetcher.SimplyHiredAPI;
import simplyhired.model.job.JobDescription;
import simplyhired.model.job.JobSearchInfo;
import simplyhired.model.job.JobsList;
import simplyhired.model.threads.JobDescriptionFetcher;
import simplyhired.model.threads.JobDescriptionScorer;
import simplyhired.model.threads.JobUrlFetcher;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class FetchJobFacade {

	private final int NUMBER_OF_RESULT_PAGES = 3;

	private final int URL_FETCHR_THREADS = 10;

	private final int DESCRIPTION_FETCHER_THREADS = 10;

	private final int SCORER_THREADS = 3;

	private FetchJobsAPI simplyHiredAPI;

	private BlockingQueue<String> jobsUrlQueue;

	private BlockingQueue<JobDescription> jobsDesQueue;

	private JobDescriptionFetcher jobDesFetcher;

	private JobDescriptionScorer jobDesScorer;

	private JobUrlFetcher jobUrlFetcher;


	public FetchJobFacade() {
		this.simplyHiredAPI = new SimplyHiredAPI();

		this.jobsUrlQueue = new LinkedBlockingQueue<>();
		this.jobsDesQueue = new LinkedBlockingQueue<>();
		
		jobUrlFetcher = new JobUrlFetcher(jobsUrlQueue, URL_FETCHR_THREADS);
		jobDesFetcher = new JobDescriptionFetcher(jobsUrlQueue, jobsDesQueue,
				DESCRIPTION_FETCHER_THREADS);
		
		jobDesScorer = new JobDescriptionScorer(jobsDesQueue,
				SCORER_THREADS);
		
	}

	@Async
	public Future<JobsList> fetch(JobSearchInfo searchInfo) {
		List<String> resultPages = getResultPagesFromJobApi(searchInfo);
		jobUrlFetcher.fetch(resultPages);
		jobDesFetcher.fetch();
		JobScorer jobScorer = new JobScorer(searchInfo.getSkills());
		return  jobDesScorer.score(jobScorer);
	}

	private List<String> getResultPagesFromJobApi(JobSearchInfo searchInfo) {
		List<String> resultPages = new LinkedList<>();

		for (String title : searchInfo.getTitles())
			for (String city : searchInfo.getCities()) {
				List<String> apiUrls = simplyHiredAPI.getResultPagesURL(title, city, NUMBER_OF_RESULT_PAGES);
				resultPages.addAll(apiUrls);
			}
		return resultPages;
	}
}