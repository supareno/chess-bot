# GitHub Copilot Instructions

## Project Context

This project is a chess bot written in Java. It is designed to play chess against a bot opponent.
It uses Swing for the graphical user interface and implements a basic AI for the bot's moves.

### Technology Stack

- **Java**: Version 17 or higher
- **Build Tool**: Maven 3.6+
- **UI Framework**: Java Swing
- **Dependencies**:
  - Lombok 1.18.30 (annotation processing for boilerplate reduction)
  - JUnit Jupiter 5.8.2 (testing framework)

### Chess Board Representation

The chess board is represented as an 8x8 grid, with pieces represented by their initials (e.g., K for King, Q for Queen).
This grid is represented in a 2D array where the first index represents the row (rank) and the second index represents the column (file).

#### Coordinate System

```
   A  B  C  D  E  F  G  H
8 [7][0-7]              ← H8 is at [7][7]
7 [6][0-7]
6 [5][0-7]
5 [4][0-7]
4 [3][0-7]
3 [2][0-7]
2 [1][0-7]
1 [0][0-7]              ← A1 is at [0][0]
```

* The **A1** square is at the bottom left of the board and its position in the 2D array is `[0][0]`.
* The **H8** square is at the top right of the board and its position in the 2D array is `[7][7]`.
* Files (columns A-H) map to array indices [y: 0-7]
* Ranks (rows 1-8) map to array indices [x: 0-7]

### Chess Rules Implemented

The following chess rules and special moves are implemented:
- **Castling**: Both kingside and queenside castling for white and black
- **En Passant**: Pawn capture of an adjacent pawn that just moved two squares
- **Pawn Promotion**: Pawns reaching the opposite end can be promoted to Queen, Rook, Bishop, or Knight
- **Check Detection**: Identifies when a king is under attack
- **Checkmate**: Determines when a player has no legal moves and is in check
- **Stalemate**: Detects when a player has no legal moves but is not in check

The language used for the code, comments, and documentation is English.

## Code Standards

### Naming Conventions

- Use **PascalCase** for classes, interfaces, and enumerations
- Use **camelCase** for variables, functions, and methods
- Use **ALL_CAPS** for constants
- Use **snake_case** for test methods
- Use meaningful and descriptive names that reflect purpose

### Code Design Principles

- **Immutability**: Use `final` for classes that should not be extended (e.g., `ChessPiece`, `Move`, `Coordinate`, `ChessBoard`)
- **Encapsulation**: Keep fields private and provide getters/setters as needed
- **Single Responsibility**: Each class should have one clear purpose
- **Strategy Pattern**: Use `MoveChecker` implementations for piece-specific move validation

### Error Handling

- Use try/catch blocks for exception handling
- Avoid empty catch blocks
- Always log errors with contextual information using `ChessUtils.log()`
- Throw `IllegalArgumentException` for invalid method parameters
- Throw `IndexOutOfBoundsException` for out-of-bounds coordinate access

### Comments and Documentation

- Use Javadoc comments for classes, methods, and public APIs
- Write clear and concise documentation
- Use consistent terminology and style
- Include code examples where applicable
- Document pre-conditions, post-conditions, and side effects

#### Javadoc Grammar Standards

* Use present tense verbs (is, opens) instead of past tense (was, opened)
* Write factual statements and direct commands. Avoid hypotheticals like "could" or "would"
* Use active voice where the subject performs the action
* Write in third person for class/method descriptions
* Use second person (you) in usage examples to speak directly to readers

### Markdown Guidelines

- Use headings to organize content
- Use bullet points for lists
- Include links to related resources
- Use code blocks for code snippets with language specification
- Use tables for structured data comparisons

### Lombok Usage

- Use `@Getter` and `@Setter` annotations for automatic accessor generation
- Avoid using `@Data` or `@AllArgsConstructor` on immutable classes
- Prefer explicit constructors for classes with complex initialization logic

## Architecture

The project follows a layered architecture with the following layers:

#### Model Layer
Contains the core game logic, including classes for pieces, the board, and game rules.

**Key Classes:**
- `ChessPiece`: Immutable representation of a chess piece (type and color)
- `ChessBoard`: 8x8 board representation with piece management
- `ChessPieceType`: Enum for piece types (KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN)
- `ChessColor`: Enum for piece colors (WHITE, BLACK)

#### View Layer
Contains the GUI components using Swing.

**Key Classes:**
- `ChessGameFrame`: Main application window
- `ChessBoardPanel`: Visual representation of the chess board
- UI components for move history, captured pieces, etc.

#### Controller Layer
Manages user input and updates the model and view accordingly.

**Key Classes:**
- `ChessGame`: Central game controller managing game state, move validation, and turn management
- `Move`: Immutable representation of a chess move (start and end coordinates)
- `Coordinate`: Immutable representation of a board position (x, y)
- `MovedPiece`: Record of a completed move with metadata (capture, check, castling, etc.)

#### AI Layer
Contains the logic for the bot's decision-making process.

**Key Classes:**
- `ChessBot`: AI implementation for move selection

#### Game Logic - Checkers
Implements the Strategy pattern for move validation.

**Key Interface:**
- `MoveChecker`: Interface for piece-specific move validation

**Implementations:**
- `PawnMoveChecker`
- `RookMoveChecker`
- `KnightMoveChecker`
- `BishopMoveChecker`
- `QueenMoveChecker`
- `KingMoveChecker`

#### Utilities
Helper functions and common utilities.

**Key Classes:**
- `ChessUtils`: Board initialization, logging, and utility methods
- `ChessMoveFormatterUtils`: Formatting moves for display

## Package Structure

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

## Testing Standards

### Test Structure

Follow the **Arrange-Act-Assert** (AAA) pattern:
1. **Arrange**: Set up test data and preconditions
2. **Act**: Execute the method under test
3. **Assert**: Verify the expected outcome

### Test Naming

- Use `snake_case` for test method names
- Format: `test_<methodName>_<scenario>_<expectedResult>`
- Example: `test_pawn_move_forward_one_square_is_valid()`

### Test Coverage

- Test all public methods in the model and controller layers
- Test edge cases and boundary conditions
- Test special chess rules (castling, en passant, promotion)
- Test check, checkmate, and stalemate detection

### Test Organization

- Mirror the source package structure in the test directory
- One test class per production class
- Group related tests using nested test classes (JUnit 5 `@Nested`)

## Common Code Patterns

### Creating and Validating Moves

```java
// Using ChessCaseEnumeration
Move move = new Move(ChessCaseEnumeration.E2.getCoordinate(), 
                     ChessCaseEnumeration.E4.getCoordinate());

// Using coordinates directly
Move move = new Move(new Coordinate(1, 4), new Coordinate(3, 4));

// Validating a move
if (chessGame.isValidMove(move)) {
    chessGame.makeMove(move);
}
```

### Checking Piece Positions

```java
// Get piece at specific coordinates
ChessPiece piece = board.getPieceAt(0, 0);

// Get piece using case enumeration
ChessPiece piece = board.getPieceAt(ChessCaseEnumeration.A1);

// Check if square is occupied
if (board.getPieceAt(x, y) != null) {
    // Square is occupied
}
```

### Board State Manipulation

```java
// Move a piece
board.move(move);

// Set a piece at a position
board.setPieceAt(x, y, new ChessPiece(ChessColor.WHITE, ChessPieceType.QUEEN));

// Get case notation
String notation = board.getCaseAt(x, y); // e.g., "E4"
```

### Implementing Move Checkers

```java
public class CustomMoveChecker implements MoveChecker {
    @Override
    public boolean isValidMove(ChessPiece piece, Move move, 
                               ChessBoard board, ChessGame game) {
        // Implement piece-specific validation logic
        return true;
    }
}
```

## Specific Instructions

### When Adding New Features

1. Maintain the layered architecture separation
2. Use immutable objects for value types (Move, Coordinate, ChessPiece)
3. Log important events and errors using `ChessUtils.log()`
4. Follow the existing MoveChecker pattern for move validation
5. Update move history when moves are executed
6. Ensure check/checkmate detection is not broken by changes

### When Fixing Bugs

1. Add a failing test case that reproduces the bug
2. Fix the bug
3. Verify the test now passes
4. Check for similar issues in related code

### When Refactoring

1. Ensure all existing tests still pass
2. Maintain backward compatibility with existing APIs
3. Update documentation to reflect changes
4. Consider performance implications for board evaluation

## Resources

- [Official Chess Rules](https://www.fide.com/FIDE/handbook/LawsOfChess.pdf)
- [Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Lombok Documentation](https://projectlombok.org/)

