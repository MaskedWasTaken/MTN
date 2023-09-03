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
    private boolean accepted;
    private boolean canceled;


    public TradeOffer(TradeMarket tradeMarket,UUID uuid, OfflinePlayer player, List<ItemStack> itemOffer, String note, boolean accepted, boolean canceled) {
        this.tradeMarket = tradeMarket;
        this.player = player;
        this.itemOffer = itemOffer;
        this.canceled = canceled;
        this.accepted = accepted;
        this.uuid = uuid;
        this.note = note;
    }

    public TradeOffer(TradeMarket tradeMarket, OfflinePlayer player, List<ItemStack> itemOffer, String note, boolean accepted, boolean canceled) {
        this.tradeMarket = tradeMarket;
        this.player = player;
        this.itemOffer = itemOffer;
        this.canceled = canceled;
        this.accepted = accepted;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
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
