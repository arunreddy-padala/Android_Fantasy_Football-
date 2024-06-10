# Fantasy_League_Companion_Java_Android

This project is an Android-based fantasy football league companion application designed to manage and display various aspects of fantasy leagues, including drafts, matchups, and standings. Utilizing the Android SDK, Android Jetpack components, and external APIs like Sleeper and ESPN, the app integrates live data fetching and robust local data management with Room local DB for a comprehensive user experience. Key features include real-time updates on league standings, detailed player stats, and interactive draft sessions, all within a user-friendly mobile interface.

Languages and Frameworks: Android SDK, Android Jetpack, Room DB, RecyclerView, SharedPreferences, Sleeper API, ESPN API, Handler

Key Components:

1. API:

- ESPN:
  - Facilitates fetching data from ESPN's NFL APIs to retrieve teams, athletes, and their stats.
  - Provides methods to get detailed information about teams, athletes, and athlete stats.
  - Utilizes a nested private class for asynchronous data retrieval from the API, ensuring non-blocking calls and processing.

- SleeperAPI:
  - Provides a comprehensive interface for accessing various Sleeper fantasy football API endpoints. It fetches details like NFL state, user information, league data, draft details, and player stats.
  - Implements multiple static methods to retrieve data related to leagues, users, drafts, and players using Sleeper's REST API.
  - Utilizes a custom thread (sleeperApiThread) to handle API requests asynchronously, ensuring non-blocking operations and efficient data retrieval.
 
- SleeperAPI Helper:
  -  Facilitates interaction with the Sleeper API for fantasy football league management within an app. This class provides a variety of methods to retrieve and process data related to leagues, players, rosters, and transactions.
  -  Comprehensive Data Management: Methods include fetching and processing detailed league information, user data, rosters, transactions, and player details directly from the Sleeper API.
  -  Utility Integration: Uses several utility functions for data conversion and manipulation, ensuring efficient data handling and updating within the app's database systems.
  -  Asynchronous Processing: Employs threading for non-blocking API calls to handle intensive tasks like retrieving all transactions for a league season or updating player information.
 
2. Models:

- DraftPick:
  -  Represents an entity within the app's database, specifically designed to store information about individual draft picks in fantasy football drafts.
  - Comprehensive Details: Stores various details about a draft pick including draft ID, round, pick number, the user who picked, player's image, name, and position.
  - Entity Annotations: Utilizes Room annotations for defining the table structure (draftPicks) and primary keys (draftId, round, pick) to ensure data integrity and efficient queries.

- LeagueUser:
  - Defines an entity for storing user information within a specific league context, including identification and display attributes.
  - Detailed User Information: Stores essential data such as league and user IDs, display name, avatar URLs, and associated roster ID.
  - Entity Structure: Configured as a Room entity with leagueId and userId as primary keys to uniquely identify users within leagues.
 
- MatchUp:
  - Models a fantasy football matchup within the application's database, tracking key matchup details per league, week, and roster.
  - Matchup Details: Stores league ID, week, roster ID, matchup points, and matchup ID, along with lists of starters and a mapping of player points.
  - Entity Configuration: Defined as a Room entity with a composite primary key (leagueId, week, roster_id) to uniquely identify each matchup.
 
- Player:
  - Represents a player entity in the fantasy football application's database, containing comprehensive details about individual players.
  - Extensive Player Information: Includes unique identifiers like sleeperId, player's team, position, physical attributes (height, weight), age, experience, and fantasy-related details.
  - Entity Configuration: Defined as a Room entity with sleeperId as the primary key.
 
- Roster:
  - Represents the roster information for teams within fantasy football leagues, capturing extensive details about team compositions and performance metrics.
  - Comprehensive Roster Management: Stores information about team starters, reserves, taxi squad, and bench players. Includes statistics like wins, losses, points scored (fpts), points against (fpts_against), and waiver details.
  - Entity Configuration: Configured as a Room entity with composite primary keys (league_id, roster_id) to uniquely manage each roster within specific leagues.
 
3. Main Screens:

- Draft:
  - Manages the user interface and interaction for the draft process in a fantasy football application, integrating various data handling and UI elements to display draft picks.
  - Dynamic UI Building: Constructs a draft table dynamically in the app using TableLayout based on draft data fetched from the Sleeper API.
  - Data Handling: Utilizes repositories and APIs to retrieve and manage draft pick data, processing JSON objects and updating the UI on the main thread.
  - User Interaction: Supports navigation to settings and logout functionalities via the options menu, enhancing user engagement and control.
 
- MatchUp:
  - This class handles the user interface for displaying fantasy football matchups in a mobile application, focusing on the weekly matchups of a fantasy league.
  - Dynamic Data Loading: Uses SleeperApi and other utilities to fetch and display detailed matchup data based on league and week parameters, integrating this information into a RecyclerView for user viewing.
  - User Interaction: Provides interactive elements such as a progress bar during data loading and menu options for settings and logout, enhancing the user experience.
  - Text Views: Displays team names and their respective points, facilitating easy visualization of ongoing or past matchups.
  - Image Views: Handles user images for two competing teams in a matchup.
 
- Standings:
  - Manages the user interface for displaying the standings of teams within a fantasy football league, providing a comprehensive view of team performance metrics.
  - Dynamic Data Handling: Retrieves and displays league standings using data from the Sleeper API, presenting them in a RecyclerView with sortable columns based on various statistical categories like wins, losses, and points.
  - Interactive Elements: Integrates sorting functionality directly into the standings table headers, allowing users to sort teams by names, wins, losses, ties, and points scored against, enhancing user interaction and data exploration.
    

