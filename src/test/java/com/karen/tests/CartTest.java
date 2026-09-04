package com.karen.tests;

import com.karen.pages.CartPage;
import com.karen.pages.HomePage;
import com.karen.pages.ProductPage;
import com.karen.utils.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Gestion del carrito")
public class CartTest {
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
    @Story("Agregar producto al carrito")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductToCart() {
        homePage.searchProduct("hammer");
        homePage.clickProduct(0);
        ProductPage productPage = new ProductPage(driver);
        String productName = productPage.getProductName();
        productPage.addToCart(1);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateTo();
        Assert.assertTrue(cartPage.hasItems(), "El carrito deberia tener items");
        Assert.assertTrue(cartPage.getCartItemsCount() > 0, "La cantidad del carrito deberia ser mayor a 0");
    }
}
