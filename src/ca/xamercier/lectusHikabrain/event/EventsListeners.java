package ca.xamercier.lectusHikabrain.event;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.xamercier.lectusHikabrain.HikaBrain;
import ca.xamercier.lectusHikabrain.games.GameState;
import ca.xamercier.lectusHikabrain.games.RunningAutoStart;
import ca.xamercier.lectusHikabrain.games.StartingAutoStart;
import ca.xamercier.lectusHikabrain.games.WinDetector;
import ca.xamercier.lectusHikabrain.utils.MapUtils;
import net.lectusAPI.MainLectusApi;
import net.lectusAPI.grade.Rank;
import net.lectusAPI.utils.ScoreboardSign;
import net.lectusAPI.utils.TeamUtils;
import net.lectusAPI.utils.VanishUtils;

public class EventsListeners implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.getInventory().clear();
		VanishUtils.getInstance().checkVanish(p);
		VanishUtils.getInstance().disableVanish(p);
		if (MainLectusApi.getInstance().getSql().hasModMode(p)) {
			HikaBrain.getInstance().mod.add(p);
		}
		if(!HikaBrain.getInstance().mod.contains(p)) {
			for(Player playerToHide : HikaBrain.getInstance().mod) {
				VanishUtils.getInstance().enableVanish(playerToHide);
			}
		}
		if (MainLectusApi.getInstance().getSql().hasModMode(p)) {
			e.setJoinMessage(null);
			HikaBrain.getInstance().mod.add(p);
			VanishUtils.getInstance().enableVanish(p);
			p.getInventory().clear();
		} else {
			if (GameState.isState(GameState.WAITING)) {
				HikaBrain.getInstance().players.add(p);
				p.setHealth(20);
				p.getInventory().clear();
				p.setGameMode(GameMode.ADVENTURE);
				if (TeamUtils.getInstance().teamDispobinibility("ROUGE", 2)) {
					TeamUtils.getInstance().addPlayerToTeam(p, "ROUGE");
				} else if (TeamUtils.getInstance().teamDispobinibility("BLEU", 2)) {
					TeamUtils.getInstance().addPlayerToTeam(p, "BLEU");
				}
				p.teleport(new Location(Bukkit.getWorld("WaitHika"), -3, 16, 6));
				ScoreboardSign scoreboard = new ScoreboardSign(p, HikaBrain.getInstance().getPrefix());
				scoreboard.create();
				scoreboard.setLine(1, "  ");
				scoreboard.setLine(2, "§cEn attente de joueur !");
				scoreboard.setLine(3, "§aLancement dans: " + "00:00");
				scoreboard.setLine(4, " ");
				scoreboard.setLine(5, "§6play.lectus.ca");
				HikaBrain.getInstance().boardsPlayers.put(p, scoreboard);

				// ROUGE
				ItemStack ROUGE = new ItemStack(Material.WOOL, 1);
				ItemMeta itemMetaROUGE = ROUGE.getItemMeta();
				itemMetaROUGE.setDisplayName(ChatColor.RED + "Rouge");
				ROUGE.setDurability((short) 14);
				ROUGE.setItemMeta(itemMetaROUGE);
				p.getInventory().setItem(0, ROUGE);
				// BLEU
				ItemStack BLEU = new ItemStack(Material.WOOL, 1);
				ItemMeta itemMetaBLEU = BLEU.getItemMeta();
				itemMetaBLEU.setDisplayName(ChatColor.BLUE + "Bleu");
				BLEU.setDurability((short) 11);
				BLEU.setItemMeta(itemMetaBLEU);
				p.getInventory().setItem(1, BLEU);

				e.setJoinMessage(ChatColor.GREEN + "+ " + ChatColor.YELLOW + p.getName() + " à rejoint la partie ! "
						+ ChatColor.GREEN + HikaBrain.getInstance().players.size() + "/" + "4");
				p.getInventory().setHelmet(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setLeggings(null);
				p.getInventory().setBoots(null);
				if (HikaBrain.getInstance().players.size() == 4) {
					Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.GREEN
							+ " Démarrage de la partie dans 15 secondes !");

					GameState.setState(GameState.STARTING);
					StartingAutoStart start = new StartingAutoStart();
					start.runTaskTimer(HikaBrain.getInstance(), 20, 20);
				}
			} else {
				p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RED
						+ "La partie a déja commencer tu est donc un spectateur !");
				HikaBrain.getInstance().spectators.add(p);
				e.setJoinMessage(null);
				p.setGameMode(GameMode.SPECTATOR);
				p.setHealth(20);
				p.getInventory().clear();
				TeamUtils.getInstance().addPlayerToTeam(p, "SPECTATEUR");
				p.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 249, 49, -67));
				ScoreboardSign scoreboard = new ScoreboardSign(p, HikaBrain.getInstance().getPrefix());
				scoreboard.create();
				scoreboard.setLine(1, "  ");
				scoreboard.setLine(2, "§cTu est un spectateur");
				scoreboard.setLine(3, " ");
				scoreboard.setLine(4, "§6play.lectus.ca");
				HikaBrain.getInstance().boardsSpectateurs.put(p, scoreboard);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (HikaBrain.getInstance().mod.contains(p)) {
			e.setQuitMessage(null);
			HikaBrain.getInstance().mod.remove(p);
			VanishUtils.getInstance().disableVanish(p);
		} else {
			if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
				TeamUtils.getInstance().removePlayerOfTeam(p, "ROUGE");
			} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
				TeamUtils.getInstance().removePlayerOfTeam(p, "BLEU");
			}
			if (HikaBrain.getInstance().boardsPlayers.containsKey(p)) {
				HikaBrain.getInstance().boardsPlayers.remove(p);
			} else if (HikaBrain.getInstance().boardsSpectateurs.containsKey(p)) {
				HikaBrain.getInstance().boardsSpectateurs.remove(p);
				TeamUtils.getInstance().removePlayerOfTeam(p, "SPECTATEUR");
			}
			if (HikaBrain.getInstance().players.contains(p) && !HikaBrain.getInstance().spectators.contains(p)) {
				HikaBrain.getInstance().players.remove(p);
				if (GameState.isState(GameState.WAITING) || GameState.isState(GameState.STARTING)) {
					e.setQuitMessage(ChatColor.RED + "- " + ChatColor.YELLOW + p.getName() + " a quitter la partie ! "
							+ ChatColor.RED + (HikaBrain.getInstance().players.size()) + "/" + "4");
				} else if (GameState.isState(GameState.RUNNING)) {
					e.setQuitMessage(ChatColor.YELLOW + p.getName() + " est mort !");
					WinDetector.getInstance().detectWinOnDeconnection();
				} else if (GameState.isState(GameState.FINISH)) {
					e.setQuitMessage(null);
				}
			} else {
				e.setQuitMessage(null);
			}
			if(GameState.isState(GameState.FINISH)) {
				if(Bukkit.getOnlinePlayers().size() <= 0) {
					Bukkit.getServer().shutdown();
				}
			}
		}
	}

	@EventHandler
	public void onDead(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setKeepInventory(true);
		p.setHealth(20);
		if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
			Player killer = p.getKiller();
			e.setDeathMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RED + p.getName() + ChatColor.RESET
					+ " a été tué par " + ChatColor.BLUE + killer.getName());
			p.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -101, 0, 5));
		} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
			Player killer = p.getKiller();
			e.setDeathMessage(HikaBrain.getInstance().getPrefix() + ChatColor.BLUE + p.getName() + ChatColor.RESET
					+ " a été tué par " + ChatColor.RED + killer.getName());
			p.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -37, 189, 5));
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!GameState.isState(GameState.RUNNING)) {
			e.setCancelled(true);
		}
		if (e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack it = e.getItem();
		Block b = e.getClickedBlock();
		if (it != null && it.getType() == Material.WOOL && !HikaBrain.getInstance().mod.contains(p)) {
			if (it.getData().getData() == 14) {
				// On ajoute le joueur dans la team rouge !
				if (TeamUtils.getInstance().teamDispobinibility("ROUGE", 1)) {
					if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
						TeamUtils.getInstance().removePlayerOfTeam(p, "BLEU");
						try {
							p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET
									+ "Vous rejoignez la team" + ChatColor.RED + " rouge " + ChatColor.RESET + "!");
							TeamUtils.getInstance().addPlayerToTeam(p, "ROUGE");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						try {
							p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET
									+ "Vous rejoignez la team" + ChatColor.RED + " rouge " + ChatColor.RESET + "!");
							TeamUtils.getInstance().addPlayerToTeam(p, "ROUGE");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
					p.sendMessage(ChatColor.RED + "Cette team est déja pleine !");
				}

			} else if (it.getData().getData() == 11) {
				// On ajoute le joueur dans la team bleu !
				if (TeamUtils.getInstance().teamDispobinibility("BLEU", 1)) {
					if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
						TeamUtils.getInstance().removePlayerOfTeam(p, "ROUGE");
						try {
							p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET
									+ "Vous rejoignez la team" + ChatColor.BLUE + " bleu " + ChatColor.RESET + "!");
							TeamUtils.getInstance().addPlayerToTeam(p, "BLEU");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						try {
							p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RESET
									+ "Vous rejoignez la team" + ChatColor.BLUE + " bleu " + ChatColor.RESET + "!");
							TeamUtils.getInstance().addPlayerToTeam(p, "BLEU");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
					p.sendMessage(ChatColor.RED + "Cette team est déja pleine !");
				}
			}
		} else if (b != null && b.getType() == Material.BED_BLOCK) {
			e.setCancelled(true);
		} else {
			return;
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Block block = e.getBlock();
		if (GameState.isState(GameState.RUNNING)) {
			if (!(block.getType() == Material.SANDSTONE)) {
				e.setCancelled(true);
				return;
			}
			MapUtils.getInstance().breakBlock(block);
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
		} else {
			e.setCancelled(true);
		}
	}

	private List<Material> ignored = Arrays.asList(Material.BARRIER, Material.STAINED_GLASS, Material.BED_BLOCK);
	private List<Material> doubleIgnored = Arrays.asList(Material.BED_BLOCK, Material.STAINED_GLASS);

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (GameState.isState(GameState.RUNNING)) {
			Block block = e.getBlock();
			if (!(block.getType() == Material.SANDSTONE)) {
				e.setCancelled(true);
				return;
			}
			Material under = block.getLocation().add(0, -1, 0).getBlock().getType();
			Material doubleUnder = block.getLocation().add(0, -2, 0).getBlock().getType();
			if (ignored.contains(under) || doubleIgnored.contains(doubleUnder)) {
				e.setCancelled(true);
				return;
			}
			MapUtils.getInstance().placeBlock(block, e.getPlayer());
			ItemStack it = new ItemStack(Material.SANDSTONE, 1);
			e.getPlayer().getInventory().addItem(it);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!HikaBrain.getInstance().spectators.contains(p) && HikaBrain.getInstance().players.contains(p)) {
			try {
				if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
					e.setFormat(ChatColor.BLUE + "[BLEU] " + p.getName() + ": " + e.getMessage());
				} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
					e.setFormat(ChatColor.RED + "[ROUGE] " + p.getName() + ": " + e.getMessage());
				}
			} catch (IllegalFormatException e1) {
				e1.printStackTrace();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			p.sendMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RED
					+ "Tu ne peut pas écrire de messages en tant que spectateur !");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		p.setFoodLevel(20);
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeatherChange(WeatherChangeEvent event) {

		boolean rain = event.toWeatherState();
		if (rain) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onThunderChange(ThunderChangeEvent event) {

		boolean storm = event.toThunderState();
		if (storm) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void spawnMob(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void cancelBed(PlayerBedEnterEvent event) {
	        event.setCancelled(true);
	}
	
	@EventHandler
	public void cancelBed(BlockExplodeEvent event) {
	        event.setCancelled(true);
	}
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		/* WITHOUT WAITHIKA but we use waithika sooooo
		if(GameState.isState(GameState.WAITING)|| GameState.isState(GameState.STARTING)) {
			if (p.getLocation().getY() < 4) {
				p.teleport(new Location(Bukkit.getWorld("world"), 67, 26, 117));
			}
		}*/
		if (!GameState.isState(GameState.RUNNING)) {
			return;
		}
		if(Rank.MODO.hasPermission(p) && HikaBrain.getInstance().mod.contains(p)) {
			return;
		}
		if(HikaBrain.getInstance().spectators.contains(p)) {
			return;
		}
		if (!(p.getGameMode() == GameMode.SPECTATOR) && GameState.isState(GameState.RUNNING)) {
			if (p.getLocation().getY() == 38) {
				if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
					p.setHealth(20);
					p.setFoodLevel(20);
					Bukkit.broadcastMessage(
							HikaBrain.getInstance().getPrefix() + ChatColor.RED + p.getName() + ChatColor.RESET
									+ " a été tué par " + ChatColor.AQUA + "une chute" + ChatColor.RESET + ".");
					p.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -101, 0, 5));
				} else if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
					p.setHealth(20);
					p.setFoodLevel(20);
					Bukkit.broadcastMessage(
							HikaBrain.getInstance().getPrefix() + ChatColor.BLUE + p.getName() + ChatColor.RESET
									+ " a été tué par " + ChatColor.AQUA + "une chute" + ChatColor.RESET + ".");
					p.teleport(new Location(Bukkit.getWorld("HikaBrainEnd"), 240, 52, -37, 189, 5));
				}
				return;
			} else if (GameState.isState(GameState.RUNNING)) {
				Block b = p.getLocation().add(0, 0, 0).getBlock();
				if (b.getType() == Material.BED_BLOCK) {
					Block SOUSBED = p.getLocation().add(0, -1, 0).getBlock();
					if (SOUSBED.getType() == Material.WOOL && SOUSBED.getData() == 11) {
						if (TeamUtils.getInstance().containsPlayerInTeam(p, "BLEU")) {
							return;
						} else {
							// TP LES JOUEURS ET RESET LEUR INVENTAIRES
							new RunningAutoStart().autoStart();
							Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.RED + p.getName()
									+ ChatColor.RESET + " a marqué!");
							HikaBrain.getInstance().REDSCORE++;
							MapUtils.getInstance().regen();
							for (Player pl : HikaBrain.getInstance().players) {
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(1, "    ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(2, "§7Objectif 5 points !");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(3, "   ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(4,
										"§cRouge: " + HikaBrain.getInstance().REDSCORE);
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(5,
										"§9Bleu: " + HikaBrain.getInstance().BLUESCORE);
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(6, "  ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(7, "§cAliances interdites !");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(8, " ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(9, "§6play.lectus.ca");
							}
							WinDetector.getInstance().detectWin();
						}
					} else if (SOUSBED.getType() == Material.WOOL && SOUSBED.getData() == 14) {
						if (TeamUtils.getInstance().containsPlayerInTeam(p, "ROUGE")) {
							return;
						} else {
							// TP LES JOUEURS ET RESET LEUR INVENTAIRES
							new RunningAutoStart().autoStart();
							Bukkit.broadcastMessage(HikaBrain.getInstance().getPrefix() + ChatColor.BLUE + p.getName()
									+ ChatColor.RESET + " a marqué!");
							HikaBrain.getInstance().BLUESCORE++;
							MapUtils.getInstance().regen();
							for (Player pl : HikaBrain.getInstance().players) {
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(1, "    ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(2, "§7Objectif 5 points !");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(3, "   ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(4,
										"§cRouge: " + HikaBrain.getInstance().REDSCORE);
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(5,
										"§9Bleu: " + HikaBrain.getInstance().BLUESCORE);
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(6, "  ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(7, "§cAliances interdites !");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(8, " ");
								HikaBrain.getInstance().boardsPlayers.get(pl).setLine(9, "§6play.lectus.ca");
							}
							WinDetector.getInstance().detectWin();
						}
					}
				}
			}
		}
	}
}
