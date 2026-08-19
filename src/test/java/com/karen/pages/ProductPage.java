package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {
    private final WebDriver driver;

    @FindBy(dataTestId = "product-name")
    private WebElement productName;

    @FindBy(dataTestId = "product-price")
    private WebElement productPrice;

    @FindBy(dataTestId = "add-to-cart")
    private WebElement addToCartButton;

    @FindBy(dataTestId = "quantity")
    private WebElement quantityInput;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Obtener nombre del producto")
    public String getProductName() {
        return productName.getText();
    }

    @Step("Obtener precio del producto")
    public String getProductPrice() {
        return productPrice.getText();
    }

    @Step("Agregar al carrito cantidad: {0}")
    public void addToCart(int quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        addToCartButton.click();
    }
}
