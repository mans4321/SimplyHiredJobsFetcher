package simplyhired.model.job;

public class JobDescription implements Comparable<JobDescription> {

	private double score;
	private String jobTitle;
	private String companyName;
	private String city;
	private String url;
	private String jobDes;
	private String jobDesInHtmlFormat;

	
	public JobDescription() {
	}
	
	public JobDescription(String jobTitle, String companyName, String city, String jobDes,
			String htmlJobDes) {
		this.jobTitle = jobTitle;
		this.companyName = companyName;
		this.city = city;
		this.jobDes = jobDes;
		this.jobDesInHtmlFormat = htmlJobDes;
	}

	@Override
	public boolean equals(Object obj) {
		JobDescription castObj = (JobDescription) obj;
		if (this.jobTitle.equals(castObj.getJobTitle()) && checkSubString(castObj))
			return true;
		return false;
	}

	@Override
	public int compareTo(JobDescription arg0) {
		if (this.jobTitle.equals(arg0.getJobTitle()) && checkSubString(arg0))
			return 0;

		if (Double.compare(this.score, arg0.getScore()) == 0)//TODO why
			return 1;
		return Double.compare(this.getScore(), arg0.getScore());
	}

	private boolean checkSubString(JobDescription arg0) {
		if (this.companyName.length() > arg0.getCompanyName().length())
			return this.companyName.contains(arg0.getCompanyName());
		else if (this.companyName.length() < arg0.getCompanyName().length())
			return arg0.getCompanyName().contains(this.companyName);
		else
			return this.companyName.equals(arg0.getCompanyName());
	}
	

	@Override
	public String toString() {
		return "JobDescription [score=" + score + ", jobTitle=" + jobTitle + ", companyName=" + companyName + ", city="
				+ city + "url=" + url + "]";
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getJobDes() {
		return jobDes;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCity() {
		return city;
	}

	public String getUrl() {
		return url;
	}

	public String getJobDesInHtmlFormat() {
		return jobDesInHtmlFormat;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
