# Chess Bot

This project is a chess bot developed in Java. It utilizes a simple AI to play chess against human players. 
The bot evaluates positions and makes moves based on its internal logic.

## Prerequisites

This project requires the following software to be installed on your machine:
- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher

## Bot Modes

### Random bot mode

One mode is implemented for the bot to play against a human player. It is the random move selection mode, where the bot 
selects a move randomly from the list of legal moves.

## Project Structure

- `src/main/java/com/chessbot/`: Contains the main source code for the chess bot.
- `src/test/java/com/chessbot/`: Contains unit tests for the chess bot.
- `pom.xml`: Maven configuration file for managing dependencies and build process.
- `README.md`: This file, providing an overview and instructions for the project.
- `LICENSE`: License information for the project.

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
   
4. Run the test using Maven:
   ```
   mvn clean test
   ```

## Usage

To run the chess bot, execute the following command:
```
mvn exec:java -Dexec.mainClass="com.chessbot.App"
```

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.