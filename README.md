# Zoom Chatbot: Framework code

## Description
The Java framework can be used to build Zoom chatbots. It implements OAuth 2.0 authentication. Note that all the primitive REST API wrappers has been throttled.  

The following bots can be used to check the framework for the following implementations:
### Bot 2 
* Implements all RESTful API calls to the Zoom API in Chat Messages and Chat Channels components.

### Bot 3 
* Implements a bot-facing function for sending chat messages to a given channel, identified by a name, for example:
client.chat.sendMessage("test", "Hello world!")
* Implements a bot-facing function for retrieving the entire chat history of a given channel, for example:
client.chat.history("test") : will return a list of messages
* Implements a bot-facing API for searching for specific events related to chat since the beginning of the chat history on a given channel, for example:
client.chat.search("test",  lambda message : message.sender.contains("diva"))
client.chat.search("test",  lambda message: message.message.contains("hello"))

### Bot 4 
* Implements a bot-facing event mechanism that allows bots to be asynchronously notified when
  * New messages arrive on a specific channel. The name of the channel will be a parameter of the event subscription process. 
The new message(s) will be sent to the bot code -- one event per new message.
  * A message has been updated on a specific channel. The name of the channel will be a parameter of the event subscription process. 
The updated message(s) will be sent to the bot code -- one event per updated message.
  * New member is added to any channel to which the bot belongs. The identification of the channel and the new member will be sent 
to the bot code -- one event per new member.

### Bot 5
* Implements a local data storage component using SQLite that supports the persistence of data when the bot program stops and resumes, 
and that supports the simultaneous existence of multiple bot programs, possibly with different clientId's.

Note: The local data storage component has been engineered using reflection and generics. That is, instead of coding multiple different sets of APIs, 
one for each table, the code has a generic API get, update, and delete for all of them and then offers table-specific functions that uses those generic 
functions. The tables themselves has been created using reflection.
