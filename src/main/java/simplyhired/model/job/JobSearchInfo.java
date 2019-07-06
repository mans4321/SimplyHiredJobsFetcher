package simplyhired.model.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobSearchInfo {

	private List<String> userSkills;
	private int userExperience;
	private List<String> cities;
	private List<String> titles;
	
	public JobSearchInfo(){
		userSkills = new ArrayList<String>();
		cities = new ArrayList<String>();
		titles = new ArrayList<String>();
	}

	public void setUserSkills(List<String> userSkills) {
		this.userSkills = Objects.isNull(userSkills)? new ArrayList<>() : userSkills;
	}
	
	public int getUserExperience() {
		return userExperience;
	}
	
	public void setUserExperience(Integer userExperience) {
		this.userExperience = Objects.isNull(userExperience)? 0 : userExperience;
	}
	
	public List<String> getCities() {
		return cities;
	}
	
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	
	public List<String> getTitles() {
		return titles;
	}
	
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}
	
	public List<String> getSkills() {
		return userSkills;

	}

	@Override
	public String toString() {
		return "JobSearchInfo{" +
				"userSkills=" + userSkills +
				", userExperience=" + userExperience +
				", cities=" + cities +
				", titles=" + titles +
				'}';
	}
}
