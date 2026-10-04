import Reports.AttendanceReport;
import Reports.Formatter;
import Reports.GradeReport;
import Reports.HtmlFormatter;
import Reports.Report;
import Reports.TextFormatter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String EXPECTED_ATTENDANCE_TEXT =
            "TEXT | attendance-1 | Attendance: 3/4 attended (75%)";
    private static final String EXPECTED_ATTENDANCE_HTML =
            "<article id=\"attendance-1\"><p>"
                    + "Attendance: 3/4 attended (75%)"
                    + "</p></article>";
    private static final String EXPECTED_GRADES_TEXT =
            "TEXT | grades-1 | Grades: [70, 80, 90]; average=80";
    private static final String EXPECTED_GRADES_HTML =
            "<article id=\"grades-1\"><p>"
                    + "Grades: [70, 80, 90]; average=80"
                    + "</p></article>";

    private static int passedChecks;
    private static int totalChecks;

    public static void main(String[] args) {
        if (args.length != 1 || !"--demo".equals(args[0])) {
            System.out.println("Usage: java -cp out Main --demo");
            return;
        }

        runDemo();
    }

    private static void runDemo() {
        passedChecks = 0;
        totalChecks = 0;

        check(
                "T1",
                "AttendanceReport + TextFormatter",
                new AttendanceReport(
                        "attendance-1", 3, 4, new TextFormatter()).execute(),
                EXPECTED_ATTENDANCE_TEXT);

        check(
                "T2",
                "AttendanceReport + HtmlFormatter",
                new AttendanceReport(
                        "attendance-1", 3, 4, new HtmlFormatter()).execute(),
                EXPECTED_ATTENDANCE_HTML);

        check(
                "T3",
                "GradeReport + TextFormatter",
                new GradeReport(
                        "grades-1", new int[] {70, 80, 90},
                        new TextFormatter()).execute(),
                EXPECTED_GRADES_TEXT);

        check(
                "T4",
                "GradeReport + HtmlFormatter",
                new GradeReport(
                        "grades-1", new int[] {70, 80, 90},
                        new HtmlFormatter()).execute(),
                EXPECTED_GRADES_HTML);

        runRuntimeSwitchCheck();

        System.out.println(
                "SUMMARY: " + passedChecks + "/" + totalChecks + " PASS");
    }

    private static void runRuntimeSwitchCheck() {
        Formatter originalFormatter = new TextFormatter();
        Formatter replacementFormatter = new HtmlFormatter();

        AttendanceReport created = new AttendanceReport(
                "attendance-1", 3, 4, originalFormatter);

        // The object is stored in a list, so the reference used after the
        // switch comes from the storage, not from the same local variable.
        List<Report> storage = new ArrayList<>();
        storage.add(created);

        Report originalReference = created;
        String originalId = created.getId();
        String originalDomainData = created.getDomainData();

        boolean usesOriginalFormatter =
                originalReference.getImplementation() == originalFormatter;
        String before = originalReference.execute();

        storage.get(0).setImplementation(replacementFormatter);

        Report referenceAfterSwitch = storage.get(0);
        String after = referenceAfterSwitch.execute();

        boolean sameObject = originalReference == referenceAfterSwitch;
        boolean stateUnchanged =
                originalId.equals(referenceAfterSwitch.getId())
                        && originalDomainData.equals(
                        referenceAfterSwitch.getDomainData());
        boolean usesReplacementFormatter =
                referenceAfterSwitch.getImplementation()
                        == replacementFormatter;
        boolean correctBefore = before.equals(EXPECTED_ATTENDANCE_TEXT);
        boolean correctAfter = after.equals(EXPECTED_ATTENDANCE_HTML);

        boolean passed = usesOriginalFormatter
                && sameObject
                && stateUnchanged
                && usesReplacementFormatter
                && correctBefore
                && correctAfter;

        totalChecks++;
        if (passed) {
            passedChecks++;
        }

        System.out.println(
                "T5 " + (passed ? "PASS" : "FAIL")
                        + " | sameObject=" + sameObject
                        + " | stateUnchanged=" + stateUnchanged);
        System.out.println(
                "   before=" + before + " | after=" + after);

        if (!passed) {
            System.out.println(
                    "   expectedBefore=" + EXPECTED_ATTENDANCE_TEXT
                            + " | expectedAfter=" + EXPECTED_ATTENDANCE_HTML
                            + " | expectedSameObject=true"
                            + " | expectedStateUnchanged=true");
        }
    }

    private static void check(
            String testId,
            String participants,
            String actual,
            String expected) {
        boolean passed = actual.equals(expected);

        totalChecks++;
        if (passed) {
            passedChecks++;
        }

        System.out.println(
                testId + " " + (passed ? "PASS" : "FAIL")
                        + " | " + participants
                        + " | result=" + actual);

        if (!passed) {
            System.out.println("   expected=" + expected);
        }
    }
}