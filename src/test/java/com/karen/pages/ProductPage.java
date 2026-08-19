package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductPage {
    private final WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Obtener nombre del producto")
    public String getProductName() {
        return driver.findElement(By.cssSelector("[data-testid='product-name']")).getText();
    }

    @Step("Obtener precio del producto")
    public String getProductPrice() {
        return driver.findElement(By.cssSelector("[data-testid='product-price']")).getText();
    }

    @Step("Agregar al carrito cantidad: {0}")
    public void addToCart(int quantity) {
        WebElement qty = driver.findElement(By.cssSelector("[data-testid='quantity']"));
        qty.clear();
        qty.sendKeys(String.valueOf(quantity));
        driver.findElement(By.cssSelector("[data-testid='add-to-cart']")).click();
    }
}
