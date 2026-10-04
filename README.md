# Assignment 3 | Bridge Pattern

Course: ShP-2216 Software Design Patterns, Astana IT University, 2026-2027

## Submission info

| Field | Value |
|---|---|
| Name | Ramazan Alzhanov |
| Group | SE-2523 |
| Topic | C (Reports) |
| Repository | https://github.com/RamazanAljaanov/Assignment_3_Bridge_Pattern_Se-2523_Ramazan |
| Base commit hash | `3fa4c18332f157e901447030d53f38f7eb7751d2` |

## Idea

Two dimensions vary independently:

| Dimension | Question | Classes |
|---|---|---|
| Abstraction | What is reported? | `Report` > `AttendanceReport`, `GradeReport` |
| Implementation | How is it presented? | `Formatter` > `TextFormatter`, `HtmlFormatter`, `MarkdownFormatter` |

`Report` stores a reference to the `Formatter` interface and delegates the
presentation to it. Report types and formats grow separately, and there is no
class for every combination (no `AttendanceHtmlReport` and similar).

```
Report (abstract)  ------ formatter (bridge) ------>  Formatter (interface)
  |-- AttendanceReport                                  |-- TextFormatter
  |-- GradeReport                                       |-- HtmlFormatter
                                                        |-- MarkdownFormatter
```

## Role map

| Role | Class | Source path |
|---|---|---|
| Abstraction | `Report` | `src/Reports/Report.java` |
| A1 | `AttendanceReport` | `src/Reports/AttendanceReport.java` |
| A2 | `GradeReport` | `src/Reports/GradeReport.java` |
| Implementor | `Formatter` | `src/Reports/Formatter.java` |
| I1 | `TextFormatter` | `src/Reports/TextFormatter.java` |
| I2 | `HtmlFormatter` | `src/Reports/HtmlFormatter.java` |
| I3 (extension) | `MarkdownFormatter` | `src/Reports/MarkdownFormatter.java` |
| Client | `Main` | `src/Main.java` |

Where to look in the code:

| What | Location |
|---|---|
| Bridge field | `private Formatter formatter;` in `Report`, set via the constructor |
| `execute()` | `Report`: calls `buildContent()` and passes the result to `formatter.format(id, content)` |
| `setImplementation(Formatter)` | `Report`: replaces the reference at runtime |
| T5 check | `runRuntimeSwitchCheck()` in `Main` |

## Build and run

From the project root (no IDE, no input, no extra dependencies):

```
javac --release 17 -encoding UTF-8 -d out "@sources.txt"
java -cp out Main --demo
```

## Expected results

| Check | Setup | Expected result |
|---|---|---|
| T1 | AttendanceReport + TextFormatter | `TEXT \| attendance-1 \| Attendance: 3/4 attended (75%)` |
| T2 | AttendanceReport + HtmlFormatter | `<article id="attendance-1"><p>Attendance: 3/4 attended (75%)</p></article>` |
| T3 | GradeReport + TextFormatter | `TEXT \| grades-1 \| Grades: [70, 80, 90]; average=80` |
| T4 | GradeReport + HtmlFormatter | `<article id="grades-1"><p>Grades: [70, 80, 90]; average=80</p></article>` |
| T5 | One AttendanceReport: Text, switch to Html, run again | same object (`==`), same ID and data; before = T1 result, after = T2 result |
| T6 | AttendanceReport + MarkdownFormatter | `**attendance-1**: Attendance: 3/4 attended (75%)` |
| T7 | GradeReport + MarkdownFormatter | `**grades-1**: Grades: [70, 80, 90]; average=80` |

The last line of the output is calculated from the checks: `SUMMARY: 7/7 PASS`

## Extension (I3)

The base version (I1 + I2, T1-T5) is the commit whose hash is at the top.
`MarkdownFormatter` was added in the next commit. Inside `src/` only the new
`MarkdownFormatter.java` and `Main.java` changed (T6 and T7). `Report`,
`AttendanceReport`, `GradeReport`, `Formatter`, `TextFormatter` and
`HtmlFormatter` stayed unchanged. The difference is saved in `extension.diff`:

```
git diff BASE_COMMIT HEAD -- src > extension.diff
```

## What I learned

### Bridge pattern

- Bridge separates an abstraction from its implementation so both can change
  independently. The link is composition: an interface-typed field instead of
  a subclass for every combination.
- Bridge vs Adapter: Adapter is applied to existing, incompatible classes to
  make them work together. Bridge is planned at design time so that two
  hierarchies can grow separately.
- Runtime switch: `setImplementation(...)` replaces the formatter on the same
  object. The ID and domain data stay unchanged, only the output format
  changes. T5 proves this with `==`, taking the second reference from a list
  instead of the same variable.
- Independent extension: a new implementation (I3) is added with one new class
  and no changes to the abstractions.

### Git: commit, hash, diff

- A commit is a saved version of the project.
- A hash is its unique 40-character identifier, so a reviewer can open exactly
  the version I submitted.
- A diff shows the differences between two commits. `extension.diff` proves
  that the extension touched only the allowed files.

### Clean Code and testing

- Separated responsibilities: calculation in reports, presentation in
  formatters.
- Meaningful names and small focused methods.
- No duplicated workflow: one `execute()` in the base class.
- Encapsulated state: private fields and a defensive copy of the grades array.
- Each check compares the actual result with a hand-written expected string,
  and PASS or FAIL is calculated, not printed as a constant.

## Files in the submission

`src/`, `sources.txt`, `README.md`, `report.pdf`, `demo-output.txt`, `extension.diff`
