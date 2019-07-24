package simplyhired.model.job;

public class JobDescription implements Comparable<JobDescription> {

	private double score;
	private String title;
	private String company;
	private String city;
	private String url;
	private String jobDescription;
	private String website;
	
	public JobDescription() {
	}
	
	public JobDescription(String jobTitle, String companyName, String city, String jobDes) {
		this.title = jobTitle;
		this.company = companyName;
		this.city = city;
		this.website = "SimplyHired";
		this.jobDescription = jobDes;
	}

	@Override
	public boolean equals(Object obj) {
		JobDescription castObj = (JobDescription) obj;
		if (this.title.equals(castObj.getTitle()) && checkSubString(castObj))
			return true;
		return false;
	}

	@Override
	public int compareTo(JobDescription arg0) {
		if (this.title.equals(arg0.getTitle()) && checkSubString(arg0))
			return 0;

		if (Double.compare(this.score, arg0.getScore()) == 0)//TODO why
			return 1;
		return Double.compare(this.getScore(), arg0.getScore());
	}

	private boolean checkSubString(JobDescription arg0) {
		if (this.company.length() > arg0.getCompany().length())
			return this.company.contains(arg0.getCompany());
		else if (this.company.length() < arg0.getCompany().length())
			return arg0.getCompany().contains(this.company);
		else
			return this.company.equals(arg0.getCompany());
	}
	


	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public String getTitle() {
		return title;
	}

	public String getCompany() {
		return company;
	}

	public String getCity() {
		return city;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebsite() {
		return website;
	}
}
