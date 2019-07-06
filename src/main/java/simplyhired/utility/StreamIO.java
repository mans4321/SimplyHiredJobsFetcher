package simplyhired.utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import simplyhired.model.WordLists;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public class StreamIO {

	private WordLists wordLists;
	private static StreamIO streamIO;

	public static StreamIO getInstance() {
		if (streamIO == null)
			streamIO = new StreamIO();
		return streamIO;
	}
	
	private StreamIO() {
		wordLists = getWordListFromFile("wordLists.json");
	}

	private WordLists getWordListFromFile(String fileName) {
		String fileLocation = "resources/files/" + fileName;
		WordLists wordLists = readFile(fileLocation, WordLists.class);
		return Objects.nonNull(wordLists) ? wordLists : new WordLists();
	}
	
	private <T> T readFile(String fileLocation, Class<T> type){
		ObjectMapper mapper = new ObjectMapper();
		try{
			return mapper.readValue(new File(fileLocation), type);
		}catch(Exception e) {
			return null;
		}
	}

	public Set<String> getLegalEntityTypes() {
		return wordLists.getLegalEntityTypes();
	}

	public Set<String> getStopWords() {
		return wordLists.getStopWords();
	}
}

