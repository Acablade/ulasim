package me.acablade.pluginjamulasim.manager;

import lombok.Getter;
import me.acablade.pluginjamulasim.PluginJamUlasim;
import me.acablade.pluginjamulasim.objects.TeleportPortal;
import me.acablade.pluginjamulasim.utils.ConfigurationFile;
import me.acablade.pluginjamulasim.utils.PortalHelper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PortalManager {

	private Map<UUID, TeleportPortal> map = new HashMap<>();
	@Getter
	private ConfigurationFile serialised;

	public PortalManager(PluginJamUlasim plugin){
		this.serialised = new ConfigurationFile(plugin,"portals");
		if(!serialised.getConfiguration().isConfigurationSection("Portals")) serialised.getConfiguration().createSection("Portals");
	}

	public void load(){

		map.clear();

		ConfigurationSection section = serialised.getConfiguration().getConfigurationSection("Portals");

		for(String key: section.getKeys(false)){

			ConfigurationSection keySection = section.getConfigurationSection(key);

			add(TeleportPortal.deserialize(keySection));

		}

	}

	public Map<UUID, TeleportPortal> getMap() {
		return map;
	}

	public void add(TeleportPortal portal){
		map.put(portal.getUuid(),portal);
		portal.serialize(serialised.getConfiguration().getConfigurationSection("Portals").createSection(portal.getUuid().toString()));
		serialised.save();
	}

	public void remove(TeleportPortal portal){
		map.remove(portal.getUuid());
		serialised.getConfiguration().getConfigurationSection("Portals").set(portal.getUuid().toString(), null);
		serialised.save();
	}

	public Optional<TeleportPortal> findFromName(String name){
		return map.values().stream().filter(portal -> portal.getName().equalsIgnoreCase(name)).findAny();
	}


}
