package me.tahacheji.mafana.menu;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeMarketTransaction;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.offer.TradeMarketViewTradeOffer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TradeMarketTransactionsMenu {


    public PaginatedGui getTradeMarketTransactions(Player player, boolean offers) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaTradeNetwork"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Go Back");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(closeShop, event -> {
            new TradeMarketMenu().getTradeMarketMenu(player);
        }));
        if(!offers) {
            for(TradeOffer tradeOffer : MafanaTradeNetwork.getInstance().getTradeMarketTransaction().getAllPlayerOffers(player)) {
                for(TradeMarketTransaction tradeMarketTransaction : MafanaTradeNetwork.getInstance().getTradeMarketTransaction().getAllTransactions()) {
                    if(tradeMarketTransaction.getTradeOffer().getUuid().toString().equalsIgnoreCase(tradeOffer.getUuid().toString())) {
                        gui.addItem(new GuiItem(getItemStack(tradeMarketTransaction), event -> {
                            new TradeMarketViewTradeOffer().getViewTradeMarketOffer(player, tradeOffer, offers).open(player);
                        }));
                    }
                }
            }
        } else {
            for(TradeMarket tradeMarket : MafanaTradeNetwork.getInstance().getTradeMarketTransaction().getAllPlayerMarket(player)) {
                for(TradeMarketTransaction tradeMarketTransaction : MafanaTradeNetwork.getInstance().getTradeMarketTransaction().getAllTransactions()) {
                    if(tradeMarketTransaction.getTradeMarket().getUuid().toString().equalsIgnoreCase(tradeMarket.getUuid().toString())) {
                        gui.addItem(new GuiItem(getPlayerHead(tradeMarketTransaction.getTradeOffer().getPlayer().getName(), tradeMarketTransaction), event -> {
                            new TradeMarketViewTradeOffer().getViewTradeMarketOffer(player, tradeMarketTransaction.getTradeOffer(), offers).open(player);
                        }));
                    }
                }
            }
        }
        return gui;
    }

    public ItemStack getPlayerHead(String playerName, TradeMarketTransaction x) {
        TradeOffer listing = x.getTradeOffer();
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skullMeta.setDisplayName(playerName + "'s Offer");
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        if (listing.getPlayer().isOnline()) {
            itemLore.add(ChatColor.DARK_GRAY + "Offer Player: " + listing.getPlayer().getName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Offer Player: " + listing.getPlayer().getName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Offer UUID: " + listing.getUuid().toString());
        itemLore.add(ChatColor.DARK_GRAY + "Note: " + listing.getNote());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Time Traded: " + x.getTime());
        itemLore.add("------------------------");
        skullMeta.setLore(itemLore);
        playerHead.setItemMeta(skullMeta);

        return playerHead;
    }

    @NotNull
    private static ItemStack getItemStack(TradeMarketTransaction x) {
        TradeMarket listing = x.getTradeMarket();
        ItemStack item = listing.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        for (String string : itemMeta.getLore()) {
            itemLore.add(string);
        }
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        if (listing.getPlayer().isOnline()) {
            itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Listing UUID: " + listing.getUuid().toString());
        itemLore.add(ChatColor.DARK_GRAY + "Note: " + listing.getNote());
        itemLore.add(ChatColor.DARK_GRAY + "Offers Made: " + MafanaTradeNetwork.getInstance().getTradeOfferData().getTradeMarketTradeOffers(listing).size());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Time Accepted: " + x.getTime());
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
