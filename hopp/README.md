# cs0320 Term Project 2020

** System Tests **

Functionality Tests

1. Rider table and Driver table updated when user signs up
2. Rider reference removed from trip table and trip reference removed from rider table when rider leaves trip
3. Rider reference removed from trip table and trip reference removed from rider table when driver cancels trip and when driver removes rider
4. Trip removed from trip table and trip reference removed from driver table when driver
	cancels trip
5. Trip table updated when driver posts new trip
6. Trip table updated with rider reference when rider joins trip
7. Rider table updated with trip reference when rider joins trip

Error Handling

1. Given driver post with invalid user token - return Driver not in database error
2. Given rider search with invalid user token - return Rider not in database error
3. Given unexpected/invalid type input for any one of the http post requests, throw invalid input error

Algorithm Tests

1. Given two trips and a rider search that contributes contrasting
	amount of detour to each, the trip results are ordered in priority
	of less detour for the driver.
2. Given a rider search with no trips within vicinity, return empty list.
3. Concurrent client calls on the algorithm (asyn handling)

Web-sockets 

1. Checked that web-socket connection registered user 
	on logging in
2. Checked that the user's unique token is added to an 
	active use set on the backend. 
2. Also confirmed that on reloading the page, the user 
	does not re-register and remains within the active user set. 
3. Checked that the user persists for a fixed period of 
	time after the session ends.

Notifications - (tested in isolation, currently not integrated)

1. Rider receives notification when they successfully join a trip
2. Rider receives notification on being removed from a trip
3. Rider receives notification on trip cancellation
4. Driver receives notification when rider joins trip
5. Driver receives notification when rider leaves trip

** System Tests End **

**Team Members:**
Mustafa Ghani, Tony Wang , Izzy Gao, Harman Suri: 

**Team Strengths and Weaknesses:**

Mustafa Ghani: 
Strengths: algorithms, critical thinking
Weaknesses: bad at reading documentations 
 
Tony Wang : 
Strength: Server
Weakness: time management, and communication 
 
Izzy Gao :
Strength: types very fast, good at frontend
Weakness: bad at backend 
 
Harman Suri: 
Strength: algorithms
Weaknesses: bad at visual design

**Project Idea(s):** _Fill this in with three unique ideas! (Due by March 2)_
### Idea 1
_Unsure (but likely) - Clear algorithm but it'll be really hard to anticipate likely usage (and vetting users is essentially impossible at a small scale)_
-   Going to NY for the weekend? Greyhounds are slow and unreliable. Train is too expensive. Why not have an app where people driving to and from cities may offer a rideshare at a cheap cost. Boston to New York, Los Angeles to San Francisco, Toronto to Chicago and more...
-   Rideshare application for long-distance travel

**Problem - high cost of long-distance travel**
-  Solution - price-matching algorithm to ensure either driver and passenger are not shortchanged

**Problem - user safety**
-  Possible Solution - background checks through social media, submission of driver’s license, rating system

**Problem - no-shows/cancellations**
-  Possible Solution - dynamically reallocate passengers to available drivers on similar routes 

**Project Features:**
-  User-safety features - apart from the money-saving incentive, safety appears to be the biggest disincentive for users as rating systems would only work if the driver is a regular participant. However, since drivers are likely to be one-time participants, we would need to ensure that they are vetted 
-  Ensuring reliability of service - provisions for ride substitution/reallocation will allow a smoother more dependable journey and improve return customers 
-  Algorithm to match passengers and drivers - plays a significant role in optimizing a passenger’s journey at the lowest cost and maximizing profit for the driver while maintaining a standard of efficient journey time. 
-  In-app payment - ensures transaction completion and refunds 

**Challenges:**
-  Developing a complex and functional algorithm to connect a driver and a hitchhiker based on the optimal route i.e matching passengers within a certain radius of the driver’s travel route. 
-  develop a visually appealing and intuitive GUI
-  find real users to do user testings

**User Perspective:**
-  We interviewed a hitchhiker, Aidan, who hitchhiked 13,000 + miles 
-  He thinks a good hitchhiking app should give the user the chance to compare prices offered by different drivers, because he is used to hitchhike for free
-  He also thinks safety is a big issue. He would like to see the driver’s social media or other credentials before going on the ride
-  Other participants were generally excited about the idea. When asked if they would use the product they said yes, but were also concerned about the safety aspect. 


### Idea 2
_Rejected - This is a "Create, Read, Update, Delete" app with an extra image classification API call_
-Problem: The current Facebook buying/selling app is not user-friendly. 
 
**Description:**
-  A lifestyle app to provide all-encompassing solutions for the typical needs of a Brown Student. 
 
 
**Project Features:**
-Buy/Sell: Brown students shop a lot on amazon and it produces a lot of waste when items no longer needed by someone are simply thrown away. For people who want something, it is cheaper to buy from peers, and people who sell things can also get some money back. Ideally, we won’t have to scrape data from Facebook’s Marketplace. Users can post the items that they want to sell, and the items will be categorized into school supplies, home supplies, women’s clothes, men's clothes, electronics, and apartments. You can also sell Spotify Premium services, Summer storage, plants,etc ...
 
         o   Every summer, off-campus students are desperate to find sublets. An app that specializes makes transactions more transparent. For the apartment section, users are able to post about sublets.
        
**Challenges:**
-Categorizing might  become a challenge. But we can use Google’s Computer vision API to automatically detect the picture being uploaded, so that the objects can be automatically grouped into a specific category.
 
**User Perspective: **
- We interviewed a fellow Brown Student, Rebecca
- She thinks that the current Brown Buying and Selling page on Facebook is not a good app because it does not provide automatic image recognition and item categorization. 
-She also thinks the verifying the seller/buyer is actually a Brown student is also critical. 
Another user concern is the current Facebook page is not very efficient in marking something as Sold as soon as an item is purchased. So users end up contacting sellers that have already sold the items. Since our map has its own channel for payment, this should not be a difficult thing to implement. 


### Idea 3
_Approved!_
-SA web application to store users chess games for peer analysis. 
-SI love playing chess, but most of the time I am playing it by myself without anyone to learn or improve with. I think lots of people could get better much faster if they had access to resources that were tailored to a level slightly above theirs. This application would allow verified users to submit their chess games in PGN format for other verified users to analyze and comment on, provided they are at an ELO slightly above the person submitting their game. 


**Features/Requirements:**
-  Verification of Chess ELO (through searching through existing database of active chess players in North America). This is important to make sure that users are matched with other users with a rating slightly above theirs. Some challenges of this may be efficiently getting the rating of a person from the rating database, and deciding what documents that users have to submit in order to get verified. From a user perspective, it would be quite important that people that are analyzing your games are actually of a given rating, so you can take confidence in the analysis that you are getting. 
-   Database to store the PGN of all the games that users submit, along with the analysis for each of those games. This is necessary to efficiently store and retrieve user games and their analyzed version. It’s possible that this database could get quite large, and then managing it would definitely be challenging. From a user perspective, would allow each user to look at the analysis of their games and get the valuable feedback of how they are playing. 
-    Matching algorithm to suggest potential users for a given user to analyze. This is the key part of the project that links users together so that one can get their games analyzed. Because there may be many users at a given rating and a sparse number of users at some other ratings, setting a matching threshold dynamically (based on how many users are online (or many registered) at a given time) would be challenging. From a user perspective, this is likely the most important feature, as chess players who do not have a coach or do not play in a chess club are often isolated to play only online where it's difficult to improve without quality feedback about your games. 
-    GUI to perform analysis with arrows, highlighting of squares on a chess board, and arrow drawing. This is important because users need to be able to conduct analysis in a visual way (this is often how analysis is done in chess). Creating an easy-to-follow GUI (something like lichess.org’s GUI) is definitely a challenging task. From a user perspective, this will affect how users like the project because it is the majority of their experience in the system. 
-    Users should be able to message each other in a chat box during the game. This allows real-time interaction between people and players can discuss strategy and techniques during and after games to improve. Some third party communication api might be incorporated for this functionality. The challenge is to enable peer-to-peer real-time communication using web socket.
Possible Features:
-   An AI that is trained to be at ELO X, where X is slightly above the ELO of a given user. This allows a user to train against someone slightly better than them, and then have those training games analyzed to find recurring problems in their game. The challenge of this feature is to incorporate many existing chess engines into the application. From a user perspective, having your training games done in the application, where they are automatically available for analysis provides easy-of-access in uploading games, as well as valuable training against a viable adversary.  


**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 13)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
