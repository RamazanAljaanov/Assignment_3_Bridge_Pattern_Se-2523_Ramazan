package Reports;

public class TextFormatter implements Formatter {
    @Override
    public String format(String reportId, String content) {
        return "TEXT | " + reportId + " | " + content;
    }
}
