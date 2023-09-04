package me.tahacheji.mafana.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class TradeOffer {

    private TradeMarket tradeMarket;
    private OfflinePlayer player;
    private List<ItemStack> itemOffer;
    private UUID uuid;

    private String note;
    private String x;


    public TradeOffer(TradeMarket tradeMarket,UUID uuid, OfflinePlayer player, List<ItemStack> itemOffer, String note, String x) {
        this.tradeMarket = tradeMarket;
        this.player = player;
        this.itemOffer = itemOffer;
        this.x = x;
        this.uuid = uuid;
        this.note = note;
    }

    public TradeOffer(TradeMarket tradeMarket, OfflinePlayer player, List<ItemStack> itemOffer, String note, String x) {
        this.tradeMarket = tradeMarket;
        this.player = player;
        this.itemOffer = itemOffer;
        this.x = x;
        this.note = note;
    }


    public void setNote(String note) {
        this.note = note;
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

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getX() {
        return x;
    }


    public OfflinePlayer getPlayer() {
        return player;
    }

    public List<ItemStack> getItemOffer() {
        return itemOffer;
    }

    public TradeMarket getTradeMarket() {
        return tradeMarket;
    }

    public void setItemOffer(List<ItemStack> itemOffer) {
        this.itemOffer = itemOffer;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTradeMarket(TradeMarket tradeMarket) {
        this.tradeMarket = tradeMarket;
    }
}
