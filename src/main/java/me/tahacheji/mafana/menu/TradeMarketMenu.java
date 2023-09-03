package me.tahacheji.mafana.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.ItemType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeMarketMenu {

    private Gui tradeMarketGUI;

    public void getTradeMarketMenu(Player player) {
        tradeMarketGUI = Gui.gui()
                .disableAllInteractions()
                .title(Component.text(ChatColor.GOLD + "MafanaTradeNetwork"))
                .rows(6)
                .disableAllInteractions()
                .create();

        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        tradeMarketGUI.setItem(10, ItemBuilder.from(getClickItem(ItemType.SWORD)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.SWORD).open(player);
        }));
        tradeMarketGUI.setItem(11, ItemBuilder.from(getClickItem(ItemType.STAFF)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.STAFF).open(player);
        }));
        tradeMarketGUI.setItem(12, ItemBuilder.from(getClickItem(ItemType.BOW)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.BOW).open(player);
        }));
        tradeMarketGUI.setItem(19, ItemBuilder.from(getClickItem(ItemType.ARMOR)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.ARMOR).open(player);
        }));
        tradeMarketGUI.setItem(20, ItemBuilder.from(getClickItem(ItemType.MATERIAL)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.MATERIAL).open(player);
        }));
        tradeMarketGUI.setItem(21, ItemBuilder.from(getClickItem(ItemType.ITEM)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", ItemType.ITEM).open(player);
        }));
        tradeMarketGUI.setItem(28, ItemBuilder.from(getClickItem(null)).asGuiItem(e -> {
            new TradeMarketListingMenu().getMarketShopGui(player, "", "", null).open(player);
        }));

        tradeMarketGUI.setItem(25, new GuiItem(getListingInfo(player), event -> {
            new TradeMarketPlayerListingMenu().getMarketShopGui(player).open(player);
        }));
        tradeMarketGUI.setItem(34, new GuiItem(getListItem(), event -> {
            new TradeMarketCreateListingMenu().getMarketListItemGUI(player, new ItemStack(Material.AIR), "").open(player);
        }));
        tradeMarketGUI.setItem(16, new GuiItem(getSearchPlayer(), event -> {
            openSearchSign(player);
        }));
        tradeMarketGUI.setItem(49, ItemBuilder.from(getCloseShop()).asGuiItem(e -> {
            tradeMarketGUI.close(player);
        }));
        tradeMarketGUI.getFiller().fill(new GuiItem(greystainedglass));
        tradeMarketGUI.open(player);
    }

    public void openSearchSign(Player player) {
        SignGUI.builder()
                .setLines(null, "---------------", "Search", "MafanaMarket") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    Player newPlayer = Bukkit.getPlayer(x);
                    if (newPlayer == null) {
                        return List.of(SignGUIAction.run(() -> getTradeMarketMenu(player)),
                                SignGUIAction.run(() -> player.sendMessage(ChatColor.RED + "MafanaMarket: PLAYER_NOT_FOUND")));
                    }
                    return List.of(SignGUIAction.run(() -> new TradeMarketPlayerListingMenu().getMarketShopGui(newPlayer).open(player)));
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }

    public ItemStack getSearchPlayer() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Player Listings");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to look up players listings");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getCloseShop() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to close the menu");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getListItem() {
        ItemStack item = new ItemStack(Material.OAK_SIGN);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "List Item");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to list a item on the market");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    public ItemStack getClickItem(ItemType type) {
        ItemStack item = null;
        if (type == ItemType.ITEM) {
            item = new ItemStack(Material.ROTTEN_FLESH);
        }
        if (type == ItemType.BOW) {
            item = new ItemStack(Material.BOW);
        }
        if (type == ItemType.SWORD) {
            item = new ItemStack(Material.IRON_SWORD);
        }
        if (type == ItemType.MATERIAL) {
            item = new ItemStack(Material.SLIME_BALL);
        }
        if (type == ItemType.SPELL) {
            item = new ItemStack(Material.BOOK);
        }
        if (type == ItemType.STAFF) {
            item = new ItemStack(Material.FEATHER);
        }
        if (type == ItemType.ARMOR) {
            item = new ItemStack(Material.NETHERITE_INGOT);
        }
        if (type == null) {
            item = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.GREEN + "All Listings");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Click to see all listings");
            lore.add("--------------------------");
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.GREEN + type.getLore());
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to see all " + type.getLore() + " listings");
        lore.add("--------------------------");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getListingInfo(Player player) {
        if (!MafanaTradeNetwork.getInstance().getTradeMarketData().getAllPlayerListings(player).isEmpty()) {
            ItemStack item = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.GOLD + "Listings");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Click to see your listings");
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        } else {
            ItemStack item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.RED + "Listing Error");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "You do not have any listings");
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
    }

    public Gui getTradeMarketGUI() {
        return tradeMarketGUI;
    }
}
