package ca.xamercier.lectusHikabrain.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.xamercier.lectusHikabrain.HikaBrain;
import net.lectusAPI.utils.SendPlayerViaBungee;

public class ShutDownUtils extends Thread{

	public void run() {
		try {
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				SendPlayerViaBungee.sendPlayer(pl, "hub");
			}
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HikaBrain.getInstance().getServer().shutdown();
	}
	
}
