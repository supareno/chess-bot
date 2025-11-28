# Chess Bot

A Java-based chess bot featuring a graphical user interface (GUI) and basic AI. Play chess against the computer, with 
support for standard chess rules.
This project is built by Frederic Cuillandre as a personal project to explore chess game development with AI in Java.

## Features

- Play chess against a bot opponent
- Java Swing GUI for interactive play
- Standard chess rules implemented (Castling kingside and queenside, En passant, ...)
- Move history displayed in algebraic notation
- Playing against different levels of AI difficulty
- Move validation per piece type

Bot modes are:
- Random Move Bot: Selects moves randomly from available legal moves
- Minimax Bot: Uses the Minimax algorithm with a basic evaluation function

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher

## Architecture

The project follows a layered architecture:

| Layer        | Description                                      | Key Classes/Packages                  |
|--------------|--------------------------------------------------|---------------------------------------|
| Model        | Core game logic, board, pieces, rules            | `ChessBoard`, `ChessPiece`, `Move`    |
| View         | Swing GUI components                             | `ChessGameFrame`, `ChessBoardPanel`   |
| Controller   | Game state, move validation, turn management     | `ChessGame`                          |
| AI           | Bot move selection logic                         | `ChessBot`                           |
| Utilities    | Helpers, logging, move formatting                | `ChessUtils`, `ChessMoveFormatterUtils`|

See the [Architecture Overview](assets/architecture.md) for detailed diagrams.

## Project Structure

```
com.fcuillandre.chessbot/
├── board/          # Board representation and case enumeration
├── bot/            # AI implementation
├── game/           # Core game logic and move management
│   └── checkers/   # Move validation strategies per piece type
├── pieces/         # Piece definitions, types, and colors
├── ui/             # Swing GUI components
└── utils/          # Helper utilities and formatters
```

## Setup Instructions

1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd chess-bot
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Run the tests using Maven:
   ```
   mvn clean test
   ```

## Usage

To run the chess bot GUI, execute:
```
mvn exec:java -Dexec.mainClass="com.fcuillandre.chessbot.ChessApp"
```

## Screenshots

<!-- Add screenshots of the GUI here -->

## Testing

- Unit tests are provided for all major game logic components
- Run tests with:
  ```
  mvn test
  ```
- Test coverage includes move validation, special rules, and edge cases

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
