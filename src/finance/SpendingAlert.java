package finance;

public class SpendingAlert {

    private String level;
    private String message;

    public SpendingAlert(String level, String message) {
        this.level = level;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return level + ": " + message;
    }
}
