package me.tahacheji.mafana.menu.offer;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.menu.TradeMarketMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeMarketOfferStatusMenu {


    public Gui getTradeMarketOffer(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Trade Status"))
                .rows(1)
                .disableAllInteractions()
                .create();

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Go Back");
        closeShopeta.setLore(new ArrayList<>());
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(closeShop, event -> {
            new TradeMarketMenu().getTradeMarketMenu(player);
        }));

        gui.setItem(2, ItemBuilder.from(Material.FERMENTED_SPIDER_EYE).setName(ChatColor.RED + "Canceled Offers").asGuiItem(event -> {
            new TradeMarketOfferEventMenu().getTradeMarketOffer(player, true).open(player);
        }));
        gui.setItem(4, ItemBuilder.from(Material.CHEST).setName(ChatColor.YELLOW + "Pending Offers").asGuiItem(event -> {
            new TradeMarketOfferMenu().getTradeMarketOffer(player, null, player.getName()).open(player);
        }));
        gui.setItem(6, ItemBuilder.from(Material.GOLD_BLOCK).setName(ChatColor.GOLD + "Accepted Offers").asGuiItem(event -> {
            new TradeMarketOfferEventMenu().getTradeMarketOffer(player, false).open(player);
        }));
        gui.getFiller().fill(ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        return gui;
    }
}
