# JClouds bug demo

Demonstrating PUT requests not requesting a new auth token when expired.

* Run a Swift installation with Keystone expiration set to 2 seconds
* Create a container called `container1`
* Edit `src/main/resources/swift.properties` to point to your installation
* Run `./gradlew run`