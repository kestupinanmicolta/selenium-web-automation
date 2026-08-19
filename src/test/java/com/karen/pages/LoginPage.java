package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private final WebDriver driver;

    @FindBy(dataTestId = "email")
    private WebElement emailInput;

    @FindBy(dataTestId = "password")
    private WebElement passwordInput;

    @FindBy(dataTestId = "login-submit")
    private WebElement loginButton;

    @FindBy(dataTestId = "login-error")
    private WebElement errorMessage;

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
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    @Step("Obtener mensaje de error")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Verificar si el error es visible")
    public boolean isErrorVisible() {
        return errorMessage.isDisplayed();
    }
}
