Moves (What's the move?)
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description

Moves is an application that helps users decide on what to do, such as weekend activities, dining options, and travel destinations. When creating an account, users will input information about themselves so that the app can make informed decisions when giving suggestions. Once a user acts on the decision, the user will rate the decision, allowing the app to learn more about the user and make more accurate predictions about what the user will enjoy doing. 

### App Evaluation

- **Category:** Entertainment / Lifestyle / Reference
- **Mobile:** App (website could also be available but app is more convenient)
- **Story:** You / group of friends have a hard time choosing what to do and end up wasting time choosing, can be helpful in a new place or hometown
- **Market:** everyone, realistically probably used mosly by younger generation
- **Habit:** very habit forming, app gets to know you, quick decisions leads to more memories made b/c less time choosing what to do
- **Scope:** Using accumulative data on a person, could begin by giving options depending on age/interests, becomes more accurate with use (can be merged with local suggestions app idea below)

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Intro Questionnaire 
    * Rate activities already completed
* Database of things to do
* matching based on age, type of event or activity, etc. 
* User rating of activity
* Right now vs. future 
* Categories:
    * distance
    * price
    * number of people
    * busyness
    * time of day
    * time spent doing activity
* Things to do:
    * restaurants
    * tourist attractions
    * local attractions
    * temporary vs. permanent 
* Risky moves 
* (like Stitch Fix!)

**Optional Nice-to-have Stories**

* People can give ideas
* Plan a trip (long-term suggestions)
* Do things with friends (app will give suggestions based on multiple users' preferences)
* Incorporate transportation
* Get online database reviews 
* User narrows down a few choices, app decides from those
* Friend connections - shows friends' past activites and if they liked it or not 
* Search feature 

### 2. Screen Archetypes

* Login Screen
   * Log In
* Registration Screen
   * Create an account
   * Input details about themselves (questionnaire)
* Home Screen
    * Feed of things happening nearby
    * Button to get to decision maker 
* Decision Screen
    * Categories for decision 
* Details Screen
    * Description 
    * Photos 
    * rating 
    * reviews 
    * (maybe) likelihood of enjoyment 
* History Screen
    * Past activities user has completed
    * Ratings

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Feed
* History
* Profile 

**Flow Navigation** (Screen to Screen)

* Login Screen
   * => Home
* Registration Screen
   * => Home
* Decision Screen 
    * => Home
* Detail Screen
    * => None
* History Screen 
    * => None


[make sure to specify what each star rating means (e.g. )]

## Wireframe
<img src="https://github.com/hkim00/moves/blob/master/wireframes.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models
**Move**
 
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for each move (default field) |
   | image         | File     | image associated with move |
   | title       | String   | name of move |
   | timeNeeded | Number   | amount of time needed for move |
   | partySize    | Number   | number of people participating in move |
   | distanceTo     | Number | distance from current location to move |
   | price     | Number | cost to participate in move |
   | favorited     | Boolean | favorite moves that user has done |
   | saved     | Boolean | moves that user wants to do in the future |
   | hours     | Number | hours available for move |
   | needsRes     | Boolean | if move needs a reservation or not | 
   | busyness     | File (?) | hours that move is bussiest (google graph) | 
   | description     | String | description/details of move | 
   | Category     | String | category of Move |
   

**User**

| Property | Type | Description |
| -------- | -------- | -------- |
| objectId      | String   | unique id for each user (default field) |
| name     | String    | Name of user    |
| age     | Number    | age of user     |
| gender     | String     | gender of user    |
| address     | String     | current location of user     |
| foodPreferencesList     | Pointer to PreferencesList     | preferences chosen when creating account    |
| attractionPreferencesList     | Pointer to PreferencesList     | preferences chosen when creating account    |
| activityPreferencesList     | Pointer to PreferencesList     | preferences chosen when creating account    |
| eventPreferencesList     | Pointer to PreferencesList     | preferences chosen when creating account    |
| favoritesList | Pointer to FavoritesList | favorite moves that user has done| 
| savedList | Pointer to SavedList | moves that user may want to do in the future | 

**FoodPreferencesList** 

| Property | Type | Description |
| -------- | -------- | -------- |
| prefersItalian | Boolean | does user like this type of food
| prefersMexican | Boolean | does user like this type of food
| prefersThai | Boolean | does user like this type of food
| prefersChinese | Boolean | does user like this type of food
| prefersVegan | Boolean | does user like this type of food
| prefersIndian | Boolean | does user like this type of food
| prefersCoffeeAndTea | Boolean | does user like this type of food
| prefersAmerican | Boolean | does user like this type of food
| prefersAsian | Boolean | does user like this type of food
| prefersGreek | Boolean | does user like this type of food
| prefersLatinAmerican | Boolean | does user like this type of food
| prefersFrench | Boolean | does user like this type of food
| prefersJapanese | Boolean | does user like this type of food
| prefersVietnamese | Boolean | does user like this type of food
| prefersAfrican | Boolean | does user like this type of food
| prefersHalal | Boolean | does user like this type of food
| prefersGerman | Boolean | does user like this type of food
| prefersKorean | Boolean | does user like this type of food
| prefersLebanese | Boolean | does user like this type of food
| prefersEthiopian | Boolean | does user like this type of food
| prefersPakistani | Boolean | does user like this type of food
| prefersSpanish | Boolean | does user like this type of food
| prefersTurkish | Boolean | does user like this type of food
| prefersCarribean | Boolean | does user like this type of food
| prefersIndonesian | Boolean | does user like this type of food


**AttractionPreferencesList** 

| Property | Type | Description |
| -------- | -------- | -------- 
|prefersIndoor | boolean | does the user what to be indoors?
|prefersOutdoor | boolean | does the user what to be outdoors?
|prefersLandmark | boolean | does the user what to see a landmark?
|prefersTemporary | boolean | does the user want to see a temporary attraction?


**ActivityPreferencesList** 

| Property | Type | Description |
| -------- | -------- | -------- |
| prefersIndoor | Boolean | does the user like indoor activities? |
| prefersOutdoor | Boolean | does the user like outdoor activities? | 
| prefersTours | Boolean | does the user want to go on group tours? |
| prefersNature | Boolean | does the user want to do activities such as hiking, water sports, or walks/runs? |

**EventPreferencesList** 

| Property | Type | Description |
| -------- | -------- | -------- |
| prefersSporting | Boolean | does the user want to watch a sporting event? |
| prefersConcerts | Boolean | does the user want to attend a concert? |
| prefersShows | Boolean | does the user want to attend a show (e.g. comedy, magic, etc.)? |
| prefersShows | Boolean | does the user want to attend a show (e.g. comedy, magic, plays etc.)? |
| prefersFestivals | Boolean | does the user want to visit a festival (e.g. music festival, farmers' markets, etc.)? |


**FavoritesList**

| Property | Type | Description |
| -------- | -------- | -------- |
|FavoriteMoves | List of Moves | favorite moves that user has done |

**SavedList** 

| Property | Type | Description |
| -------- | -------- | -------- |
|SavedMoves | List of Moves | moves that user wants to do in the future | 


### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
    - Log In Screen 
        -  (Read/GET) Query for user profile details 
 - Search Result Feed Screen
      - (Read/GET) Query all posts where user is author
         ```swift
         let query = PFQuery(className:"Move")
         query.whereKey("author", equalTo: currentUser)
         query.order(byDescending: "createdAt")
         query.findObjectsInBackground { (posts: [PFObject]?, error: Error?) in
            if let error = error { 
               print(error.localizedDescription)
            } else if let posts = posts {
               print("Successfully retrieved \(posts.count) posts.")
           // TODO: Do something with posts...
            }
         }
         ```
   - Create Post Screen
      - (Create/POST) Create a new post object
   - Move Details Screen
      - (Create/POST) Favorite a move
      - (Delete) Unfavorite a move 
      - (Create/POST) Save a move 
      - (Delete) Unsave a Move 
   - Profile Screen 
   - History Screen 
