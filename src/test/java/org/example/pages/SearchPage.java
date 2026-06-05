package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchInput = By.id("search");
    private By searchResultItems = By.cssSelector(".search-result a, .search-result div[onclick]");
    private By acceptCookiesButton = By.xpath("//button[contains(., 'Agree and close')]");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url) {
        driver.get(url);
        handleCookieConsent();
    }

    private void handleCookieConsent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement button = shortWait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
            button.click();
        } catch (Exception e) {
            System.out.println("Consent pop-up not present, proceeding.");
        }
    }

    public void enterSearchTerm(String term) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(term);

        // Pause to allow DOM update from asynchronous search queries
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean hasClickableResults() {
        try {
            List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(searchResultItems));

            if (results.isEmpty()) {
                return false;
            }

            // Verify target element is interactable
            WebElement firstResult = results.get(0);
            wait.until(ExpectedConditions.elementToBeClickable(firstResult));

            return true;

        } catch (Exception e) {
            System.out.println("Search result validation failed: " + e.getMessage());
            return false;
        }
    }
}