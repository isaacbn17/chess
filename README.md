# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Sequence Diagrams
[Sequence Diagrams](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEOgg+hgBKKPZIqhZySBBogQDuABZIYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARG2UaMAAtii9Jb0wvQA0U7jqSdAc45MzUyjDwEgIK1MAvpjCxTD5rOxclGV9A1BDo7trvfOqi1DLE1OzvRtbOx+9BzYnG4sFOR1EZSgkWiGSgAAoIlEYpQIgBHUIxACUhyKohOBVk8iUKnUZXsKDAAFV2nCbncUNjCYplGpVPijDoSgAxJCcGDUyhMmA6SwwOkjMRM4msk5g3EqMpoHwIBA4kQqdlSlmkkBQuQoAXwm5MxnaaXqdnGEoKDgcfntJlqqh405akmqEq6lD6hQ+MApOHAP0pE2YN0y05Wm1233+x3gjU5U5Ai6lGCImEotTKrApkGygpHS4wa7temTMqrL5B-11CAAa3QFam+ydlAL8GQ5jKACYnE4uqXBhLm1WpjWUnXG2hR630BxTF5fP4AtB2OSYAAZCBRRIBVLpTJd3KForFqq1RotAzqeJoLoASG6U3F93+X2er3eqxgByLoIKPNiyfF8yxHd85n0F4lgeX9MCAgDCnVFASgQHdeThbddzRDEwGxBNDFdM1tQ9GBySpGlXxQWZPyWU0iRIy1OR5PljW0YVRSorpzhgCdzEIfUYCSdIUhgWi3jDYj3Q7AiyjY+QnRdAkpNZMovR9YNA2DUNwwtSNOWjGBYxDbRFMTZNzhBMosN5CJVBzeDLPbJNT2oYtn36MC316R4JynJsJkmP8z1BU4sm7GA+wHHpQOHbzfODfyZ0CuD50Xbw-ECLwUHQLcd18Zh9zSDJMHCk8kOKMpymkABRTcarqGqmmaG9VDvbjEobdA20QhCrl6PyuuS1Y9lmRzgWc1znQVGA0PsArMPyv0cLUPCzMI5SGPdMkKWMpKtNrIb6OZaT9O5XkY066dJK2iM5WQxVlVVAjNRU0kjBQbgMmMg7JyOm6TojAorWkT6KUMPahvWjs+ry+bluzBBcyc0Kpvc4K3NRztsjAXt+0HA40s8DKVyhO1NxhGAAHEJTZIrD1K49mHuyqKiphrmvsCUOsO67-3ZWG+kG6cHlGmBxtTdlZLIikadGVRfqS-D5Q2mQ3tI8jIenRX-t0tkzpYy7ee6vWZJVx6VWhojbve8i5bUOEtfQY7zX14HmIu6naY4oyrpN9WzYer35fFl6XLOCa03t1Q7IchDA9Zjyufl8YKj6ZOUAASWkVOAEYewAZgAFi+A8MkNctAumLopmCUB6wr8CfK+DOADkJUCvYYAaDHjnDsrcci-GYt6DPVFT8p04lbO88LkupjLg0vLGKua96OuQAb5eVkeNuO58rue7MTh0uXQJsB8KBsG4eA9QyYPDHpkqB6lkKqpqepOe5oY-fvHpR4lO3byrZ+YWUjv1YWAURqzCnqMIBK8RoS3zCzCEMB1IZHtnCe2K0sRW02oDd66CUBOzQDracLtGJnUMsZeMKtXo21IkQzB9t4EUNOu7EocA76GHtkJESD94EA1dgnVB9srZgNTGUbBiNkaRwTu5KYGcZ5lHzsXOCoCwpMzxtFJO08c4qLnqlE+xMz4BEsJ9NCSQYAACkIC8gfoEDe9ZGY41fpjd+lIrzNAzjzP605BxzAQMAcxUA4AQDQlAFYAB1FgmdGrNAAEKbgUHAAA0rvPRs81G90mhHSRJYBq-1FjAwJwTKBhIidE2J8SkkpPSS3TJBjslINydLAAVnYtAzCJQ4LWmHfBrsdpgEwUo6QbCgYcjKIbMieihGUJQTNJUlt+lqwYWUDpvJMHXzKaE8J0Bxl6Q4TYzpD9xGARRmUWxtkZEtMQv+K4OTEID20QTUwRMlyZQCF4YJnZvSwGANga+AkCAJGSMVI8riWbnlqvVRqzVjA9QFhcksgIUZuOmihNB3A8AKEBXCZWyF6EENIlCYYEAaCGgVgct2kyZBg3vsAFUYp2gaBWXrSEGxyUoGjviuZ7DaWgy+oYRlCAZnyzwas4lHKyU0BIVSvlEyrR0qFbxJlE4YCQGnKyuh4dYaCvBrHJGtz5Fpm6I8jszyh46IOMfBcQA)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.


## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
