package me.acablade.pluginjamulasim;

import lombok.Getter;
import me.acablade.pluginjamulasim.listener.PortalListener;
import me.acablade.pluginjamulasim.manager.PortalManager;
import me.acablade.pluginjamulasim.utils.Colorize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PluginJamUlasim extends JavaPlugin {

	private Settings settings;
	private PortalManager portalManager;

	private static PluginJamUlasim instance;

	public static PluginJamUlasim getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		// Plugin startup logic
		instance = this;

		this.portalManager = new PortalManager(this);
		this.settings = new Settings(this);

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		reloadConfig();

		getCommand("bladeportal").setExecutor(this);

		this.portalManager.load();

		getServer().getPluginManager().registerEvents(new PortalListener(this),this);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Getter
	public static class Settings{

		private int xpNeeded = 25;

		public Settings(PluginJamUlasim plugin){
			xpNeeded = plugin.getConfig().getInt("settings.needed-xp",xpNeeded);
		}

		
	}

	public void reload(){

		reloadConfig();
		this.settings = new Settings(this);
		this.portalManager.getSerialised().reload();
		this.portalManager.load();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission(command.getPermission())) return false;

		reload();
		sender.sendMessage(Colorize.format("&7[&6BP&7] &aYenilendi!"));


		return true;
	}
}
