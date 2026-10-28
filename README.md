# The Immortal Billy

A text-based roguelike adventure written in Java with a SQLite save backend.

You play Billy, an adventurer who awakens in a central hub with four elemental
districts branching off it (Earth, Water, Air, Fire). Each district is a
randomly-ordered chain of puzzle, battle, and special rooms ending in a
miniboss. Defeat all four minibosses to collect their keys, return to the hub,
and unlock the Throne Room to face a five-phase final boss.

## Requirements

- JDK 17+ (tested on JDK 26)
- The bundled SQLite JDBC driver: `resources/libraries/sqlite-jdbc-3.43.0.0.jar`

## Running

Compile and run from the `billy/` directory:

```powershell
cd billy
javac -encoding UTF-8 -cp "resources/libraries/sqlite-jdbc-3.43.0.0.jar" -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
java --enable-native-access=ALL-UNNAMED -cp "out;resources/libraries/sqlite-jdbc-3.43.0.0.jar" billy.Main
```

> `--enable-native-access=ALL-UNNAMED` only silences a harmless JDK 24+ warning
> from the SQLite driver; it is not required on older JDKs.

After logging in you land in an account menu where you can Play Game, change
your password, or delete your account. Passwords are stored as SHA-256 hashes.
Each account has three save slots, so you can keep three separate playthroughs
or three snapshots of the same run. Progress (inventory, equipment, stats,
defeated monsters, room, keys) is persisted to `save.db` automatically on
`QUIT` or `SAVE`.

## Testing

The test suite uses JUnit 5. A standalone runner jar is downloaded on first use
(cached under `resources/libraries/`). Run the tests from `billy/` with:

```powershell
powershell -ExecutionPolicy Bypass -File test/run-tests.ps1
```

Tests cover the model (`Player`), persistence (`SaveStore`), content loading
(`ContentStore`), world generation (`World`), turn-based combat
(`CombatEngine`), and the interactive flows (`MainMenu`, `Game`). Interactive
components are tested by injecting scripted input through `Console`'s stream
constructor.

A deterministic end-to-end playthrough (all four districts, the four keys, and
the final boss) runs with:

```powershell
powershell -ExecutionPolicy Bypass -File test/playthrough.ps1
```

## Gameplay

- Move with `NORTH/SOUTH/EAST/WEST/UP` (in the hub) and `FORWARD/BACK` (in a
  district). Single letters (`n`, `s`, `e`, `w`, `u`, `f`, `b`) also work.
- `SEARCH` (or `TAKE`) a room to pick up loot.
- `USE <item>`, `EQUIP <item>`, `UNEQUIP <item>`, `INVENTORY`.
- `SPECIAL` (or `ACTION`) to see a puzzle room's available actions.
- `CAST <spell>` to cast a known spell outside combat (and solve spell puzzles).
- `LEVELUP` to spend XP on a stat (Max HP / Attack / Defense / Magic).
- `STATS`, `MAP`, `HELP`.
- `SAVE` to write your progress to a save slot (1-3).
- `QUIT` to save and return to the main menu.
- `EXIT` to save and close the game.

Combat is turn-based: `Attack`, `Cast Spell`, `Use Item`, or `Run Away`.
Spells cost MP. Monster attacks are chosen by their authored probability and can
miss. `Run Away` returns you to the previous room. Defeating monsters grants XP
and can teach you spells; dying returns you to the hub (re-randomizing the
districts) with your inventory and progress intact.

Puzzle rooms are solved with special actions, the right item, or the right
spell; solving grants XP and rewards. Accumulated XP is spent manually via
`LEVELUP`.

District layouts are re-randomized at the start of each life and whenever you
re-enter a district from the hub.

## Project layout

```
billy/
  src/billy/
    Main.java            entry point
    Game.java            main game loop + command routing
    GameException.java   custom exception
    model/               domain objects (Player, Room, Monster, Item, Attack, ...)
    data/                Database, ContentStore (static content), SaveStore (accounts/saves)
    world/               World + randomized district generation
    combat/              turn-based CombatEngine
    ui/                  Console I/O and the login MainMenu
  resources/content/*.txt  authored content (rooms, monsters, items, attacks, puzzles, help)
  resources/libraries/     the SQLite JDBC driver
  legacy/                  the original (broken) implementation, preserved for reference
```

Static content lives in `resources/*.txt` as SQL and is loaded once into memory
by `ContentStore`. Player state is stored in `save.db` by `SaveStore`.

## Status

Implemented: accounts/login (hashed passwords + account management), three save
slots per account, save/load, the cross-shaped map with randomized district arms
(re-randomized per life and on re-entry), movement, loot pickup, consumables,
equipment, turn-based combat with an MP resource and weighted/missable monster
attacks, spell learning, puzzle rooms (action/item/spell), manual level-ups,
miniboss key drops, and a five-phase final boss / win condition. A 52-test suite
includes a deterministic end-to-end playthrough.

Balance has had a first tuning pass; further playtesting may suggest more
adjustments.


