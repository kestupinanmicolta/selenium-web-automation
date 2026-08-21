package com.karen.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    public static WebDriver getDriver(String browser) {
        if (driverPool.get() == null) {
            driverPool.set(createDriver(browser));
        }
        return driverPool.get();
    }

    private static boolean isHeadless() {
        return System.getProperty("headless") != null || System.getenv("CI") != null;
    }

    private static WebDriver createDriver(String browser) {
        boolean headless = isHeadless();
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (headless) ffOpts.addArguments("--headless");
                return new FirefoxDriver(ffOpts);
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                    chromeOpts.addArguments("--no-sandbox");
                    chromeOpts.addArguments("--disable-dev-shm-usage");
                    chromeOpts.addArguments("--disable-gpu");
                    chromeOpts.addArguments("--window-size=1920,1080");
                }
                return new ChromeDriver(chromeOpts);
        }
    }

    public static WebDriverWait getWait(WebDriver driver, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public static void quitDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
