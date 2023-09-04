package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeOfferData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public TradeOfferData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void setTradeOffer(TradeOffer tradeOffer) {
        UUID uuid;
        if(tradeOffer.getUuid() != null) {
             uuid = tradeOffer.getUuid();
             System.out.println(1);
        } else {
            uuid = UUID.randomUUID();
            tradeOffer.setUuid(uuid);
        }
        sqlGetter.setString(new MysqlValue("NAME", uuid, tradeOffer.getPlayer().getName()));
        sqlGetter.setString(new MysqlValue("TRADE_OFFER", uuid, new EncryptionUtil().encryptTradeOffer(tradeOffer)));
        sqlGetter.setString(new MysqlValue("TRADE_UUID", uuid, uuid.toString()));
        sqlGetter.setUUID(new MysqlValue("UUID", uuid, tradeOffer.getPlayer().getUniqueId()));
    }

    public void updateTradeOffer(TradeOffer tradeOffer) {
        sqlGetter.setString(new MysqlValue("TRADE_OFFER", tradeOffer.getUuid(), new EncryptionUtil().encryptTradeOffer(tradeOffer)));
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
                allListings.add(new EncryptionUtil().decryptTradeOffer(tradeOffers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }

    public List<TradeOffer> getAllPlayerTradeOffers(OfflinePlayer player) {
        List<TradeOffer> tradeOfferList = new ArrayList<>();
        for(TradeOffer tradeOffer : getAllTradeOffer()) {
            if(tradeOffer.getPlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                tradeOfferList.add(tradeOffer);
            }
        }
        return tradeOfferList;
    }

    public List<TradeOffer> getTradeMarketTradeOffers(TradeMarket tradeMarket) {
        List<TradeOffer> tradeOfferList = new ArrayList<>();
        if(tradeMarket == null) {
            return tradeOfferList;
        }
        for(TradeOffer tradeOffer : getAllTradeOffer()) {
            if(tradeOffer == null) {
                continue;
            }
            if(tradeOffer.getTradeMarket() == null) {
                continue;
            }
            if(tradeOffer.getTradeMarket().getUuid().toString().equalsIgnoreCase(tradeMarket.getUuid().toString())) {
                tradeOfferList.add(tradeOffer);
            }
        }
        return tradeOfferList;
    }

    public void removeTradeOffer(TradeOffer tradeOffer) {
        sqlGetter.removeString(tradeOffer.getUuid().toString(), new MysqlValue("TRADE_UUID"));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_player_offers",
                new MysqlValue("NAME", ""),
                new MysqlValue("TRADE_OFFER", ""),
                new MysqlValue("TRADE_UUID", "")
        );
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
