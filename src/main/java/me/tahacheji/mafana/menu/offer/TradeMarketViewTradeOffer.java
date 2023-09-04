package me.tahacheji.mafana.menu.offer;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeMarketTransaction;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.TradeMarketMenu;
import me.tahacheji.mafana.menu.TradeMarketTransactionsMenu;
import me.tahacheji.mafana.util.TradeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TradeMarketViewTradeOffer {


    int[] openSlots = new int[]{1, 2, 3, 10, 11, 12, 19, 20, 21};

    public Gui getTradeMarketOffer(Player player, TradeOffer tradeOffer) {
        Gui gui = Gui.gui()
                .title(Component.text(player.getName() + "'s " +ChatColor.GOLD + "Offer"))
                .rows(3)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);
        int slotIndex = 0;
        if (tradeOffer.getItemOffer() != null) {
            for (ItemStack offerItem : tradeOffer.getItemOffer()) {
                if (slotIndex >= openSlots.length) {
                    break;
                }
                int slot = openSlots[slotIndex];
                gui.setItem(slot, new GuiItem(offerItem));
                slotIndex++;
            }
        }

        for (int i = slotIndex; i < openSlots.length; i++) {
            int slot = openSlots[i];
            gui.setItem(slot, new GuiItem(Material.AIR));
        }

        if(player.getUniqueId().toString().equalsIgnoreCase(tradeOffer.getPlayer().getUniqueId().toString())) {
            gui.setItem(16, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Click To Cancel Offer").asGuiItem(event -> {
                tradeOffer.setX("1");
                MafanaTradeNetwork.getInstance().getTradeOfferData().removeTradeOffer(tradeOffer);
                MafanaTradeNetwork.getInstance().getTradeOfferData().setTradeOffer(tradeOffer);
                new TradeMarketOfferMenu().getTradeMarketOffer((Player) event.getWhoClicked(), tradeOffer.getTradeMarket(), "").open(player);
            }));
        }
        gui.setItem(15, new GuiItem(tradeOffer.getTradeMarket().getItem()));
        if(player.getUniqueId().toString().equalsIgnoreCase(tradeOffer.getTradeMarket().getPlayer().getUniqueId().toString())) {
            gui.setItem(6, ItemBuilder.from(Material.IRON_INGOT).setName(ChatColor.GOLD + "Click To Accept Offer").asGuiItem(event -> {
                tradeOffer.setX("3");
                TradeMarket tradeMarket = tradeOffer.getTradeMarket();
                MafanaTradeNetwork.getInstance().getTradeMarketData().removeListing(tradeMarket);

                MafanaTradeNetwork.getInstance().getTradeMarketTransaction().addTradeMarketTransaction(player, new TradeMarketTransaction(tradeMarket, tradeOffer));
                MafanaTradeNetwork.getInstance().getTradeMarketTransaction().addTradeMarketTransaction(tradeOffer.getPlayer(), new TradeMarketTransaction(tradeMarket, tradeOffer));
                MafanaTradeNetwork.getInstance().getTradeOfferData().removeTradeOffer(tradeOffer);
                MafanaTradeNetwork.getInstance().getTradeOfferData().setTradeOffer(tradeOffer);
                for(ItemStack itemStack : tradeOffer.getItemOffer()) {
                    player.getInventory().addItem(itemStack);
                }
                new TradeMarketMenu().getTradeMarketMenu(player);
            }));
        }
        gui.getFiller().fill(new GuiItem(greystainedglass));
        return gui;
    }

    public Gui getViewTradeMarketOffer(Player player, TradeOffer tradeOffer, boolean x) {
        Gui gui = Gui.gui()
                .title(Component.text(tradeOffer.getPlayer().getName() + "'s " +ChatColor.GOLD + "Offer"))
                .rows(3)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);
        int slotIndex = 0;
        if (tradeOffer.getItemOffer() != null) {
            for (ItemStack offerItem : tradeOffer.getItemOffer()) {
                if (slotIndex >= openSlots.length) {
                    break;
                }
                int slot = openSlots[slotIndex];
                gui.setItem(slot, new GuiItem(offerItem));
                slotIndex++;
            }
        }

        for (int i = slotIndex; i < openSlots.length; i++) {
            int slot = openSlots[i];
            gui.setItem(slot, new GuiItem(Material.AIR));
        }

        if(player.getUniqueId().toString().equalsIgnoreCase(tradeOffer.getPlayer().getUniqueId().toString())) {
            gui.setItem(16, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Click To Go Back").asGuiItem(event -> {
                new TradeMarketTransactionsMenu().getTradeMarketTransactions(player, x).open(player);
            }));
        }
        gui.setItem(15, new GuiItem(tradeOffer.getTradeMarket().getItem()));
        gui.getFiller().fill(new GuiItem(greystainedglass));
        return gui;
    }

}
