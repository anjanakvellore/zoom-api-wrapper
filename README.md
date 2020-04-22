# Zoom API Library

Java version: 11.0.7

Maven version: 3.6.3


## Build 

Navigate to the project root directory.

```
mvn clean install
```

## Run the project

Step 1: Open a new terminal and execute the following command to start Ngrok

```
ngrok start -none
```

Step 2: Once ngrok has started, execute the following command in the other terminal. Make sure you are in the project root directory. 
```
mvn exec:java
```

