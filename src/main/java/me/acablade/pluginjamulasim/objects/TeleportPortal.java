package me.acablade.pluginjamulasim.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.acablade.pluginjamulasim.PluginJamUlasim;
import me.acablade.pluginjamulasim.utils.ExpUtils;
import me.acablade.pluginjamulasim.utils.LocationUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

@EqualsAndHashCode
public class TeleportPortal {

	private PluginJamUlasim plugin;

	@Getter
	private UUID uuid;

	@Getter
	private UUID ownerUUID;

	@Getter
	private Location location;

	@Getter
	private String name;

	public TeleportPortal(PluginJamUlasim plugin, UUID ownerUUID, Location location){
		this.plugin = plugin;
		this.ownerUUID = ownerUUID;
		this.uuid = UUID.randomUUID();
		this.name = uuid.toString();
		this.location = location;
	}


	public TeleportPortal(PluginJamUlasim plugin, UUID uuid, UUID ownerUUID, String name, Location location){
		this.plugin = plugin;
		this.uuid = uuid;
		this.name = name;
		this.ownerUUID = ownerUUID;
		this.location = location;
	}

	public void serialize(ConfigurationSection section){

		section.set("UUID",uuid.toString());
		section.set("Owner",ownerUUID.toString());
		section.set("Name",name);
		ConfigurationSection location = section.createSection("Location");
		LocationUtils.write(location,this.location);

	}

	public void setName(String name) {
		this.name = name;
		plugin.getPortalManager().add(this);
	}

	public static TeleportPortal deserialize(ConfigurationSection section){
		UUID uuid = UUID.fromString(section.getString("UUID"));
		UUID owner = UUID.fromString(section.getString("Owner"));
		String name = section.getString("Name");
		Location location = LocationUtils.read(section.getConfigurationSection("Location"));

		return new TeleportPortal(PluginJamUlasim.getInstance(), uuid, owner, name, location);
	}

	/**
	 * teleports player
	 * @param player player getting teleported
	 * @return is teleported
	 */
	public boolean teleport(Player player){

		int xp = plugin.getSettings().getXpNeeded();

		int exp = ExpUtils.getPlayerExp(player);
		if(player.getGameMode() != GameMode.CREATIVE && exp < xp) return false;

		player.teleport(location);
		player.setExp(0);
		player.setLevel(0);
		player.giveExp(exp - xp);

		return true;
	}

}
