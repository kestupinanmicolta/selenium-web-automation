package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar a la página de login")
    public void navigateTo() {
        driver.get("https://practicesoftwaretesting.com/#/login");
    }

    @Step("Iniciar sesión con email: {0}")
    public void login(String email, String password) {
        WebElement emailInput = driver.findElement(By.cssSelector("[data-testid='email']"));
        emailInput.clear();
        emailInput.sendKeys(email);
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-testid='password']"));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        driver.findElement(By.cssSelector("[data-testid='login-submit']")).click();
    }

    @Step("Obtener mensaje de error")
    public String getErrorMessage() {
        return driver.findElement(By.cssSelector("[data-testid='login-error']")).getText();
    }

    @Step("Verificar si el error es visible")
    public boolean isErrorVisible() {
        return driver.findElement(By.cssSelector("[data-testid='login-error']")).isDisplayed();
    }
}
