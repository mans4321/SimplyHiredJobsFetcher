package simplyhired.model;

import java.util.HashSet;
import java.util.Set;

public class WordLists {

	private Set<String> stopWords;
	private Set<String> legalEntityTypes;
	
	public WordLists(){
		stopWords = new HashSet<>();
		legalEntityTypes = new HashSet<>();
	}
	
	public Set<String> getStopWords() {
		return stopWords;
	}
	
	public void setStopWords(Set<String> stopWords) {
		this.stopWords = stopWords;
	}
	
	public Set<String> getLegalEntityTypes() {
		return legalEntityTypes;
	}

	public void setLegalEntityTypes(Set<String> legalEntityTypes) {
		this.legalEntityTypes = legalEntityTypes;
	}
}
