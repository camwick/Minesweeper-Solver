# Minesweeper-Bot

A Minesweeper bot to automatically solve minesweeper puzzles.
The goal is not to achieve a competitive solver to achieve low time solves.
This is just a fun project to challenge ourselves. We will be utilizing algorithms and pattern recognition to hopefully achieve a solve state.

I am not opposed to creating our own minesweeper gui using Java Swing.

Make sure to create a pull request before merging to main.

## Working On:

- hole pattern
- more mine pattern recognition

## Goals:

- Solve no-guess boards on [minesweeper.online](https://minesweeper.online/)
  - Might switch to [minesweeperonline.com](https://minesweeperonline.com/) due to bots being against minesweeper.online's rules
- Automatically update board state ✅
- Bot is capable of moving the mouse and clicking ✅
- Basic minesweeper pattern recognition ✅
- Advanced pattern recognition

## Rough Outline:

- Screen input
- Updating Board object based on screen input
- Linking Cell objects based on proximity (adjacent)
- Mine pattern recognition
- Main bot loop
  - basic mine recognition
  - advanced pattern recognition 

## Potential Additions:

- Mine statistics
- 80% solve rate on regular minesweeper boards (not no-guess)
- Enable program to work on any minesweeper board

## Resources:

- [Java JDK 16: Color](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Color.html)
- [Java JDK 16: Robot](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Robot.html)
- [Java JDK 16: Dimension](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Dimension.html)
- [Java JDK 16: Toolkit](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Toolkit.html)
- [Java JDK 16: InputEvent](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/event/InputEvent.html)
- [Minesweeper Patterns](https://minesweeper.online/help/patterns)
