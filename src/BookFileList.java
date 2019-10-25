import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookFileList {

    private final List<BookFile> bookFileList = new ArrayList<>();
    private BookFile referenceBookFile;

    /**
     * Adds a book file to the book file list.
     * This method will only add the book file if not already present in the list.
     *
     * @param newBookFile new book file to add
     */
    public void addBookFileToList(BookFile newBookFile) {
        if (bookFileList.contains(newBookFile)) {
            System.out.println("Le fichier est déjà dans la liste, il sera ignoré.");
        } else {
            bookFileList.add(newBookFile);
            System.out.println("Le fichier [" + newBookFile + "] a bien été ajouté.");
        }
    }

    /**
     * Removes a book file from the book file list.
     * This method will only remove the book file if existing in the list.
     *
     * @param bookFileToRemove the book file to remove
     */
    public void removeBookFileFromList(BookFile bookFileToRemove) {
        if (bookFileList.contains(bookFileToRemove)) {
            bookFileList.remove(bookFileToRemove);
            System.out.println("Le fichier [" + bookFileToRemove + "] a bien été supprimé.");
        } else {
            System.out.println("Le fichier n'existe pas dans la liste, il sera ignoré.");
        }
    }

    /**
     * Lists all loaded book files in console.
     */
    public void listBookFiles() {
        for (BookFile bookFile : bookFileList) {
            if (bookFile.equals(referenceBookFile)) {
                System.out.print("(*) ");
            } else {
                System.out.print("( ) ");
            }
            System.out.println(bookFile);
        }
    }

    /**
     * Chooses the reference file from the book file list.
     *
     * @param bookFileIndex the book file index in the book file list.
     */
    public void chooseReferenceFile(int bookFileIndex) {
        if (bookFileIndex <= bookFileList.size() && bookFileIndex > 0) {
            referenceBookFile = bookFileList.get(bookFileIndex - 1);
        } else {
            referenceBookFile = null;
        }
    }

    /**
     * Gets the book file list size.
     *
     * @return the number of book files
     */
    public int getBookFileListSize() {
        return bookFileList.size();
    }

    /**
     * Gets the reference book file.
     *
     * @return the selected book file
     */
    public BookFile getReferenceBookFile() {
        return this.referenceBookFile;
    }

    /**
     * Get words that appears in reference file and not in other files.
     *
     * @return the list of reference file unique words
     */
    public List<Word> getWordsOnlyPresentInReferenceFile() {
        List<Word> referenceWordList = referenceBookFile.getWordList();
        List<Word> uniqueWordList = referenceBookFile.getWordList();

        for (BookFile compareBook : bookFileList) {
            if (compareBook != referenceBookFile) {
                for (Word word : referenceWordList) {
                    if (compareBook.getWordList().contains(word)) {
                        uniqueWordList.remove(word);
                    }
                }
            }
        }
        return uniqueWordList;
    }

    /**
     * Get common word percentage regarding reference file.
     *
     * @return a map with the compared book file and the percentage of common words.
     */
    public Map<BookFile, String> getCommonWordsPercentage() {
        Map<BookFile, String> bookFileMap = new HashMap<>();

        List<Word> referenceWordList = referenceBookFile.getWordList();
        for (BookFile compareBook : bookFileList) {
            double commonWords = 0.d;
            if (compareBook != referenceBookFile) {
                for (Word word : referenceWordList) {
                    if (compareBook.getWordList().contains(word)) {
                        commonWords += 1;
                    }
                }
                double similarityRate = commonWords / referenceWordList.size();
                bookFileMap.put(compareBook, String.format(" %1$.1f%2$s", similarityRate * 100, "%"));
            }
        }

        return bookFileMap;
    }
}
