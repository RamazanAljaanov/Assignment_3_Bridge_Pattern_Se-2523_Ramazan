package Reports;

import java.util.Arrays;
import java.util.Locale;

public class GradeReport extends Report {
    private final int[] grades;

    public GradeReport(
            String id,
            int[] grades,
            Formatter formatter) {
        super(id, formatter);

        if (grades == null || grades.length == 0) {
            throw new IllegalArgumentException(
                    "Grades cannot be null or empty");
        }

        this.grades = grades.clone();
    }

    public int[] getGrades() {
        return grades.clone();
    }

    @Override
    public String getDomainData() {
        return Arrays.toString(grades);
    }

    @Override
    protected String buildContent() {
        double sum = 0;

        for (int grade : grades) {
            sum += grade;
        }

        double average = sum / grades.length;

        return String.format(
                Locale.ROOT,
                "Grades: %s; average=%.0f",
                Arrays.toString(grades),
                average);
    }
}