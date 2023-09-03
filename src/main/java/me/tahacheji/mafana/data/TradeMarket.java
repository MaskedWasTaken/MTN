package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaTradeNetwork;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class TradeMarket {

    private final OfflinePlayer player;
    private final ItemStack item;

    private final String note;
    private List<TradeOffer> tradeOfferList;
    private UUID uuid;

    public TradeMarket(OfflinePlayer player, ItemStack item, String note) {
        this.player = player;
        this.item = item;
        UUID uuid = UUID.randomUUID();
        this.note = note;
        this.uuid = uuid;
    }

    public TradeMarket(OfflinePlayer player, ItemStack item, String note, String uuid) {
        this.player = player;
        this.item = item;
        this.note = note;
        this.uuid = UUID.fromString(uuid);
    }

    public void saveListing(){
        MafanaTradeNetwork.getInstance().getTradeMarketData().addTradeMarket(this);
    }


    public void removeListing() {
        MafanaTradeNetwork.getInstance().getTradeMarketData().removeListing(this);
    }

    public void setTradeOfferList(List<TradeOffer> tradeOfferList) {
        this.tradeOfferList = tradeOfferList;
    }

    public List<TradeOffer> getTradeOfferList() {
        return tradeOfferList;
    }

    public String getNote() {
        return note;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }


    public OfflinePlayer getOfflinePlayer() {
        return player;
    }
    public Player getPlayer() {
        return player.getPlayer();
    }

    public ItemStack getItem() {
        return item;
    }

}
