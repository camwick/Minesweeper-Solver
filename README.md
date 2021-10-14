# Minesweeper-Solver

A Minesweeper bot to automatically solve minesweeper puzzles.
The goal is not to achieve a competitive solver to achieve low time solves. 
This is just a fun project to challenge ourselves.

## Goals:
- Solve no-guess boards on [minesweeper.online](https://minesweeper.online/)
  - Might switch to [Minesweeper Arbiter](https://minesweepergame.com/download/arbiter.php) due to bots being against minesweeper.online's rules
- Automatically update board state
- Bot is capable of moving the mouse and clicking
- Basic minesweeper pattern recognition

## Rough Outline:
- Screen input
- Updating Board object based on screen input
- Linking Cell objects based on proximity (adjacent)
- Mine pattern recognition
- Main bot loop

## Potential Additions:
- Mine statistics
- 80% solve rate on regular minesweeper boards (not no-guess)
- Enable program to work on any minesweeper board

## Resources
- [Java JDK 16: Color](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Color.html)
- [Java JDK 16: Robot](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Robot.html)
- [Java JDK 16: Dimension](https://docs.oracle.com/en/java/javase/16/docs/api/java.desktop/java/awt/Dimension.html)
- [Minesweeper Patterns](https://minesweeper.online/help/patterns)
