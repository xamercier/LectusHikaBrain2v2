package ca.xamercier.lectusHikabrain.games;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.xamercier.lectusHikabrain.HikaBrain;
import net.lectusAPI.utils.TeamUtils;

public class RunningAutoStart {
	
	public void autoStart() {
		for (Player pl : HikaBrain.getInstance().players) {
			if (TeamUtils.getInstance().containsPlayerInTeam(pl, "ROUGE")) {
				pl.setHealth(20);
				pl.setGameMode(GameMode.SURVIVAL);
				pl.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -101, 0, 5));
				pl.getInventory().clear();
				pl.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET, 1));
				pl.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
				pl.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
				pl.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
				pl.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD, 1));
				pl.getInventory().setItem(1, new ItemStack(Material.GOLDEN_APPLE, 64));
				ItemStack i = new ItemStack(Material.SANDSTONE, 64);
				pl.getInventory().setItem(2, new ItemStack(Material.DIAMOND_PICKAXE, 1));
				pl.getInventory().setItem(3, i);
				pl.getInventory().setItem(4, i);
				pl.getInventory().setItem(5, i);
				pl.getInventory().setItem(6, i);
				pl.getInventory().setItem(7, i);
				pl.getInventory().setItem(8, i);

			} else if (TeamUtils.getInstance().containsPlayerInTeam(pl, "BLEU")) {
				pl.setHealth(20);
				pl.setGameMode(GameMode.SURVIVAL);
				pl.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -37, 189, 5));
				pl.getInventory().clear();
				pl.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET, 1));
				pl.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
				pl.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
				pl.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
				pl.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD, 1));
				pl.getInventory().setItem(1, new ItemStack(Material.GOLDEN_APPLE, 64));
				ItemStack i = new ItemStack(Material.SANDSTONE, 64);
				pl.getInventory().setItem(2, new ItemStack(Material.DIAMOND_PICKAXE, 1));
				pl.getInventory().setItem(3, i);
				pl.getInventory().setItem(4, i);
				pl.getInventory().setItem(5, i);
				pl.getInventory().setItem(6, i);
				pl.getInventory().setItem(7, i);
				pl.getInventory().setItem(8, i);
			}
		}

	}

}
