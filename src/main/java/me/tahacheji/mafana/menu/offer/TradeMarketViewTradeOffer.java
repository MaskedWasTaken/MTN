package me.tahacheji.mafana.menu.offer;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
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



    public Gui getTradeMarketOffer(Player player, TradeOffer tradeOffer) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Create Offer"))
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
        List<ItemStack> offer = new ArrayList<>();
        if(offerItems != null) {
            for (ItemStack offerItem : offerItems) {
                if (slotIndex >= openSlots.length) {
                    break;
                }
                int slot = openSlots[slotIndex];
                gui.setItem(slot, new GuiItem(offerItem));
                offer.add(offerItem);
                slotIndex++;
            }
        }

        for (int i = slotIndex; i < openSlots.length; i++) {
            int slot = openSlots[i];
            gui.setItem(slot, new GuiItem(Material.AIR));
        }

        gui.setItem(16, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Click To Cancel Offer").asGuiItem(event -> {
            gui.close(player);
            sentOffer.set(false);
        }));
        gui.setItem(6, new GuiItem(tradeMarket.getItem()));
        gui.setItem(15, ItemBuilder.from(Material.IRON_INGOT).setName(ChatColor.GOLD + "Click To Send Offer").asGuiItem(event -> {
            if(!offer.isEmpty()) {
                MafanaTradeNetwork.getInstance().getTradeOfferData().addTradeOffer(player, new TradeOffer(tradeMarket, player, offer, note, false, false));
                sentOffer.set(true);
                gui.close(player);
            }
        }));
        gui.setItem(24, ItemBuilder.from(Material.NAME_TAG).setName(ChatColor.YELLOW + "Click To Set Note: " + note).asGuiItem(event -> {
            openNoteSign(player, offer, tradeMarket, note);
            sentOffer.set(true);
        }));
        gui.getFiller().fill(new GuiItem(greystainedglass));
        return gui;
    }

}
