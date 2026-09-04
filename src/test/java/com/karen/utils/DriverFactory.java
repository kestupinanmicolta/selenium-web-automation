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
                if (headless) {
                    ffOpts.addArguments("--headless");
                    ffOpts.addArguments("--width=1920");
                    ffOpts.addArguments("--height=1080");
                }
                return new FirefoxDriver(ffOpts);
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                chromeOpts.addArguments("--disable-blink-features=AutomationControlled");
                chromeOpts.setExperimentalOption("excludeSwitches", java.util.List.of("enable-automation"));
                chromeOpts.setExperimentalOption("useAutomationExtension", false);
                chromeOpts.addArguments("--window-size=1920,1080");
                chromeOpts.addArguments("--start-maximized");
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                    chromeOpts.addArguments("--no-sandbox");
                    chromeOpts.addArguments("--disable-dev-shm-usage");
                    chromeOpts.addArguments("--disable-gpu");
                    chromeOpts.addArguments("--disable-extensions");
                    chromeOpts.addArguments("--disable-infobars");
                    chromeOpts.addArguments("--disable-popup-blocking");
                    chromeOpts.addArguments("--disable-background-networking");
                    chromeOpts.addArguments("--disable-sync");
                    chromeOpts.addArguments("--disable-translate");
                    chromeOpts.addArguments("--metrics-recording-only");
                    chromeOpts.addArguments("--mute-audio");
                    chromeOpts.addArguments("--no-first-run");
                    chromeOpts.addArguments("--disable-backgrounding-occluded-windows");
                    chromeOpts.addArguments("--disable-renderer-backgrounding");
                    chromeOpts.addArguments("--disable-features=TranslateUI");
                    chromeOpts.addArguments("--window-size=1920,1080");
                }
                ChromeDriver driver = new ChromeDriver(chromeOpts);
                driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", java.util.Map.of(
                    "source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
                ));
                return driver;
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
