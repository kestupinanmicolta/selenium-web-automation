package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    private final WebDriver driver;

    @FindBy(dataTestId = "search-query")
    private WebElement searchInput;

    @FindBy(dataTestId = "search-submit")
    private WebElement searchButton;

    @FindBy(dataTestId = "product-card")
    private List<WebElement> productCards;

    @FindBy(dataTestId = "nav-cart")
    private WebElement cartIcon;

    @FindBy(dataTestId = "cart-count")
    private WebElement cartCount;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar a la página principal")
    public void navigateTo() {
        driver.get("https://practicesoftwaretesting.com");
    }

    @Step("Buscar producto: {0}")
    public void searchProduct(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
        searchButton.click();
    }

    @Step("Obtener cantidad de productos")
    public int getProductCount() {
        return productCards.size();
    }

    @Step("Hacer clic en producto índice: {0}")
    public void clickProduct(int index) {
        productCards.get(index).click();
    }

    @Step("Obtener conteo del carrito")
    public String getCartCount() {
        return cartCount.getText();
    }
}
