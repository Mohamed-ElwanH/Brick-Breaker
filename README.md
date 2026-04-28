# Brick Breaker

A classic arcade Brick Breaker game built in Java with Swing. Destroy all the bricks by bouncing the ball off your paddle. Bricks take 1–3 hits to break and change appearance as they take damage.

---

## How to Run

1. Open the project in IntelliJ IDEA (or any Java IDE).
2. Make sure you're using **Java 8 or later**.
3. Run `Main.java`.

The game window will open at 800×600. No external dependencies required.

---

## Controls

| Key | Action |
|---|---|
| `←` / `→` | Move paddle |
| `Escape` | Pause / unpause |
| `Enter` | Restart after losing |

---

## Features

- 10×7 brick grid with randomly assigned colors and hit counts
- 3 visual states per brick (normal → cracked → broken)
- Score tracking with persistent high score saved to `highscore.txt`
- Pause screen, win screen, and lose screen

---

## Project Structure

```
src/
├── Main.java
├── BBUI.java
├── GamePanel.java
├── GameManager.java
├── InputHandler.java
├── GameObject.java
├── Ball.java
├── Paddle.java
├── Brick.java
├── Collider.java
├── BallCollider.java
├── BoxCollider.java
└── UIElements/       ← sprites and assets
docs/
└── DOCUMENTATION.md  ← full technical reference
highscore.txt         ← auto-created on first run
```

---

## Documentation

For a full breakdown of every class, method, and system (collision, brick, score, game loop), see [`docs/DOCUMENTATION.md`](docs/DOCUMENTATION.md).