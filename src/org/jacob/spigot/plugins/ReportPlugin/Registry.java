package org.jacob.spigot.plugins.ReportPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jacob.spigot.plugins.ReportPlugin.commands.ReportCommand;
import org.jacob.spigot.plugins.ReportPlugin.commands.ReportsCommand;
import org.jacob.spigot.plugins.ReportPlugin.commands.ViewReportCommand;

public class Registry {

	public static void commands() {
		ReportPlugin.getInstance().getCommand("report").setExecutor(new ReportCommand());
		ReportPlugin.getInstance().getCommand("viewreport").setExecutor(new ViewReportCommand());
		ReportPlugin.getInstance().getCommand("clearreports").setExecutor(new ReportCommand());
		ReportPlugin.getInstance().getCommand("reports").setExecutor(new ReportsCommand());
	}
	
	public static void listeners() {
		PluginManager pm = Bukkit.getPluginManager();

	}
	
}
