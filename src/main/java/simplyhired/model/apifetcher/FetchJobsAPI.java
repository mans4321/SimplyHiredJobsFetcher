package simplyhired.model.apifetcher;

import java.util.List;

import org.jsoup.nodes.Document;

import simplyhired.model.job.JobDescription;

public interface FetchJobsAPI {
	
	public List<String> getResultPagesURL(String jobDes, String city, int numberOfPages);
	
	public List<String> getJobsUrlsOnPage(Document page);
	
	public JobDescription extractJobDescriptionOnPage(Document page);
	
}
