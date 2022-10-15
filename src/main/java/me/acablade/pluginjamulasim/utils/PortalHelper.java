package me.acablade.pluginjamulasim.utils;

import com.jeff_media.customblockdata.CustomBlockData;
import me.acablade.pluginjamulasim.PluginJamUlasim;
import me.acablade.pluginjamulasim.objects.TeleportPortal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PortalHelper {

	private static NamespacedKey namespacedKey = new NamespacedKey(PluginJamUlasim.getInstance(),"portal");

	// currently hard-coded, might make it configurable
	public static boolean isShape(Block block){
		return block.getType() == Material.END_ROD && block.getRelative(BlockFace.UP).getType() == Material.IRON_BLOCK && block.getRelative(BlockFace.DOWN).getType() == Material.IRON_BLOCK;
	}

	public static TeleportPortal getPortal(Block block){

		if(block.getType() != Material.IRON_BLOCK && block.getType() != Material.END_ROD) return null;

		CustomBlockData blockData = new CustomBlockData(block, PluginJamUlasim.getInstance());
		if(!blockData.has(namespacedKey, PersistentDataType.STRING)) return null;

		return PluginJamUlasim.getInstance().getPortalManager().getMap().get(UUID.fromString(blockData.get(namespacedKey,PersistentDataType.STRING)));
	}

	public static TeleportPortal createPortal(Block block, Player player, String name){
		if(!isShape(block)) return null;

		UUID uuid = UUID.randomUUID();

		CustomBlockData blockData = new CustomBlockData(block, PluginJamUlasim.getInstance());
		if(blockData.has(namespacedKey)) return null;
		blockData.set(namespacedKey, PersistentDataType.STRING, uuid.toString());

		blockData = new CustomBlockData(block.getRelative(BlockFace.UP), PluginJamUlasim.getInstance());
		if(blockData.has(namespacedKey)) return null;
		blockData.set(namespacedKey, PersistentDataType.STRING, uuid.toString());

		blockData = new CustomBlockData(block.getRelative(BlockFace.DOWN), PluginJamUlasim.getInstance());
		if(blockData.has(namespacedKey)) return null;
		blockData.set(namespacedKey, PersistentDataType.STRING, uuid.toString());

		Location highest = block.getWorld().getHighestBlockAt(block.getX(), block.getZ()).getLocation();

		TeleportPortal portal = new TeleportPortal(PluginJamUlasim.getInstance(),uuid,player.getUniqueId(),name,highest.clone().add(0.5,1,0.5));

		PluginJamUlasim.getInstance().getPortalManager().add(portal);

		return portal;

	}


}
