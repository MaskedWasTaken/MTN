package me.tahacheji.mafana.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.ItemType;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.offer.TradeMarketCreateOfferMenu;
import me.tahacheji.mafana.menu.offer.TradeMarketOfferMenu;
import me.tahacheji.mafana.util.NBTUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TradeMarketPlayerListingMenu {


    public PaginatedGui getMarketShopGui(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaTradeNetwork User Listings"))
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

        for (TradeMarket listing : MafanaTradeNetwork.getInstance().getTradeMarketData().getAllPlayerListings(player)) {
            ItemStack item = getItemStack(player, listing);
            item = NBTUtils.setString(item, "ListUUID", listing.getUuid().toString());
            gui.addItem(ItemBuilder.from(item).asGuiItem(e -> {
                if (e.getClick() == ClickType.RIGHT) {
                    if(listing.getOfflinePlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                        for(TradeOffer tradeOffer : listing.getTradeOfferList()) {
                            tradeOffer.setX("1");
                            MafanaTradeNetwork.getInstance().getTradeOfferData().setTradeOffer(tradeOffer);
                        }
                        listing.removeListing();
                        getMarketShopGui(player).open(player);
                    } else {
                        new TradeMarketCreateOfferMenu().getTradeMarketOffer(player, null, listing, "").open(player);
                    }
                }
                if (e.getClick() == ClickType.LEFT) {
                    new TradeMarketOfferMenu().getTradeMarketOffer(player, listing, "").open(player);
                }
            }));
        }
        return gui;
    }

    @NotNull
    private static ItemStack getItemStack(Player player, TradeMarket listing) {
        ItemStack item = listing.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        for (String string : itemMeta.getLore()) {
            itemLore.add(string);
        }
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        if(listing.getOfflinePlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            itemLore.add(ChatColor.DARK_GRAY + "Right Click to remove!");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Right Click to offer!");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Left Right to view offers!");
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

}
