package ca.xamercier.lectusHikabrain.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ca.xamercier.lectusHikabrain.HikaBrain;
import net.lectusAPI.utils.TeamUtils;
import net.md_5.bungee.api.ChatColor;

public class StartingAutoStart extends BukkitRunnable {

	private int timer = 15;

	@Override
	public void run() {

		for (Player pl : HikaBrain.getInstance().players) {
			if (HikaBrain.getInstance().boardsPlayers.containsKey(pl)) {
				HikaBrain.getInstance().boardsPlayers.get(pl).removeLine(5);
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(1, "");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(2, "§aLancement dans: " + timer);
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(3, " ");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(4, "§6play.lectus.ca");
			}
		}

		if (HikaBrain.getInstance().players.size() < 4) {
			Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RED
					+ "Il n'y a pas asser de joueur pour lancer la partie !");
			GameState.setState(GameState.WAITING);
			for (Player pl : Bukkit.getOnlinePlayers()) {
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(1, "  ");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(2, "§cEn attente de joueur !");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(3, "§aLancement dans: " + "00:00");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(4, " ");
				HikaBrain.getInstance().boardsPlayers.get(pl).setLine(5, "§6play.lectus.ca");
				cancel();
			}
			this.cancel();
		}

		if (timer == 0) {
			Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.AQUA + "La partie demarre !");
			GameState.setState(GameState.RUNNING);
			for (Player pl : HikaBrain.getInstance().players) {
				if (HikaBrain.getInstance().boardsPlayers.containsKey(pl)) {
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(1, "    ");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(2, "§7Objectif 5 points !");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(3, "   ");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(4, "§cRouge: 0");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(5, "§9Bleu: 0");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(6, "  ");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(7, "§cAliances interdites !");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(8, " ");
					HikaBrain.getInstance().boardsPlayers.get(pl).setLine(9, "§6play.lectus.ca");
				}
			}

			if (TeamUtils.getInstance().getNumberOfPlayerInTeam("ROUGE") > 2) {
				Player victime = TeamUtils.getInstance().getRandomPlayerInTeam("ROUGE");
				TeamUtils.getInstance().addPlayerToTeam(victime, "BLEU");
			}

			if (TeamUtils.getInstance().getNumberOfPlayerInTeam("BLEU") > 2) {
				Player victime = TeamUtils.getInstance().getRandomPlayerInTeam("BLEU");
				TeamUtils.getInstance().addPlayerToTeam(victime, "ROUGE");
			}

			new RunningAutoStart().autoStart();
			cancel();
		}
		timer--;
	}

}
