package com.karen.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY = 2; // Reintentar hasta 2 veces (3 intentos total)

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println("Reintentando test: " + iTestResult.getName() + " - Intento " + retryCount);
            // Cleanup driver before retry
            DriverFactory.quitDriver();
            return true;
        }
        return false;
    }
}
