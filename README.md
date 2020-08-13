This is a Twitter bot written for learning purposes that will tweet out 
(using twitter4j) weather reports from the National Weather Service API 
for a given location when prompted. It is a work in progress currently.

keys.txt file must be stored in /src/keys/. Expected format is:

```
key.NOAA=<ncdc key>
key.TwitterConsumerAPI=<twitter consumer api>
key.TwitterConsumerAPISecret=<twitter consumer api secret>
key.TwitterAccessToken=<twitter access token>
key.TwitterAccessTokenSecret=<twitter accesss token secret>
```
Per the Twitter4J standards, the twitter4j.properties file into which the 
twitter keys will be written must be in the base directory for the
program.

Dependencies:

1.twitter4j

2.gson
