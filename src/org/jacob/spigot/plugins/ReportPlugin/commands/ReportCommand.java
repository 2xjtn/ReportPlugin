package org.jacob.spigot.plugins.ReportPlugin.commands;

import com.google.common.xml.XmlEscapers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jacob.spigot.plugins.ReportPlugin.ReportPlugin;
import org.jacob.spigot.plugins.ReportPlugin.utils.Report;

import java.util.HashMap;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private HashMap<UUID, Long> report_cooldown = new HashMap<UUID, Long>();
    private HashMap<UUID, BukkitRunnable> report_task = new HashMap<UUID, BukkitRunnable>();
    private int cooldowntime = 120;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (s.equalsIgnoreCase("report")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "You must be a player!");
                return true;

            }
            Player p = (Player) commandSender;

            if (!p.hasPermission("commands.report")) {
                p.sendMessage(ChatColor.RED + "No permission!");
                return true;
            }

            if(!p.hasPermission("reports.cooldown")) {
                if(report_cooldown.containsKey(p.getUniqueId())) {
                    p.sendMessage(ChatColor.RED + "You must wait " + ChatColor.YELLOW + report_cooldown.get(p.getUniqueId()) + ChatColor.RED + " seconds!");
                    return true;
                }

                report_cooldown.put(p.getUniqueId(), (long)cooldowntime);
                report_task.put(p.getUniqueId(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        report_cooldown.put(p.getUniqueId(), report_cooldown.get(p.getUniqueId()) - 1);
                        if(report_cooldown.get(p.getUniqueId()) == 0) {
                            report_cooldown.remove(p.getUniqueId());
                            report_task.remove(p.getUniqueId());
                            cancel();
                        }
                    }
                });

                report_task.get(p.getUniqueId()).runTaskTimer(ReportPlugin.getInstance(), 20, 20);
            }

            if (strings.length < 2) {
                p.sendMessage(ChatColor.RED + "Usage: /report <player> reason>");
                return true;
            }

            Player t = Bukkit.getPlayer(strings[0]);

            if (t == null) {
                p.sendMessage(ChatColor.RED + "That player isn't online!");
                return true;
            }

            String reason = "";
            for (int i = 1; i < strings.length; i++) {
                reason = reason + strings[i] + " ";
            }

            if (t == p) {
                p.sendMessage(ChatColor.RED + "You can't report yourself!");
                return true;
            }

            for(int i = 1; i < Report.reportArrayList.size(); i++) {
                Report report = Report.reportArrayList.get(i);

                if(t == report.getTarget()) {
                    p.sendMessage(ChatColor.RED + "There is already a report on that player!");
                    return true;
                }

                ReportPlugin.getInstance().getReportsFile().set("reports." + i + ".Reported: ", report.getTarget().getName());
                ReportPlugin.getInstance().getReportsFile().set("reports." + i + ".Reporter: ", report.getReporter().getName());
                ReportPlugin.getInstance().getReportsFile().set("reports." + i + ".Reason: ", report.getReason());
                ReportPlugin.getInstance().savePlayerReports();
            }

            new Report(p, t, reason, Report.reportArrayList.size());
            p.sendMessage(ChatColor.YELLOW + "(" + ChatColor.GOLD + "#" + Report.counter + ChatColor.YELLOW
                    + ") " + "Successfully reported " + ChatColor.GOLD + t.getName() + ChatColor.YELLOW + "!");

            for(Player staff : Bukkit.getOnlinePlayers()) {
                if(staff.hasPermission("report.notify")) {
                    staff.sendMessage(ChatColor.YELLOW + "> " + ChatColor.GOLD + "Report #" + Report.counter + ChatColor.YELLOW + ": " +
                            ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " has reported " + ChatColor.GOLD +
                            t.getName() + ChatColor.YELLOW + " for " + ChatColor.GOLD + reason);
                }
            }

        }

        if (s.equalsIgnoreCase("clearreports")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "You must be a player!");
                return true;

            }
            Player p = (Player) commandSender;

            if (!p.hasPermission("commands.clearreports")) {
                p.sendMessage(ChatColor.RED + "No permission!");
                return true;
            }

            if (Report.reportArrayList.isEmpty()) {
                p.sendMessage(ChatColor.RED + "There are no reports!");
                return true;
            }

            p.sendMessage(ChatColor.YELLOW + "You cleared " + ChatColor.GOLD + (Report.reportArrayList.size() - 1) + ChatColor.YELLOW + " reports!");

            Report.reportArrayList.clear();

            Report.counter = 0;
        }

        return true;
    }
}
