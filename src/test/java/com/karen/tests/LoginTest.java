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

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        driver = DriverFactory.getDriver(browser);
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
    @Story("Inicio de sesión exitoso")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLogin() {
        loginPage.login("test@example.com", "password123");
        Assert.assertFalse(loginPage.isErrorVisible(), "No debería mostrar error");
    }

    @Test(groups = {"regression", "smoke"})
    @Story("Inicio de sesión fallido")
    @Severity(SeverityLevel.NORMAL)
    public void testFailedLogin() {
        loginPage.login("invalid@email.com", "wrongpassword");
        Assert.assertTrue(loginPage.isErrorVisible(), "Debería mostrar error de credenciales");
    }

    @Test(groups = {"regression"})
    @Story("Login con campos vacíos")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyFieldsLogin() {
        loginPage.login("", "");
        Assert.assertTrue(loginPage.isErrorVisible(), "Debería mostrar error con campos vacíos");
    }
}
