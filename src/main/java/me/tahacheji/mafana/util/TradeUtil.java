package me.tahacheji.mafana.util;

import dev.triumphteam.gui.guis.Gui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TradeUtil {


    public Player getRequestTradePlayerSender(Player sender) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            if(sender.getUniqueId().toString().equalsIgnoreCase(tradeRequest.getSender().toString())) {
                return tradeRequest.getRequest();
            }
         }
        return null;
    }

    public Player getRequestTradePlayerRequest(Player request) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            if(request.getUniqueId().toString().equalsIgnoreCase(tradeRequest.getRequest().toString())) {
                return tradeRequest.getRequest();
            }
        }
        return null;
    }

    public boolean hasSentTradeRequest(Player sender) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            return sender.getUniqueId().toString().equalsIgnoreCase(tradeRequest.getSender().getUniqueId().toString());
        }
        return false;
    }

    public boolean hasReceivedTradeRequest(Player receiver) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            return receiver.getUniqueId().toString().equalsIgnoreCase(tradeRequest.getRequest().getUniqueId().toString());
        }
        return false;
    }

    public TradeRequest getSenderTradeRequest(Player sender) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            if(tradeRequest.getSender().getUniqueId().toString().equalsIgnoreCase(sender.getUniqueId().toString())) {
                return tradeRequest;
            }
        }
        return null;
    }

    public TradeRequest getRequestTradeRequest(Player request) {
        for(TradeRequest tradeRequest : MafanaTradeNetwork.getInstance().getTradeRequestList()) {
            if(tradeRequest.getRequest().getUniqueId().toString().equalsIgnoreCase(request.getUniqueId().toString())) {
                return tradeRequest;
            }
        }
        return null;
    }

    public boolean areAllSlotsFilled(int[] i, Gui gui) {
        int l = 0;
        for(int x : i) {
            if(gui.getGuiItem(x) != null) {
                if (gui.getGuiItem(x).getItemStack().getType() != Material.AIR) {
                  l += 1;
                }
            }
        }
        return l == i.length;
    }
}
