# Brick Breaker – Code Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [Class Reference](#class-reference)
   - [Main](#main)
   - [BBUI](#bbui)
   - [GamePanel](#gamepanel)
   - [GameManager](#gamemanager)
   - [InputHandler](#inputhandler)
   - [GameObject](#gameobject)
   - [Ball](#ball)
   - [Paddle](#paddle)
   - [Brick](#brick)
   - [Collider](#collider)
   - [BallCollider](#ballcollider)
   - [BoxCollider](#boxcollider)
4. [Game Loop](#game-loop)
5. [Collision System](#collision-system)
6. [Brick System](#brick-system)
7. [Score & High Score](#score--high-score)
8. [Known Issues & TODOs](#known-issues--todos)

---

## Project Overview

Brick Breaker is a classic arcade game implemented in Java using Swing. The player controls a paddle to bounce a ball and destroy all bricks on screen. Bricks require between 1 and 3 hits to destroy and change their sprite visually after each hit. The game tracks score and persists a high score across sessions.

**Window size:** 800 × 600 px  
**Game loop target:** ~60 FPS (16 ms timer interval)  
**Ball speed:** 7 px/frame on both axes  
**Paddle speed:** 15 px/frame

---

## Architecture Overview

The project follows a lightweight custom game engine pattern built on top of Java Swing.

```
Main
 └── BBUI (JFrame)
      └── GamePanel (JPanel — game loop, rendering, input)
           ├── Ball         extends GameObject
           ├── Paddle       extends GameObject
           └── Brick[]      extends GameObject

GameObject (abstract base)
 └── Collider (abstract)
      ├── BallCollider
      └── BoxCollider

GameManager  (static utility — score, win/lose state)
InputHandler (KeyAdapter — keyboard input)
```

**Key design decisions:**
- `GameObject` is the base class for all game entities, centralizing position, sprite, and collider management.
- `Collider` is abstract — `BallCollider` and `BoxCollider` define their own `OnCollisionEnter` behavior via polymorphism.
- `GameManager` is a static-only utility class (private constructor) that decouples global state from individual game objects.
- `InputHandler` stores key states as static booleans, allowing any class to query input without holding a reference to the handler.

---

## Class Reference

---

### Main

**File:** `Main.java`

The application entry point. Instantiates `BBUI` to launch the game window.

```java
public static void main(String[] args)
```

Creates a new `BBUI` instance, which sets up the JFrame and starts the game.

---

### BBUI

**File:** `BBUI.java`  
**Extends:** `JFrame`

Responsible for creating and displaying the game window.

**Constructor: `BBUI()`**
- Loads the ball sprite as the window icon.
- Creates a `GamePanel` instance and adds it to the frame.
- Sets the window title to `"BrickBreaker"`, disables resizing, packs the frame to fit `GamePanel`'s preferred size (800×600), and makes it visible.

---

### GamePanel

**File:** `GamePanel.java`  
**Extends:** `JPanel`

The core class of the game. Owns the game loop, all game objects, rendering, and collision orchestration.

**Key fields:**

| Field | Type | Description |
|---|---|---|
| `ballObj` | `Ball` | The ball instance |
| `paddleObj` | `Paddle` | The player's paddle |
| `bricksObj` | `static ArrayList<Brick>` | All active bricks (static so `GameManager` can check if it's empty) |
| `gameLoop` | `static Timer` | The Swing timer driving the game loop |
| `inputHandler` | `InputHandler` | Keyboard input reference |
| `backgroundImage` | `Image` | Background sprite |

**Constructor: `GamePanel()`**

Initializes all game constants, creates `Ball`, `Paddle`, and brick grid, loads the high score, registers `InputHandler` as a `KeyListener`, and starts the game loop timer at 16 ms intervals.

**Key methods:**

```java
public void update()
```
Called every frame. Skips update if the game is paused or if the player has lost (but still listens for Enter to restart). On each frame:
1. Updates ball position and collider.
2. Checks ball–paddle collision.
3. Moves paddle based on input.
4. Checks paddle–ball collider overlap.
5. Updates paddle collider.
6. Checks ball against all four borders.
7. Checks paddle against left/right borders.
8. Iterates bricks: removes destroyed ones, checks ball collision for the rest.
9. Evaluates win/lose conditions.

```java
protected void paintComponent(Graphics g)
```
Renders the background, ball, paddle, all bricks, and the HUD (score, high score). Overlays the appropriate full-screen panel (win, lose, or pause) when applicable.

```java
private void setBricks()
```
Generates a 10×7 grid of bricks filling the top of the 800 px wide window. Each brick is randomly assigned a color type and a hit count between 1 and 3.

```java
private void restartGame()
```
Resets score, ball position/speed, paddle position, clears and regenerates the brick grid, and restarts the game loop.

```java
private void checkBallBorderCollision()
```
Handles ball bouncing off the left, right, and top walls. If the ball reaches the bottom edge, it sets `ballAlive = false` in `GameManager`, triggering the lose condition. A position clamp is applied on wall hits to prevent the ball from clipping through.

```java
private void checkPaddleBorderCollision()
```
Clamps the paddle position to stay within the left and right window boundaries.

```java
private String chooseRandomBrick()
```
Returns a random brick color string from: `blueBrick`, `greenBrick`, `yellowBrick`, `orangeBrick`, `redBrick`, `darkRedBrick`.

---

### GameManager

**File:** `GameManager.java`

A static utility class managing global game state. The constructor is private — this class cannot be instantiated.

**Fields:**

| Field | Type | Description |
|---|---|---|
| `gameScore` | `static int` | Current session score |
| `highscore` | `static int` | Loaded from / saved to file |
| `hitScore` | `static final int` | Points awarded per brick hit (10) |
| `ballAlive` | `static boolean` | False when the ball falls below the screen |

**Key methods:**

```java
public static void loadHighScore() throws IOException
```
Reads the high score from a local `highscore.txt` file. Gracefully falls back to 0 if the file is missing, unreadable, or contains invalid data.

> ⚠️ **Known issue:** The file path is currently hardcoded to `F://Repository//Brick Breaker Game//...`. This must be changed to a relative path (e.g., `./highscore.txt`) before the project will run on any other machine. See [Known Issues](#known-issues--todos).

```java
private static void setHighscore(int newHighScore)
```
Writes the new high score to the same hardcoded file path.

```java
public static void setNewHighScore(int newHighScore)
```
Compares the given score against the current high score and calls `setHighscore()` only if it is higher.

```java
public static boolean isWin()
```
Returns `true` if the ball is still alive and the brick list is empty.

```java
public static boolean isLose()
```
Returns `true` if `ballAlive` is `false`.

---

### InputHandler

**File:** `InputHandler.java`  
**Extends:** `KeyAdapter`

Handles all keyboard input. All state is stored as static booleans so any class can read input without needing a reference.

**Mapped keys:**

| Key | Action |
|---|---|
| `→` Right Arrow | Move paddle right |
| `←` Left Arrow | Move paddle left |
| `Escape` | Toggle pause (disabled on win/lose screens) |
| `Enter` | Restart game (on lose screen only) |

**Key methods:**

```java
public void keyPressed(KeyEvent e)   // Sets the relevant flag to true
public void keyReleased(KeyEvent e)  // Sets the relevant flag to false (for movement keys)
```

Static getters: `isRightKeyPressed()`, `isLeftKeyPressed()`, `isPaused()`, `isEnterKeyPressed()`, `setEnterKey(boolean)`.

---

### GameObject

**File:** `GameObject.java`  
**Abstract**

The base class for all game entities. Owns position, size, sprite, and collider.

**Constructor:**
```java
public GameObject(int posX, int posY, int width, int height, String path)
```
Sets position and dimensions, then calls `loadSprite()` to load and scale the image.

**Key methods:**

```java
protected Image loadSprite(String path, int width, int height)
```
Loads a sprite from the classpath using `getClass().getResource(path)`. Throws `RuntimeException` if the path is not found. Scales the image to `width × height` using `Image.SCALE_SMOOTH`.

```java
protected void updateCollider()
```
Moves the collider rectangle to match the current object position. Throws `RuntimeException` if the collider is null.

```java
public abstract void update()   // Called every frame
public abstract void onHit()    // Called when this object is hit
```

---

### Ball

**File:** `Ball.java`  
**Extends:** `GameObject`

Controls ball movement and collision detection against bricks and the paddle.

**Constructor:**
```java
public Ball(int posX, int posY, int width, int height, int speedX, int speedY, String path)
```
Initializes with the given speed and attaches a `BallCollider`.

**Key methods:**

```java
protected void moveBall()
```
Advances the ball's position by `(ballSpeedX, ballSpeedY)` each frame.

```java
public void checkSideCollision(Brick brick)
```
The core collision algorithm. Checks if the ball's rectangle intersects the brick's rectangle. If it does, calculates the four overlap amounts (top, bottom, left, right) between the two rectangles. The **smallest overlap** determines which axis to invert:

- Smallest overlap is top or bottom → invert `speedY`
- Smallest overlap is left or right → invert `speedX`

Then calls `brick.onHit()`.

```java
public void checkPaddleCollision(Paddle paddleObj)
```
If the ball intersects the paddle, snaps the ball to just above the paddle surface and forces `speedY` to be negative (upward), preventing the ball from clipping through.

```java
public void onHit()   // Inverts speedY (called by BallCollider on paddle hit)
public void update()  // Calls moveBall() then updateCollider()
```

---

### Paddle

**File:** `Paddle.java`  
**Extends:** `GameObject`

The player-controlled paddle.

**Constructor:**
```java
Paddle(int posX, int posY, int width, int height, int paddleSpeedX, String path)
```
Attaches a `BoxCollider`.

**Key methods:**

```java
public void movePaddle(InputHandler keyInput)
```
Reads `InputHandler.isRightKeyPressed()` and `isLeftKeyPressed()` and adjusts the paddle's X position by `±paddleSpeedX`. Border clamping is handled separately in `GamePanel`.

```java
public void update()   // Calls updateCollider()
public void onHit()    // No-op for paddle
```

---

### Brick

**File:** `Brick.java`  
**Extends:** `GameObject`

Represents a single destructible brick.

**Constructor:**
```java
public Brick(int posX, int posY, int width, int height, String type, int hits)
```
`type` is the color name string (e.g., `"blueBrick"`), used to build the sprite path. `hits` is the number of hits required to destroy this brick (1–3). Attaches a `BoxCollider`.

**Sprite states per brick type:**

| State | Hits remaining | Sprite path |
|---|---|---|
| Normal | 3 (or original) | `/<type>.png` |
| Split/Cracked | 2 | `/<type>Split.png` |
| Broken | 1 | `/<type>Broken.png` |
| Destroyed | 0 | Removed from list |

**Key methods:**

```java
public void onHit()
```
- Awards `hitScore` (10 points) to `GameManager`.
- Decrements `hitsToBreak`.
- At 2 hits remaining: swaps sprite to `Split` variant.
- At 1 hit remaining: swaps sprite to `Broken` variant.
- At 0 hits remaining: sets `isDestroyed = true`, causing `GamePanel` to remove it from the list on the next frame.

```java
public void update()   // No-op — bricks don't move
```

---

### Collider

**File:** `Collider.java`  
**Abstract**

Wraps a `java.awt.Rectangle` to provide collision detection.

**Constructor:**
```java
public Collider(int posX, int posY, int width, int height)
```

**Key methods:**

```java
public boolean Collided(Rectangle other)
```
Uses `Rectangle.intersects()` to check for overlap. If overlapping, calls `OnCollisionEnter()` and returns `true`.

```java
public abstract void OnCollisionEnter()
```
Implemented by subclasses to define what happens on collision.

---

### BallCollider

**File:** `BallCollider.java`  
**Extends:** `Collider`

The collider attached to the `Ball`. Holds a reference to its parent `Ball`.

```java
public void OnCollisionEnter()
```
Calls `gameObject.onHit()` on the parent `Ball` (inverts `speedY`).

> **Note:** This is used for paddle–ball collision detection via `paddleCollider.Collided(ballCollider.getCollider())`. Brick–ball collision is handled directly in `Ball.checkSideCollision()` instead.

---

### BoxCollider

**File:** `BoxCollider.java`  
**Extends:** `Collider`

A generic box collider used by `Paddle` and `Brick`. Holds a reference to its parent `GameObject`.

```java
public void OnCollisionEnter()
```
Calls `gameObject.onHit()` on the parent object.

---

## Game Loop

The game loop is driven by a `javax.swing.Timer` with a 16 ms interval (~62.5 FPS target):

```
Timer fires every 16ms
  └── GamePanel.update()     ← physics, input, collision, state
  └── GamePanel.repaint()    ← triggers paintComponent()
```

The loop runs continuously. On win, the timer is stopped. On lose, the timer keeps running (to allow the lose screen to render and listen for the restart key).

---

## Collision System

There are two separate collision approaches used in the project:

### 1. Ball vs. Brick — Overlap-based side detection (`Ball.checkSideCollision`)

Calculates how much the ball rectangle overlaps each of the four sides of the brick rectangle. The side with the **minimum overlap** is assumed to be the side the ball entered from, and the corresponding velocity axis is inverted. This prevents the ball from tunneling through bricks and handles corner cases better than a simple center-point check.

### 2. Ball vs. Paddle — Snap and force-upward (`Ball.checkPaddleCollision`)

On intersection, the ball is snapped to sit exactly on top of the paddle, and `speedY` is forced to `-Math.abs(speedY)`. This prevents the ball from bouncing downward through the paddle if it clips in from the side.

### 3. Paddle vs. Ball — Collider-based (`paddleCollider.Collided`)

`GamePanel.update()` also calls `paddleCollider.Collided(ballCollider.getCollider())`, which routes through the `Collider` system and calls `BallCollider.OnCollisionEnter()` → `Ball.onHit()` (inverts Y). This provides a secondary collision path via the abstract collider system.

---

## Brick System

On game start (and restart), `GamePanel.setBricks()` fills a **10 columns × 7 rows** grid:

- Each brick is 80 × 20 px.
- Color type is chosen randomly from 6 options via `chooseRandomBrick()`.
- Hit count is randomly assigned: `Random.nextInt(3) + 1` → values 1, 2, or 3.
- Bricks are stored in a `static ArrayList<Brick>`. Destroyed bricks are removed via an `Iterator` during the update loop to avoid `ConcurrentModificationException`.

Score per hit is fixed at **10 points**, awarded in `Brick.onHit()` regardless of hit count or brick type.

---

## Score & High Score

- **Score** is tracked in `GameManager.gameScore` and incremented by 10 on every brick hit.
- **High score** is loaded from `highscore.txt` at startup via `GameManager.loadHighScore()`.
- At the end of each game (win or lose), `GameManager.setNewHighScore()` is called. If the current score beats the stored high score, the file is overwritten.
- Both values are rendered in the bottom-left corner of the game window each frame.
