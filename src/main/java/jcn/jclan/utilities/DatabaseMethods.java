package jcn.jclan.utilities;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseMethods {
    private final Connection connection;

    public DatabaseMethods(Connection connection) {
        this.connection = connection;
    }

    // Создание клана
    @SneakyThrows
    public boolean createClan(Player player, String clanName, String clanPrefix) {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO clans (clancreator, clanname, clanprefix, members) VALUES (?, ?, ?, ?)");
        statement.setString(1, player.getName());
        statement.setString(2, clanName);
        statement.setString(3, clanPrefix);
        statement.setString(4, player.getName());
        statement.executeUpdate();
        statement.close();
        return true;
    }


    // Удаление клана по имени клана
    @SneakyThrows
    public void deleteClan(String clanName){
        PreparedStatement statement = connection.prepareStatement("DELETE FROM clans WHERE clanname = ?");
        statement.setString(1, clanName);
        statement.executeUpdate();
    }

    // Получение списка всех кланов и краткой информации о них
    @SneakyThrows
    public List<String> getClansList() {
        List<String> clansInfo = new ArrayList<>();
        int i = 1;
        PreparedStatement statement = connection.prepareStatement("SELECT clanname AS ClanName, members AS Members, clanprefix AS Prefix, clancreator AS ClanCreator FROM clans;");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            StringBuilder info = new StringBuilder();
            String clanName = resultSet.getString("ClanName");
            String members = resultSet.getString("Members");
            String prefix = resultSet.getString("Prefix");
            String clanCreator = resultSet.getString("ClanCreator");
            int valueOfMembers = members.split(", ").length;

            info.append(ChatColor.RESET).append(i).append(". Название: ").append(clanName).append("\n");
            info.append(ChatColor.RESET).append("Префикс: ").append(prefix).append("\n");
            info.append(ChatColor.RESET).append("Создатель: ").append(clanCreator).append("\n");
            info.append(ChatColor.RESET).append("Количество участников: ").append(valueOfMembers).append("\n");
            info.append(ChatColor.GOLD).append("--------------------------------------").append(ChatColor.RESET);
            i++;

            clansInfo.add(info.toString());

        }
        statement.close();
        return clansInfo;
    }

    // Получение информации об одном клане
    @SneakyThrows
    public List<String> getClanInfo(Player player){
    List<String> clanInfo = new ArrayList<>();
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
    }

    // Получение названия клана по имени игрока
    @SneakyThrows
    public String getClanName(Player player) {
        PreparedStatement statement = connection.prepareStatement("SELECT clanname FROM clans WHERE members LIKE ?");
        statement.setString(1, "%" + player.getName() + "%");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) return resultSet.getString("clanname");
        statement.close();
        return null;
    }

    // Получение списка участников клана
    @SneakyThrows
    public List<String> getClanMembers(String clanName) {
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
    }


    // Добавление участника в клан
    @SneakyThrows
    public void addMemberToClan(Player player, String clanName) {
        // Проверяем, не является ли строка "members" пустой
        String separator = "";
        String currentMembers = getCurrentMembers(clanName);

        // Если уже есть члены в клане, добавляем разделитель
        if (!currentMembers.isEmpty()) {
            separator = ", ";
        }

        // Обновляем запись в базе данных
        PreparedStatement statement = connection.prepareStatement("UPDATE clans SET members = ? || ? WHERE clanname = ?");
        statement.setString(1, currentMembers + separator);
        statement.setString(2, player.getName());
        statement.setString(3, clanName);
        statement.executeUpdate();
    }

    // Получаем строку уже имеющихся игроков в клане
    @SneakyThrows
    private String getCurrentMembers(String clanName) {
        PreparedStatement statement = connection.prepareStatement("SELECT members FROM clans WHERE clanname = ?");
        statement.setString(1, clanName);
        ResultSet resultSet = statement.executeQuery();
        // Проверяем, есть ли результат
        if (resultSet.next()) {
            return resultSet.getString("members");
        }
        // Если что-то пошло не так, возвращаем пустую строку
        return "";
    }

    // Удаление участника в клан
    @SneakyThrows
    public boolean removeMemberFromClan(Player player){
        PreparedStatement statement = connection.prepareStatement("UPDATE clans SET members = REPLACE(members, ?, '') WHERE members LIKE ?");
        statement.setString(1, ", " + player.getName());
        statement.setString(2, "%" + player.getName() + "%");
        statement.executeUpdate();
        statement.close();
        return true;
    }

    // Получение префикса клана
    @SneakyThrows
    public String getClanPrefix(Player player){
        PreparedStatement statement = connection.prepareStatement("SELECT clanprefix FROM clans WHERE members LIKE ?");
        statement.setString(1, "%"+player.getName()+"%");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) return resultSet.getString("clanprefix");
        return "";
    }

    // Проверка имени клана
    @SneakyThrows
    public boolean checkClanName(String clanName){
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM clans WHERE clanname = ?");
        statement.setString(1, clanName);
        ResultSet resultSet = statement.executeQuery();
        boolean exist = resultSet.next();
        resultSet.close();
        statement.close();
        return exist;
    }


    // Изменить имя клана
    @SneakyThrows
    public void updateNameByClanName(String oldName, String newName) {
        PreparedStatement statement = connection.prepareStatement("UPDATE clans SET clanname = ? WHERE clanname = ?");
        statement.setString(1, newName);
        statement.setString(2, oldName);
        statement.executeUpdate();
    }

    // Изменить префикс клана
    @SneakyThrows
    public void updatePrefixByClanName(String clanName, String newPrefix) {
        PreparedStatement statement = connection.prepareStatement("UPDATE clans SET clanprefix = ? WHERE clanname = ?");
        statement.setString(1, newPrefix);
        statement.setString(2, clanName);
        statement.executeUpdate();
    }
}
