package org.jacob.spigot.plugins.ReportPlugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jacob.spigot.plugins.ReportPlugin.ReportPlugin;
import org.jacob.spigot.plugins.ReportPlugin.utils.Report;

import java.util.Arrays;

public class ReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (s.equalsIgnoreCase("reports")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "You must be a player!");
                return true;

            }
            Player p = (Player) commandSender;

            if (!p.hasPermission("commands.reports")) {
                p.sendMessage(ChatColor.RED + "No permission!");
                return true;
            }

            if (strings.length != 0) {
                p.sendMessage(ChatColor.RED + "No additional arguments required.");
                return true;

            }

            Inventory inv = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Active Reports");

            int reportcounter;

            for(int i = 1; i < Report.reportArrayList.size(); i++) {
                Report report = Report.reportArrayList.get(i);

                ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 64);

                SkullMeta meta = (SkullMeta) skull.getItemMeta();

                meta.setOwningPlayer((OfflinePlayer) report.getTarget());
                meta.setDisplayName(ChatColor.GREEN + "Report on " + ChatColor.YELLOW + report.getTarget().getName());

                meta.setLore(Arrays.asList(
                        ChatColor.YELLOW + "Reporter: " + ChatColor.GOLD + report.getReporter().getName(),
                        ChatColor.YELLOW + "Reported: " + ChatColor.GOLD + report.getTarget().getName(),
                        ChatColor.YELLOW + "Reason: " + ChatColor.GOLD + report.getReason(),
                        "",
                        ChatColor.RED + "Click to close report"
                ));
                skull.setItemMeta(meta);

                inv.addItem(skull);
            }

            p.openInventory(inv);

            return true;
        }
        return true;
    }
}
