package com.karen.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class ExtentReportListener implements ITestListener {
    private ExtentReports extent;
    private ExtentTest test;
    private static final String REPORT_PATH = "public/extent-report.html";

    @Override
    public void onStart(ITestContext context) {
        new File("public").mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
        spark.config().setDocumentTitle("Selenium Test Report");
        spark.config().setReportName("Karen Paola Estupinan - QA Automation");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Karen Paola Estupinan Micolta");
        extent.setSystemInfo("Application", "Practice Software Testing");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Java", System.getProperty("java.version"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getRealClass().getSimpleName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
