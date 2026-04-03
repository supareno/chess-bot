# Java Swing Chess Game

A Java-based chess bot featuring a graphical user interface (GUI) and basic AI. Play chess against the computer, with support for standard chess rules. This project is built by Frederic Cuillandre as a personal project to explore chess game development with AI in Java.
## Features

- **Intuitive graphical interface** with interactive chessboard
- **Intelligent bot** using Minimax algorithm with alpha-beta pruning
- **4 difficulty levels** (Easy, Medium, Hard, Expert)
- **Color choice** to play against the bot
- **Two-player mode** on the same screen
- **All chess rules**: castling, en passant capture, promotion
- **Automatic detection** of check, checkmate, stalemate
- **Move history** in algebraic notation
- **Legal move highlighting**

## Prerequisites

- Java JDK 17 or higher
- Maven 3.6 or higher

## Compilation and Execution

Compilation is done with Maven

```bash
# Compile the project
mvn compile

# Run the game
mvn exec:java -Dexec.mainClass="com.fcuillandre.chessbot.ChessApp"

# Create executable JAR
mvn package
java -jar target/chess-bot-1.0-SNAPSHOT.jar
```

## Project Structure

```
src/main/java/com/fcuillandre/chessbot/
├── ChessApp.java             # Entry point
├── model/                    # Data model
├── engine/                   # Game engine
├── gui/                      # Graphical interface
└── bot/                      # Artificial intelligence
└── utils/                    # Utils
```

## How to Play

1. Launch the application
2. Choose the game mode (against bot or two players)
3. If playing against the bot:
   - Select your color (White or Black)
   - Choose difficulty level
4. Click on a piece to see its possible moves
5. Click on a highlighted square to move the piece

## Shortcuts

- **New Game**: Button in left panel
- **Resign**: Button in left panel
- **Flip Board**: Button in left panel

## Bot Difficulty Levels

| Level      | Depth | Description |
|------------|-------|-------------|
| Easy       | 2     | For beginners |
| Medium     | 3     | Casual player |
| Hard       | 4     | Experienced player |
| Expert     | 5     | Serious challenge |

## Technologies Used

- **Java 17** - Programming language
- **Swing** - Graphical interface
- **Minimax** - Search algorithm for bot
- **Alpha-Beta Pruning** - Search optimization
- **Piece-Square Tables** - Positional evaluation

## Contributing

Contributions are welcome! Please:
- Follow code style and architecture guidelines
- Add tests for new features and bug fixes
- Submit pull requests with clear descriptions
- Open issues for bugs or feature requests

See [here](CONTRIBUTING.md) for more details.

## Resources

- [Official Chess Rules](https://www.fide.com/FIDE/handbook/LawsOfChess.pdf)
- [Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Lombok Documentation](https://projectlombok.org/)
- [Algebraic Notation (Wikipedia)](https://en.wikipedia.org/wiki/Algebraic_notation_(chess))

## License

See [LICENCE.txt](LICENCE.txt) for license information.