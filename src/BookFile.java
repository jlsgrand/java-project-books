import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BookFile {

    private final Path filePath;
    private final List<Word> wordList = new ArrayList<>();

    private boolean isBookLoadedInMemory = false;

    /**
     * Constructor with book file path argument.
     *
     * @param filePath the book file path.
     */
    public BookFile(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Get book word count.
     *
     * @return the number of words contained in book.
     */
    public int getWordCount() {
        if (!isBookLoadedInMemory)
            loadBookInMemory();

        return wordList.size();
    }

    /**
     * Gets the most used words of a book.
     *
     * @param count the number of most used words.
     * @return a sub list of the book word list.
     */
    public List<Word> getMostUsedWords(int count) {
        if (!isBookLoadedInMemory)
            loadBookInMemory();

        return wordList.subList(0, count);
    }

    /**
     * Gets the words list of a book.
     *
     * @return the list of words contained in a book.
     */
    public List<Word> getWordList() {
        if (!isBookLoadedInMemory)
            loadBookInMemory();

        List<Word> wordListToReturn = new ArrayList<>(wordList.size());
        wordListToReturn.addAll(wordList);

        return wordListToReturn;
    }

    /**
     * Load book file as a list of words.
     */
    private void loadBookInMemory() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));

            wordList.add(null);
            String line;
            do {
                line = reader.readLine();

                if (line != null) {
                    line = line.toLowerCase();
                    Word readWord = new Word(line, 1);

                    if (wordList.contains(readWord)) {
                        wordList.get(wordList.indexOf(readWord)).incrementCount();
                    } else {
                        wordList.add(readWord);
                    }
                }

            } while (line != null);

            wordList.add(null);
            wordList.sort(new Comparator<Word>() {
                /**
                 * Compares the word regarding descending count number (and if equal, word content comparison in ascending order).
                 *
                 * @param o1 word 1
                 * @param o2 word 2
                 * @return comparison result
                 *          level 1 ==> 0 if both words are null; 1 if o1 is null but not o2; -1 if o2 is null but not o1
                 *          level 2 ==> 1 if o1.count < o2.count; -1 if o1.count > o2.count
                 *          level 3 ==> o1.content.compareTo(o2.content) (alphabetical ascending ordering)
                 */
                @Override
                public int compare(Word o1, Word o2) {
                    int comparison = 0;

                    // If both words are null then they are equal
                    if (o1 == null && o2 == null) return 0;
                    // Else if word 1 is null (but not word 2), it should be greater compared to word 2
                    else if (o1 == null) return 1;
                    // Else if word 2 is null, it should go
                    else if (o2 == null) return -1;

                    // If counts are different, compare them
                    // Otherwise compare word contents
                    if (o1.getCount() < o2.getCount()) {
                        comparison = 1;
                    } else if (o1.getCount() > o2.getCount()) {
                        comparison = -1;
                    } else {
                        comparison = o1.getContent().compareTo(o2.getContent());
                    }

                    return comparison;
                }

            });
            isBookLoadedInMemory = true;
        } catch (IOException ioEx) {
            System.err.println("Une erreur est survenue lors de la lecture du fichier : " + filePath);
            isBookLoadedInMemory = false;
        }
    }

    @Override
    public String toString() {
        return filePath.toString();
    }

    @Override
    public boolean equals(Object o) {
        // If o ref is null or is not the same class, we should return false directly.
        if (o == null || getClass() != o.getClass()) return false;

        // Otherwise, it means that it is a BookFile object, then we can compare filePath property.
        BookFile bookFile = (BookFile) o;
        return Objects.equals(filePath, bookFile.filePath);
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }

}
