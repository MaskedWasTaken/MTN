package me.tahacheji.mafana.command;

import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeRequest;
import me.tahacheji.mafana.menu.TradeMarketMenu;
import me.tahacheji.mafana.util.TradeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mt")) {
            Player senderPlayer = (Player) sender;
            TradeUtil tradeUtil = new TradeUtil();
            if(args[0].equalsIgnoreCase("open")) {
                new TradeMarketMenu().getTradeMarketMenu(senderPlayer);
            }
            if (args[0].equalsIgnoreCase("trade")) {
                if (tradeUtil.hasSentTradeRequest(senderPlayer)) {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PENDING_REQUEST");
                    return true;
                }
                Player targetPlayer = sender.getServer().getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PLAYER_NOT_FOUND");
                    return true;
                }
                if(targetPlayer.getUniqueId().toString().equalsIgnoreCase(senderPlayer.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PLAYER_TRADE_SELF");
                    return true;
                }
                if (Bukkit.getOnlinePlayers().contains(targetPlayer)) {
                    senderPlayer.sendMessage(ChatColor.DARK_GRAY + "You sent a trade request to: " + targetPlayer.getDisplayName());
                    TradeRequest tr = new TradeRequest(senderPlayer, targetPlayer);
                    sendRequest(senderPlayer, targetPlayer);
                    Bukkit.getScheduler().runTaskLater(MafanaTradeNetwork.getInstance(), () -> {
                        if (tr != null) {
                            tr.destroyRequest();
                            if(!tr.isAccepted()) {
                                sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "REQUEST_TIME_OUT");
                            }
                        }
                    }, 20L * 10);
                } else {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PLAYER_NOT_ONLINE");
                }
            }

            if (args[0].equalsIgnoreCase("accept")) {
                TradeRequest tradeRequest = tradeUtil.getRequestTradeRequest(senderPlayer);
                if (tradeRequest != null) {
                    Player tradeWith = tradeRequest.getSender();
                    if (tradeWith != null && Bukkit.getOnlinePlayers().contains(tradeWith)) {
                        tradeRequest.acceptRequest();
                    } else {
                        sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PLAYER_NOT_ONLINE");
                        if (tradeRequest != null) {
                            tradeRequest.destroyRequest();
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "REQUEST_NOT_FOUND");
                }
            }
            if (args[0].equalsIgnoreCase("decline")) {
                if (tradeUtil.hasReceivedTradeRequest(senderPlayer)) {
                    Player tradeWith = tradeUtil.getRequestTradeRequest(senderPlayer).getSender();
                    if (Bukkit.getOnlinePlayers().contains(tradeWith)) {
                        tradeUtil.getRequestTradeRequest(tradeWith).declineRequest();
                    } else {
                        sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "PLAYER_NOT_ONLINE");
                        tradeUtil.getSenderTradeRequest(senderPlayer).destroyRequest();
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "REQUEST_NOT_FOUND");
                }
            }
        }
        return false;
    }

    public void sendRequest(Player sender, Player request) {
        TextComponent message = new TextComponent(ChatColor.DARK_GRAY + "You have received a trade request from ");

        TextComponent senderName = new TextComponent(sender.getDisplayName());
        senderName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mt accept " + sender.getName())); // Clicking accepts the trade
        message.addExtra(senderName);

        TextComponent acceptButton = new TextComponent(ChatColor.GREEN + " [Accept] ");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mt accept " + sender.getName())); // Clicking accepts the trade
        message.addExtra(acceptButton);

        TextComponent declineButton = new TextComponent(ChatColor.RED + "[Decline]");
        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mt decline " + sender.getName())); // Clicking declines the trade
        message.addExtra(declineButton);

        request.spigot().sendMessage(message);
    }
}
