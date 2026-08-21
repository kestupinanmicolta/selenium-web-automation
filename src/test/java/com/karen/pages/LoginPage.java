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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='login-submit']"))).click();
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
