package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPage {
    private final WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar al carrito")
    public void navigateTo() {
        driver.get("https://practicesoftwaretesting.com/#/cart");
    }

    @Step("Obtener cantidad de itens en el carrito")
    public int getCartItemsCount() {
        return driver.findElements(By.cssSelector("[data-testid='cart-item']")).size();
    }

    @Step("Obtener precio total")
    public String getTotalPrice() {
        return driver.findElement(By.cssSelector("[data-testid='total-price']")).getText();
    }

    @Step("Proceder al checkout")
    public void proceedToCheckout() {
        driver.findElement(By.cssSelector("[data-testid='checkout']")).click();
    }

    @Step("Eliminar item")
    public void removeItem(int index) {
        List<WebElement> buttons = driver.findElements(By.cssSelector("[data-testid='remove-item']"));
        if (index < buttons.size()) {
            buttons.get(index).click();
        }
    }

    @Step("Verificar si el carrito está vacío")
    public boolean isCartEmpty() {
        return driver.findElement(By.cssSelector("[data-testid='empty-cart']")).isDisplayed();
    }
}
