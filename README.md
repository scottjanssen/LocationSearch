# LocationSearch

## GCP Deployment

First create the JAR file of the Spring Boot Application using the following command in the directory `/spring-boot/`

```
$ mvn package
```

Then change all API request URLs to use https://api-dot-location-search-361515.ue.r.appspot.com.

and then build the Node.js package using the following commands in the directory `/react/location-search/`

```
$ npm install
$ npm run build
```

Then we upload the following files/directories to our Cloud Shell in our GCP Project's App Engine.
Remember to first delete any of the files in the Cloud Shell that are already there before uploading.

```
/react/location-search/build/
/react/lcation-search/fontend.yaml
/spring-boot/target/LocationSearch-1.jar
/spring-boot/src/main/appengine/backend.yaml
```

Then run the following deployment commands to deploy the application in the Cloud Shell terminal.
First for the back-end:

```
$ gcloud app deploy backend.yaml
```

Then the front-end:

```
$ gcloud app deploy frontend.yaml
```

Now the app should be running at https://location-search-361515.ue.r.appspot.com.

## Application File Hierarchy

Frontend is under "react/location-search" and Backend is under "spring-boot".

react/location-search/src/:
* App.js: Code related to linking pages
* LandingPage.js: Code in charge of the first landing page with the login
* LandingPageStyles.css: CSS for LandingPage
* MapContainer.js: Code related to Google Map implementation
* UserPage.js: Code related to the userpage with the main map search functionality
* UserPageStyles.css: CSS for UserPage
* index.js: Root start of all the codes
* index.css: CSS for index.js
* reportWebVitals.js: Code for testing performance
* setupTests.js: Code for jest tests for DOM

spring-boot/src/main/java/com/cs3300/LocationSearch/controllers/:
* ApiControllers.java: Custom APIs for user handling
* Place.java: Custom APIs for single Google Map result
* Places.java: Custom APIs for a list of Google Map results based on location

spring-boot/src/main/java/com/cs3300/LocationSearch/entities/
* User.java: Code for handling users in backend

spring-boot/src/main/java/com/cs3300/LocationSearch/Repo/
* UserRepo.java: Code for DB

spring-boot/src/test/java/com/cs3300/LocationSearch/
* Contains unit testing on the backend

## Using the Application

First create an user ID by clicking "Not a User?", fill out username and password, and proceed to sign in.
If you already have a user ID, you can log in from the main page directly.

After signing in, you will be presented with your current location in the latitude and longtitude text boxes,
and your current location displayed on Google Map. You can then type the latitude, longitude and radius of the location
you want to search. + is N - is S for latitude and +is East and - is West for longitude. The radius is in miles.
If you input all the values, you can click find places to get your results.

When you get your result, Google Maps will show the vicinity of your results. The results are seen in
both the list on the left and markers on the right. For the list, the names of the location
are hyperlinks to the respective Google Maps location so you can use that. The markers are also clickable and
they show the name, address, and the Google Maps link to the location.

When you are done with using the application, you can click "Menu" on the left top handside which will
show a dropdown menu. Click the logout button from the menu and you will be able to log out.

