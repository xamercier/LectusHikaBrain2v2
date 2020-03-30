package ca.xamercier.lectusHikabrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.xamercier.lectusHikabrain.event.EventsListeners;
import ca.xamercier.lectusHikabrain.games.GameState;
import net.lectusAPI.utils.ScoreboardSign;
import net.lectusAPI.utils.TeamUtils;

public class HikaBrain extends JavaPlugin {

	public Map<Player, ScoreboardSign>	boardsPlayers		= new HashMap<>();
	public Map<Player, ScoreboardSign>	boardsSpectateurs	= new HashMap<>();
	
	public List<Player>					spectators			= new ArrayList<>();
	public List<Player>					players				= new ArrayList<>();
	public List<Player>					mod					= new ArrayList<>();

	private final String prefix = ChatColor.GOLD + "§e[HikaBrain] ";

	private static HikaBrain instance;
    
	public static HikaBrain getInstance() {
		return instance;
	}

	public int BLUESCORE = 0;
	public int REDSCORE = 0;

	public void onEnable() {
		super.onEnable();
		instance = this;
		
		GameState.setState(GameState.WAITING);
		Bukkit.getPluginManager().registerEvents(new EventsListeners(), this);
		
		TeamUtils.getInstance().createTeam("SPECTATEUR", "§7[Spectateur] ");
		TeamUtils.getInstance().createTeam("ROUGE", "§c[Rouge] ");
		TeamUtils.getInstance().getTeam("ROUGE").setAllowFriendlyFire(false);
		TeamUtils.getInstance().createTeam("BLEU", "§9[Bleu] ");
		TeamUtils.getInstance().getTeam("BLEU").setAllowFriendlyFire(false);
		
		new WorldCreator("WaitHika").createWorld();
		WorldCreator end = new WorldCreator("HikaBrainEnd");
		end.environment(Environment.THE_END);
		end.createWorld();
		
		for (World world : Bukkit.getWorlds()) {
			String name = world.getName();
			getServer().getWorld(name).setStorm(false);
			getServer().getWorld(name).getEntities().clear();
			getServer().getWorld(name).setTime(0);
	          for(Entity en : getServer().getWorld(name).getEntities()){
	              if(!(en instanceof Player)) {
	              en.remove();
	              }
	          }
		}
	}

	public void onDisable() {
		TeamUtils.getInstance().deleteTeam("SPECTATEUR");
		TeamUtils.getInstance().deleteTeam("ROUGE");
		TeamUtils.getInstance().deleteTeam("BLEU");
		super.onDisable();
	}

	public String getPrefix() {
		return prefix;
	}

}
