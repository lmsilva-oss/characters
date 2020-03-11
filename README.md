# characters

This project mimics Marvel's public API for characters, using Nitrite in-memory NoSQL database, and agreeing to the exact same
contract, as defined on their swagger spec on https://developer.marvel.com/docs#!/public/. There's also a copy of this spec on 
this repo, updated to comply with OpenAPI 2.0 and also including the necessary authentication (that is, your private & public keys).

# Requirements
* Java 8
* Maven
* Private & Public keys for the Marvel API, further details here https://developer.marvel.com/docs#!/public/

# Configuration
There's an application.properties Spring Boot file which must be updated with your Marvel API private & public keys,
and optional configurations for Nitrite database name and file location (default location is "java.io.temp")

# Import/Export
There's also functionality to export data from Marvel's API into Nitrite so this project can be used without an internet
connection:

* use the -Dcom.lmsilva.characters.exportPath environment variable to point to a valid file location (/home/user/export.json, for instance)
to generate the repository description for the characters, and the comics, events, series and stories these characters
participate or are otherwised related with (in the previous example, these files will be created as /home/user/export.json.characters, 
/home/user/export.json.series, and so on)
* use the -Dcom.lmsilva.characters.importPath environment variable to point to a location of a previous export (in the example above, /home/user/export.json)