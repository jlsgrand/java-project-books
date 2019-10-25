import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    /*
     * Ce que doit faire le mini projet :
     *
     * - Proposer de compter le nombre de mots dans les fichiers donnés en arguments
     * - Proposer d'afficher les 50 mots les plus utilisés et le nombre d'occurence dans les fichiers
     * - Afficher les mots présents seulement dans un fichier et pas dans les autres
     * - Afficher le pourcentage de mots présents dans les autres fichier (par rapport au premier)
     */

    private final static Map<String, String> mainMenuChoices = new HashMap<>();
    private final static Map<String, String> statsMenuChoices = new HashMap<>();

    private final static Scanner inputScanner = new Scanner(System.in);

    private static BookFileList bookFileList = new BookFileList();

    /**
     * Entry point of the book statistics program.
     *
     * @param args list of book files.
     */
    public static void main(String[] args) {

        // Load menus
        initMenus();

        // Load arguments as file paths in the list of file paths
        for (String file : args) {
            Path filePath = Path.of(file);
            if (!Files.exists(filePath)) {
                System.out.println("Le programme ne peut fonctionner qu'avec des chemins de fichier en argument");
                System.out.println("Il va donc s'arrêter car les arguments contiennent des chemins non trouvés sur le disque");
                return;
            }

            bookFileList.addBookFileToList(new BookFile(filePath));
        }

        String mainChoice;
        do {
            System.out.println();
            mainChoice = showMenu(mainMenuChoices);

            switch (mainChoice) {
                case "1":
                    bookFileList.listBookFiles();
                    break;
                case "2":
                    updateBookFileList(true);
                    break;
                case "3":
                    updateBookFileList(false);
                    break;
                case "4":
                    statisticsMenu();
                    break;
                default:
                    break;
            }
        } while (!mainChoice.equals("5"));

        // Close input scanner
        inputScanner.close();
    }

    /**
     * Load menus in the menu choices hash maps.
     */
    private static void initMenus() {
        mainMenuChoices.put("1", "Lister les fichiers");
        mainMenuChoices.put("2", "Ajouter un fichier");
        mainMenuChoices.put("3", "Supprimer un fichier");
        mainMenuChoices.put("4", "Afficher des informations sur un livre");
        mainMenuChoices.put("5", "Quitter le programme");

        statsMenuChoices.put("1", "Lister les fichiers");
        statsMenuChoices.put("2", "Choisir le fichier de référence");
        statsMenuChoices.put("3", "Afficher le nombre de mots différents du fichier");
        statsMenuChoices.put("4", "Afficher les 50 mots les plus représentés");
        statsMenuChoices.put("5", "Afficher les mots présents seulement dans le fichier de référence");
        statsMenuChoices.put("6", "Afficher le pourcentage de mots communs des fichiers avec celui de référence");
        statsMenuChoices.put("7", "Quitter le menu statistiques");
    }

    /**
     * Shows a menu based on a list of choices given as parameter in a map and gets the user choice.
     *
     * @param menuPossibleChoiceMap the list of possible choices.
     * @return the actual choice made by the user.
     */
    private static String showMenu(Map<String, String> menuPossibleChoiceMap) {
        ArrayList<String> menuPossibleChoiceList = new ArrayList<>();
        System.out.println("----------- Statistiques sur les livres ----------");
        for (Map.Entry<String, String> menuChoice : menuPossibleChoiceMap.entrySet()) {
            System.out.print(menuChoice.getKey());
            System.out.print(". --> ");
            System.out.println(menuChoice.getValue());

            menuPossibleChoiceList.add(menuChoice.getKey());
        }
        System.out.println("----------- ########################### ----------");

        return getUserChoice(menuPossibleChoiceList);
    }

    /**
     * Gets a user choice based on a list of possible choices.
     *
     * @param possibleValues the possible values to check against.
     * @return the user choice.
     */
    private static String getUserChoice(List<String> possibleValues) {
        String userChoice;
        do {
            System.out.print("Entrez votre choix : ");
            userChoice = inputScanner.nextLine();
        } while (!possibleValues.contains(userChoice));

        return userChoice;
    }

    /**
     * Adds (or remove) a book file to the book file list.
     *
     * @param choice adds if choice is true, removes otherwise.
     */
    private static void updateBookFileList(boolean choice) {
        String bookFilePath;

        do {
            System.out.print("Veuillez entrer le chemin du livre à " + ((choice) ? "ajouter" : "supprimer") + " : ");
            bookFilePath = inputScanner.nextLine();
        } while (!checkBookFilePath(bookFilePath));

        BookFile bookFile = new BookFile(Path.of(bookFilePath));

        if (choice) {
            bookFileList.addBookFileToList(bookFile);
        } else {
            bookFileList.removeBookFileFromList(bookFile);
        }
    }

    /**
     * Statistics menu handling.
     */
    private static void statisticsMenu() {

        String subChoice;
        do {
            System.out.println();
            subChoice = showMenu(statsMenuChoices);

            switch (subChoice) {
                case "1":
                    bookFileList.listBookFiles();
                    break;
                case "2":
                    chooseReferenceFile();
                    break;
                case "3":
                    ensureReferenceFileChoice();
                    System.out.println(bookFileList.getReferenceBookFile().getWordCount());

                    break;
                case "4":
                    ensureReferenceFileChoice();
                    System.out.println(bookFileList.getReferenceBookFile().getMostUsedWords(50));

                    break;
                case "5":
                    ensureReferenceFileChoice();
                    System.out.println(bookFileList.getWordsOnlyPresentInReferenceFile());

                    break;
                case "6":
                    ensureReferenceFileChoice();
                    System.out.println(bookFileList.getCommonWordsPercentage());

                    break;
                default:
                    break;

            }
        } while (!subChoice.equals("7"));
    }

    /**
     * Force reference file choice.
     */
    private static void ensureReferenceFileChoice() {
        if (bookFileList.getReferenceBookFile() == null) {
            System.out.println("Vous devez choisir un fichier de référence !");
            chooseReferenceFile();
        }
    }

    /**
     * Checks that a file exists.
     *
     * @param bookFilePath file path to check
     * @return true if file exists, false otherwise
     */
    private static boolean checkBookFilePath(String bookFilePath) {
        return bookFilePath.length() > 0 && Files.exists(Path.of(bookFilePath));
    }

    /**
     * Choose a reference file from file list.
     */
    private static void chooseReferenceFile() {
        int possibleChoiceNumber = bookFileList.getBookFileListSize();
        List<String> possibleChoiceList = new ArrayList<>();

        for (int i = 1; i <= possibleChoiceNumber; i++) {
            possibleChoiceList.add(String.valueOf(i));
        }

        String userChoice;
        do {
            System.out.print("Veuillez entrer le numéro du fichier à prendre en référence : ");
            userChoice = getUserChoice(possibleChoiceList);
        } while (!possibleChoiceList.contains(userChoice));

        bookFileList.chooseReferenceFile(Integer.parseInt(userChoice));
    }

}
