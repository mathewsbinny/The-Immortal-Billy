package billy.combat;

import billy.data.ContentStore;
import billy.model.Attack;
import billy.model.Element;
import billy.model.Monster;
import billy.model.MonsterAttack;
import billy.model.Player;
import billy.model.Status;
import billy.ui.Console;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Turn-based combat between the player and a single monster.
 */
public class CombatEngine {

    public enum Outcome { VICTORY, DEFEAT, RAN_AWAY }

    private static final int CRIT_ROLL = 20;      // a natural 20 doubles damage
    private static final int MISS_MAX_ROLL = 3;   // monsters miss on a d20 roll of 1-3 (15%)
    private static final int STATUS_CHANCE_PERCENT = 25;

    private final Console console;
    private final ContentStore content;
    private final Random random;

    public CombatEngine(Console console, ContentStore content) {
        this(console, content, new Random());
    }

    public CombatEngine(Console console, ContentStore content, Random random) {
        this.console = console;
        this.content = content;
        this.random = random;
    }

    public Outcome fight(Player player, Monster monster, List<Attack> allAttacks) {
        List<Attack> spells = new ArrayList<>();
        for (Attack attack : allAttacks) {
            if ("Battle".equalsIgnoreCase(attack.getUiType())
                    && player.knowsAttack(attack.getId()) && attack.getId() != 1) {
                spells.add(attack);
            }
        }
        Attack punch = findAttack(allAttacks, 1);
        if (punch == null) {
            punch = new Attack(1, "Punch", "Battle", 0, 1, 0, 20, "You throw a punch.", Element.PHYSICAL);
        }

        Map<Status, Integer> playerStatuses = new EnumMap<>(Status.class);
        Map<Status, Integer> monsterStatuses = new EnumMap<>(Status.class);

        final double[] monsterHp = {monster.getMaxHp()};
        console.println();
        console.println("=== COMBAT: " + monster.getName() + " (" + monster.getCategory() + ") ===");
        console.println(monster.getName() + " attacks!");

        while (true) {
            // Player's turn: tick statuses, then act (unless frozen).
            boolean playerFrozen = tickStatuses(player, monster, monsterHp, playerStatuses, true);
            if (player.getHp() <= 0) {
                console.println("You have been defeated...");
                return Outcome.DEFEAT;
            }
            if (playerFrozen) {
                console.println("You are frozen solid and cannot act!");
            } else {
                status(player, monster, monsterHp[0]);
                console.println("  1. Attack");
                console.println("  2. Cast Spell");
                console.println("  3. Use Item");
                console.println("  4. Run Away");
                String choice = console.prompt("Action: ");
                if (choice == null) {
                    return Outcome.DEFEAT;
                }
                choice = choice.trim();

                if (choice.equals("1")) {
                    playerAttack(player, monster, monsterHp, punch, playerStatuses, monsterStatuses);
                } else if (choice.equals("2")) {
                    if (spells.isEmpty()) {
                        console.println("You have no battle spells yet.");
                        continue;
                    }
                    Attack spell = chooseSpell(spells, player);
                    if (spell == null) {
                        continue;
                    }
                    player.spendMp(spell.getMpLevel());
                    playerAttack(player, monster, monsterHp, spell, playerStatuses, monsterStatuses);
                    if (spell.getAddHp() > 0) {
                        player.setHp(Math.min(player.getMaxHp(), player.getHp() + spell.getAddHp()));
                    }
                } else if (choice.equals("3")) {
                    if (!useItem(player)) {
                        continue;
                    }
                } else if (choice.equals("4")) {
                    return Outcome.RAN_AWAY;
                } else {
                    console.println("Choose 1-4.");
                    continue;
                }

                if (monsterHp[0] <= 0) {
                    console.println(monster.getName() + " has been defeated!");
                    return Outcome.VICTORY;
                }
            }

            // Monster's turn.
            boolean monsterFrozen = tickStatuses(player, monster, monsterHp, monsterStatuses, false);
            if (monsterHp[0] <= 0) {
                console.println(monster.getName() + " has been defeated!");
                return Outcome.VICTORY;
            }
            if (monsterFrozen) {
                console.println("The " + monster.getName() + " is frozen solid!");
            } else {
                monsterTurn(player, monster, monsterHp, playerStatuses);
            }

            if (player.getHp() <= 0) {
                console.println("You have been defeated...");
                return Outcome.DEFEAT;
            }
        }
    }

    private void playerAttack(Player player, Monster monster, double[] monsterHp, Attack attack,
                              Map<Status, Integer> playerStatuses, Map<Status, Integer> monsterStatuses) {
        int roll = random.nextInt(20) + 1;
        boolean crit = roll == CRIT_ROLL;
        double multiplier = attack.getElement().multiplierVs(monster.getElement());
        double damage = Math.max(1, attack.getAddAp() + player.getAp() - monster.getDp()) * multiplier;
        if (crit) {
            damage *= 2;
        }
        if (playerStatuses.containsKey(Status.DAZED)) {
            damage *= Status.DAZED.getAttackModifier();
        }
        if (playerStatuses.containsKey(Status.BURN)) {
            damage *= Status.BURN.getAttackModifier();
        }
        damage = Math.max(1, damage);
        monsterHp[0] -= damage;

        StringBuilder message = new StringBuilder(attack.getDescription())
                .append(" (").append(fmt(damage)).append(" damage)");
        if (crit) {
            message.append(" CRITICAL HIT!");
        } else if (multiplier > 1.0) {
            message.append(" It's super effective!");
        } else if (multiplier < 1.0) {
            message.append(" It's not very effective...");
        }
        console.println(message.toString());

        applyStatusToMonster(monster, attack.getElement(), monsterStatuses, crit);
    }

    private void applyStatusToMonster(Monster monster, Element element, Map<Status, Integer> target, boolean crit) {
        Status status = element.status();
        if (status == null) {
            return;
        }
        if (crit || random.nextInt(100) < STATUS_CHANCE_PERCENT) {
            target.put(status, status.getDuration());
            console.println("The " + monster.getName() + " is " + status.getLabel() + "!");
        }
    }

    private void applyStatusToPlayer(Player player, Element element, Map<Status, Integer> target, boolean crit) {
        Status status = element.status();
        if (status == null) {
            return;
        }
        if (crit || random.nextInt(100) < STATUS_CHANCE_PERCENT) {
            target.put(status, status.getDuration());
            console.println("You are " + status.getLabel() + "!");
        }
    }

    /**
     * Applies damage-over-time effects and decrements ailment durations.
     * Returns true when the entity was frozen at the start of the turn and
     * must skip it.
     */
    private boolean tickStatuses(Player player, Monster monster, double[] monsterHp,
                                 Map<Status, Integer> statuses, boolean isPlayer) {
        boolean frozen = statuses.containsKey(Status.FROZEN);
        double maxHp = isPlayer ? player.getMaxHp() : monster.getMaxHp();
        for (Status dot : new Status[]{Status.POISON, Status.BURN}) {
            if (statuses.containsKey(dot) && dot.getDamagePerTurn() > 0) {
                double dmg = maxHp * dot.getDamagePerTurn();
                if (isPlayer) {
                    player.setHp(player.getHp() - dmg);
                    console.println("You take " + fmt(dmg) + " damage from " + dot.name().toLowerCase() + ".");
                } else {
                    monsterHp[0] -= dmg;
                    console.println("The " + monster.getName() + " takes " + fmt(dmg)
                            + " damage from " + dot.name().toLowerCase() + ".");
                }
            }
        }
        for (Status status : Status.values()) {
            Integer turns = statuses.get(status);
            if (turns == null) {
                continue;
            }
            if (turns <= 1) {
                statuses.remove(status);
            } else {
                statuses.put(status, turns - 1);
            }
        }
        return frozen;
    }

    private Attack findAttack(List<Attack> attacks, int id) {
        for (Attack attack : attacks) {
            if (attack.getId() == id) {
                return attack;
            }
        }
        return null;
    }

    private Attack chooseSpell(List<Attack> spells, Player player) {
        console.println("Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Attack spell = spells.get(i);
            String note = player.hasMp(spell.getMpLevel()) ? "" : " (needs " + spell.getMpLevel() + " MP)";
            console.println("  " + (i + 1) + ". " + spell.getName() + note);
        }
        String input = console.prompt("Spell # (or BACK): ");
        if (input == null || input.equalsIgnoreCase("back")) {
            return null;
        }
        try {
            int index = Integer.parseInt(input.trim()) - 1;
            if (index >= 0 && index < spells.size()) {
                Attack spell = spells.get(index);
                if (!player.hasMp(spell.getMpLevel())) {
                    console.println("Not enough MP to cast that.");
                    return null;
                }
                return spell;
            }
        } catch (NumberFormatException ignored) {
        }
        console.println("Invalid spell.");
        return null;
    }

    private boolean useItem(Player player) {
        List<Player.ItemInstance> consumables = new ArrayList<>();
        for (Player.ItemInstance item : player.getInventory()) {
            if ("Consumable".equalsIgnoreCase(item.type)) {
                consumables.add(item);
            }
        }
        if (consumables.isEmpty()) {
            console.println("You have no consumables.");
            return false;
        }
        console.println("Consumables:");
        for (int i = 0; i < consumables.size(); i++) {
            console.println("  " + (i + 1) + ". " + consumables.get(i).name);
        }
        String input = console.prompt("Item # (or BACK): ");
        if (input == null || input.equalsIgnoreCase("back")) {
            return false;
        }
        try {
            int index = Integer.parseInt(input.trim()) - 1;
            if (index >= 0 && index < consumables.size()) {
                Player.ItemInstance inst = consumables.get(index);
                billy.model.Item item = content.getItem(inst.itemId);
                if (item != null) {
                    player.setMaxHp(player.getMaxHp() + item.getAddMaxHp());
                    player.setHp(Math.min(player.getMaxHp(), player.getHp() + item.getAddHp()));
                    player.setAp(player.getAp() + item.getAddAp());
                    player.setDp(player.getDp() + item.getAddDp());
                    player.setMaxMp(player.getMaxMp() + item.getAddMp());
                    player.setMp(Math.min(player.getMaxMp(), player.getMp() + item.getAddMp()));
                }
                player.removeItem(inst.itemId);
                console.println("You use " + inst.name + ".");
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        console.println("Invalid item.");
        return false;
    }

    private void monsterTurn(Player player, Monster monster, double[] monsterHp,
                             Map<Status, Integer> playerStatuses) {
        MonsterAttack attack = pickMonsterAttack(monster);
        if (attack == null) {
            basicMonsterAttack(player, monster, playerStatuses);
            return;
        }

        // An attack may both heal and deal damage (e.g. the boss's Stone Skin).
        if (attack.getAddHp() > 0) {
            monsterHp[0] = Math.min(monster.getMaxHp(), monsterHp[0] + attack.getAddHp());
            console.println(attack.getDescription() + " (the " + monster.getName()
                    + " heals " + fmt(attack.getAddHp()) + ")");
        }
        if (attack.getAddAp() <= 0) {
            return;
        }

        int roll = random.nextInt(20) + 1;
        if (roll <= MISS_MAX_ROLL) {
            console.println("The " + monster.getName() + "'s " + attack.getName() + " misses!");
            return;
        }
        boolean crit = roll == CRIT_ROLL;
        double damage = Math.max(1, attack.getAddAp() + monster.getAp() - player.getDp());
        if (crit) {
            damage *= 2;
        }
        if (playerStatuses.containsKey(Status.DAZED)) {
            damage *= Status.DAZED.getAttackModifier();
        }
        if (playerStatuses.containsKey(Status.BURN)) {
            damage *= Status.BURN.getAttackModifier();
        }
        damage = Math.max(1, damage);
        player.setHp(player.getHp() - damage);
        console.println(attack.getDescription() + " (" + fmt(damage) + " damage)"
                + (crit ? " CRITICAL HIT!" : ""));
        applyStatusToPlayer(player, monster.getElement(), playerStatuses, crit);
    }

    private void basicMonsterAttack(Player player, Monster monster, Map<Status, Integer> playerStatuses) {
        int roll = random.nextInt(20) + 1;
        if (roll <= MISS_MAX_ROLL) {
            console.println("The " + monster.getName() + " attacks... and misses!");
            return;
        }
        boolean crit = roll == CRIT_ROLL;
        double damage = Math.max(1, monster.getAp() - player.getDp());
        if (crit) {
            damage *= 2;
        }
        damage = Math.max(1, damage);
        player.setHp(player.getHp() - damage);
        console.println("The " + monster.getName() + " attacks you for " + fmt(damage) + " damage."
                + (crit ? " CRITICAL HIT!" : ""));
        applyStatusToPlayer(player, monster.getElement(), playerStatuses, crit);
    }

    private MonsterAttack pickMonsterAttack(Monster monster) {
        List<MonsterAttack> attacks = monster.getAttacks();
        if (attacks.isEmpty()) {
            return null;
        }
        double total = 0;
        for (MonsterAttack attack : attacks) {
            total += Math.max(0.01, attack.getProbability());
        }
        double roll = random.nextDouble() * total;
        for (MonsterAttack attack : attacks) {
            roll -= Math.max(0.01, attack.getProbability());
            if (roll <= 0) {
                return attack;
            }
        }
        return attacks.get(attacks.size() - 1);
    }

    private void status(Player player, Monster monster, double monsterHp) {
        console.println();
        console.println("You: " + fmt(player.getHp()) + "/" + fmt(player.getMaxHp()) + " HP | "
                + monster.getName() + ": " + fmt(Math.max(0, monsterHp)) + "/" + fmt(monster.getMaxHp()) + " HP");
    }

    private String fmt(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(Math.round(value * 10) / 10.0);
    }
}
