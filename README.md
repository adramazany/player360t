<!-- TOC -->
* [Definition](#definition)
  * [ASSIGNMENT](#assignment-)
  * [REQUIREMENTS:](#requirements)
  * [DELIVERABLES:](#deliverables)
* [How to use](#how-to-use)
  * [Local players run:](#local-players-run)
  * [Network players run:](#network-players-run)
  * [Syntax:](#syntax)
* [Architecture](#architecture)
  * [Local players:](#local-players)
  * [Network players:](#network-players)
* [Decisions](#decisions)
  * [Java 17](#java-17)
  * [Using Network programming instead of messaging queue](#using-network-programming-instead-of-messaging-queue)
  * [Designing network-base messaging as a second layer above local multi-thread messaging between players](#designing-network-base-messaging-as-a-second-layer-above-local-multi-thread-messaging-between-players)
  * [Define request as a message in the local and network zone](#define-request-as-a-message-in-the-local-and-network-zone)
  * [Dynamic using by define arguments](#dynamic-using-by-define-arguments)
  * [Extract ReplyMessageProvider](#extract-replymessageprovider)
<!-- TOC -->

# Definition
## ASSIGNMENT 

The goal of this exercise is for you to deliver a program with the cleanest and clearest design possible that fulfills the defined requirements.

Please focus on design of your solution and not on the technology used.

The technology employed should be the simplest possible for fulfilling the requirements.

The program must be written in the Java programming language.

Everything that is not clearly specified must be decided by developer (please document your decisions).

Everything that is specified is a hard requirement.

## REQUIREMENTS:

- Implement a class that represents a player in a game.

- Each instance of the "Player" class must be able to communicate with other players.

- Each player must be represented by a separate instance of the Player class.

When executed the program is required to perform the following sequence of actions:

1. Create 2 players.

The players (class instances) must run inside the same Java process but in separate threads.

2. One of the players, henceforth referred to as the "initiator", must send a "message" to the second player.

3. When a player receives a message, it must send back a new message that contains the message received concatenated together with the count of messages that the receiving player has sent.

4. A new message should only be sent out if a response to the previous message was received.

5. Finalize the program gracefully once the initiator has sent a predefined number of messages and has received the same number of responses (this is the "stop condition”).



Example output:

player 1: “message 0” -> player2

player 1 <- player2: “message 0 1”

player 1: “message 0 1 1” -> player2

player 1 <- player2: “message 0 1 1 2”

…

6. Additional challenge (nice to have):

Each player must be able to run inside a separate Java process (instance of the Java Virtual Machine).

This is an extension to the behavior detailed in step 1.



## DELIVERABLES:

- Provide documentation for every class, detailing its responsibilities.

- Use pure Java as much as possible in the main project. Avoid using 3rd party libraries or frameworks, e.g. Spring.

- You are allowed to use testing frameworks such as JUnit and Mockito in the test scope of the project.

- Provide a shell script to start the program.

- You must provide a Maven project that builds a ".jar" file from Java source code.

- Create a "zip" or "tar.gz" archive of your project that includes only the Java source code, shell script(s), build configuration file(s) and documentation. The archive must not contain any ".class" or ".jar" files.

- Send the e-mail to your 360T contact as an attachment. Links to download the archive from an external location will be ignored due to company security policy.

 

# How to use
To run you need to have at least java 17, because of 

## Local players run:
```
run-local-players.bat
OR
c:\workspace\player360t>java -cp player-1.0-SNAPSHOT-full.jar com.t360.game.LocalPlayers
```
Results will be something like this picture:
<img src='/doc/local-players-output.png'/>

## Network players run:
```
run-network-players.bat
OR the below shell commands in separate windows
c:\workspace\player360t>java -jar target\player-1.0-SNAPSHOT-full.jar player1
c:\workspace\player360t>java -jar target\player-1.0-SNAPSHOT-full.jar initiator player1 TEST_MESSAGE 5
```
It is simply using -jar because the NetworkPlayer defined as mainClass in the manifest
Results will be something like this picture:
<img src='/doc/network-players-output.png'/>

## Syntax:
The syntax of using the software is like this: 
```
Syntax: java -jar player-1.0-SNAPSHOT-full.jar <playerId> [initial-chat-to] [initial-message] [initial-count]
    playerId            The unique id for player from list [initiator,player1..player10]
                        In case of playerId is 'initiator' and the rest of arguments are not entered, it will try to send initial predefined messages to 'player1' in loop of 5 sequential send and receive.
    initial-chat-to     The unique id for another player from above list that you would like to send initial message to him/her
    initial-message     The initial message that you would like to send to another player
    initial-count       The count of initial sequential send and receive to the other player
```

Then you can simply are able to send initial request for loop messages from any player to any player, 

Also, these request can be sent as a network message by json format like this:

```json
    {"requestId":"r1","count":1,"from":"player1","to":"player2","message":"test"}
```

# Architecture
<img src='/doc/player360t-architecure.png'/>

As you can see in the picture, two different goals of this project combined and working very well each-other.

## Local players:
    
- MessageQueuProvider: As core of infrastructure for messaging between local players it provides the scalability due to response to the network players,  
      It provides local message queues for message and request topics to enable messaging between local players
      and provide scalable, abstract and asynchronous way of communication of network players with local player
      There is an extension point in there by replacing it with an external message queue engine
      will provide a simple way of having network based communication between players

- Player: As core of this messaging system between players,
      it provide infrastructure of messaging between this player with other local and network players,
      Handling local messages,
      by listening messages on local message queue,
      and replying in the same way.
      And handling local request of repeating a sequential send/receive messaging between local players
      and returning the response into the local messaging queue

- ReplyMessageProvider: A business specific placeholder for responding rules and regulations,
      Currently, it parses the received message and add numbers to the message as reply,
      to be able to track the interaction of messages between players


## Network players:
    
   - NetworkMessageHandler: As core of infrastructure for network based messaging between players it provides the ability of direct interaction of messages between players (ability to inform the game-center is extendable) to provide the ability of managing millions of players without worrying about centric resources,
      It provides infrastructure of messaging between network players,
      and listening to messages.
      By running a network server to get message from network players.
      And handling network messages receive in string format by detection of its type (message/request),
      and putting it over local messaging infrastructure,
      and taking and replying over network.
      And handling local messages waiting to transferred over network,
      and returning the response into the local messaging queue

   - NetworkServer: Utility that simplify managing and creating network server to listen for received messages
      And getting a reply function provided by caller to be used in time of getting new connection and message

   - NetworkClient: Utility that facilitate sending message over network and waiting for response to return to caller


# Decisions

## Java 17
I decided to use java 17 because of using features like text block, record, ...

## Using Network programming instead of messaging queue
When I started to design the handling network players,
there was two major solution of using network programming either using messaging queue,
As you know the network programming will appear very better than messaging,
when it comes to handle lots of users.

## Designing network-base messaging as a second layer above local multi-thread messaging between players
It will help software to adopt with client's (player) hardware in replying to asynchronous received message

## Define request as a message in the local and network zone
Add the ability of managing players by sending administration messages any time (not just at startup time)

## Dynamic using by define arguments
When It comes to start of application as a network users,
I decided to able every player to send initiated loop of entered count of messages to another player at starting point.
And also possibility of messaging between every player by declaring it at command line. 

## Extract ReplyMessageProvider
To add abstraction in creating reply message
