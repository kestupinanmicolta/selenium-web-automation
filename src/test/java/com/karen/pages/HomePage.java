package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    private final WebDriver driver;

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
        WebElement input = driver.findElement(By.cssSelector("[data-testid='search-query']"));
        input.clear();
        input.sendKeys(query);
        driver.findElement(By.cssSelector("[data-testid='search-submit']")).click();
    }

    @Step("Obtener cantidad de productos")
    public int getProductCount() {
        return driver.findElements(By.cssSelector("[data-testid='product-card']")).size();
    }

    @Step("Hacer clic en producto índice: {0}")
    public void clickProduct(int index) {
        List<WebElement> products = driver.findElements(By.cssSelector("[data-testid='product-card']"));
        if (index < products.size()) {
            products.get(index).click();
        }
    }

    @Step("Obtener conteo del carrito")
    public String getCartCount() {
        return driver.findElement(By.cssSelector("[data-testid='cart-count']")).getText();
    }
}
