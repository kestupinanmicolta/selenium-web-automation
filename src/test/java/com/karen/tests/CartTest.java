package com.karen.tests;

import com.karen.pages.CartPage;
import com.karen.pages.HomePage;
import com.karen.pages.ProductPage;
import com.karen.utils.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Gestión del carrito")
public class CartTest {
    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        driver = DriverFactory.getDriver(browser);
        driver.manage().window().maximize();
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
        productPage.addToCart(1);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateTo();
        Assert.assertEquals(cartPage.getCartItemsCount(), 1, "El carrito debería tener 1 item");
    }

    @Test(groups = {"regression"})
    @Story("Eliminar producto del carrito")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveProductFromCart() {
        homePage.searchProduct("hammer");
        homePage.clickProduct(0);
        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart(1);

        CartPage cartPage = new CartPage(driver);
        cartPage.navigateTo();
        cartPage.removeItem(0);
        Assert.assertTrue(cartPage.isCartEmpty(), "El carrito debería estar vacío");
    }
}
