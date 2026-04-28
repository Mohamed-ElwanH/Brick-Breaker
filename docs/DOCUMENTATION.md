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
**Game loop target:** ~62.5 FPS (16 ms timer interval)  
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
Called every frame. Skips update if the game is paused. If the player has lost, listens for Enter to restart and then returns early (rendering still continues). On each active frame:
1. Updates ball position and collider.
2. Checks ball–paddle collision (snap and force-upward method).
3. Moves paddle based on input.
4. Checks paddle–ball collider overlap via the abstract collider system.
5. Updates paddle collider.
6. Checks ball against all four borders.
7. Checks paddle against left/right borders.
8. Iterates bricks: removes destroyed ones, checks ball collision for the rest.
9. Evaluates win/lose conditions.

```java
protected void paintComponent(Graphics g)
```
Renders the background, ball, paddle, and all bricks. Calls `createGameText()` to draw the HUD and overlay screens.

```java
private void createGameText(Graphics g)
```
Draws the score and high score text at the bottom of the screen. Also conditionally overlays the win, lose, or pause screen on top of the game when applicable.

```java
private void setBricks()
```
Generates a 10×7 grid of bricks filling the top of the 800 px wide window. Bricks start at y=0 with no vertical offset. Each brick is randomly assigned a color type and a hit count between 1 and 3.

```java
private void restartGame()
```
Resets score, ball position/speed, and paddle position. Clears and regenerates the brick grid. Re-fetches collider references and restarts the game loop.

```java
private void checkBallBorderCollision()
```
Handles ball bouncing off the left, right, and top walls. Returns immediately if the panel dimensions are not yet set. If the ball reaches the bottom edge, it calls `GameManager.setBallLife(false)`, triggering the lose condition. A position clamp is applied on wall hits to prevent the ball from clipping through.

```java
private void checkPaddleBorderCollision()
```
Clamps the paddle position to stay within the left and right window boundaries. Returns immediately if panel dimensions are not yet set.

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
| `HIGHSCORE_FILE` | `static final File` | Points to `highscore.txt` in the working directory |

**Key methods:**

```java
public static void loadHighScore() throws IOException
```
Reads the high score from `highscore.txt` in the current working directory. Gracefully falls back to 0 if the file is missing, unreadable, or contains non-integer data.

```java
private static void setHighscore(int newHighScore)
```
Writes the new high score to `highscore.txt`. Errors are caught and logged to stdout.

```java
public static void setNewHighScore(int newHighScore)
```
Compares the given score against the current high score and calls `setHighscore()` only if it is higher.

```java
public static boolean isWin()
```
Returns `true` if the ball is still alive (`ballAlive == true`) and the brick list is empty.

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
| `→` Right Arrow | Move paddle right (held) |
| `←` Left Arrow | Move paddle left (held) |
| `Escape` | Toggle pause (disabled on win/lose screens) |
| `Enter` | Restart game (on lose screen only) |

**Key methods:**

```java
public void keyPressed(KeyEvent e)   // Sets the relevant flag to true
public void keyReleased(KeyEvent e)  // Clears movement flags on release; also clears Enter on release
```

Static getters: `isRightKeyPressed()`, `isLeftKeyPressed()`, `isPaused()`, `isEnterKeyPressed()`, `setEnterKey(boolean)`.

> **Note:** The Escape key does **not** toggle pause when the win or lose screen is active. This guard is in `keyPressed()`, checking `GameManager.isLose()` and `GameManager.isWin()`.

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
Moves the collider rectangle to match the current object position using `setLocation()`. Throws `RuntimeException` if the collider is null.

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
The core brick collision algorithm. Checks if the ball's rectangle intersects the brick's rectangle. If it does, calculates the four overlap amounts (top, bottom, left, right) between the two rectangles. The **smallest overlap** determines which axis to invert:

- Smallest overlap is top or bottom → invert `speedY`
- Smallest overlap is left or right → invert `speedX`

> **Note:** When the minimum overlap is shared by overlaps from both axes simultaneously (a true corner hit), the code evaluates both `if` branches independently rather than using `else if`. This means both `speedX` and `speedY` may be inverted in the same frame on exact corner collisions.

Then calls `brick.onHit()`.

```java
public void checkPaddleCollision(Paddle paddleObj)
```
If the ball intersects the paddle, snaps the ball to sit exactly on top of the paddle surface (`paddleObj.getGameObjPos().y - ballHeight`) and forces `speedY` to `-Math.abs(speedY)`, preventing the ball from clipping through or bouncing downward.

```java
public void onHit()   // Inverts speedY — called by BallCollider when the abstract collider path triggers
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
Reads `InputHandler.isRightKeyPressed()` and `isLeftKeyPressed()` and adjusts the paddle's X position by `±paddleSpeedX`. Border clamping is handled separately in `GamePanel.checkPaddleBorderCollision()`.

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
`type` is the color name string (e.g., `"blueBrick"`), used to build the sprite path as `/UIElements/<type>.png`. `hits` is the number of hits required to destroy this brick (1–3). Attaches a `BoxCollider`.

**Sprite states per brick type:**

| Hits remaining after decrement | Sprite loaded |
|---|---|
| 2 | `/<type>Split.png` |
| 1 | `/<type>Broken.png` |
| 0 | `isDestroyed = true` — removed from list next frame |

> **Note:** The sprite swap checks are against the value of `hitsToBreak` *after* it has been decremented. A brick starting with 1 hit goes directly to `isDestroyed = true` without loading a cracked or broken sprite, since the decrement takes it straight to 0.

**Key methods:**

```java
public void onHit()
```
- Awards `hitScore` (10 points) via `GameManager.setGameScore()`.
- Decrements `hitsToBreak`.
- Swaps sprite to `Split` or `Broken` variant based on the new `hitsToBreak` value.
- Sets `isDestroyed = true` when `hitsToBreak` reaches 0, causing `GamePanel` to remove it from the list on the next frame via an `Iterator`.

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
Calls `gameObject.onHit()` on the parent `Ball`, which inverts `speedY`.

> **Note:** This is used for the secondary paddle–ball collision path: `GamePanel.update()` calls `paddleCollider.Collided(ballCollider.getCollider())`. If the rectangles intersect, `BallCollider.OnCollisionEnter()` fires and inverts the ball's Y speed. The primary paddle collision (with snapping) is handled separately in `Ball.checkPaddleCollision()`, which runs first in the update loop. Brick–ball collision is handled directly in `Ball.checkSideCollision()` and does not go through this collider path.

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

The loop runs continuously. On win, `gameLoop.stop()` is called followed by an explicit `repaint()` to render the win screen. On lose, the timer keeps running so the lose screen renders and the restart key can be detected.

---

## Collision System

There are two separate collision approaches used in the project:

### 1. Ball vs. Brick — Overlap-based side detection (`Ball.checkSideCollision`)

Calculates how much the ball rectangle overlaps each of the four sides of the brick rectangle. The side with the **minimum overlap** is assumed to be the side the ball entered from, and the corresponding velocity axis is inverted. Both overlap comparisons use independent `if` statements (not `else if`), so a precise corner hit can invert both axes in the same frame.

### 2. Ball vs. Paddle — Snap and force-upward (`Ball.checkPaddleCollision`)

On intersection, the ball is snapped to sit exactly on top of the paddle, and `speedY` is forced to `-Math.abs(speedY)`. This is the primary paddle collision path and runs first in `update()`.

### 3. Paddle vs. Ball — Collider-based secondary path (`paddleCollider.Collided`)

`GamePanel.update()` also calls `paddleCollider.Collided(ballCollider.getCollider())`, routing through the abstract `Collider` system. If the rectangles overlap, this calls `BallCollider.OnCollisionEnter()` → `Ball.onHit()`, which inverts `speedY`. Because `checkPaddleCollision()` already snapped the ball above the paddle earlier in the same frame, this secondary check typically finds no intersection. It acts as a safety net for edge cases.

---

## Brick System

On game start (and restart), `GamePanel.setBricks()` fills a **10 columns × 7 rows** grid:

- Each brick is 80 × 20 px.
- The grid starts at y=0 with no vertical offset applied in `setBricks()`.
- Color type is chosen randomly from 6 options via `chooseRandomBrick()`.
- Hit count is randomly assigned: `Random.nextInt(3) + 1` → values 1, 2, or 3.
- Bricks are stored in a `static ArrayList<Brick>`. Destroyed bricks are removed via an `Iterator` during the update loop to avoid `ConcurrentModificationException`.

Score per hit is fixed at **10 points**, awarded in `Brick.onHit()` regardless of hit count or brick type.

---

## Score & High Score

- **Score** is tracked in `GameManager.gameScore` and incremented by 10 on every brick hit.
- **High score** is loaded from `highscore.txt` (relative to the working directory) at startup via `GameManager.loadHighScore()`.
- When the game is won, `GameManager.setNewHighScore()` is called immediately in `gameWon()` before stopping the loop. On a loss, it is called in `gameLost()` each frame until the player restarts.
- Both values are rendered near the bottom-left corner of the game window (at y=500 and y=530) each frame.

---

## Known Issues & TODOs

- **`update()` loop comment:** The brick iteration block in `GamePanel.update()` has an inline comment `//TO DO: put inside a method`. This logic can be extracted into a private `checkBrickCollisions()` method for clarity.
- **`createGameText` placement comment:** The method has a comment suggesting it might be better placed in `BBUI`. Currently it mixes HUD rendering with overlay screen logic.
- **Corner collision:** Because `checkSideCollision` uses independent `if` statements instead of `else if`, an exact corner hit inverts both `speedX` and `speedY` simultaneously, which may not always feel correct.
- **Lose screen wording:** The lose screen displays `"LOOOSE!!!"` — this is likely unintentional.
- **No lives system:** The game currently ends immediately when the ball hits the bottom. The comment `//TO DO: change so that the player loses a life` in `checkBallBorderCollision()` notes this as a planned feature.