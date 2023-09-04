package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeMarketTransactionData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);
    public TradeMarketTransactionData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void addTradeMarketTransaction(OfflinePlayer player, TradeMarketTransaction tradeMarketTransaction) {
        UUID uuid = UUID.randomUUID();
        tradeMarketTransaction.setUuid(uuid);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        tradeMarketTransaction.setTime(time);

        sqlGetter.setString(new MysqlValue("NAME", uuid, player.getName()));
        sqlGetter.setString(new MysqlValue("TIME", uuid, time));
        sqlGetter.setString(new MysqlValue("TRADE_MARKET", uuid, new EncryptionUtil().encryptTradeMarket(tradeMarketTransaction.getTradeMarket())));
        sqlGetter.setString(new MysqlValue("TRADE_OFFER", uuid, new EncryptionUtil().encryptTradeOffer(tradeMarketTransaction.getTradeOffer())));
        sqlGetter.setString(new MysqlValue("TRANSACTION_UUID", uuid, uuid.toString()));
        sqlGetter.setUUID(new MysqlValue("UUID", uuid, player.getUniqueId()));
    }

    public List<TradeMarket> getAllPlayerMarket(OfflinePlayer player) {
        List<TradeMarket> tradeMarketList = new ArrayList<>();
        for(TradeMarketTransaction tradeMarketTransaction : getAllTransactions()) {
            if(tradeMarketTransaction.getTradeMarket().getPlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                tradeMarketList.add(tradeMarketTransaction.getTradeMarket());
            }
        }
        return tradeMarketList;
    }

    public List<TradeOffer> getAllPlayerOffers(OfflinePlayer player) {
        List<TradeOffer> tradeMarketTransactions = new ArrayList<>();
        for(TradeMarketTransaction tradeMarketTransaction : getAllTransactions()) {
            if(tradeMarketTransaction.getTradeOffer().getPlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                tradeMarketTransactions.add(tradeMarketTransaction.getTradeOffer());
            }
        }
        return tradeMarketTransactions;
    }

    public List<TradeMarketTransaction> getAllTransactions() {
        List<TradeMarketTransaction> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> transactionTime = sqlGetter.getAllString(new MysqlValue("TIME"));
            List<String> tradeMarkets = sqlGetter.getAllString(new MysqlValue("TRADE_MARKET"));
            List<String> tradeOffers = sqlGetter.getAllString(new MysqlValue("TRADE_OFFER"));
            List<String> transactionUuids = sqlGetter.getAllString(new MysqlValue("TRANSACTION_UUID"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                String playerUUIDString = playerUUIDs.get(i);
                UUID playerUUID = (playerUUIDString != null) ? UUID.fromString(playerUUIDString) : null;
                TradeMarket tradeMarket = new EncryptionUtil().decryptTradeMarket(tradeMarkets.get(i));
                TradeOffer tradeOffer = new EncryptionUtil().decryptTradeOffer(tradeOffers.get(i));
                String time = transactionTime.get(i);
                UUID transactionUUID = UUID.fromString(transactionUuids.get(i));
                if (playerUUID == null || tradeMarket == null || tradeOffer == null) {
                    continue;
                }

                OfflinePlayer listingPlayer = Bukkit.getOfflinePlayer(playerUUID);

                TradeMarketTransaction tradeMarketTransaction = new TradeMarketTransaction(tradeMarket, tradeOffer, transactionUUID);
                tradeMarketTransaction.setTime(time);
                allListings.add(tradeMarketTransaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_trade_transactions",
                new MysqlValue("NAME", ""),
                new MysqlValue("TIME", ""),
                new MysqlValue("TRADE_MARKET", ""),
                new MysqlValue("TRADE_OFFER", ""),
                new MysqlValue("TRANSACTION_UUID","")
        );
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
