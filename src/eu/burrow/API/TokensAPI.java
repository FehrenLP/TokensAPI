package eu.burrow.API;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.burrow.mysql.Stats;
import eu.burrow.tokensapi.main.Main;

public class TokensAPI {
	
	private final static String prefix = Main.getInstance().prefix;
	public static HashMap<Player, Integer> tokens = new HashMap<>();

	public static int getTokens(Player p){
		return getTokens(p.getUniqueId());
	}
	
	public static int getTokens(UUID uuid){
		if(Bukkit.getPlayer(uuid) != null){
			if(!tokens.containsKey(Bukkit.getPlayer(uuid))){
				try {
					tokens.put(Bukkit.getPlayer(uuid), Stats.getTokens(uuid));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return tokens.get(Bukkit.getPlayer(uuid));
		} else {
			return getTokensOffline(uuid);
		}
	}
	
	public static int getTokens(String Name){
		if(Bukkit.getPlayer(Name) != null){
			if(!tokens.containsKey(Bukkit.getPlayer(Name))){
				try {
					tokens.put(Bukkit.getPlayer(Name), Stats.getTokens(Name));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return tokens.get(Bukkit.getPlayer(Name));
		} else {
			return getTokensOffline(Name);
		}
	}
	
	public static int getTokensOffline(UUID uuid){
		int tokens = -1;
		
		try {
			tokens = Stats.getTokens(uuid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tokens;
	}
	
	public static int getTokensOffline(String name){
		int tokens = -1;
		
		try {
			tokens = Stats.getTokens(name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tokens;
	}
	
	public static void addTokens(Player p, int amount){
		addTokens(p.getUniqueId(), amount);
	}
	
	public static void addTokens(final UUID uuid, final int amount){
		if(Bukkit.getPlayer(uuid) != null){
			Bukkit.getPlayer(uuid).sendMessage(prefix + ChatColor.GREEN + "+ " + ChatColor.YELLOW + amount + ChatColor.GREEN + " Tokens!");
			if(!tokens.containsKey(Bukkit.getPlayer(uuid))){
				getTokens(uuid);
			}
			int t = tokens.get(Bukkit.getPlayer(uuid));
			tokens.remove(Bukkit.getPlayer(uuid));
			tokens.put(Bukkit.getPlayer(uuid), (t + amount));
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				
				try {
					Stats.addTokens(uuid, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void addTokens(final String name, final int amount){
		if(Bukkit.getPlayer(name) != null){
			Bukkit.getPlayer(name).sendMessage(prefix + ChatColor.GREEN + "+ " + ChatColor.YELLOW + amount + ChatColor.GREEN + " Tokens!");
			if(!tokens.containsKey(Bukkit.getPlayer(name))){
				getTokens(name);
			}
			int t = tokens.get(Bukkit.getPlayer(name));
			tokens.remove(Bukkit.getPlayer(name));
			tokens.put(Bukkit.getPlayer(name), (t + amount));
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					Stats.addTokens(name, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void removeTokens(Player p, int amount){
		removeTokens(p.getUniqueId(), amount);
	}
	
	public static void removeTokens(final UUID uuid, final int amount){
		if(Bukkit.getPlayer(uuid) != null){
			int to = getTokens(uuid);
			if(to >= amount){
				Bukkit.getPlayer(uuid).sendMessage(prefix + ChatColor.RED + "- " + ChatColor.YELLOW + amount + ChatColor.RED + " Tokens!");
				int t = tokens.get(Bukkit.getPlayer(uuid));
				tokens.remove(Bukkit.getPlayer(uuid));
				tokens.put(Bukkit.getPlayer(uuid), (t - amount));
			}
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					int to = getTokensOffline(uuid);
					if(!(to >= amount)){
						System.err.println("[TokensAPI] Couldn't remove tokens => Player doesn't have enough!");
						return;
					}
					Stats.removeTokens(uuid, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void removeTokens(final String name, final int amount){
		
		if(Bukkit.getPlayer(name) != null){
			int to = getTokens(name);
			if(to >= amount){
				Bukkit.getPlayer(name).sendMessage(prefix + ChatColor.RED + "- " + ChatColor.YELLOW + amount + ChatColor.RED + " Tokens!");
				int t = tokens.get(Bukkit.getPlayer(name));
				tokens.remove(Bukkit.getPlayer(name));
				tokens.put(Bukkit.getPlayer(name), (t - amount));
			}
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					int to = getTokensOffline(name);
					if(!(to >= amount)){
						System.err.println("[TokensAPI] Couldn't remove tokens => Player doesn't have enough!");
						return;
					}
					Stats.removeTokens(name, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setTokens(Player p, int amount){
		setTokens(p.getUniqueId(), amount);
	}
	
	public static void setTokens(final UUID uuid, final int amount){
		
		if(Bukkit.getPlayer(uuid) != null){
			Bukkit.getPlayer(uuid).sendMessage(prefix + ChatColor.GREEN + "Your Tokens has been set to: " + ChatColor.YELLOW + amount);
			if(tokens.containsKey(Bukkit.getPlayer(uuid))){
				tokens.remove(Bukkit.getPlayer(uuid));
			}
			tokens.put(Bukkit.getPlayer(uuid), amount);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					Stats.setTokens(uuid, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setTokens(final String name, final int amount){
		
		if(Bukkit.getPlayer(name) != null){
			Bukkit.getPlayer(name).sendMessage(prefix + ChatColor.GREEN + "Your Tokens has been set to: " + ChatColor.YELLOW + amount);
			if(tokens.containsKey(Bukkit.getPlayer(name))){
				tokens.remove(Bukkit.getPlayer(name));
			}
			tokens.put(Bukkit.getPlayer(name), amount);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				try {
					Stats.setTokens(name, amount);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
}
