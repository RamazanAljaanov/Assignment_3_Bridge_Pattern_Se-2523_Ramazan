package Reports;

public class HtmlFormatter implements Formatter {
    @Override
    public String format(String reportId, String content) {
        return "<article id=\""
                + reportId
                + "\"><p>"
                + content
                + "</p></article>";
    }
}
