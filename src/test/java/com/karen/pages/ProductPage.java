package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    @Step("Obtener nombre del producto")
    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='product-name']"))).getText();
    }

    @Step("Obtener precio del producto")
    public String getProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='unit-price']"))).getText();
    }

    @Step("Agregar al carrito cantidad: {0}")
    public void addToCart(int quantity) {
        WebElement qty = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='quantity']")));
        qty.clear();
        qty.sendKeys(String.valueOf(quantity));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
    }
}
