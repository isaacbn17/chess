# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Sequence Diagrams

[View Sequence Diagrams](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAA5M9qBACu2GADEOgg+hgBKKPZIqhZySBBogQDuABZIYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARG2UaMAAtii9Jb0wvQA0U7jqSdAc45MzUyjDwEgIK1MAvpjCxTD5rOxclGV9A1BDo7trvfOqi1DLE1OzvRtbOx+9BzYnG4sFOR1EZSgkWiGSgAAoIlEYpQIgBHUIxACUhyKohOBVk8iUKnUZXsKDAAFV2nCbncUNjCYplGpVPijDoSgAxJCcGDUyhMmA6SwwOkjMRM4msk5g3EqMrYKFUeJtAVQJk4kQqWUFIEXUowBQIKHADiiurAADW6BgAFEoN4oJh9SDdYVqJcYN0pqN1MByeMpg6nWVgCaUGbRfIbWhJgcjpR3VlzGUACxOADMXR9vT9qgDYzKvRD0DDEajMBj6HjWpVOtOUpZpJApoy6tp7SZjO00vU7OMJQUHA4-K72jreMbvebqhKrcjGQUPjAKThwBXKW7mCbJLZp0Hw9Hy9XmvBDdOrq9iJhKLUPgQWCvoLlnsN13a9Mmxc+Uw3q7qCBY2-KZ9jrJMclOFMMDKAAmJwnBzfpPwlEDVi+f8UkA4CYB-AEzE4UwvF8fwAmgdhyRgAAZCAokSAJUnSTJkHMdlE0NKpakaFoDHUeI0C6ABIXNxXuf4vmeV53lWGAEyKCC9XOEEymEqZRKLdC5n0F4lgeWSXSUhSPXrFASgQWjeThGi6LRDEwGxc9DGnIlZzJCkO3UnsXL3AdOR5PkbiFEUxRQ0YdxnHzIIKRyykCidHLY+VTJgTDzEIOQUEqbSpLhSSlgcpL2V3VkygXDKTzXTDt2K-sD05I8jU3M9Cqis5gS9azeQiVQHyfQyX2i+T3zU0KNMeTDsJrCZa3Y5MWJgmB4MQnoRsGVDpowzdJrjab9PQDgiO8PxAi8FBbWs3xmAYtIMkwaDmFfYoynKaQ7Sou06jtJpml41R+K6CagPQcDQUU9r316QHgP+PZZgM8HEu1ZLzPsS6rNoy7bLUezJwbAkIpKmByTACrtvXLagbQLzmUigpB3848mu0YVRSh4Gav3R6IUa09meqHQACsUHAXGnMvfqykpIZN2gJAAC8UFHUtnWfd12KuX1WULIMS0dMsxWl1dZYV5Z9Nm1r7vTJwAEYc01-1A2LZXYsNlJjcVmaWvx7zCY4FBuCXTdyYAynqb7fc6c5aR-YpQxScp0X3WfMoLpXbrevhg01aGq45LfObsjAOCEJzA59sOkjAihUcqJhGAAHEJTZa6mLu+aHsGt9nvrj7vvsCUAYp2MQfZZPvUhoeptWWGYEzt0uYVImKUbv1g6w0PE+cmnCeJ+PYzX7aw9nXzuV5RnefkFmUsntBwp92qF+SiqhX5oWRYS1qx6lzD3aVvWVf6tnLu3p7YFkdsGf+Lsf5QHlh7M2Q0C6phgBmW2K08xa3AbrUMBtoGwNNnnEyRUCakiXmAFeag4RH1phyU+fJyEaA5kAkyZR6Gzw-uLcGLCm7p0fHPCCj0vS5n7n6IM5Q+jCJQAASWkEGa2sFMxpi+Ixdso0VgzC6FMYIoArTqi-BtKYEiAByEppp7BgA0Ah-CCiW0WiXNBEjVCiPERKaRsj5GKKmMolAuj1q9HUYkXoWiQA6NUfo3oRiTF+LMRYgiB1PBHVItgHwUBsDcHgG2Qw5Dkg3WYoXRGT0Kg1HqH3AerttpIQiWJaeI8OEGiuBPEO0Np6zGcaMYxVSwKq0fqVDJ5C4TkKxliTe3tt4kN3jfA+G9GF1VoefLczNgps1vtM7pPN5mX1fsLLA7CwZ1P5K7X+9p-58IGsZQRoDtZO0gTgmWMCTae3zhbduVtUG5nzJciB2CfAHLuXAghU4Rnhx6YuFAfSJHVWIRHGhcAMlEwlDAJI6QUhwtGJqYw7oOZkhcdIO+ozOad2YQ3CUm9anKSJaMHhfUEatXViA8J2K3EKPgY8qCzzbHLSEQysocimVl0IvEyuARLD+3MkkGAAApCAvJyWGCCAgbRbc8kCI4pUSk3FmgSMHo09AdsnjyuFVAOAEBzJQBWAAdRYJIz6zQABCVEFBwAANKPAka47l7jmXHFJYIhp68mn7BaXMfVlAjUmvNZa61dqHXOq+K6mR7reUnPydzAWUq0B9IGSgdE2MCpIyIffOcpC97oEmbGKhMoZkMzWUFVmN9cXhyYdzZ+fNBZbJJbssl39bl4KOU6JNNKc50veZg52Nyja-PwTU1lhcXm6uHUWT5+tvm4PuZ6gFMhIVuTIRKOEcby21UjmUKt4KFmijjfW4+A6kZYtRS2t+2yvbGW5tEUN0BLT71ScAA1r6oC5sIZ-CWRoKzmhgO+20yt+3Ko1ugh2C6sH63DKaED1Y4yeqsfANlGZsxoPnTrUdiHIzIetDWNdeMn2L15O0MAlRg3wk-d+410A-3rsxWKbAWgMhgolBCgtJ82MccMCey+wVyHSPbW1PZkqur3l4V0glgjLGgwwzO9lpdTDlwFcdAIXgv0YcjLAYA2BUnpQIAkbJrd7r5K9C9N6H0votGMDUjtCn+0Eu5iAbgeAFBGcocMjdBbIQbAgDQdUqhfPTMPTIGOGQUoIAQCFSgGgP6AtcjAKEwxgugqbuFyFfHo4B0MOGeLDi-OsfS5l4taAwv7qhYOKLBXYvxaWUllq3rDT5djpSyD8n3yKcQQtJaanYmYCAA)

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
