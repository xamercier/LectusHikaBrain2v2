package ca.xamercier.lectusHikabrain.games;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import ca.xamercier.lectusHikabrain.HikaBrain;
import ca.xamercier.lectusHikabrain.utils.ShutDownUtils;
import net.lectusAPI.utils.GameUtils;
import net.lectusAPI.utils.TeamUtils;
import net.md_5.bungee.api.ChatColor;

public class WinDetector {

	private static WinDetector instance = null;

	public static WinDetector getInstance() {
		if (instance == null) {
			instance = new WinDetector();
		}
		return instance;
	}

	public void detectWin() {
		if (HikaBrain.getInstance().BLUESCORE == 5) {
			GameState.setState(GameState.FINISH);
			Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET + "La team " + ChatColor.BLUE
					+ "bleu" + ChatColor.RESET + " a gagner la partie !");
			HashMap<Player, String> map = new HashMap<Player, String>();
			for (Player pl : HikaBrain.getInstance().players) {
				pl.setGameMode(GameMode.SPECTATOR);
				clearInventory(pl);
				map.put(pl, "0");
			}
			GameUtils.sendCoins(HikaBrain.getInstance().getPrefix(), true, map);
			Thread shutdown = new ShutDownUtils();
			shutdown.start();
		} else if (HikaBrain.getInstance().REDSCORE == 5) {
			GameState.setState(GameState.FINISH);
			Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET + "La team " + ChatColor.RED
					+ "rouge" + ChatColor.RESET + " a gagner la partie !");
			HashMap<Player, String> map = new HashMap<Player, String>();
			for (Player pl : HikaBrain.getInstance().players) {
				pl.setGameMode(GameMode.SPECTATOR);
				clearInventory(pl);
				map.put(pl, "0");
			}
			GameUtils.sendCoins(HikaBrain.getInstance().getPrefix(), true, map);
			Thread shutdown = new ShutDownUtils();
			shutdown.start();
		}
	}

	public void detectWinOnDeconnection() {
		int players = (HikaBrain.getInstance().players.size());
		if (players == 1) {
			for (Player p : HikaBrain.getInstance().players) {
				if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
					GameState.setState(GameState.FINISH);
					Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET + "La team "
							+ ChatColor.RED + "rouge" + ChatColor.RESET + " a gagner la partie !");
					HashMap<Player, String> map = new HashMap<Player, String>();
					for (Player pl : HikaBrain.getInstance().players) {
						pl.setGameMode(GameMode.SPECTATOR);
						clearInventory(pl);
						map.put(pl, "0");
					}
					GameUtils.sendCoins(HikaBrain.getInstance().getPrefix(), false, map);
					Thread shutdown = new ShutDownUtils();
					shutdown.start();
				} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
					GameState.setState(GameState.FINISH);
					Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET + "La team "
							+ ChatColor.BLUE + "bleu" + ChatColor.RESET + " a gagner la partie !");
					HashMap<Player, String> map = new HashMap<Player, String>();
					for (Player pl : HikaBrain.getInstance().players) {
						pl.setGameMode(GameMode.SPECTATOR);
						clearInventory(pl);
						map.put(pl, "0");
					}
					GameUtils.sendCoins(HikaBrain.getInstance().getPrefix(), false, map);
					Thread shutdown = new ShutDownUtils();
					shutdown.start();
				}
			}
		} else if (players == 0) {
			Thread shutdown = new ShutDownUtils();
			shutdown.start();
		} else if (players == 2) {
			int rouge = 0;
			int bleu = 0;

			for (Player p : HikaBrain.getInstance().players) {
				if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
					rouge++;
				} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
					bleu++;
				}
			}

			if (rouge == 2) {
				Player p = HikaBrain.getInstance().players.get(1);
				TeamUtils.getInstance().removePlayerOfTeam(p, "ROUGE");
				TeamUtils.getInstance().addPlayerToTeam(p, "BLEU");
				new RunningAutoStart().autoStart();
			} else if (bleu == 2) {
				Player p = HikaBrain.getInstance().players.get(1);
				TeamUtils.getInstance().removePlayerOfTeam(p, "BLEU");
				TeamUtils.getInstance().addPlayerToTeam(p, "ROUGE");
				new RunningAutoStart().autoStart();
			} else if (rouge == 1 && bleu == 1) {
				return;
			}

		}
	}

	private static void clearInventory(Player pl) {
		pl.getInventory().clear();
		pl.getInventory().setHelmet(null);
		pl.getInventory().setChestplate(null);
		pl.getInventory().setLeggings(null);
		pl.getInventory().setBoots(null);
	}

}
