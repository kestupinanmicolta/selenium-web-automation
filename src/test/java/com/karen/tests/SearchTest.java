package com.karen.tests;

import com.karen.pages.HomePage;
import com.karen.pages.ProductPage;
import com.karen.utils.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Búsqueda de productos")
public class SearchTest {
    private WebDriver driver;
    private HomePage homePage;

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

    @Test(groups = {"regression", "smoke"})
    @Story("Buscar producto existente")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchExistingProduct() {
        homePage.searchProduct("hammer");
        Assert.assertTrue(homePage.getProductCount() > 0, "Debería encontrar productos");
    }

    @Test(groups = {"regression"})
    @Story("Buscar producto inexistente")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchNonExistentProduct() {
        homePage.searchProduct("xyznonexistent");
        Assert.assertEquals(homePage.getProductCount(), 0, "No debería encontrar productos");
    }

    @Test(groups = {"regression"})
    @Story("Ver detalle de producto")
    @Severity(SeverityLevel.CRITICAL)
    public void testViewProductDetail() {
        homePage.clickProduct(0);
        ProductPage productPage = new ProductPage(driver);
        Assert.assertNotNull(productPage.getProductName(), "El nombre del producto no debería ser nulo");
        Assert.assertNotNull(productPage.getProductPrice(), "El precio del producto no debería ser nulo");
    }
}
