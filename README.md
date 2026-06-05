# Gameloft-QA-Challenge

## Overview
Mix of API and UI tests for the gameloft QA automation coding challenge, using javascript in Postman for the API tests and Java Selenium for the UI tests.

### Prerequisites

Ensure [Git](https://git-scm.com/install/) is installed on your device.

#### API Tests:

**1. Ensure [Node.js](https://nodejs.org/en) is installed on the machine**

**2. Install Newman:**
```bash
npm install -g newman
```

#### UI Tests:

**1. Ensure the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (version 17 or higher) is installed**
```bash
java -version
```

**2. Ensure Google Chrome is installed on the machine.**

*(Note: You do not need to manually download ChromeDriver. The framework utilizes WebDriverManager to automatically detect your local browser version and download the corresponding driver).*

**3. An IDE with built-in Maven support (Ex: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows), Eclipse) is recommended for execution.** 

## Run Tests

**Firstly, clone the GitHub repository**
```bash
git clone https://github.com/HexBane1/Gameloft-QA-Challenge
```

### API tests:

**1. Enter the API tests folder**
```bash
cd Gameloft-QA-Challenge/api-tests
```

**2. Run the tests**
```bash
newman run Gameloft_API_Tests.postman_collection.json
```

All tests should fire successfully, providing the following table:
<img width="629" height="364" alt="image" src="https://github.com/user-attachments/assets/86ba036b-2ea7-477b-a51b-2d928d129159" />

### UI Tests:

**Option A: Via Command Line**
Open your terminal, navigate to the root `Gameloft_QA_Challenge_UI` folder, and execute the Maven test phase:
```bash
mvn clean test
```

**Option B: Via IDE (IntelliJ, Eclipse)**

Because this project uses Maven for dependency management, the easiest way to execute the suite is directly through your IDE:

1. Open the `Gameloft_QA_Challenge_UI` folder in **IntelliJ IDEA** (or your preferred IDE).

2. Allow the IDE to automatically download the Maven dependencies listed in the `pom.xml`.

3. Navigate to the `src/test/java/org/example/runners/` directory.

4. Right-click the `TestRunner.java` file and select **Run 'TestRunner'**.

Every test should pass, providing the following result:
<img width="695" height="218" alt="image" src="https://github.com/user-attachments/assets/e7a32e3e-fa4f-4e16-832a-4477eb7a1876" />


## Test decisions/thought process

### API Tests:

**1. Core_Pages_Status_Response_Time_and_Structure**

First, I created a smoke test for all the main pages of the website, with basic tests like: a 200 OK status code check, a response time under 800ms, and an HTML structure check verifying that the returned page isn't blank.

I ran into some trouble initially, as I was initiating the tests for all pages using a forEach loop. This caused the response times to scale linearly from 200-300ms all the way up to 2000ms, with the time increasing depending on the network I was connected to. I managed to solve this by iterating sequentially through the list of URLs and firing the requests one at a time.

**2. Search_Function_Data_Integrity**

For the second test, I started by looking at the network requests made through Inspect Element. A particular request stood out: gamelist.php. Initially, I thought that the search function probably worked by requesting a list of games each time I hit search, but it actually loads the whole database upon entering the website, and only matches the string I search for to a result in the local list.

The entire database is split into different keys: one related to the "top 10" list of games that pops up when you search for a game that doesn't exist, a huge "master database" containing the majority of the games on the site, and three other keys that are probably related to the news page or the featured games tab.

After this discovery, I created the basic smoke tests, only increasing the response time threshold to 1000ms due to the heavier load of the request. I also added two other tests: one verifying that the response returns an object containing these specific keys, and another to verify that all games contained inside the keys have the valid required structure (name, product_id, isPortrait, product key, assets, banners).

### UI Tests:

I have opted for Behaviour Driven Development and used Cucumber feature files written in Gherkin to ease the reviewing process, but also because this is how I was taught to handle tests in Software Development in France on my Erasmus+ mobility.

#### General UI Architecture
Before detailing the individual scenarios, there are two global mechanisms implemented across the entire UI suite:
* **The Page Object Model (POM):** Step definitions (Ex: `GameSteps.java`) are built based on the page classes (Ex:`GamePage.java`), turning the backend Java functions into interpretable `Given/When/Then` steps. This architecture is used for every single UI test.
* **Universal Cookie Handling:** I built a dynamic function to handle the cookie consent banner that will pop up upon loading all pages of the website for the first time. This is used for every single UI test to ensure the browser view is not obstructed.

**1. GameLaunch.feature**

```gherkin
Feature: Game Launch Infrastructure
  As a player,
  I want the game rendering engine to boot successfully
  So that I can play the game without crashes.

  Scenario: Verify the Pixelcraft Parkour HTML5 canvas loads successfully
    Given the Gameloft browser environment is ready
    When the user navigates to the game with URL "https://play.ludigames.com/game.html?pID=8070"
    Then the game rendering canvas should be visible and active
```

For this first UI test, we try to load the site's most popular game based on the invalid search top 10 list and the .php game list file that I found through inspect element. Also through inspect element, I found 2 nested iframe wrappers for the game frame, we verify if these load and if the game engine canvas dimensions are appropriate (valid width and height > 0).

**2. Monetization.feature**

```gherkin
  Feature: Monetization Infrastructure
    As a business stakeholder,
    I want to ensure the advertisement containers render correctly
    So that the portal can successfully display monetized content.

  Scenario: Verify the first ad container loads on the homepage
    Given the Gameloft browser environment is initialized
    When the user navigates to the LudiGames homepage at "https://play.ludigames.com"
    Then the primary advertisement should be successfully rendered
```

This test verifies if the first ad banner is displayed upon launching the home page. Because third-party ad networks can be heavily blocked or delayed by the browser, the script specifically targets the `google_ads_iframe`. The iframe should return a width & height > 0.

**3. Search.feature**

```gherkin
  Feature: Client-Side Search Functionality
    As a user,
    I want to search for specific game genres or titles
    So that I can quickly find and play relevant games.

  Scenario: Verify the search engine returns clickable game results
    Given the user navigates to the Gameloft search page "https://play.ludigames.com/search.html"
    When the user types "puzzle" into the search bar
    Then the search results should populate with at least one clickable game
```

To verify if the client-side search functionality is working, we open an instance of the search page, locate the id for the search input box, enter it and try to search for the word "puzzle". The site sends an asynchronous JavaScript request, without reloading the whole site. We then wait to check if the resulted elements are interactable.
