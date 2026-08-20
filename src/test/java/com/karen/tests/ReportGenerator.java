package com.karen.tests;

import com.karen.listeners.HtmlReportListener;
import org.testng.annotations.AfterSuite;

public class ReportGenerator {
    @AfterSuite
    public void generateHtmlReport() {
        HtmlReportListener.generateReport();
    }
}
