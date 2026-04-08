# 🏰 Dungeon of Trials

### Text-Based Adventure Game

---

##  Overview

**Dungeon of Trials** is a command-line, text-based adventure game where the player explores a mysterious dungeon, solves puzzles, and survives encounters with monsters to reach the final victory.

The game focuses on **decision-making, puzzle-solving, and resource management**, rather than traditional RPG combat systems.

---

## 🎮 Features

### 🧩 Puzzle-Based Trials

* Each trial has two variants: **A or B**
* Different choices lead to different outcomes
* Rewards and penalties depend on decisions

### ⚔️ Combat System (Clash-Based)

* Combat is simple and turn-based:

  * Player loses **1 HP**
  * Monster loses **1 HP**
* No complex damage system

### 🗡️ Sword Mechanic

* The **only weapon** in the game
* Instantly defeats enemies
* Has special rules:

  * Limited use
  * May disappear after trials
  * Can include penalties

### ❤️ Health System

* Start with **5 HP**
* Max HP can increase up to **10**
* Can decrease depending on choices

### 🎒 Inventory System

* Store and use items
* Includes potions and special items

### 💾 Save & Load

* Save your progress
* Load previous game state

---

## 🏗️ System Design (MVC)

The game follows the **Model-View-Controller (MVC)** pattern:

* **Model**
  Handles game logic (Player, Room, Puzzle, Monster, Item)

* **View**
  Displays output to the console (`GameView`)

* **Controller**
  Manages input and game flow (`GameController`)
 
<img width="840" height="571" alt="Screenshot 2026-04-08 at 1 51 55 AM" src="https://github.com/user-attachments/assets/4ca7fd81-b3d1-468e-a96b-cd9fbbdb61ae" />


---

## 🧠 Core Classes

* `Player` – Manages HP, inventory, and actions
* `Room` – Represents locations in the dungeon
* `Puzzle` – Handles trial logic
* `Monster` – Represents enemies
* `Combat` – Controls clash-based combat
* `Item` – Base class for all items
* `GameController` – Controls the game flow
* `GameView` – Handles display/output

---

## ▶️ How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/dungeon-of-trials.git
```

2. Open in your IDE (IntelliJ, VS Code, etc.)

3. Run the `Main` class

---

## 🧪 Example Commands

```text
move to room2
inspect room
take item
use potion
attack
flee
```

---

## 👥 Author

Created by: **Team Cobra – Spring 2026**


