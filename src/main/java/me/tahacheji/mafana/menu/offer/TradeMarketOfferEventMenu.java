package me.tahacheji.mafana.menu.offer;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.TradeMarketMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeMarketOfferEventMenu {


    public PaginatedGui getTradeMarketOffer(Player player, boolean cancel) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaTradeNetwork Offers"))
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
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));
        for(TradeOffer tradeOffer : MafanaTradeNetwork.getInstance().getTradeOfferData().getAllPlayerTradeOffers(player)) {
            if (tradeOffer.getTradeMarket() != null) {
                if (cancel && tradeOffer.getX().equalsIgnoreCase("1")) {
                    gui.addItem(new GuiItem(getPlayerHead(tradeOffer.getTradeMarket().getPlayer().getName(), tradeOffer), event -> {
                        if (event.getClick() == ClickType.RIGHT) {
                            for (ItemStack itemStack : tradeOffer.getItemOffer()) {
                                player.getInventory().addItem(itemStack);
                            }
                            MafanaTradeNetwork.getInstance().getTradeOfferData().removeTradeOffer(tradeOffer);
                            new TradeMarketMenu().getTradeMarketMenu(player);
                        }
                    }));
                } else if (!cancel && tradeOffer.getX().equalsIgnoreCase("3")) {
                    gui.addItem(new GuiItem(getPlayerHead(tradeOffer.getTradeMarket().getPlayer().getName(), tradeOffer), event -> {
                        if (event.getClick() == ClickType.RIGHT) {
                            player.getInventory().addItem(tradeOffer.getTradeMarket().getItem());
                            MafanaTradeNetwork.getInstance().getTradeOfferData().removeTradeOffer(tradeOffer);
                            TradeMarket tradeMarket = tradeOffer.getTradeMarket();
                            MafanaTradeNetwork.getInstance().getTradeMarketData().removeListing(tradeMarket);
                            new TradeMarketMenu().getTradeMarketMenu(player);
                        }
                    }));
                }
            }
        }
        return gui;
    }

    public ItemStack getPlayerHead(String playerName, TradeOffer tradeOffer) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skullMeta.setDisplayName(playerName + "'s Offer");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "");
        lore.add("------------------------");
        for (ItemStack itemStack : tradeOffer.getItemOffer()) {
            lore.add(ChatColor.DARK_GRAY + "+ " + itemStack.getItemMeta().getDisplayName() + " x" + itemStack.getAmount());
        }
        lore.add("------------------------");
        if (tradeOffer.getPlayer().isOnline()) {
            lore.add(ChatColor.DARK_GRAY + "Player: " + tradeOffer.getPlayer().getPlayer().getDisplayName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            lore.add(ChatColor.DARK_GRAY + "Player: " + tradeOffer.getPlayer().getName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        lore.add(ChatColor.DARK_GRAY + "Trade UUID: " + tradeOffer.getUuid().toString());
        lore.add(ChatColor.DARK_GRAY + "Note: " + tradeOffer.getNote());
        lore.add(ChatColor.DARK_GRAY + "");
        if(tradeOffer.getX().equalsIgnoreCase("1")) {
            lore.add(ChatColor.DARK_GRAY + "Right Click To Redeem Offer!");
        } else if(tradeOffer.getX().equalsIgnoreCase("3")) {
            lore.add(ChatColor.DARK_GRAY + "Right Click To Redeem Item!");
        }
        lore.add("------------------------");
        skullMeta.setLore(lore);
        playerHead.setItemMeta(skullMeta);

        return playerHead;
    }


}
