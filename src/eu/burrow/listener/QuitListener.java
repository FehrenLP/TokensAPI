package eu.burrow.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.burrow.API.TokensAPI;
import eu.burrow.scoreboard.Scoreboards;

public class QuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		
		if(TokensAPI.tokens.containsKey(p)){
			TokensAPI.tokens.remove(p);
		}
		
		if(Scoreboards.amount.containsKey(p)){
			Scoreboards.amount.remove(p);
		}
		
		if(Scoreboards.update.contains(p)){
			Scoreboards.update.remove(p);
		}
		
	}
	
}
