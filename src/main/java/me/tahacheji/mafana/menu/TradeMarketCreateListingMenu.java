package me.tahacheji.mafana.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.util.NBTUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TradeMarketCreateListingMenu {

    public Gui getMarketListItemGUI(Player player, ItemStack itemStack, String note) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "List Trade Item"))
                .rows(4)
                .disableAllInteractions()
                .create();
        AtomicBoolean x = new AtomicBoolean(false);
        gui.setItem(13, new GuiItem(itemStack, event -> {
            if(gui.getGuiItem(13).getItemStack().getType() != Material.AIR) {
                gui.updateItem(13, new GuiItem(Material.AIR));
                player.getInventory().addItem(gui.getGuiItem(13).getItemStack());
            }
        }));

        ItemStack listItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta listItemMeta = listItem.getItemMeta();
        listItemMeta.setDisplayName(ChatColor.YELLOW + "Click To List Item");
        listItem.setItemMeta(listItemMeta);

        ItemStack setNote = new ItemStack(Material.NAME_TAG);
        ItemMeta setNoteMeta = setNote.getItemMeta();
        setNoteMeta.setDisplayName(ChatColor.YELLOW + "Click To Set Note");
        setNote.setItemMeta(setNoteMeta);

        gui.setItem(28, new GuiItem(setNote, event -> {
            gui.getInventory().setItem(1, new ItemStack(Material.OAK_SIGN));
            openNoteSign(player, gui.getGuiItem(13).getItemStack());
            x.set(true);
        }));

        gui.setItem(31, new GuiItem(listItem, event -> {
            if(gui.getGuiItem(13).getItemStack().getType() != Material.AIR) {
                MafanaTradeNetwork.getInstance().getTradeMarketData().addTradeMarket(new TradeMarket(player, gui.getGuiItem(13).getItemStack(), note));
                x.set(true);
                gui.close(player);
            }
        }));

        gui.setCloseGuiAction(inventoryCloseEvent -> {
            if(gui.getGuiItem(13).getItemStack().getType() != Material.AIR) {
                if(!x.get()) {
                    player.getInventory().addItem(gui.getGuiItem(13).getItemStack());
                }
            }
        });

        gui.setPlayerInventoryAction(event -> {
            ItemStack i = event.getCurrentItem();
            if(i != null) {
                if(i.getItemMeta() != null) {
                    gui.updateItem(13, i);
                    event.setCurrentItem(null);
                }
            }
        });

        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(new ArrayList<>());
        greystainedglass.setItemMeta(newmeta);
        greystainedglass = NBTUtils.setString(greystainedglass, "NONCLICKABLE", "");

        gui.getFiller().fill(ItemBuilder.from(greystainedglass).asGuiItem());
        return gui;
    }

    public void openNoteSign(Player player, ItemStack itemStack){
        SignGUI.builder()
                .setLines(null, "---------------", "Set Note", "MafanaMarket") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    if (x.isEmpty()) {
                        return List.of(SignGUIAction.run(() -> getMarketListItemGUI(player,itemStack, x).open(player)));
                    }
                    return List.of(SignGUIAction.run(() -> getMarketListItemGUI(player,itemStack, x).open(player)));
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }

}
