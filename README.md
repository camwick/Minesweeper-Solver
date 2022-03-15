# Minesweeper-Bot

A Minesweeper bot to automatically solve minesweeper puzzles.
The goal is not to achieve a competitive solver to achieve low time solves.
This is just a fun project to challenge ourselves. We will be utilizing algorithms and pattern recognition to hopefully achieve a solve state.

Utlizes pattern recognition alogithms to brute solve Minesweeper boards. Good practice for graphs, linked lists, pixel management, tree search algorithms, and basic OOP practices. 

## Working On:

Project could be considered done at this stage. We've succesfully implemented a gui and enough pattern recognition algorithms to solve easy-evil minesweeper boards. 
Possible future implementations:
- settings page for gui
- progress bar for gui
- multithreading

## Goals:

- Solve no-guess boards on [minesweeper.online](https://minesweeper.online/) ✅
- Automatically update board state ✅
- Bot is capable of moving the mouse and clicking ✅
- Basic minesweeper pattern recognition ✅
- Advanced pattern recognition ✅
- gui ✅

## Rough Outline:

- Screen input
- Updating Board object based on screen input
- Linking Cell objects based on proximity (adjacent)
- Mine pattern recognition
- Main bot loop
  - basic mine recognition
  - advanced pattern recognition 

## Resources:

- [Java JDK 16: Color](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Color.html)
- [Java JDK 16: Robot](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Robot.html)
- [Java JDK 16: Dimension](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Dimension.html)
- [Java JDK 16: Toolkit](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Toolkit.html)
- [Java JDK 16: InputEvent](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/event/InputEvent.html)
- [Java Swing](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html)
- [Minesweeper Patterns](https://minesweeper.online/help/patterns)
