# How to Build and Run

First we need to start up GemFire

**Linux:**
```
scripts/startGemFire.sh 
```
**Windows:**
```
scripts\startGemFire.bat
 
```

Then we need to run the spring boot app that uses session state cacheing.

**Linux:**
```
./gradlew bootRun
```

**Windows:**
```
gradlew bootRun
```

Then open your browser and on inital page load the code will recognize that it is a new session and create a counter.    Then hit reload at the page will show the counter increaseing.

http://localhost:8080

Since the session state is stored out side the scope of the spring boot process we can restart the spring boot application and the session details will still be there.   Just make sure to restart the spring boot app with in the timeout of the session which is configured for 180 seconds in this example.


