package org.jacob.spigot.plugins.ReportPlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jacob.spigot.plugins.ReportPlugin.utils.Report;

public class ViewReportCommand implements CommandExecutor {

    int id;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (s.equalsIgnoreCase("viewreport")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "You must be a player!");
                return true;

            }
            Player p = (Player) commandSender;

            if (!p.hasPermission("commands.reports")) {
                p.sendMessage(ChatColor.RED + "No permission!");
                return true;
            }

            if (strings.length != 1) {
                p.sendMessage(ChatColor.RED + "Usage: /viewreport <id>");
                return true;
            }

            try {
                id = Integer.parseInt(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Invalid ID!");
            }

            if(id == 0) {
                p.sendMessage(ChatColor.RED + "There is no report for that ID.");
                return true;
            }

            if ((id > Report.reportArrayList.size() - 1)) {
                p.sendMessage(ChatColor.RED + "There is no report for that ID.");
                return true;
            }
            Report report = Report.reportArrayList.get(id);

            p.sendMessage("");
            p.sendMessage(ChatColor.YELLOW + "Reporter: " + ChatColor.GOLD + report.getReporter().getName());
            p.sendMessage(ChatColor.YELLOW + "Reported: " + ChatColor.GOLD + report.getTarget().getName());
            p.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + report.getReason());
            p.sendMessage(ChatColor.YELLOW + "Report ID: " + ChatColor.GOLD + id);
            return true;
        }

        return true;
    }
}
