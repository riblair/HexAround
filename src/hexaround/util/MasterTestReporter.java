/*
 * Copyright (c) 2023. Gary F. Pollice
 *
 * This files was developed for personal or educational purposes. All rights reserved.
 *
 *  You may use this software for any purpose except as follows:
 *  1) You may not submit this file without modification for any educational assignment.
 *  2) You may not remove this copyright, even if you have modified this file.
 */

package hexaround.util;

public class MasterTestReporter {
    /*****************************************************************
     *  Variables that are used for the reports.
     *****************************************************************/
    private int pointsPossible;
    private int pointsDeducted;
    private StringBuilder currentReport;
    private String currentTestGroup;


    private static final MasterTestReporter instance = new MasterTestReporter();
    public static MasterTestReporter getInstance() { return instance; }

    private MasterTestReporter()
    {
        currentTestGroup = "Unknown";
        currentReport = null;
    }

    public void startNewTestGroup(String name, int points) {
        currentReport = new StringBuilder();
        currentTestGroup = name;
        pointsPossible = points;
        pointsDeducted = 0;
        currentReport.append("\n---------- " + currentTestGroup + " (" + pointsPossible + ") ----------\n");
    }

    public StringBuilder endTestGroup() {
        String groupSummary = String.format("\nGroup: %s, Possible: %d, Adjustment: %d, Score: %d\n\n",
                currentTestGroup, pointsPossible, -pointsDeducted, (pointsPossible - pointsDeducted));
        currentReport.append(groupSummary);
        return currentReport;
    }

    public void reportTestResults(String name, int pointsToDeduct, boolean pass) {
        currentReport.append("\t" + name + ": ");
        currentReport.append(pass ? "PASS " : "FAIL ");
        if (!pass) {
            currentReport.append(String.format("-%d",pointsToDeduct));
            deductPoints(pointsToDeduct);
        } else {
            currentReport.append(String.format("+%d",pointsToDeduct));
        }
        currentReport.append("\n");
    }

    /*
     * The next two methods are used before and after a test. The second one only
     * triggers if the test has passed.
     */
    public void deductPoints(int points) {
        pointsDeducted += points;
    }

    public void addPoints(int points) {
        pointsDeducted -= points;
    }
}
