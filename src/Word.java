import java.util.Objects;

public class Word {

    private final String content;
    private int count;

    public Word(String content, int count) {
        this.content = content;
        this.count = count;
    }

    /**
     * Gets a word count.
     *
     * @return the count attribute.
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets a word content.
     *
     * @return the word content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Adds one to word count.
     */
    public void incrementCount() {
        count += 1;
    }

    @Override
    public boolean equals(Object o) {
        // If o ref is null or is not the same class, we should return false directly.
        if (o == null || getClass() != o.getClass()) return false;

        // Otherwise, it means that it is a Word object, then we can compare content property.
        Word word = (Word) o;
        return Objects.equals(content, word.content);
    }

    @Override
    public String toString() {
        return "'" + content + "' : " + count + " occurences";
    }

}
