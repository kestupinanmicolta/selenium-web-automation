package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar al carrito")
    public void navigateTo() {
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ngx-toastr")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
    }

    @Step("Obtener cantidad de itens en el carrito")
    public int getCartItemsCount() {
        try {
            String qty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))).getText();
            return Integer.parseInt(qty.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Verificar si el carrito tiene items")
    public boolean hasItems() {
        try {
            String qty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))).getText();
            return !qty.trim().equals("0") && !qty.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
