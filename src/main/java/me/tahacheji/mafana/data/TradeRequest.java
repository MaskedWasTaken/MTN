package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaTradeNetwork;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TradeRequest {

    private final Player sender;
    private final Player request;

    private boolean accepted = false;


    public TradeRequest(Player sender, Player request) {
        this.sender = sender;
        this.request = request;
        MafanaTradeNetwork.getInstance().getTradeRequestList().add(this);
    }

    public void declineRequest() {
        sender.sendMessage(request.getDisplayName() + ChatColor.DARK_GRAY + " has " + ChatColor.RED + "DECLINED " + ChatColor.DARK_GRAY + "your trade request.");
        request.sendMessage(ChatColor.DARK_GRAY + "You have " + ChatColor.RED + "DECLINED " + ChatColor.DARK_GRAY + sender.getDisplayName() + ChatColor.DARK_GRAY + "'s trade request.");
    }

    public void acceptRequest() {
        accepted = true;
        sender.sendMessage(request.getDisplayName() + ChatColor.DARK_GRAY + " has " + ChatColor.GREEN + "ACCEPTED " + ChatColor.DARK_GRAY + "your trade request.");
        request.sendMessage(ChatColor.DARK_GRAY + "You have " + ChatColor.GREEN + "ACCEPTED " + ChatColor.DARK_GRAY + sender.getDisplayName() + ChatColor.DARK_GRAY + "'s trade request.");
        TradeManager senderManager = new TradeManager(sender);
        TradeManager requestManager = new TradeManager(request);
        senderManager.setPlayer2TradeManager(requestManager);
        requestManager.setPlayer2TradeManager(senderManager);
        destroyRequest();
        senderManager.openTradeGUI(true);
        requestManager.openTradeGUI(true);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void destroyRequest() {
        MafanaTradeNetwork.getInstance().getTradeRequestList().remove(this);
    }

    public Player getRequest() {
        return request;
    }

    public Player getSender() {
        return sender;
    }
}
