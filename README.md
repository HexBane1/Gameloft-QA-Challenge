# Gameloft-QA-Challenge

## Overview
Mix of API and UI tests for the gameloft QA automation coding challenge, using javascript in Postman for the API tests and Java Selenium for the UI tests.

### Prerequisites

#### API Tests:

**1. Ensure [Node.js](https://nodejs.org/en) is installed on the machine**

**2. Install Newman:**
```bash
npm install -g newman
```

## Run Tests

### API tests:

**1. Clone the repository and enter the API tests folder**
```bash
git clone https://github.com/HexBane1/Gameloft-QA-Challenge
cd Gameloft-QA-Challenge/api-tests
```

**2. Run the tests**
```bash
newman run Gameloft_API_Tests.postman_collection.json
```

All tests should fire successfully, providing the following table
<img width="629" height="364" alt="image" src="https://github.com/user-attachments/assets/86ba036b-2ea7-477b-a51b-2d928d129159" />

## Test decisions/thought process

**1. Core_Pages_Status_Response_Time_and_Structure**

First, I created a smoke test for all the main pages of the website, with basic tests like: a 200 OK status code check, a response time under 800ms, and an HTML structure check verifying that the returned page isn't blank.

I ran into some trouble initially, as I was initiating the tests for all pages using a forEach loop. This caused the response times to scale linearly from 200-300ms all the way up to 2000ms, with the time increasing depending on the network I was connected to. I managed to solve this by iterating sequentially through the list of URLs and firing the requests one at a time.

**2. Search_Function_Data_Integrity**

For the second test, I started by looking at the network requests made through Inspect Element. A particular request stood out: gamelist.php. Initially, I thought that the search function probably worked by requesting a list of games each time I hit search, but it actually loads the whole database upon entering the website, and only matches the string I search for to a result in the local list.

The entire database is split into different keys: one related to the "top 10" list of games that pops up when you search for a game that doesn't exist, a huge "master database" containing the majority of the games on the site, and three other keys that are probably related to the news page or the featured games tab.

After this discovery, I created the basic smoke tests, only increasing the response time threshold to 1000ms due to the heavier load of the request. I also added two other tests: one verifying that the response returns an object containing these specific keys, and another to verify that all games contained inside the keys have the valid required structure (name, product_id, isPortrait, product key, assets, banners).
