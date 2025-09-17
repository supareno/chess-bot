# GitHub Copilot instructions

## Project context

This project is a chess bot written in Java. It is designed to play chess against a bot opponent.
It uses swing for the graphical user interface and implements a basic AI for the bot's moves.

The chess board is represented as an 8x8 grid, with pieces represented by their initials (e.g., K for King, Q for Queen).
This grid is represented in a 2D array where the first index represents the row (rank) and the second index represents the column (file).

* The A1 square is at the bottom left of the board and its position in the 2D array is `[0][0]`.
* The H8 square is at the top right of the board and its position in the 2D array is `[7][7]`.

The language used for the code, the comments, and the documentation is English.

## Code standards

### Naming Conventions

- Use PascalCase for classes, interfaces, and enumerations
- Use camelCase for variables, functions, and methods
- Use ALL_CAPS for constants
- Use meaningful and descriptive names
- Use snake_case for test methods

### Error Handling
- Use try/catch blocks for exception handling
- Avoid empty catch blocks
- Always log errors with contextual information

### Comments
- Use Javadoc comments for classes, methods, and public APIs
- Write clear and concise documentation.
- Use consistent terminology and style.
- Include code examples where applicable.

### Grammar
* Use present tense verbs (is, open) instead of past tense (was, opened).
* Write factual statements and direct commands. Avoid hypotheticals like "could" or "would".
* Use active voice where the subject performs the action.
* Write in second person (you) to speak directly to readers.

### Markdown Guidelines
- Use headings to organize content.
- Use bullet points for lists.
- Include links to related resources.
- Use code blocks for code snippets.

## Architecture

The project follows a layered architecture with the following layers:
1. **Model Layer**: Contains the core game logic, including classes for pieces, the board, and game rules.
2. **View Layer**: Contains the GUI components using Swing.
3. **Controller Layer**: Manages user input and updates the model and view accordingly.
4. **AI Layer**: Contains the logic for the bot's decision-making process.

## Specific Instructions

