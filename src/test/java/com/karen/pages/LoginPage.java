package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        int timeout = System.getenv("CI") != null ? 40 : 20;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar a la pagina de login")
    public void navigateTo() {
        driver.get("https://practicesoftwaretesting.com/auth/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='email']")));
    }

    @Step("Iniciar sesion con email: {0}")
    public void login(String email, String password) {
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='email']")));
        emailInput.clear();
        emailInput.sendKeys(email);
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='password']")));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-submit'], input[type='submit'], button[type='submit']")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
    }

    @Step("Verificar que login fue exitoso (redirige fuera de login)")
    public boolean isLoginSuccessful() {
        try {
            int timeout = System.getenv("CI") != null ? 40 : 20;
            new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(d -> {
                    String url = d.getCurrentUrl();
                    return !url.contains("/auth/login") && !url.contains("/login");
                });
            return true;
        } catch (Exception e) {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Login fallido - URL actual: " + currentUrl);
            try {
                String pageSource = driver.getPageSource();
                if (pageSource.contains("Invalid") || pageSource.contains("error") || pageSource.contains("incorrect")) {
                    System.out.println("Posible mensaje de error en pagina");
                }
            } catch (Exception ex) {
                // ignore
            }
            return false;
        }
    }

    @Step("Obtener mensaje de error")
    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='login-error']"))).getText();
    }

    @Step("Verificar si el error es visible")
    public boolean isErrorVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='login-error']"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
