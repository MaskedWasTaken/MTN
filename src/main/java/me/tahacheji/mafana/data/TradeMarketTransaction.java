package me.tahacheji.mafana.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TradeMarketTransaction {


    private final TradeMarket tradeMarket;
    private final TradeOffer tradeOffer;
    private String time;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    private UUID uuid;


    public TradeMarketTransaction(TradeMarket tradeMarket, TradeOffer tradeOffer) {
        this.tradeMarket = tradeMarket;
        this.tradeOffer = tradeOffer;
    }

    public TradeMarketTransaction(TradeMarket tradeMarket, TradeOffer tradeOffer, UUID uuid) {
        this.tradeMarket = tradeMarket;
        this.tradeOffer = tradeOffer;
        this.uuid = uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TradeOffer getTradeOffer() {
        return tradeOffer;
    }

    public TradeMarket getTradeMarket() {
        return tradeMarket;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

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
}
