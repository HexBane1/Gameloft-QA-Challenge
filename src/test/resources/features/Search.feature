@SearchTest
  Feature: Client-Side Search Functionality
    As a user,
    I want to search for specific game genres or titles
    So that I can quickly find and play relevant games.

  Scenario: Verify the search engine returns clickable game results
    Given the user navigates to the Gameloft search page "https://play.ludigames.com/search.html"
    When the user types "puzzle" into the search bar
    Then the search results should populate with at least one clickable game