package eu.burrow.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.burrow.API.TokensAPI;

public class Stats {
	
	public static Connection con;
	
	public Stats(MySQL s){
		con = MySQL.getConnection();
	}
	
	public static void createTables() throws SQLException {
		Connection c = con;
		Statement st = c.createStatement();
		st.executeUpdate("CREATE TABLE IF NOT EXISTS Tokens(UUID VARCHAR(255) PRIMARY KEY, Name VARCHAR(255), Tokens INT)");
	}
	
	public static void resetPlayer(UUID uuid, Player player, boolean all) throws SQLException {
		Connection c = con;
		Statement st = c.createStatement();
		if(!all){
			if(TokensAPI.getTokens(uuid) == -1){
				st.executeUpdate("INSERT INTO Tokens VALUES ('" + uuid.toString() + "', '" + player.getName() + "', '0')");
			} else {
				st.executeUpdate("UPDATE `Tokens` SET `Tokens`='0' WHERE UUID = '" + uuid.toString() + "'");
			}
		} else {
			st.executeUpdate("UPDATE `Tokens` SET `Tokens`='0'");
		}
		st.close();
	}
	
	public static void updateName(UUID uuid, Player p) throws SQLException {
		Connection c = con;
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT Name FROM Tokens WHERE UUID = '" + uuid.toString() + "'");
		
		while(rs.next()){
			if(!(rs.getString(1).equalsIgnoreCase(p.getName()))){
				st.executeUpdate("UPDATE `Tokens` SET `Name`='" + p.getName() + "' WHERE UUID = '" + uuid.toString() + "'");
				
				rs.close();
				st.close();
				return;
			} else {
				rs.close();
				st.close();
				
				return;
			}
		}
		
		rs.close();
		st.close();
		return;
	}
	
	public static void addTokens(UUID uuid, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(uuid);
		if(t == -1){
			if(Bukkit.getPlayer(uuid) != null){
				resetPlayer(uuid, Bukkit.getPlayer(uuid), false);
				t = 0;
			}
		}
		if(t != -1){
			int new_tokens = t;
			if(Bukkit.getPlayer(uuid) == null){
				new_tokens = (t - amount);
			}
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + new_tokens + "' WHERE UUID = '" + uuid.toString() + "'");
		}
		st.close();
	}
	
	public static void addTokens(String Name, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(Name);
		if(t == -1){
			if(Bukkit.getPlayer(Name) != null){
				resetPlayer(Bukkit.getPlayer(Name).getUniqueId(), Bukkit.getPlayer(Name), false);
				t = 0;
			}
		}
		if(t != -1){
			int new_tokens = t;
			if(Bukkit.getPlayer(Name) == null){
				new_tokens = (t - amount);
			}
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + new_tokens + "' WHERE Name = '" + Name + "'");
		}
		st.close();
	}
	
	public static void removeTokens(UUID uuid, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(uuid);
		if(t == -1){
			if(Bukkit.getPlayer(uuid) != null){
				resetPlayer(uuid, Bukkit.getPlayer(uuid), false);
				t = 0;
			}
		}
		if(t != -1){
			int new_tokens = t;
			if(Bukkit.getPlayer(uuid) == null){
				new_tokens = (t - amount);
			}
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + new_tokens + "' WHERE UUID = '" + uuid.toString() + "'");
		}
		st.close();
	}
	
	public static void removeTokens(String Name, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(Name);
		if(t == -1){
			if(Bukkit.getPlayer(Name) != null){
				resetPlayer(Bukkit.getPlayer(Name).getUniqueId(), Bukkit.getPlayer(Name), false);
				t = 0;
			}
		}
		if(t != -1){
			int new_tokens = t;
			if(Bukkit.getPlayer(Name) == null){
				new_tokens = (t - amount);
			}
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + new_tokens + "' WHERE Name = '" + Name + "'");
		}
		st.close();
	}
	
	public static void setTokens(UUID uuid, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(uuid);
		if(t == -1){
			if(Bukkit.getPlayer(uuid) != null){
				resetPlayer(Bukkit.getPlayer(uuid).getUniqueId(), Bukkit.getPlayer(uuid), false);
				t = 0;
			}
		}
		if(t != -1){
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + amount + "' WHERE UUID = '" + uuid + "'");
		}
		st.close();
	}
	
	public static void setTokens(String name, int amount) throws SQLException{
		Connection c = con;
		Statement st = c.createStatement();
		int t = TokensAPI.getTokens(name);
		if(t == -1){
			if(Bukkit.getPlayer(name) != null){
				resetPlayer(Bukkit.getPlayer(name).getUniqueId(), Bukkit.getPlayer(name), false);
				t = 0;
			}
		}
		if(t != -1){
			st.executeUpdate("UPDATE Tokens set `Tokens` = '" + amount + "' WHERE Name = '" + name + "'");
		}
		st.close();
	}
	
	public static int getTokens(UUID uuid) throws SQLException{
		int tokens = -1;
		
		Connection c = con;
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT Tokens FROM Tokens WHERE UUID = '" + uuid.toString() + "'");
		
		while(rs.next()){
			tokens = rs.getInt(1);
		}
		
		rs.close();
		st.close();
		
		return tokens;
	}
	
	public static int getTokens(String name) throws SQLException{
		int tokens = -1;
				
		Connection c = con;
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT Tokens FROM Tokens WHERE Name = '" + name + "'");
		
		while(rs.next()){
			tokens = rs.getInt(1);
		}
		
		rs.close();
		st.close();
		
		return tokens;
	}
	
	public static boolean isRegistered(String Name) throws SQLException{
		boolean registered = false;
		
		Connection c = con;
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT Tokens FROM Tokens WHERE Name = '" + Name + "'");
		
		while(rs.next()){
			registered = true;
		}
		
		rs.close();
		st.close();
		
		return registered;
	}
	
	public static boolean isRegistered(UUID uuid) throws SQLException{
		boolean registered = false;
		
		Connection c = con;
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT Tokens FROM Tokens WHERE UUID = '" + uuid.toString() + "'");
		
		while(rs.next()){
			registered = true;
		}
		
		rs.close();
		st.close();
		
		return registered;
	}
	
}
