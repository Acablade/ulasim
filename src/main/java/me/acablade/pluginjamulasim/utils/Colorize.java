package me.acablade.pluginjamulasim.utils;

import org.bukkit.ChatColor;

public class Colorize {

	public static String format(String msg){
		return ChatColor.translateAlternateColorCodes('&',msg);
	}

}
