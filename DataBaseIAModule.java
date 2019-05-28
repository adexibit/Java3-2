package server;

import java.sql.*;

public class DataBaseIAModule {

    public static Connection conn = null;
    public static Statement stmt = null;
    public static ResultSet resSet = null;
    public static PreparedStatement ps = null;

    public DataBaseIAModule() {
        try {
            init();
            stmt = conn.createStatement();
            stmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'nick' text, 'password' text);");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:CHAT.s3db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            resSet.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean register(String nick, String pass) {
        try {
            init();
            ps = conn.prepareStatement("SELECT * FROM users WHERE nick = ?");
            ps.setString(1, nick);
            resSet = ps.executeQuery();
            if (resSet.next()) return false;
            else {
                ps = conn.prepareStatement("INSERT INTO users (nick, password) VALUES (?, ?)");
                ps.setString(1, nick);
                ps.setString(2, pass);
                ps.executeUpdate();
                closeDB();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNick(int id) {
        if (id != 0) {
            try {
                init();
                ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
                ps.setInt(1, id);
                resSet = ps.executeQuery();
                return resSet.getString("nick");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "Незарегистрирован";
    }

    public int login(String nick, String pass) {
        try {
            init();
            ps = conn.prepareStatement("SELECT * FROM users WHERE nick = ?");
            ps.setString(1, nick);
            resSet = ps.executeQuery();
            if (resSet.next()) {
                int id = resSet.getInt("id");
                String password = resSet.getString("password");
                if (pass.equals(password)) return id;
            }
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void changeNick(int id, String nick) {

        if (id != 0) {
            try {
                init();
                ps = conn.prepareStatement("UPDATE users SET nick = ? WHERE id = ?");
                ps.setString(1, nick);
                ps.setInt(2, id);
                ps.executeUpdate();
                closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}


