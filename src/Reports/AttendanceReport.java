package Reports;

public class AttendanceReport extends Report {
    private final int attendedSessions;
    private final int totalSessions;

    public AttendanceReport(
            String id,
            int attendedSessions,
            int totalSessions,
            Formatter formatter) {
        super(id, formatter);

        if (totalSessions <= 0) {
            throw new IllegalArgumentException(
                    "Total sessions must be greater than zero");
        }

        if (attendedSessions < 0 || attendedSessions > totalSessions) {
            throw new IllegalArgumentException(
                    "Attended sessions must be between zero and total sessions");
        }

        this.attendedSessions = attendedSessions;
        this.totalSessions = totalSessions;
    }

    public int getAttendedSessions() {
        return attendedSessions;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    @Override
    public String getDomainData() {
        return attendedSessions + "/" + totalSessions;
    }

    @Override
    protected String buildContent() {
        int percentage = attendedSessions * 100 / totalSessions;

        return "Attendance: "
                + attendedSessions
                + "/"
                + totalSessions
                + " attended ("
                + percentage
                + "%)";
    }
}
