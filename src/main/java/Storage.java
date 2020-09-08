import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Storage object.
 * For reading and writing of the specified file.
 */
public class Storage {
    private static String filePath;
    private static List<Task> savedTasks;
    private static final char TICK = '\u2713';
    private static final char CROSS = '\u2718';
    private static final char CONVERTED_TICK = '1';
    private static final char CONVERTED_CROSS = '0';

    /**
     * Creates a Storage object.
     * It is used reading a file.
     *
     * @param filePath is a relative directory to the file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        this.savedTasks = new ArrayList<>();
    }

    /**
     * Loads the file.
     * A java Scanner object is used to read the .txt file and temporarily
     * stores the data.
     *
     * @throws FileNotFoundException when file is not found from the
     * given file path.
     */
    public static void loadFileContent() throws FileNotFoundException {
        File f = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(f); // create a Scanner using the File as the source

        while (s.hasNext()) {
            String taskSummary = s.nextLine();
            Storage.lineReader(taskSummary);
        }
    }

    /**
     * File line parser.
     * Reads and identifies the tasks specified in the file.
     *
     * @param task is the line String representation of task
     * read from the file.
     */
    public static void lineReader(String task) {
        String type = task.substring(1, 2);
        String description = "";
        String timeDescription = "";
        boolean isDone = false;

        if (task.substring(4, 5).equals("1")) {
            isDone = true;
        }

        if (type.equals(Task.TODO_TASK)) {
            int end = task.length();
            description = task.substring(7, end);
            Todo newTodo = new Todo(description, isDone);
            Storage.savedTasks.add(newTodo);

        } else if (type.equals(Task.EVENT_TASK)) {
            int start = task.indexOf("(");
            int end = task.lastIndexOf(")");
            timeDescription = task.substring(start + 5, end);
            description = task.substring(7, start);
            Event newEvent = new Event(description, timeDescription, isDone);
            Storage.savedTasks.add(newEvent);

        } else if (type.equals(Task.DEADLINE_TASK)) {
            int start = task.indexOf("(");
            int end = task.lastIndexOf(")");
            timeDescription = task.substring(start + 5, end);
            description = task.substring(7, start);
            Deadline newDeadline = new Deadline(description, timeDescription, isDone);
            Storage.savedTasks.add(newDeadline);
        }
    }

    /**
     * Returns a list of Tasks object stored after reading the file.
     *
     * @return a list of Task objects.
     */
    public static List<Task> getSavedTasks() {
        return Storage.savedTasks;
    }

    /**
     * Writes the data to the file.
     *
     * @param tasksToWrite is the list of Tasks.
     * This method will convert Task to their String representation,
     * modify them and write in the file.
     *
     * @throws IOException if file is somehow not found.
     */
    public static void writeToFile(List<Task> tasksToWrite) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        int totalTasks = tasksToWrite.size();

        for (int i = 0; i < totalTasks; i++) {
            Task writeTask = tasksToWrite.get(i);
            String taskString = writeTask.toString();
            String replacement1 = taskString.replace(TICK, CONVERTED_TICK);
            String replacement2 = replacement1.replace(CROSS, CONVERTED_CROSS);
            fw.write(replacement2 + "\n");
        }

        fw.close();
    }
}
