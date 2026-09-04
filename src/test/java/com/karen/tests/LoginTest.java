package com.karen.tests;

import com.karen.pages.HomePage;
import com.karen.pages.LoginPage;
import com.karen.utils.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Login de usuario")
public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver("chrome");
        if (System.getProperty("headless") == null) {
            driver.manage().window().maximize();
        }
        loginPage = new LoginPage(driver);
        loginPage.navigateTo();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test(groups = {"regression", "smoke"})
    @Story("Inicio de sesion exitoso")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLogin() {
        loginPage.login("customer@practicesoftwaretesting.com", "welcome01");
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                         "Login exitoso: deberia redirigir fuera de la pagina de login");
    }

    @Test(groups = {"regression", "smoke"})
    @Story("Inicio de sesion fallido")
    @Severity(SeverityLevel.NORMAL)
    public void testFailedLogin() {
        loginPage.login("invalid@email.com", "wrongpassword");
        Assert.assertTrue(loginPage.isErrorVisible(), "Deberia mostrar error de credenciales");
    }

    @Test(groups = {"regression"})
    @Story("Login con campos vacios")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyFieldsLogin() {
        loginPage.login("", "");
        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"),
                         "Deberia permanecer en la pagina de login");
    }
}
