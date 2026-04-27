# Brick Breaker

A classic Brick Breaker arcade game built in Java using Swing and a custom-built game engine with an object-oriented architecture.

---

## Features

- **Multiple brick types** — Blue, red, green, yellow, orange, and dark-red bricks, each with unique sprite states (normal → cracked → broken)
- **Multi-hit bricks** — Bricks require up to 3 hits to destroy, with sprites updating visually after each hit
- **Physics-based collision** — Overlap-based side detection ensures the ball bounces correctly off every brick face
- **Score system** — Points awarded per brick hit, displayed live on screen
- **Persistent high score** — High score saved to and loaded from a local file between sessions
- **Pause / Resume** — Toggle pause mid-game
- **Win & Lose screens** — End states with clear UI prompts
- **Restart** — Press Enter on the lose screen to restart without relaunching

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java |
| UI / Rendering | Java Swing (`JPanel`, `JFrame`, `Graphics`) |
| Game Loop | `javax.swing.Timer` (~60 FPS) |
| Input | `KeyListener` |
| Build | IntelliJ IDEA |

---

## Architecture

The project is built around a lightweight custom game engine with clear separation of concerns:

```
src/
├── Main.java           # Entry point
├── BBUI.java           # JFrame window setup
├── GamePanel.java      # Main game loop, rendering, collision orchestration
├── GameManager.java    # Static game state: score, high score, win/lose flags
├── InputHandler.java   # Keyboard input handling
├── GameObject.java     # Base class for all game objects (position, size, sprite)
├── Ball.java           # Ball movement and collision logic
├── Paddle.java         # Paddle movement
├── Brick.java          # Brick state, hit logic, and sprite swapping
├── Collider.java       # Abstract collider base class
├── BallCollider.java   # Ball-specific collider
├── BoxCollider.java    # Box collider for bricks and paddle
└── UIElements/         # All game sprites (PNG)
```

**Key design decisions:**
- `GameObject` is the base class for `Ball`, `Paddle`, and `Brick`, keeping rendering and position logic in one place
- `Collider` is abstract, allowing `BallCollider` and `BoxCollider` to define their own `OnCollisionEnter` behavior
- `GameManager` is a static utility class managing global game state, keeping it decoupled from individual game objects

---

## Getting Started

### Prerequisites

- Java JDK 11 or later
- IntelliJ IDEA (recommended) or any Java IDE

### Running the project

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/brick-breaker.git
   cd brick-breaker
   ```

2. Open the project in IntelliJ IDEA

3. Run `Main.java`

> **Note:** The high score file path in `GameManager.java` is currently hardcoded to a local machine path. Before running, update the file path in `loadHighScore()` and `setHighscore()` to match your environment, or change it to a relative path.

---

## Controls

| Key | Action |
|---|---|
| `←` / `→` Arrow Keys | Move paddle |
| `P` | Pause / Resume |
| `Enter` | Restart (on lose screen) |

---

## Known Issues

- High score file path is hardcoded — needs to be updated per machine (see setup note above)
- No levels system yet — single fixed brick layout per run
