package me.acablade.pluginjamulasim.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class LocationUtils {

	public static void write(ConfigurationSection section, Location location){

		section.set("world",location.getWorld().getName());
		section.set("x", location.getX());
		section.set("y", location.getY());
		section.set("z", location.getZ());

	}

	public static Location read(ConfigurationSection section){

		World world = Bukkit.getWorld(section.getString("world"));
		double x = section.getDouble("x");
		double y = section.getDouble("y");
		double z = section.getDouble("z");

		return new Location(world,x,y,z);

	}

}
