Tic Tac Toe
=========

Neumont Capstone Project - Tic Tac Toe game which uses image recognition and a white board.

Overview
--------
The premise is a game of tic tac toe where the human player plays against a computer. The move logic for the computer is nearly non-existent; it simply makes a move on whatever space is next available.

Gameplay
--------
The game is played by a projector projecting a tic tac toe board onto a whiteboard while the player draws their moves on the board. The human draws X's as their move, and the computer projects O's on the whiteboard as its move. The program uses a webcam to read the positions the player drew in.

Technologies
------------
The program is written purely in Java, but uses a JavaCV framework which relies on the OpenCV framework. Those frameworks are only used to perform the webcam image reading. Past that, all of the image processing is done by custom code that uses Java's BufferedImage.

Complexity
----------
The program does all of the image recognition by custom code. The process of detection starts with applying a gaussian blur and converting the image to grayscale. After that it performs a series of line detection. It starts by finding the coordinates of the tic tac toe board by finding the main 4 lines (2 horizontal and 2 vertical). Once it has found the board, it determines the locations of the squares and scans them for crossing diagnol lines (the players X moves). Once it detects a move it notifies the game logic which then updates the UI with the computer's move. 

Project Setup
------------
The project is currently an Eclipse project, though I plan to convert it to an IntelliJ project. There are three modules with source code: the main module, the library module, and the test module.
