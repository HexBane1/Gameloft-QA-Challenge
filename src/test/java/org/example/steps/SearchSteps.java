package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.SearchPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SearchSteps {

    WebDriver driver;
    SearchPage searchPage;

    @Before("@SearchTest")
    public void setup() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--no-sandbox");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
    }

    @Given("the user navigates to the Gameloft search page {string}")
    public void the_user_navigates_to_the_gameloft_search_page(String url) {
        searchPage = new SearchPage(driver);
        searchPage.navigateTo(url);
    }

    @When("the user types {string} into the search bar")
    public void the_user_types_into_the_search_bar(String term) {
        searchPage.enterSearchTerm(term);
    }

    @Then("the search results should populate with at least one clickable game")
    public void the_search_results_should_populate_with_at_least_one_clickable_game() {
        Assertions.assertTrue(searchPage.hasClickableResults(), "Search results failed to render or were not clickable.");
    }

    @After("@SearchTest")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}