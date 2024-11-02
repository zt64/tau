### Package Layout

- `di` - Dependency injection modules and related functionality.
- `domain` - Contains business logic and use cases.
    - `domain.manager` - Functionality encapsulated by a manager class to be shared throughout the app.
- `model` - Data structures and entities used throughout the app.
- `ui` - User interface components and related classes.
    - `component` - Smaller components intended for use by larger widgets and other UI components.
    - `dialog` - Dialog UI components.
    - `theme` - Theme definitions and styling for the app.
    - `util` - Utility functions and helpers used across the UI layer.
    - `viewmodel` - ViewModel classes responsible for preparing and managing UI-related data.
    - `widget` - Components intended for only a single purpose.
    - `window` - Window components.
