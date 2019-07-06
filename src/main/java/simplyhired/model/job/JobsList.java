package simplyhired.model.job;

import java.util.List;

public class JobsList {

    private List<JobDescription> jobs;

    public JobsList(){

    }

    public JobsList(List<JobDescription> jobs){
        this.jobs = jobs;
    }

    public List<JobDescription> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDescription> jobs) {
        this.jobs = jobs;
    }
}
