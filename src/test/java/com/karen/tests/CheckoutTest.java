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

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver("chrome");
        if (System.getProperty("headless") == null) { driver.manage().window().maximize(); }
        homePage = new HomePage(driver);
        homePage.navigateTo();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test(groups = {"regression"})
    @Story("Agregar producto y verificar carrito")
    @Severity(SeverityLevel.CRITICAL)
    public void testProceedToCheckout() {
        homePage.searchProduct("hammer");
        homePage.clickProduct(0);
        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart(1);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateTo();
        Assert.assertTrue(cartPage.hasItems(), "El carrito deberia contener el producto agregado");
    }
}
