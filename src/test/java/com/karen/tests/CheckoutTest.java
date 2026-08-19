package com.karen.tests;

import com.karen.pages.CartPage;
import com.karen.pages.HomePage;
import com.karen.pages.ProductPage;
import com.karen.utils.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Flujo de checkout")
public class CheckoutTest {
    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        driver = DriverFactory.getDriver(browser);
        if (System.getProperty("headless") == null) { driver.manage().window().maximize(); }
        homePage = new HomePage(driver);
        homePage.navigateTo();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test(groups = {"regression"})
    @Story("Proceder al checkout")
    @Severity(SeverityLevel.CRITICAL)
    public void testProceedToCheckout() {
        homePage.searchProduct("hammer");
        homePage.clickProduct(0);
        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart(1);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateTo();
        cartPage.proceedToCheckout();

        Assert.assertTrue(driver.getCurrentUrl().contains("checkout") || 
                         driver.getCurrentUrl().contains("login"),
                         "Debería navegar a checkout o login");
    }
}
