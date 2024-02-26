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

import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class BaseTestMaster {
    protected static MasterTestReporter testReporter;
    protected static String configurationDirectoryPrefix;
    protected static String testResultsDirectory = "test_results/";
    protected static String testResultsFile = null;
    protected boolean currentTestResult;
    protected int currentTestPoints;
    protected String currentTestName;
    protected boolean debugging;

    protected int extraCreditPoints;

    public BaseTestMaster() {
        testReporter = MasterTestReporter.getInstance();
    }

    protected void startTest(String testName, int points) {
        currentTestResult = false;
        currentTestName = testName;
        currentTestPoints = points;
    }

    protected void markTestPassed() {
        currentTestResult = true;
//        currentTestPoints = 0;
    }

    protected void startExtraCreditTest(String testName, int points) {
        currentTestResult = false;
        currentTestName = testName;
        currentTestPoints = 0;
        extraCreditPoints = points;
    }

    protected void markExtraCreditTestPassed() {
        currentTestResult = true;
        currentTestPoints = extraCreditPoints;
    }

    @AfterEach
    protected void reportResults() {
        testReporter.reportTestResults(currentTestName, currentTestPoints, currentTestResult);
    }

    @AfterAll
    protected static void printResults() {
        String s = testReporter.endTestGroup().toString();
        System.out.println(s);
        writeResultsToOutputFile(s);
    }

    protected static void writeResultsToOutputFile(String s) {
        if (testResultsFile == null) return;
        try {
            FileWriter fileWriter = new FileWriter(testResultsDirectory + testResultsFile,true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(s);
            bw.flush();
            bw.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Used for debugging the tests as I create them.
     * @param predicate
     */
    protected void debugTest(boolean predicate) {
        if (debugging) {
            assertTrue(predicate);
        }
    }
}
