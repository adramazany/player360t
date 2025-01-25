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



DELIVERABLES:

- Provide documentation for every class, detailing its responsibilities.

- Use pure Java as much as possible in the main project. Avoid using 3rd party libraries or frameworks, e.g. Spring.

- You are allowed to use testing frameworks such as JUnit and Mockito in the test scope of the project.

- Provide a shell script to start the program.

- You must provide a Maven project that builds a ".jar" file from Java source code.

- Create a "zip" or "tar.gz" archive of your project that includes only the Java source code, shell script(s), build configuration file(s) and documentation. The archive must not contain any ".class" or ".jar" files.

- Send the e-mail to your 360T contact as an attachment. Links to download the archive from an external location will be ignored due to company security policy.

 

# Decisions
Issue 1- Each instance of the "Player" class must be able to communicate with other players.

Solution 1-1- For providing ability of communicating between user simplest way is adding reference of one user to other user.
And in case of remote communication as additional challenge (I mean run inside a separate Java process) 
It can be handle by simply adding the wrapper of player to access a remote user in a defined way,
It is more like a client/server architecture.
But when we are going to extends the connections it will be hard to manage. 

Solution 1-2- But in the new stacks it mostly resolved by having a message queue to manage the messages.
Then we will have the ability of sending message and getting response from anyone
and also it will be easy to change the communication way from pull-based (querying) to push-base (callback) like websockets
Then I 




TODOs:
1. separate processes
2. adding to github
3. adding todos in place that required the timeout to preventing deadlock occurrence
3. comments
4. unit tests
5. mockito test
5. architecture diagram
6. documentation

