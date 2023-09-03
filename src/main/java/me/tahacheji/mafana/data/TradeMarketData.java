package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.util.EncryptionUtil;
import me.tahacheji.mafana.util.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeMarketData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);
    public TradeMarketData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    public void addTradeMarket(TradeMarket tradeMarket) {
        UUID uuid = UUID.randomUUID();
        tradeMarket.setUuid(uuid);
        sqlGetter.setString(new MysqlValue("NAME", uuid, tradeMarket.getPlayer().getName()));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", uuid, NBTUtils.getString(tradeMarket.getItem(), "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", uuid, new EncryptionUtil().encodeItem(tradeMarket.getItem())));
        sqlGetter.setString(new MysqlValue("NOTE", uuid, tradeMarket.getNote()));

        sqlGetter.setString(new MysqlValue("TRADE_UUID", uuid, uuid.toString()));

        sqlGetter.setUUID(new MysqlValue("UUID", uuid, tradeMarket.getPlayer().getUniqueId()));
    }

    public TradeMarket getTradeMarketFromUUID(UUID tradeMarketUUID) {
        try {
            String uuidString = tradeMarketUUID.toString();
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(new MysqlValue("ITEM"));
            List<String> noteData = sqlGetter.getAllString(new MysqlValue("NOTE"));
            List<String> listingUUIDStrings = sqlGetter.getAllString(new MysqlValue("TRADE_UUID"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                String playerUUIDString = playerUUIDs.get(i);
                UUID playerUUID = (playerUUIDString != null) ? UUID.fromString(playerUUIDString) : null;

                // Check if this entry matches the provided tradeMarketUUID
                if (listingUUIDStrings.get(i).equalsIgnoreCase(uuidString)) {
                    String itemData = itemsData.get(i);
                    String note = noteData.get(i);

                    // Check for null values before proceeding
                    if (playerUUID == null || itemData == null) {
                        continue;
                    }

                    Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                    ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                    TradeMarket listing = new TradeMarket(listingPlayer, item, note, uuidString);
                    return listing; // Found the matching TradeMarket
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // TradeMarket with the given tradeMarketUUID was not found
    }



    public List<TradeMarket> getAllPlayerListings(OfflinePlayer player) {
        List<TradeMarket> playerListings = new ArrayList<>();
        for(TradeMarket tradeMarket : getAllListings()) {
            if(tradeMarket.getOfflinePlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                playerListings.add(tradeMarket);
            }
        }
        return playerListings;
    }


    public List<TradeMarket> getAllListings() {
        List<TradeMarket> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(new MysqlValue("ITEM"));
            List<String> noteData = sqlGetter.getAllString(new MysqlValue("NOTE"));
            List<String> listingUUIDStrings = sqlGetter.getAllString(new MysqlValue("TRADE_UUID"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                String playerUUIDString = playerUUIDs.get(i);
                UUID playerUUID = (playerUUIDString != null) ? UUID.fromString(playerUUIDString) : null;
                String itemData = itemsData.get(i);
                String note = noteData.get(i);
                String uuidString = listingUUIDStrings.get(i);

                // Check for null values before proceeding
                if (playerUUID == null || itemData == null || uuidString == null) {
                    continue;
                }

                Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                TradeMarket listing = new TradeMarket(listingPlayer, item, note, uuidString);
                listing.setTradeOfferList(MafanaTradeNetwork.getInstance().getTradeOfferData().getTradeMarketTradeOffers(listing));
                allListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }


    public void removeListing(TradeMarket listing) {
        sqlGetter.removeString(listing.getUuid().toString(), new MysqlValue("TRADE_UUID"));
    }
    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_trade_items",
                new MysqlValue("NAME", ""),
                new MysqlValue("ITEM_NAME", ""),
                new MysqlValue("ITEM", ""),
                new MysqlValue("NOTE", ""),
                new MysqlValue("TRADE_UUID","")
        );
    }
}
