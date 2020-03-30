package ca.xamercier.lectusHikabrain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import ca.xamercier.lectusHikabrain.HikaBrain;

public class MapUtils {
	
	private static MapUtils			instance	= null;
	public List<Block>				placedblocks;
	public Map<Location, Material>	destroyedBlocks;
	
	private MapUtils() {
		placedblocks = new ArrayList<>();
		destroyedBlocks = new HashMap<>();
	}
	
	public void regen() {
		Iterator<Block> bs = placedblocks.iterator();
		while (bs.hasNext()) {
			bs.next().setType(Material.AIR);
		}
		
		for (Map.Entry<Location, Material> blocks : destroyedBlocks.entrySet()) {
			blocks.getKey().getBlock().setType(blocks.getValue());
		}
	}
	
	public void placeBlock(Block block, Player player) {
		block.setMetadata("placed", new FixedMetadataValue(HikaBrain.getInstance(), player.getName()));
		if (!placedblocks.contains(block)) {
			placedblocks.add(block);
		}
	}
	
	public void breakBlock(Block block) {
		if (!block.hasMetadata("placed") && !destroyedBlocks.containsKey(block)) {
			destroyedBlocks.put(block.getLocation(), block.getType());
		}
		
		if (!placedblocks.contains(block) && destroyedBlocks.containsKey(block)) {
			destroyedBlocks.put(block.getLocation(), block.getType());
		}
	}
	
	public static MapUtils getInstance() {
		if (instance == null) {
			instance = new MapUtils();
		}
		return instance;
	}
	
}
