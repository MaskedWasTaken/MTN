package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeManagerData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);
    public TradeManagerData() {
        super("localhost", "3306", "mafanation", "root", "");
    }


    public void addTradeManagerTransaction(TradeManagerTransaction tradeManagerTransaction) {
        UUID uuid = UUID.randomUUID();
        tradeManagerTransaction.setUuid(uuid);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]";
        tradeManagerTransaction.setTime(time);

        sqlGetter.setString(new MysqlValue("TIME", uuid, time));
        sqlGetter.setString(new MysqlValue("TRADE_MANAGER", uuid, new EncryptionUtil().encryptTradeManagerTransaction(tradeManagerTransaction)));
        sqlGetter.setString(new MysqlValue("TRADE_MANAGER_UUID", uuid, uuid.toString()));
        sqlGetter.setUUID(new MysqlValue("UUID", uuid, uuid));
    }

    public List<TradeManagerTransaction> getAllPlayerTrades(OfflinePlayer offlinePlayer) {
        List<TradeManagerTransaction> tradeManagerTransactions = new ArrayList<>();
        for(TradeManagerTransaction tradeManagerTransaction : getAllTransactions()) {
            if(tradeManagerTransaction.getPlayer1().getName().equalsIgnoreCase(offlinePlayer.getUniqueId().toString()) ||
            tradeManagerTransaction.getPlayer2().getName().equalsIgnoreCase(offlinePlayer.getUniqueId().toString())) {
                tradeManagerTransactions.add(tradeManagerTransaction);
            }
        }
        return tradeManagerTransactions;
    }

    public List<TradeManagerTransaction> getAllTransactions() {
        List<TradeManagerTransaction> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> tradeManagers = sqlGetter.getAllString(new MysqlValue("TRADE_MANAGER"));
            for (int i = 0; i < listingUUIDs.size(); i++) {
                allListings.add(new EncryptionUtil().decryptTradeManagerTransaction(tradeManagers.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allListings;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("trade_transactions",
                new MysqlValue("TIME", ""),
                new MysqlValue("TRADE_MANAGER", ""),
                new MysqlValue("TRADE_MANAGER_UUID","")
        );
    }
}
