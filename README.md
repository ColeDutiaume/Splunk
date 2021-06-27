# Splunk
An android version of my Battleship type game
A battleship style game where the player clicks on a grid to try and sink the hidden enemy ships.
The size of the board and the amount of attempts are set by the user via radio groups, that data is then fed into the program which generates a board of the 
appropriate size, along size varying sized of ships. The ships are placed randomly on the board. Coded logic prevents the ships from being placed in "illogical"
positions (diagonals, board wrapping, scattered placement),as well as preventing overlapping ships.
The App tracks various stats such as wins, losses, total "shots fired", and accuracy in a persistant sharedPreferences file.
