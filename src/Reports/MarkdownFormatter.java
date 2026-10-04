package Reports;

public class MarkdownFormatter implements Formatter {
    @Override
    public String format(String reportId, String content) {
        return "**" + reportId + "**: " + content;
    }
}
