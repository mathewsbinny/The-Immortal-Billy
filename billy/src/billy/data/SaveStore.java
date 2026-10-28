package billy.data;

import billy.GameException;
import billy.model.Account;
import billy.model.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Persists accounts and per-account player saves in a SQLite database.
 */
public class SaveStore {

    private final String dbName;

    public SaveStore(String dbName) throws GameException {
        this.dbName = dbName;
        createSchema();
    }

    private void createSchema() throws GameException {
        try (Connection conn = Database.connect(dbName); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS accounts("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT UNIQUE NOT NULL,"
                    + "password TEXT NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS saves("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "account_id INTEGER NOT NULL,"
                    + "slot INTEGER NOT NULL,"
                    + "player_name TEXT NOT NULL,"
                    + "room_id INTEGER NOT NULL DEFAULT 1,"
                    + "hp REAL NOT NULL,"
                    + "max_hp REAL NOT NULL,"
                    + "ap REAL NOT NULL,"
                    + "dp REAL NOT NULL,"
                    + "mp INTEGER NOT NULL,"
                    + "max_mp INTEGER NOT NULL DEFAULT 10,"
                    + "xp INTEGER NOT NULL,"
                    + "level INTEGER NOT NULL,"
                    + "deaths INTEGER NOT NULL DEFAULT 0,"
                    + "monsters_killed INTEGER NOT NULL DEFAULT 0,"
                    + "rooms_visited INTEGER NOT NULL DEFAULT 0,"
                    + "play_time REAL NOT NULL DEFAULT 0,"
                    + "inventory TEXT NOT NULL DEFAULT '',"
                    + "flags TEXT NOT NULL DEFAULT '',"
                    + "UNIQUE(account_id, slot))");
        } catch (SQLException e) {
            throw new GameException("Could not initialize save database: " + e.getMessage(), e);
        }
    }

    public Account createAccount(String username, String password) throws GameException {
        String insert = "INSERT INTO accounts(username, password) VALUES (?, ?)";
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("That username is already taken.", e);
        }
        String select = "SELECT id FROM accounts WHERE username = ?";
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"), username, password);
                }
            }
        } catch (SQLException e) {
            throw new GameException("Account was created but could not be looked up.", e);
        }
        throw new GameException("Account was not created.");
    }

    public Account authenticate(String username, String password) throws GameException {
        String sql = "SELECT id FROM accounts WHERE username = ? AND password = ?";
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"), username, password);
                }
            }
        } catch (SQLException e) {
            throw new GameException("Could not log in: " + e.getMessage(), e);
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available.", e);
        }
    }

    public void updatePassword(int accountId, String newPassword) throws GameException {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashPassword(newPassword));
            ps.setInt(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Could not update password: " + e.getMessage(), e);
        }
    }

    public void deleteAccount(int accountId) throws GameException {
        try (Connection conn = Database.connect(dbName)) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM saves WHERE account_id = ?")) {
                ps.setInt(1, accountId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM accounts WHERE id = ?")) {
                ps.setInt(1, accountId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new GameException("Could not delete account: " + e.getMessage(), e);
        }
    }

    /** Deletes a single save slot for the account, leaving the account intact. */
    public void deleteSave(int accountId, int slot) throws GameException {
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM saves WHERE account_id = ? AND slot = ?")) {
            ps.setInt(1, accountId);
            ps.setInt(2, slot);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GameException("Could not delete save: " + e.getMessage(), e);
        }
    }

    /** Creates a fresh player for the account (with the starting attack learned). */
    public Player newPlayer(int accountId, String playerName) {
        Player player = new Player();
        player.setAccountId(accountId);
        player.setName(playerName);
        player.learnAttack(1); // Punch is always known
        return player;
    }

    public Player loadPlayer(int accountId, int slot) throws GameException {
        String sql = "SELECT * FROM saves WHERE account_id = ? AND slot = ?";
        try (Connection conn = Database.connect(dbName);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setInt(2, slot);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Player player = new Player();
                player.setAccountId(accountId);
                player.setSaveSlot(slot);
                player.setName(rs.getString("player_name"));
                player.setCurrentRoomId(rs.getInt("room_id"));
                player.setHp(rs.getDouble("hp"));
                player.setMaxHp(rs.getDouble("max_hp"));
                player.setAp(rs.getDouble("ap"));
                player.setDp(rs.getDouble("dp"));
                player.setMp(rs.getInt("mp"));
                player.setMaxMp(rs.getInt("max_mp"));
                player.setXp(rs.getInt("xp"));
                player.setLevel(rs.getInt("level"));
                player.setDeaths(rs.getInt("deaths"));
                player.setMonstersKilled(rs.getInt("monsters_killed"));
                player.setRoomsVisited(rs.getInt("rooms_visited"));
                player.setPlayTime(rs.getDouble("play_time"));
                player.getInventory().addAll(decodeInventory(rs.getString("inventory")));
                player.getFlags().putAll(decodeFlags(rs.getString("flags")));
                return player;
            }
        } catch (SQLException e) {
            throw new GameException("Could not load save: " + e.getMessage(), e);
        }
    }

    public void savePlayer(Player player, int slot) throws GameException {
        try (Connection conn = Database.connect(dbName)) {
            boolean exists = false;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM saves WHERE account_id = ? AND slot = ?")) {
                ps.setInt(1, player.getAccountId());
                ps.setInt(2, slot);
                try (ResultSet rs = ps.executeQuery()) {
                    exists = rs.next();
                }
            }
            if (exists) {
                String sql = "UPDATE saves SET player_name=?, room_id=?, hp=?, max_hp=?, ap=?, dp=?, mp=?, max_mp=?, xp=?, level=?,"
                        + " deaths=?, monsters_killed=?, rooms_visited=?, play_time=?, inventory=?, flags=? WHERE account_id=? AND slot=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    bindPlayerFields(ps, player, 1);
                    ps.setInt(17, player.getAccountId());
                    ps.setInt(18, slot);
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO saves(account_id, slot, player_name, room_id, hp, max_hp, ap, dp, mp, max_mp, xp, level,"
                        + " deaths, monsters_killed, rooms_visited, play_time, inventory, flags)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, player.getAccountId());
                    ps.setInt(2, slot);
                    bindPlayerFields(ps, player, 3);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new GameException("Could not save game: " + e.getMessage(), e);
        }
    }

    private void bindPlayerFields(PreparedStatement ps, Player player, int start) throws SQLException {
        int i = start;
        ps.setString(i++, player.getName());
        ps.setInt(i++, player.getCurrentRoomId());
        ps.setDouble(i++, player.getHp());
        ps.setDouble(i++, player.getMaxHp());
        ps.setDouble(i++, player.getAp());
        ps.setDouble(i++, player.getDp());
        ps.setInt(i++, player.getMp());
        ps.setInt(i++, player.getMaxMp());
        ps.setInt(i++, player.getXp());
        ps.setInt(i++, player.getLevel());
        ps.setInt(i++, player.getDeaths());
        ps.setInt(i++, player.getMonstersKilled());
        ps.setInt(i++, player.getRoomsVisited());
        ps.setDouble(i++, player.getPlayTime());
        ps.setString(i++, encodeInventory(player.getInventory()));
        ps.setString(i, encodeFlags(player.getFlags()));
    }

    // ---- serialization (delimiter-based; content names/flags contain none of the delimiters) ----

    private String encodeInventory(java.util.List<Player.ItemInstance> inventory) {
        StringBuilder sb = new StringBuilder();
        for (Player.ItemInstance item : inventory) {
            if (sb.length() > 0) {
                sb.append(';');
            }
            sb.append(item.itemId).append('|').append(item.name).append('|').append(item.type).append('|').append(item.equipped ? 1 : 0);
        }
        return sb.toString();
    }

    private java.util.List<Player.ItemInstance> decodeInventory(String value) {
        java.util.List<Player.ItemInstance> inventory = new java.util.ArrayList<>();
        if (value == null || value.isEmpty()) {
            return inventory;
        }
        for (String part : value.split(";")) {
            if (part.isEmpty()) {
                continue;
            }
            String[] fields = part.split("\\|", -1);
            if (fields.length < 4) {
                continue;
            }
            inventory.add(new Player.ItemInstance(Integer.parseInt(fields[0]), fields[1], fields[2], "1".equals(fields[3])));
        }
        return inventory;
    }

    private String encodeFlags(java.util.Map<String, String> flags) {
        StringBuilder sb = new StringBuilder();
        for (java.util.Map.Entry<String, String> entry : flags.entrySet()) {
            if (sb.length() > 0) {
                sb.append(';');
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.toString();
    }

    private java.util.Map<String, String> decodeFlags(String value) {
        java.util.Map<String, String> flags = new java.util.LinkedHashMap<>();
        if (value == null || value.isEmpty()) {
            return flags;
        }
        for (String part : value.split(";")) {
            int eq = part.indexOf('=');
            if (eq > 0) {
                flags.put(part.substring(0, eq), part.substring(eq + 1));
            }
        }
        return flags;
    }
}
