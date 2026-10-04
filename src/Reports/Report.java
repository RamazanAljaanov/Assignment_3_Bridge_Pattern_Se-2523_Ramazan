package Reports;

public abstract class Report {
    private final String id;
    private Formatter formatter;

    protected Report(String id, Formatter formatter) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Report ID cannot be empty");
        }

        if (formatter == null) {
            throw new IllegalArgumentException("Formatter cannot be null");
        }

        this.id = id;
        this.formatter = formatter;
    }

    public String getId() {
        return id;
    }

    public Formatter getImplementation() {
        return formatter;
    }

    public void setImplementation(Formatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("Formatter cannot be null");
        }

        this.formatter = formatter;
    }

    public String execute() {
        String content = buildContent();
        return formatter.format(id, content);
    }

    public abstract String getDomainData();

    protected abstract String buildContent();
}
