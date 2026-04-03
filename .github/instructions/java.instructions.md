---
applyTo: "**/*.java"
---

# Java Instructions

Use these conventions for Java changes in this repository.

## Style

- Use Java 17 features where appropriate (e.g., switch expressions, records).
- Keep methods short and cohesive; refactor large blocks into helpers.
- Prefer explicit types in public APIs; avoid `var` in public interfaces.
- Keep null-handling consistent; avoid returning null where Optional is clearer.

## Documentation

- Use English for all comments and documentation.
- Add JavaDoc for all classes, enums and methods.
- Keep comments concise and focused on intent, not mechanics.

## Testing

- Add or update unit tests for behavioral changes.
- Keep tests readable; use descriptive test names.
- Unit tests : JUnit 5 + Mockito.
- Assertions : AssertJ.

## UI

- Ensure Swing updates are on the EDT.
- Avoid blocking the UI thread; use background tasks when needed.

