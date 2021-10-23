package fr.mrcubee.groups.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author MrCubee
 * @since 1.0
 */
public class SQLDataBase {

    private final String host;
    private final String dataBase;
    private final String user;
    private final String password;

    private Connection connection;

    protected SQLDataBase(String host, String dataBase, String user, String password) {
        this.host = host;
        this.dataBase = dataBase;
        this.user = user;
        this.password = password;
    }

    private Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(
                        String.format("jdbc:mysql://%s/%s?characterEncoding=utf8", this.host, this.dataBase),
                        this.user, this.password);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            this.connection = null;
        }
        return this.connection;
    }

    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed())
                this.connection.close();
        } catch (SQLException ignored) {}
        this.connection = null;
    }

    public boolean hasPlayerGroup(UUID uuid) {
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;

        if (uuid == null)
            return false;
        connection = getConnection();
        if (connection == null)
            return false;
        try {
            statement = connection.prepareStatement("SELECT `uuid` FROM `groups` WHERE `uuid` = ?");
            statement.setString(1, uuid.toString().replaceAll("-", ""));
            resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public String getPlayerName(UUID uuid) {
        PreparedStatement statement;
        ResultSet resultSet;

        if (uuid == null)
            return null;
        connection = getConnection();
        if (connection == null)
            return null;
        try {
            statement = connection.prepareStatement("SELECT `name` FROM `mc_username` WHERE `uuid` = ? ORDER BY `register_date` DESC LIMIT 1");
            statement.setString(1, uuid.toString().replaceAll("-", ""));
            resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return resultSet.getString(1);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public boolean setPlayerName(UUID uuid, String name) {
        Connection connection;
        PreparedStatement statement;

        if (uuid == null)
            return false;
        connection = getConnection();
        if (connection == null)
            return false;
        try {
            statement = connection.prepareStatement("INSERT `mc_username` VALUES (?, ?, DEFAULT) ON DUPLICATE KEY UPDATE `name` = ?");
            statement.setString(1, uuid.toString().replaceAll("-", ""));
            statement.setString(2, name);
            statement.setString(3, name);
            return statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }


    public String getPlayerGroup(UUID uuid) {
        PreparedStatement statement;
        ResultSet resultSet;

        if (uuid == null)
            return null;
        connection = getConnection();
        if (connection == null)
            return null;
        try {
            statement = connection.prepareStatement("SELECT `group` FROM `groups` WHERE `uuid` = ?");
            statement.setString(1, uuid.toString().replaceAll("-", ""));
            resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;
            return resultSet.getString(1);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public boolean setPlayerGroup(UUID uuid, String groupName) {
        Connection connection;
        PreparedStatement statement;

        if (uuid == null)
            return false;
        connection = getConnection();
        if (connection == null)
            return false;
        if (groupName == null) {
            try {
                statement = connection.prepareStatement("DELETE FROM `groups` WHERE `uuid` = ?");
                statement.setString(1, uuid.toString().replaceAll("-", ""));
                return statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return false;
        }
        try {
            statement = connection.prepareStatement("INSERT `groups` VALUES (?, ?) ON DUPLICATE KEY UPDATE `group` = ?");
            statement.setString(1, uuid.toString().replaceAll("-", ""));
            statement.setString(2, groupName);
            statement.setString(3, groupName);
            return statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean close() {
        try {
            if (this.connection == null || this.connection.isClosed())
                return true;
            this.connection.close();
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public String toString() {
        return "DataBase{" +
                "host='" + this.host + '\'' +
                ", dataBase='" + this.dataBase + '\'' +
                ", user='" + this.user + '\'' +
                ", connection=" + this.connection +
                '}';
    }

    public static SQLDataBase create(String host, String dataBase, String user, String password) {
        if (host == null || dataBase == null || user == null || password == null)
            return null;
        return new SQLDataBase(host, dataBase, user, password);
    }
}
