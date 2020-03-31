## Spotify API Consumer

Written with Java 8 and Spring Boot 2.2.5 RELEASE

App was made as part of Java programming course organised by [Connectis](https://www.connectis.pl/) based in Warsaw, Poland.

Application runs with arguments provided by user.
Data is being collected from HTTP body answers and mapped from JSON to Java objects. 
It is printed on console window in info logs.
You can find further API documentation on which this app is based on [here.](https://developer.spotify.com/documentation/web-api/)



#### Application setup

Configure your own application.properties in project or jar directory.
Generate your own client id and client secret based on instruction on [this site.](https://developer.spotify.com/documentation/general/guides/app-settings/#register-your-app)
**Spotify account is required.**
 
After setting your properties run java -jar Spotify_API_CLI-0.0.1-SNAPSHOT.jar <command>  from 
your favorite command line interface from jar directory.


#### Current commands and features:
* `-a --authorize `
With it you can manually run authorization procedure. 
Based on `authorizationMethod` setting in `application.properties` it will by 
specified OAuth2 flow
request the access token from the provider and save it to file named `token` in your 
running application directory. 
    * It isn't mandatory to use this command as if there is no `token` file present or 
it expired application will automatically run authorization before any other command.

**IMPORTANT NOTE** _If `authorizationMethod=authorization_code` 
then set `spring.main.web-application-type=servlet` for proper `token` handling_



* `-ab --album <ids>` Application will request `album` data for `ids` provided. 
Multiple `ids` should be separated by a comma. 
`Example: -ab 41MnTivkwTO3UUJ8DrqEJJ,6JWc4iAiJ9FjyK0B59ABb4,6UXCm6bOO4gFlDQZV5yL37`

* `-tr --track <ids>` Application will request `track` data for `ids` provided. 
Multiple `ids` should be separated by a comma.
`Example: -tr 11dFghVXANMlKmJXsNCbNl,20I6sIOMTCkB6w7ryavxtO,7xGfFoTpQ2E7fRF5lN10tr`
