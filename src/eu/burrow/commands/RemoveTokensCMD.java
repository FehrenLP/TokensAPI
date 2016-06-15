package eu.burrow.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.burrow.API.TokensAPI;
import eu.burrow.mysql.Stats;
import eu.burrow.tokensapi.main.Main;

public class RemoveTokensCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = null;
		if(sender instanceof Player){
			p = (Player) sender;
		}
		
		if(p != null){
			if(p.hasPermission("tokens.modify")){
				if(args.length == 1){
					int tokens = -1;
					
					try{
						tokens = Integer.parseInt(args[0]);
					} catch(NumberFormatException e){
						p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "<Amount> has to be a Number!");
						
						return true;
					}
					
					if(tokens > 0){
						if(TokensAPI.getTokens(p) >= tokens){
							TokensAPI.removeTokens(p, tokens);
						} else {
							p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "You don't have enough tokens to remove this amount!");
						}
						return true;
					} else {
						p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "<Amount> has to be a positive Number!");
						
						return true;
					}
					
				} else if(args.length == 2){
					int tokens = -1;
					
					try{
						tokens = Integer.parseInt(args[1]);
					} catch(NumberFormatException e){
						p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "<Amount> has to be a Number!");
						
						return true;
					}
					
					if(tokens > 0){
						boolean registered = false;
						
						try {
							registered = Stats.isRegistered(args[0]);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						if(registered){
							if(TokensAPI.getTokens(args[0]) >= tokens){
								TokensAPI.removeTokens(args[0], tokens);
								p.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "Removed " + ChatColor.YELLOW + tokens + ChatColor.GREEN + " Tokens of " + ChatColor.YELLOW + args[0]);	
							} else {
								p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "The Player doesn't have enough tokens to remove this amount!");
							}
							return true;
						} else {
							p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "This Player is not registered in our Database!");
							
							return true;
						}
					} else {
						p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "<Amount> has to be a positive Number!");
						
						return true;
					}
					
				} else {
					p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Please use: " + ChatColor.BLUE + "/removetokens <Player> <Amount>");
					
					return true;
				}
			} else {
				p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "You don't have the permission to remove tokens!");
				
				return true;
			}
		} else {
			sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Only Players could remove tokens!");
			
			return true;
		}
	}
	
}
