package me.tahacheji.mafana.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TradeManagerTransaction {

    private final OfflinePlayer player1;
    private final OfflinePlayer player2;

    private String time;

    private final List<ItemStack> player1Items;
    private final List<ItemStack> player2Items;

    private UUID uuid;

    public TradeManagerTransaction(OfflinePlayer player1, OfflinePlayer player2, List<ItemStack> player1Items, List<ItemStack> player2Items,String time, UUID uuid) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Items = player1Items;
        this.player2Items = player2Items;
        this.uuid = uuid;
        this.time = time;
    }

    public TradeManagerTransaction(OfflinePlayer player1, OfflinePlayer player2, List<ItemStack> player1Items, List<ItemStack> player2Items, String time) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Items = player1Items;
        this.player2Items = player2Items;
        this.time = time;
    }

    public TradeManagerTransaction(OfflinePlayer player1, OfflinePlayer player2, List<ItemStack> player1Items, List<ItemStack> player2Items, UUID uuid) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Items = player1Items;
        this.player2Items = player2Items;
        this.uuid = uuid;
    }

    public TradeManagerTransaction(OfflinePlayer player1, OfflinePlayer player2, List<ItemStack> player1Items, List<ItemStack> player2Items) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Items = player1Items;
        this.player2Items = player2Items;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

    public String getTime() {
        return time;
    }

    public String getLocalDateTime() {
        String newTime = time;
        newTime = newTime.replace("[", "");
        newTime = newTime.replace("]", "");
        LocalDateTime parsedTime = LocalDateTime.parse(newTime, dtf);
        return dtf.format(parsedTime);
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<ItemStack> getPlayer1Items() {
        return player1Items;
    }

    public List<ItemStack> getPlayer2Items() {
        return player2Items;
    }

    public OfflinePlayer getPlayer1() {
        return player1;
    }

    public OfflinePlayer getPlayer2() {
        return player2;
    }
}
