package me.tahacheji.mafana;

import me.tahacheji.mafana.command.TradeCommand;
import me.tahacheji.mafana.data.*;
import me.tahacheji.mafana.event.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MafanaTradeNetwork extends JavaPlugin {

    private static MafanaTradeNetwork instance;
    private List<TradeManager> tradeManagerList = new ArrayList<>();
    private List<TradeRequest> tradeRequestList = new ArrayList<>();

    private TradeMarketData tradeMarketData;

    private TradeOfferData tradeOfferData;

    private TradeMarketTransactionData tradeMarketTransaction;
    private TradeManagerData tradeManagerData;
    @Override
    public void onEnable() {
        instance = this;
        tradeMarketData = new TradeMarketData();
        tradeOfferData = new TradeOfferData();
        tradeMarketTransaction = new TradeMarketTransactionData();
        tradeManagerData = new TradeManagerData();
        tradeManagerData.connect();
        tradeMarketData.connect();
        tradeOfferData.connect();
        tradeMarketTransaction.connect();
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getCommand("mt").setExecutor(new TradeCommand());
    }

    public TradeManagerData getTradeManagerData() {
        return tradeManagerData;
    }

    public TradeMarketTransactionData getTradeMarketTransaction() {
        return tradeMarketTransaction;
    }

    public TradeOfferData getTradeOfferData() {
        return tradeOfferData;
    }

    public TradeMarketData getTradeMarketData() {
        return tradeMarketData;
    }

    public List<TradeManager> getTradeManagerList() {
        return tradeManagerList;
    }

    public List<TradeRequest> getTradeRequestList() {
        return tradeRequestList;
    }

    public static MafanaTradeNetwork getInstance() {
        return instance;
    }
}
