package billy.combat;

import billy.data.ContentStore;
import billy.model.Attack;
import billy.model.Monster;
import billy.model.MonsterAttack;
import billy.model.Player;
import billy.ui.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Turn-based combat between the player and a single monster.
 */
public class CombatEngine {

    public enum Outcome { VICTORY, DEFEAT, RAN_AWAY }

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

        final double[] monsterHp = {monster.getMaxHp()};
        console.println();
        console.println("=== COMBAT: " + monster.getName() + " (" + monster.getCategory() + ") ===");
        console.println(monster.getName() + " attacks!");

        while (true) {
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
                double dmg = Math.max(1, (punch == null ? 20 : punch.getAddAp()) + player.getAp() - monster.getDp());
                monsterHp[0] -= dmg;
                console.println("You throw a punch for " + fmt(dmg) + " damage.");
            } else if (choice.equals("2")) {
                if (spells.isEmpty()) {
                    console.println("You have no battle spells yet.");
                    continue;
                }
                Attack spell = chooseSpell(spells, player);
                if (spell == null) {
                    continue;
                }
                monsterHp[0] -= Math.max(1, spell.getAddAp() + player.getAp() - monster.getDp());
                if (spell.getAddHp() > 0) {
                    player.setHp(Math.min(player.getMaxHp(), player.getHp() + spell.getAddHp()));
                }
                console.println(spell.getDescription());
            } else if (choice.equals("3")) {
                if (!useItem(player)) {
                    continue;
                }
            } else if (choice.equals("4")) {
                console.println("You flee back the way you came.");
                return Outcome.RAN_AWAY;
            } else {
                console.println("Choose 1-4.");
                continue;
            }

            if (monsterHp[0] <= 0) {
                console.println("You defeated the " + monster.getName() + "!");
                return Outcome.VICTORY;
            }

            monsterTurn(player, monster, monsterHp);

            if (player.getHp() <= 0) {
                console.println("You have been defeated...");
                return Outcome.DEFEAT;
            }
        }
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
            String note = player.getMp() >= spell.getMpLevel() ? "" : " (needs MP " + spell.getMpLevel() + ")";
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
                if (player.getMp() < spell.getMpLevel()) {
                    console.println("Your MP level is too low to cast that.");
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
                    player.setMp(player.getMp() + item.getAddMp());
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

    private void monsterTurn(Player player, Monster monster, double[] monsterHp) {
        MonsterAttack attack = pickMonsterAttack(monster);
        if (attack == null) {
            double dmg = Math.max(1, monster.getAp() - player.getDp());
            player.setHp(player.getHp() - dmg);
            console.println("The " + monster.getName() + " attacks you for " + fmt(dmg) + " damage.");
            return;
        }
        if (attack.getAddHp() > 0) {
            monsterHp[0] = Math.min(monster.getMaxHp(), monsterHp[0] + attack.getAddHp());
            console.println(attack.getDescription() + " (the " + monster.getName() + " heals)");
            return;
        }
        double dmg = Math.max(1, attack.getAddAp() + monster.getAp() - player.getDp());
        player.setHp(player.getHp() - dmg);
        console.println(attack.getDescription() + " (" + fmt(dmg) + " damage)");
    }

    private MonsterAttack pickMonsterAttack(Monster monster) {
        List<MonsterAttack> attacks = monster.getAttacks();
        if (attacks.isEmpty()) {
            return null;
        }
        return attacks.get(random.nextInt(attacks.size()));
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
