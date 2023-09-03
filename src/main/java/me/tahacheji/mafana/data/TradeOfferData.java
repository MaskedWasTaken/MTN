package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeOfferData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public TradeOfferData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void addPlayer(Player player) {
        if (!sqlGetter.exists(player.getUniqueId())) {
            sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
            sqlGetter.setString(new MysqlValue("TRADE_OFFER", player.getUniqueId(), ""));
        }
    }

    public List<TradeOffer> getAllTradeOffer() {
        List<TradeOffer> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> listingTradeOffers = sqlGetter.getAllString(new MysqlValue("TRADE_OFFER"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                UUID playerUUID = UUID.fromString(playerUUIDs.get(i));
                String tradeOffers = listingTradeOffers.get(i);
                if (playerUUID == null) {
                    continue;
                }
                allListings.addAll(new EncryptionUtil().decryptTradeOffer(tradeOffers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }

    public List<TradeOffer> getTradeMarketTradeOffers(TradeMarket tradeMarket) {
        List<TradeOffer> tradeOfferList = new ArrayList<>();
        if(tradeMarket == null) {
            return tradeOfferList;
        }
        for(TradeOffer tradeOffer : getAllTradeOffer()) {
            if(tradeOffer.getTradeMarket().getUuid().toString().equalsIgnoreCase(tradeMarket.getUuid().toString())) {
                tradeOfferList.add(tradeOffer);
            }
        }
        return tradeOfferList;
    }

    public void removeTradeOffer(Player player, TradeOffer tradeOffer) {
        List<TradeOffer> tradeOfferList = getAllPlayerTradeOffers(player);
        tradeOfferList.remove(tradeOffer);
        setTradeOffers(player, tradeOfferList);
    }

    public void addTradeOffer(Player player, TradeOffer tradeOffer) {
        List<TradeOffer> tradeOfferList = getAllPlayerTradeOffers(player);
        UUID uuid = UUID.randomUUID();
        tradeOffer.setUuid(uuid);
        tradeOfferList.add(tradeOffer);
        setTradeOffers(player, tradeOfferList);
    }

    public void setTradeOffer(Player player, TradeOffer newTradeOffer) {
        // Get the list of all player's TradeOffers
        List<TradeOffer> allPlayerTradeOffers = getAllPlayerTradeOffers(player);

        // Check if the new TradeOffer already exists in the list
        boolean found = false;
        for (TradeOffer existingOffer : allPlayerTradeOffers) {
            // You need to define a way to compare TradeOffers; for example, comparing unique IDs
            if (existingOffer.getUuid().toString().equalsIgnoreCase(newTradeOffer.getUuid().toString())) {
                // Update the existing TradeOffer with the new data
                existingOffer.setCanceled(newTradeOffer.isCanceled());
                existingOffer.setAccepted(newTradeOffer.isAccepted());
                found = true;
                break;
            }
        }

        if (!found) {
            // If the new TradeOffer doesn't exist, add it to the list
            allPlayerTradeOffers.add(newTradeOffer);
        }

        // Save the updated list of TradeOffers back to the database
        setTradeOffers(player, allPlayerTradeOffers);
    }

    public void setTradeOffers(Player player, List<TradeOffer> offers) {
        sqlGetter.setString(new MysqlValue("TRADE_OFFER", player.getUniqueId(), new EncryptionUtil().encryptTradeOffer(offers)));
    }

    public List<TradeOffer> getAllPlayerTradeOffers(Player player) {
        return new EncryptionUtil().decryptTradeOffer(sqlGetter.getString(player.getUniqueId(), new MysqlValue("TRADE_OFFER")));
    }


    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_player_offers",
                new MysqlValue("NAME", ""),
                new MysqlValue("TRADE_OFFER", "")
        );
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
