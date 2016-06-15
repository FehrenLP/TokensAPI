package eu.burrow.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import eu.burrow.API.TokensAPI;
import eu.burrow.tokensapi.main.Main;

public class Scoreboards {

	public final static HashMap<Player, Integer> amount = new HashMap<>();
	public static ArrayList<Player> update = new ArrayList<>();
	
	public static void setScoreboard(Player p){
		Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
		
		Objective stats = s.getObjective("stats-Tokens");
		
		if(stats == null){
			stats = s.registerNewObjective("stats-Tokens", "dummy");
		}
		
		s.clearSlot(DisplaySlot.SIDEBAR);
		stats.setDisplaySlot(DisplaySlot.SIDEBAR);
		stats.setDisplayName(ChatColor.DARK_GRAY + "  ** " + ChatColor.GREEN + "§lTokens" + ChatColor.DARK_GRAY + " ** ");
		
		int tokens = TokensAPI.getTokens(p);
		String t = ChatColor.GOLD + "" + tokens;
		if(amount.containsKey(p)){
			amount.remove(p);
		}
		amount.put(p, tokens);
		
		if(t.length() > 16){
			t = ChatColor.GOLD + "/tokens";
		}
		
		Score l1 = stats.getScore(ChatColor.GOLD + "");
		Score ueTokens = stats.getScore(ChatColor.BLUE + "Your Tokens:");
		Score Tokens = stats.getScore(t);
		Score l2 = stats.getScore(ChatColor.BLUE + "");
		
		l1.setScore(3);
		ueTokens.setScore(2);
		Tokens.setScore(1);
		l2.setScore(0);

		p.setScoreboard(s);
	}
	
	public static void updateScoreboard(final Player p){
		
		final Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
		
		final Objective stats = s.getObjective("stats-Tokens");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {

				stats.setDisplayName(ChatColor.DARK_GRAY + "  ** " + ChatColor.GREEN + "§lTokens" + ChatColor.DARK_GRAY + " ** ");
				
				int tokens = TokensAPI.getTokens(p);
				
				String a = ChatColor.GOLD + "" + amount.get(p);
				String t = ChatColor.GOLD + "" + tokens;
				
				if(!(a.equalsIgnoreCase(t))){
					s.resetScores(a);
				}
				
				if(amount.containsKey(p)){
					amount.remove(p);
				}
				amount.put(p, tokens);
				
				if(t.length() > 16){
					t = ChatColor.GOLD + "/tokens";
				}
				
				Score l1 = stats.getScore(ChatColor.GOLD + "");
				Score ueTokens = stats.getScore(ChatColor.BLUE + "Your Tokens:");
				Score Tokens = stats.getScore(t);
				Score l2 = stats.getScore(ChatColor.BLUE + "");
				
				l1.setScore(3);
				ueTokens.setScore(2);
				Tokens.setScore(1);
				l2.setScore(0);

				p.setScoreboard(s);
				
			}
		});
		
	}
	
	public static void startScoreboard(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				for(int i = 0; i < update.size(); i++){
					Player all = update.get(i);
					updateScoreboard(all);
				}
				
			}
		}, 0L, 10L);
	}
	
}
