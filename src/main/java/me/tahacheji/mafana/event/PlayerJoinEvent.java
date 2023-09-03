package me.tahacheji.mafana.event;

import me.tahacheji.mafana.MafanaTradeNetwork;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        MafanaTradeNetwork.getInstance().getTradeOfferData().addPlayer(event.getPlayer());
    }

}
