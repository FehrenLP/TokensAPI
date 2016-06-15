package eu.burrow.listener;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.burrow.API.TokensAPI;
import eu.burrow.mysql.Stats;
import eu.burrow.scoreboard.Scoreboards;
import eu.burrow.tokensapi.main.Main;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		final Player p = e.getPlayer();

		if(TokensAPI.tokens.containsKey(p)){
			TokensAPI.tokens.remove(p);
		}
		
		if(Scoreboards.amount.containsKey(p)){
			Scoreboards.amount.remove(p);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Stats.updateName(p.getUniqueId(), p);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				
				if(TokensAPI.getTokens(p) == -1){
					try {
						Stats.resetPlayer(p.getUniqueId(), p, false);
						TokensAPI.tokens.put(p, 0);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				createScoreboard(p);
			}
		}, 30L);
		
	}
	
	public static void createScoreboard(Player p){
		if(Main.Scoreboard){
			Scoreboards.setScoreboard(p);
			Scoreboards.update.add(p);
		}
	}
	
}
