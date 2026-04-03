# GitHub Copilot Instructions

This repository contains a Java 17 chess application with Swing GUI, a chess engine, and a Minimax-based bot.

## Project Overview

### Technology Stack

- **Java**: Version 17 or higher
- **Build Tool**: Maven 3.6+
- **UI Framework**: Java Swing
- **Dependencies**:
    - Lombok 1.18.30 (annotation processing for boilerplate reduction)
    - JUnit Jupiter 5.8.2 (testing framework)
    - AssertJ 3.21.0 (assertion library for tests)

### Project Structure

- UI: Swing (`src/main/java/com/fcuillandre/chessbot/gui`)
- Core logic: engine + model (`src/main/java/com/fcuillandre/chessbot/engine`, `src/main/java/com/fcuillandre/chessbot/model`)
- Bot: `src/main/java/com/fcuillandre/chessbot/bot`
- Tests: `src/test/java/com/fcuillandre/chessbot`

### Chess Board Representation

The chess board is represented as an 8x8 grid, with pieces represented by their initials (e.g., K for King, Q for Queen).
This grid is represented in a 2D array where the first index represents the row (rank) and the second index represents the column (file).

### Chess board coordinates:

```
  col[0] [1]  [2]  [3]  [4]  [5]  [6]  [7]

8  [0]   r    n    b    q    k    b    n    r   ← BLACK back rank
7  [1]   p    p    p    p    p    p    p    p   ← BLACK pawns
6  [2]   .    .    .    .    .    .    .    .
5  [3]   .    .    .    .    .    .    .    .
4  [4]   .    .    .    .    .    .    .    .
3  [5]   .    .    .    .    .    .    .    .
2  [6]   P    P    P    P    P    P    P    P   ← WHITE pawns
1  [7]   R    N    B    Q    K    B    N    R   ← WHITE back rank
```
* Row index 0 → rank 8 (top, BLACK)
* Row index 7 → rank 1 (bottom, WHITE)
* Col index 0 → file A
* Col index 7 → file H
* A1 (WHITE rook) = squares[7][0]
* E8 (BLACK king) = squares[0][4]
* E1 (WHITE king) = squares[7][4]

## General Guidance

- Keep changes minimal and focused on the task.
- Prefer small, readable methods; avoid deeply nested logic when possible.
- Maintain current public API and behavior unless explicitly requested.
- Use ASCII-only text in source files unless a file already uses Unicode.
- Update tests or add new tests when behavior changes.
- Avoid introducing new dependencies unless requested or clearly necessary.

## Coding Conventions

- Follow existing formatting and naming conventions.
- Keep UI strings consistent and centralized when practical.
- Use JavaDoc for public types and methods when adding new ones.
- Use `final` where appropriate for immutability.
- Favor enums and records where they improve clarity.

## GUI-Specific Notes

- Keep Swing changes on the Event Dispatch Thread (EDT).
- Avoid long-running work on the EDT; use `SwingWorker` or background threads when needed.
- Repaint the board and update labels through existing listener callbacks.

## Engine/Bot Notes

- Preserve chess rules: castling, en passant, promotion, check/checkmate/stalemate.
- When adjusting evaluation, keep piece values and tables consistent.
- Do not change move legality rules without updating tests.

## Tests

- Existing tests live under `src/test/java`.
- Add tests for new rules or critical bug fixes.
- Keep tests deterministic; avoid timing-dependent assertions.

