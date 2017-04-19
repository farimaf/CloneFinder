/**
 * Created by Farima on 3/27/2017.
 */
public class FileItem {
    private String word;
    private Integer frequency;

    public FileItem(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public Integer getFrequency() {
        return frequency;
    }
}
