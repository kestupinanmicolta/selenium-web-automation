package com.karen.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPage {
    private final WebDriver driver;

    @FindBy(dataTestId = "cart-item")
    private List<WebElement> cartItems;

    @FindBy(dataTestId = "total-price")
    private WebElement totalPrice;

    @FindBy(dataTestId = "checkout")
    private WebElement checkoutButton;

    @FindBy(dataTestId = "remove-item")
    private List<WebElement> removeButtons;

    @FindBy(dataTestId = "empty-cart")
    private WebElement emptyCartMessage;

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
        return cartItems.size();
    }

    @Step("Obtener precio total")
    public String getTotalPrice() {
        return totalPrice.getText();
    }

    @Step("Eliminar item índice: {0}")
    public void removeItem(int index) {
        removeButtons.get(index).click();
    }

    @Step("Proceder al checkout")
    public void proceedToCheckout() {
        checkoutButton.click();
    }

    @Step("Verificar si el carrito está vacío")
    public boolean isCartEmpty() {
        return emptyCartMessage.isDisplayed();
    }
}
