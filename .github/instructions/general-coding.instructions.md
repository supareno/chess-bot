---
applyTo: "**"
---
# Project general coding standards

## Naming Conventions

- Use **PascalCase** for classes, interfaces, and enumerations
- Use **camelCase** for variables, functions, and methods
- Use **ALL_CAPS** for constants
- Use **snake_case** for test methods
- Use meaningful and descriptive names that reflect purpose

## Code Design Principles

- **Immutability**: Use `final` for classes that should not be extended (e.g., `ChessPiece`, `Move`, `Coordinate`, `ChessBoard`)
- **Encapsulation**: Keep fields private and provide getters/setters as needed
- **Single Responsibility**: Each class should have one clear purpose
- **Strategy Pattern**: Use `MoveChecker` implementations for piece-specific move validation

## Error Handling

- Use try/catch blocks for exception handling
- Avoid empty catch blocks
- Always log errors with contextual information using `ChessUtils.log()`
- Throw `IllegalArgumentException` for invalid method parameters
- Throw `IndexOutOfBoundsException` for out-of-bounds coordinate access

## Comments and Documentation

- Use Javadoc comments for classes, methods, and public APIs
- When writing Javadoc for a class, add @version and @author tags
- Use English for all comments and documentation
- Write clear and concise documentation
- Use consistent terminology and style
- Include code examples where applicable
- Document pre-conditions, post-conditions, and side effects
- Include the following header in all source files. Aadjust the year as needed and do not update the rest of the header.:
```java
/*
 * Copyright 2025-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```


### Javadoc Grammar Standards

* Use present tense verbs (is, opens) instead of past tense (was, opened)
* Write factual statements and direct commands. Avoid hypotheticals like "could" or "would"
* Use active voice where the subject performs the action
* Write in third person for class/method descriptions
* Use second person (you) in usage examples to speak directly to readers

## Markdown Guidelines

- Use headings to organize content
- Use bullet points for lists
- Include links to related resources
- Use code blocks for code snippets with language specification
- Use tables for structured data comparisons

## Lombok Usage

- Use `@Getter` and `@Setter` annotations for automatic accessor generation for mutable classes
- Avoid using `@Data` or `@AllArgsConstructor` on immutable classes
- Prefer explicit constructors for classes with complex initialization logic


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
- Test class names should mirror the production class names with `Test` suffix
- Example: `ChessGameTest` for `ChessGame` class
- Test classes have default (package-private) visibility unless needed otherwise
- Test methods should have default (package-private) visibility and be annotated with `@Test`

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
