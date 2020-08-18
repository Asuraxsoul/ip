public class Event extends Task {
    private String timeDescription;

    public Event(String description, String timeDescription) {
        super(description, "E");
        this.timeDescription = timeDescription;
    }

    @Override
    public String toString() {
        return "[" + getType() + "]" + "[" + getStatusIcon() + "] " + description
                + "(at:" + this.timeDescription + ")";
    }
}
