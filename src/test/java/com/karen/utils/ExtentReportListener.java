package com.karen.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtentReportListener implements ITestListener {
    private ExtentReports extent;
    private ExtentTest test;
    private static final String REPORT_PATH = "public/extent-report.html";
    private static final String SCREENSHOT_DIR = "public/screenshots";

    @Override
    public void onStart(ITestContext context) {
        new File(SCREENSHOT_DIR).mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
        spark.config().setDocumentTitle("Selenium Test Report");
        spark.config().setReportName("Karen Paola Estupinan - QA Automation");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("utf-8");
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

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
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        String description = method.getAnnotation(org.testng.annotations.Test.class) != null
                ? method.getAnnotation(org.testng.annotations.Test.class).description()
                : "";

        test = extent.createTest(result.getMethod().getMethodName(),
                description.isEmpty() ? result.getMethod().getMethodName() : description);
        test.assignCategory(result.getMethod().getRealClass().getSimpleName());

        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            test.assignCategory(groups);
        }

        test.log(Status.INFO, MarkupHelper.createLabel("Inicio del test", ExtentColor.BLUE));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, MarkupHelper.createLabel(
                "Test completado exitosamente", ExtentColor.GREEN));
        test.log(Status.INFO, "Duracion: " + getDuration(result));
        attachScreenshot(result, "SUCCESS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, MarkupHelper.createLabel(
                "Test fallido", ExtentColor.RED));
        test.log(Status.FAIL, "Error: " + result.getThrowable().getMessage());
        test.log(Status.INFO, "Duracion: " + getDuration(result));
        test.fail(result.getThrowable());
        attachScreenshot(result, "FAILURE");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, MarkupHelper.createLabel(
                "Test omitido", ExtentColor.ORANGE));
        if (result.getThrowable() != null) {
            test.log(Status.SKIP, "Razon: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String getDuration(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        long seconds = (duration / 1000) % 60;
        long millis = duration % 1000;
        return String.format("%02d:%02d:%02d.%03d",
                duration / 3600000, (duration / 60000) % 60, seconds, millis);
    }

    private void attachScreenshot(ITestResult result, String status) {
        try {
            Object currentClass = result.getInstance();
            WebDriver driver = null;

            if (currentClass instanceof com.karen.tests.LoginTest loginTest) {
                driver = loginTest.getDriver();
            } else if (currentClass instanceof com.karen.tests.SearchTest searchTest) {
                driver = searchTest.getDriver();
            } else if (currentClass instanceof com.karen.tests.CartTest cartTest) {
                driver = cartTest.getDriver();
            } else if (currentClass instanceof com.karen.tests.CheckoutTest checkoutTest) {
                driver = checkoutTest.getDriver();
            }

            if (driver != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                String testName = result.getMethod().getMethodName();
                String fileName = testName + "_" + status + "_" + timestamp + ".png";
                String filePath = SCREENSHOT_DIR + "/" + fileName;
                String absolutePath = new File(filePath).getAbsolutePath();

                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                java.nio.file.Files.copy(screenshot.toPath(),
                        new File(absolutePath).toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                test.addScreenCaptureFromPath(absolutePath,
                        "Screenshot - " + status + " - " + testName);
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "No se pudo capturar screenshot: " + e.getMessage());
        }
    }
}
