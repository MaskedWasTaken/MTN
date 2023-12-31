package me.tahacheji.mafana.menu.offer;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.TradeMarketMenu;
import me.tahacheji.mafana.menu.TradeMarketPlayerListingMenu;
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

public class TradeMarketOfferMenu {

    public PaginatedGui getTradeMarketOffer(Player player, TradeMarket tradeMarket, String playerFilter) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.YELLOW + "Trade Offers"))
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

        for(TradeOffer tradeOffer : MafanaTradeNetwork.getInstance().getTradeOfferData().getAllTradeOffer()) {
            if(tradeOffer == null) {
                continue;
            }
            if ((!playerFilter.isEmpty() && !tradeOffer.getPlayer().getName().equalsIgnoreCase(playerFilter)) || // Filter by note
                    (tradeMarket != null && !tradeMarket.getUuid().toString().equalsIgnoreCase(tradeOffer.getTradeMarket().getUuid().toString()))) {
                continue;
            }
            if(!tradeOffer.getX().equalsIgnoreCase("2")) {
                continue;
            }
            gui.addItem(new GuiItem(getPlayerHead(tradeOffer.getPlayer().getName(), tradeOffer), event -> {
                if(event.getClick() == ClickType.RIGHT) {
                    new TradeMarketViewTradeOffer().getTradeMarketOffer(player, tradeOffer).open(player);
                }
            }));
        }
        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Player Filter: " + (playerFilter.isEmpty() ? "None" : playerFilter));
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openSearchSign((Player) event.getWhoClicked(), tradeMarket, playerFilter);
        }));
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
        lore.add(ChatColor.DARK_GRAY + "Right Click view offer!");
        lore.add("------------------------");
        skullMeta.setLore(lore);
        playerHead.setItemMeta(skullMeta);

        return playerHead;
    }

    public void openSearchSign(Player player, TradeMarket tradeMarket, String playerFilter) {
        SignGUI.builder()
                .setLines(null, "---------------", "Search", "MafanaMarket") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    Player newPlayer = Bukkit.getPlayer(x);
                    if (newPlayer == null) {
                        return List.of(SignGUIAction.run(() -> getTradeMarketOffer(player,tradeMarket, x).open(player)),
                                SignGUIAction.run(() -> player.sendMessage(ChatColor.RED + "MafanaMarket: PLAYER_NOT_FOUND")));
                    }
                    return List.of(SignGUIAction.run(() -> getTradeMarketOffer(player,tradeMarket, x).open(player)));
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }
}
