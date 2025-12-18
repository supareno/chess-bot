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

Apply the [general coding guidelines](./general-coding.instructions.md) to all code.

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

