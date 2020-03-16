package org.jacob.spigot.plugins.ReportPlugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;

public class ReportPlugin extends JavaPlugin {
	
	private static ReportPlugin instance;
	
	public static ReportPlugin getInstance() {
		return instance;
	}

	private File playerReportFile;
	private FileConfiguration playerReport;


	@Override
	public void onEnable() {
		
		instance = this;
		
		System.out.println(ChatColor.GREEN + "Plugin is starting up!");
		
		Registry.listeners();
		Registry.commands();

		createPlayerReports();
	}

	private void createPlayerReports() {
		playerReportFile = new File(getDataFolder(), "reports.yml");
		if (!playerReportFile.exists()) {
			playerReportFile.getParentFile().mkdirs();
			saveResource("reports.yml", false);
		}

		playerReport = new YamlConfiguration();
		try {
			playerReport.load(playerReportFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public FileConfiguration getReportsFile() {
		return this.playerReport;
	}

	public void savePlayerReports() {
		try {
			playerReport.save(playerReportFile);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
}
