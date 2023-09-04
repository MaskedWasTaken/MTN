package me.tahacheji.mafana.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeManagerTransaction;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeMarketTransaction;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.offer.TradeMarketViewTradeOffer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TradeManagerLogMenu {

    public PaginatedGui getTradeMarketTransactions(Player player, String playerFilter, boolean sortNewestToOldest) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.DARK_GREEN + "MafanaTradeNetwork"))
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

        ItemStack sortButton = new ItemStack(Material.COMPARATOR);
        ItemMeta sortButtonMeta = sortButton.getItemMeta();

        if (sortNewestToOldest) {
            sortButtonMeta.setDisplayName(ChatColor.YELLOW + "Sort: Newest to Oldest");
        } else {
            sortButtonMeta.setDisplayName(ChatColor.YELLOW + "Sort: Oldest to Newest");
        }
        sortButton.setItemMeta(sortButtonMeta);

        gui.setItem(53, new GuiItem(sortButton, event -> {
            boolean newSortDirection = !sortNewestToOldest;
            Player clicker = (Player) event.getWhoClicked();
            try {
                clicker.closeInventory();
                getTradeMarketTransactions(clicker, playerFilter, newSortDirection).open(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        ItemStack playerFilterButton = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta playerFilterButtonMeta = playerFilterButton.getItemMeta();
        playerFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Player Filter: " + (playerFilter.isEmpty() ? "None" : playerFilter));
        playerFilterButton.setItemMeta(playerFilterButtonMeta);

        gui.setItem(48, new GuiItem(playerFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), sortNewestToOldest, playerFilter);
        }));

        List<TradeManagerTransaction> tradeManagerTransactions = MafanaTradeNetwork.getInstance().getTradeManagerData().getAllPlayerTrades(player);
        List<TradeManagerTransaction> filteredTransactions = new ArrayList<>();
        for (TradeManagerTransaction tradeManagerTransaction : tradeManagerTransactions) {
            OfflinePlayer player1 = Bukkit.getOfflinePlayer(UUID.fromString(tradeManagerTransaction.getPlayer1().getName()));
            OfflinePlayer player2 = Bukkit.getOfflinePlayer(UUID.fromString(tradeManagerTransaction.getPlayer2().getName()));
            if (playerFilter.isEmpty() ||
                    player1.getName().equalsIgnoreCase(playerFilter) ||
                   player2.getName().equalsIgnoreCase(playerFilter)) {
                filteredTransactions.add(tradeManagerTransaction);
            }
        }
        if (sortNewestToOldest) {
            filteredTransactions.sort(Comparator.comparing(TradeManagerTransaction::getLocalDateTime).reversed());
        } else {
            filteredTransactions.sort(Comparator.comparing(TradeManagerTransaction::getLocalDateTime));
        }
        for (TradeManagerTransaction tradeManagerTransaction : filteredTransactions) {
            if(tradeManagerTransaction.getPlayer1().getName().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                gui.addItem(new GuiItem(getPlayerHead(Bukkit.getOfflinePlayer(UUID.fromString(tradeManagerTransaction.getPlayer2().getName())), tradeManagerTransaction), event -> {
                    new TradeManagerSimulateMenu().openTradeGUI(player, tradeManagerTransaction).open(player);
                }));
            } else {
                gui.addItem(new GuiItem(getPlayerHead(Bukkit.getOfflinePlayer(UUID.fromString(tradeManagerTransaction.getPlayer1().getName())), tradeManagerTransaction), event -> {
                    new TradeManagerSimulateMenu().openTradeGUI(player, tradeManagerTransaction).open(player);
                }));
            }
        }
        return gui;
    }

    public void openFilterSign(Player player, boolean sortNewestToOldest, String playerNameFilter) {
        String filterName = "Player";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", playerNameFilter, "MTN") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getTradeMarketTransactions(player, filterValue, sortNewestToOldest).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }

    public ItemStack getPlayerHead(OfflinePlayer player, TradeManagerTransaction x) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getName()));
        skullMeta.setDisplayName(player.getName() + " Trade: " + x.getTime());
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        if (player.isOnline()) {
            itemLore.add(ChatColor.DARK_GRAY + "Trade Player: " + Bukkit.getOfflinePlayer(UUID.fromString(x.getPlayer2().getName())).getName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Trade Player: " + Bukkit.getOfflinePlayer(UUID.fromString(x.getPlayer2().getName())).getName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Trade UUID: " + x.getUuid().toString());
        itemLore.add(ChatColor.DARK_GRAY + "Time Traded: " + x.getTime());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Click to view trade!");
        itemLore.add("------------------------");
        skullMeta.setLore(itemLore);
        playerHead.setItemMeta(skullMeta);

        return playerHead;
    }
}
