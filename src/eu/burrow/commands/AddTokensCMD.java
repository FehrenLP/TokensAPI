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

public class AddTokensCMD implements CommandExecutor {

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
						TokensAPI.addTokens(p, tokens);
							
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
							TokensAPI.addTokens(args[0], tokens);
							p.sendMessage(Main.getInstance().prefix + ChatColor.GREEN + "Added " + ChatColor.YELLOW + tokens + ChatColor.GREEN + " Tokens to " + ChatColor.YELLOW + args[0]);	
							
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
					p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Please use: " + ChatColor.BLUE + "/addtokens <Player> <Amount>");
					
					return true;
				}
			} else {
				p.sendMessage(Main.getInstance().prefix + ChatColor.RED + "You don't have the permission to add tokens!");
				
				return true;
			}
		} else {
			sender.sendMessage(Main.getInstance().prefix + ChatColor.RED + "Only Players could add tokens!");
			
			return true;
		}
	}
	
}
