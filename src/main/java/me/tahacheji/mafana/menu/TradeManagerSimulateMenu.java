package me.tahacheji.mafana.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.data.TradeManagerTransaction;
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

public class TradeManagerSimulateMenu {

    int[] userSide = new int[]{0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20};
    int[] otherUserSide = new int[]{5, 6, 7, 8, 14, 15, 16, 17, 24, 25, 26};

    public Gui openTradeGUI(Player player, TradeManagerTransaction tradeManagerTransaction) {
        Gui tradeMenu = Gui.gui()
                .disableAllInteractions()
                .title(Component.text(ChatColor.GOLD + "MafanaTrade Log"))
                .rows(3)
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);
        tradeMenu.setItem(4, new GuiItem(greystainedglass));
        tradeMenu.setItem(13, new GuiItem(greystainedglass));
        tradeMenu.setItem(22, new GuiItem(greystainedglass));

        for (int i : userSide) {
            tradeMenu.setItem(i, new GuiItem(Material.AIR));
        }
        for (int i : otherUserSide) {
            tradeMenu.setItem(i, new GuiItem(Material.AIR));
        }

        // Check if the player is player1 or player2
        if (tradeManagerTransaction.getPlayer1().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
            // Populate user's side with player1's items
            populateUserSide(tradeMenu, tradeManagerTransaction.getPlayer1Items(), userSide);

            // Populate other user's side with player2's items
            populateOtherUserSide(tradeMenu, tradeManagerTransaction.getPlayer2Items(), otherUserSide);
        } else {
            // Populate user's side with player2's items
            populateUserSide(tradeMenu, tradeManagerTransaction.getPlayer2Items(), userSide);

            // Populate other user's side with player1's items
            populateOtherUserSide(tradeMenu, tradeManagerTransaction.getPlayer1Items(), otherUserSide);
        }

        tradeMenu.setItem(22, ItemBuilder.from(Material.BARRIER).name(Component.text(ChatColor.DARK_RED + "Close View")).asGuiItem(e -> {
            new TradeManagerLogMenu().getTradeMarketTransactions(player, "", true).open(player);
        }));
        return tradeMenu;
    }

    // Helper method to populate items on the user side
    private void populateUserSide(Gui tradeMenu, List<ItemStack> items, int[] userSide) {
        int index = 0;
        for (ItemStack item : items) {
            if (index >= userSide.length) {
                break;
            }
            tradeMenu.setItem(userSide[index++], new GuiItem(item));
        }
    }

    // Helper method to populate items on the other user side
    private void populateOtherUserSide(Gui tradeMenu, List<ItemStack> items, int[] otherUserSide) {
        int index = 0;
        for (ItemStack item : items) {
            if (index >= otherUserSide.length) {
                break;
            }
            tradeMenu.setItem(otherUserSide[index++], new GuiItem(item));
        }
    }
}
