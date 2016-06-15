package eu.burrow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.burrow.API.TokensAPI;
import eu.burrow.tokensapi.main.Main;

public class GetTokensCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = null;
		if(sender instanceof Player){
			p = (Player) sender;
		}
		
		if(p != null){
			if(args.length == 0){
				int tokens = TokensAPI.getTokens(p);
				p.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "Your tokens: " + ChatColor.YELLOW + tokens);
				return true;
			} else if(args.length == 1){
				if(p.hasPermission("tokens.lookup")){
					if(Bukkit.getPlayer(args[0]) != null){
						p.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "Tokens of " + ChatColor.YELLOW + args[0] + ": " + TokensAPI.getTokens(Bukkit.getPlayer(args[0])));
						
						return true;
					} else {
						int to = TokensAPI.getTokensOffline(args[0]);
						if(to == -1){
							p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "This Player is not registered in our database!");
							
							return true;
						} else {
							p.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "Tokens of " + ChatColor.YELLOW + args[0] + ": " + to);
							
							return true;
						}
					}
				} else {
					p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "You don't have the permission to lookup the tokens of other Players!");
					
					return true;
				}
			} else {
				if(p.hasPermission("tokens.lookup")){
					p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Please use: " + ChatColor.BLUE + "/lookup [Player]");
					
					return true;
				} else {
					p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Please use: " + ChatColor.BLUE + "/tokens");
					
					return true;
				}
			}
		} else {
			sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Only players can lookup the tokens!");
			
			return true;
		}
	}

}
