import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BookIntegrationTests {

    private BookFileList bookFileList;
    private BookFile ethiqueBookFile;
    private BookFile reformeBookFile;
    private BookFile traiteBookFile;

    @BeforeEach
    public void setUp() {
        bookFileList = new BookFileList();

        ethiqueBookFile = new BookFile(Path.of("resources/books/ethique-line.txt"));
        reformeBookFile = new BookFile(Path.of("resources/books/reforme-line.txt"));
        traiteBookFile = new BookFile(Path.of("resources/books/traite-line.txt"));

        bookFileList.addBookFileToList(ethiqueBookFile);
        bookFileList.addBookFileToList(reformeBookFile);
        bookFileList.addBookFileToList(traiteBookFile);

        bookFileList.chooseReferenceFile(1);
    }

    @Test
    public void ethiqueCountTest() {
        assertEquals(ethiqueBookFile.getMostUsedWords(1).get(0).getCount(), 7098);
    }

    @Test
    public void reformeCountTest() {
        assertEquals(reformeBookFile.getMostUsedWords(1).get(0).getCount(), 668);
    }

    @Test
    public void traiteCountTest() {
        assertEquals(traiteBookFile.getMostUsedWords(1).get(0).getCount(), 1814);
    }

    @Test
    public void accompagnementOnlyInEthique() {
        assertTrue(bookFileList.getWordsOnlyPresentInReferenceFile().contains(new Word("accompagnement", 1)));
    }

    @Test
    public void deNotOnlyInEthique() {
        assertFalse(bookFileList.getWordsOnlyPresentInReferenceFile().contains(new Word("de", 1)));
    }

}