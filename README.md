# Chess Opening Database
### Summary
This is an application for chess players who want to keep track and expand of their opening repertoire for chess.
As amateur chess player myself, I have trouble memorizing certain lines so this type of database would be helpful. 
This program would allow you to input openings, save them, and view all the openings you have saved from a
 user-friendly interface.
### User Stories
As a user, I want to be able to
 - view all openings stored in the database
 - add openings
 - add alternative lines
 - delete openings
 - export and import openings via PGN 
 - save current opening list to disk
 - load opening list that was previously saved
### Phase 4: Task 2
I have chosen to make the Board class more robust by throwing a NotPieceException in the method stringToPiece when
the parameter cannot be converted to a piece. 
### Phase 4: Task 3
 - I would have made it more save friendly, and implemented an alternative method to fir the data into JSON format
 - Some variables do not need to be included in the constructor, and can be implicitly instantiated
 - Some functions in Board and Move could be instead put into a utility class. 