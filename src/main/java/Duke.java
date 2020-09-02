import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Represents a Duke object that acts like a program.
 * Contains the main method.
 */
public class Duke {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private boolean isLoadingSuccess;
    private String filepath;

    /**
     * Creates a Duke object.
     * It is to start the Duke program.
     *
     * Variable filepath is directory to the duke.txt file where
     * reading and writing of the file occurs.
     *
     * Initialises Ui, Storage classes.
     * Sets isLoadingSuccess to true, assuming no errors.
     *
     * Loads the file contents.
     */
    public Duke() {
        String home = System.getProperty("user.home");
        Path path = Paths.get(home, "Desktop", "CS2103T", "ip", "data", "duke.txt");

        this.filepath = path.toString();
        this.ui = new Ui();
        this.storage = new Storage(filepath);
        this.isLoadingSuccess = true;

        try {
            this.storage.loadFileContent();
            this.tasks = new TaskList(this.storage.getTaskList());

        } catch (FileNotFoundException e) {
            System.out.println(e);
            this.isLoadingSuccess = false;
        }
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {
        return "Duke:\n" + input;
    }

    /**
     * Runs the bulk of the Duke program.
     *
     * Prints the greetings using Ui object.
     * Tells the user if file loading is successful or not.
     *
     * Continues off data from the file.
     * Edit the file as user types in the console.
     */
    public void run() {
        this.ui.displayWelcome();

        if (!isLoadingSuccess) {
            this.ui.displayLoadFileError();

        } else {
            this.ui.displayLoadFileSuccess();

            Parser parser = new Parser(tasks);
            Scanner sc = new Scanner(System.in);

            while (parser.isRunning()) {
                String userInput = sc.nextLine();

                try {
                    parser.checkUserInput(userInput);
                } catch (DukeException e) {
                    ui.displayUserInputError(e.getMessage());
                }

                try {
                    storage.writeToFile(parser.getTasks());
                } catch (IOException e) {
                    ui.displayUpdateFileError(e.getMessage());
                }
            }
        }
    }

    /**
     * Main method of Duke program.
     *
     * Sets the relative path to the .txt file that requires updating.
     * Creates a Duke object and runs it.
     */
    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.run();
    }
}
