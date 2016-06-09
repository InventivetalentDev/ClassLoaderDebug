package org.inventivetalent.classloaderdebug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class ClassLoaderDebug extends JavaPlugin {

	@Override
	public void onEnable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("classloader.debug")) {
			sender.sendMessage("§cMissing permission: classloader.debug");
			return false;
		}
		if (args.length == 0) {
			printInfo(getClass(), sender);
			return true;
		}
		try {
			Class clazz = Class.forName(args[0]);
			printInfo(clazz, sender);
			return true;
		} catch (ClassNotFoundException e) {
			sender.sendMessage("§cClass " + args[0] + " not found");
			return false;
		}
	}

	void printInfo(Class<?> clazz, CommandSender sender) {
		sender.sendMessage("§e  Class: " + clazz);
		sender.sendMessage("§a Loader: " + clazz.getClassLoader());
		if (clazz.getClassLoader().getClass().getName().equals("org.bukkit.plugin.java.PluginClassLoader")) {
			try {
				Field field = clazz.getClassLoader().getClass().getDeclaredField("plugin");
				field.setAccessible(true);
				JavaPlugin plugin = (JavaPlugin) field.get(clazz.getClassLoader());
				sender.sendMessage("§e Plugin: " + plugin.getName() + " by " + plugin.getDescription().getAuthors());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				sender.sendMessage("§cCould not get plugin class loader");
				throw new RuntimeException(e);
			}
		}
	}

}
