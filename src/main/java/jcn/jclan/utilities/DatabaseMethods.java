package jcn.jclan.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseMethods {
    private Connection connection;

    public DatabaseMethods(Connection connection) {
        this.connection = connection;
    }

    // Создание клана
    public boolean createClan(Player player, String clanName, String clanPrefix){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clans (clancreator, clanname, clanprefix, members) VALUES (?, ?, ?, ?)");
            statement.setString(1, player.getName());
            statement.setString(2, clanName);
            statement.setString(3, clanPrefix);
            statement.setString(4, player.getName());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удаление клана по имени клана
    public void deleteClan(String clanName){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clans WHERE clanname = ?");
            statement.setString(1, clanName);
            statement.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    // Получение списка всех кланов и краткой информации о них
    public String getClansList() {
        StringBuilder info = new StringBuilder();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT clanname AS ClanName, members AS Members, clanprefix AS Prefix FROM clans;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String clanName = resultSet.getString("ClanName");
                String members = resultSet.getString("Members");
                String prefix = resultSet.getString("Prefix");
                int valueOfMembers = members.split(", ").length;

                info.append(ChatColor.RESET).append("Название: ").append(clanName).append("\n");
                info.append(ChatColor.RESET).append("Префикс: ").append(prefix).append("\n");
                info.append(ChatColor.RESET).append("Количество участников: ").append(valueOfMembers).append("\n");
                info.append("\n");
            }
            statement.close();
        } catch (Exception e) { e.printStackTrace(); }
        return info.toString();
    }

    // Получение информации об одном клане
    public List<String> getClanInfo(Player player){
        List<String> clanInfo = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT clanname, clanprefix, clancreator, members FROM clans WHERE INSTR(members, ?) > 0");
            statement.setString(1, player.getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                clanInfo.add(resultSet.getString("clanname"));
                clanInfo.add(resultSet.getString("clanprefix"));
                clanInfo.add(resultSet.getString("clancreator"));
                clanInfo.add(resultSet.getString("members"));
            }
            resultSet.close();
            statement.close();
            return clanInfo;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Получение названия клана по имени игрока
    public String getClanName(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT clanname FROM clans WHERE members LIKE ?");
            statement.setString(1, "%" + player.getName() + "%");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getString("clanname");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Получение списка участников клана
    public List<String> getClanMembers(String clanName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT members FROM clans WHERE clanname = ?");
            statement.setString(1, clanName);
            ResultSet resultSet = statement.executeQuery();
            List<String> members = new ArrayList<>();
            if (resultSet.next()) {
                String membersString = resultSet.getString("members");
                if (membersString != null && !membersString.isEmpty()) {
                    members = Arrays.asList(membersString.split(", "));
                }
            }
            return members;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }


    // Добавление участника в клан
    public boolean addMemberToClan(Player player, String clanName){
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clans SET members = CONCAT(members, ?) WHERE clanname = ?");
            statement.setString(1, ", " + player.getName());
            statement.setString(2, clanName);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Удаление участника в клан
    public boolean removeMemberFromClan(Player player){
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clans SET members = REPLACE(members, ?, '') WHERE members LIKE ?");
            statement.setString(1, ", " + player.getName());
            statement.setString(2, "%" + player.getName() + "%");
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получение префикса клана
    public String getClanPrefix(Player player){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT clanprefix FROM clans WHERE FIND_IN_SET(?, REPLACE(members, ' ', '')) > 0");
            statement.setString(1, player.getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getString("clanprefix");
        } catch (Exception e) {e.printStackTrace(); }
        return "";
    }

    // Проверка имени клана
    public boolean checkClanName(String clanName){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clans WHERE clanname = ?");
            statement.setString(1, clanName);
            ResultSet resultSet = statement.executeQuery();
            boolean exist = resultSet.next();
            resultSet.close();
            statement.close();
            return exist;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Изменить имя клана
    public void updateNameByClanName(String oldName, String newName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clans SET clanname = ? WHERE clanname = ?");
            statement.setString(1, newName);
            statement.setString(2, oldName);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Изменить префикс клана
    public void updatePrefixByClanName(String clanName, String newPrefix) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE clans SET clanprefix = ? WHERE clanname = ?");
            statement.setString(1, newPrefix);
            statement.setString(2, clanName);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
