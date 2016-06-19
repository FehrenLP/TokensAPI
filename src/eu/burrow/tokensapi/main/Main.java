package eu.burrow.tokensapi.main;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.burrow.commands.AddTokensCMD;
import eu.burrow.commands.GetTokensCMD;
import eu.burrow.commands.RemoveTokensCMD;
import eu.burrow.commands.SetTokensCMD;
import eu.burrow.listener.JoinListener;
import eu.burrow.listener.QuitListener;
import eu.burrow.mysql.MySQL;
import eu.burrow.mysql.Stats;
import eu.burrow.scoreboard.Scoreboards;

public class Main extends JavaPlugin {
	public static Main instance;
	
	private int Port = 3306;
	private String Host = "localhost";
	private String Password = "password";
	private String User = "user";
	private String Database = "database";
	public String prefix = ChatColor.GRAY + "[" + ChatColor.YELLOW + "§l!§r" + ChatColor.GRAY + "] ";
	
	public static boolean Scoreboard = false;

	@Override
	public void onEnable() {
		instance = this;
		loadConfig();
		loadOptions();
		
		MySQL s = new MySQL(Host, Port, User, Password, Database);
		new Stats(s);
		
		try {
			Stats.createTables();
		} catch (Exception e) {
			System.err.println("[TokensAPI] Das Plugin konnte nicht aktiviert werden, da keine MySQL Verbindung aufgebaut werden konnte!");
			System.err.println("[TokensAPI] Bitte stelle die MySQL-Daten in der config ein!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		loadListener();
		loadCommands();
		
		if(Scoreboard){
			Scoreboards.startScoreboard();
		}
		
		prevent_mysql_disconnect();
		
		System.out.println("[TokensAPI] Plugin has been enabled!");
	}
	
	@Override
	public void onDisable() {
		MySQL.close();
		
		System.out.println("[TokensAPI] Plugin has been disabled!");
	}
	
	private void loadConfig(){
		this.getConfig().options().copyDefaults(true);
		
		this.getConfig().addDefault("MySQL.Host", "localhost");
		this.getConfig().addDefault("MySQL.Password", "password");
		this.getConfig().addDefault("MySQL.User", "User");
		this.getConfig().addDefault("MySQL.Database", "Database");
		this.getConfig().addDefault("MySQL.Port", 3306);
		this.getConfig().addDefault("Options.Prefix", "&7[&e&l!&7] ");
		this.getConfig().addDefault("Options.Scoreboard", false);
		
		this.saveConfig();
		
		System.out.println("[TokensAPI] Config has been loaded!");
	}
	
	private void loadListener(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new QuitListener(), this);
		
		System.out.println("[TokensAPI] Listener has been loaded!");
	}
	
	private void loadCommands(){
		this.getCommand("gettokens").setExecutor(new GetTokensCMD());
		this.getCommand("addtokens").setExecutor(new AddTokensCMD());
		this.getCommand("removetokens").setExecutor(new RemoveTokensCMD());
		this.getCommand("settokens").setExecutor(new SetTokensCMD());
		
		System.out.println("[TokensAPI] Commands has been loaded!");
	}
	
	private void loadOptions(){
		Port = this.getConfig().getInt("MySQL.Port");
		Host = this.getConfig().getString("MySQL.Host");
		Password = this.getConfig().getString("MySQL.Password");
		User = this.getConfig().getString("MySQL.User");
		Database = this.getConfig().getString("MySQL.Database");
		prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Options.Prefix"));
		Scoreboard = this.getConfig().getBoolean("Options.Scoreboard");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	private void prevent_mysql_disconnect(){
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Stats.getTokens("FehrenLP");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}, 0L, 20L*60L*10L);
	}
	
}
