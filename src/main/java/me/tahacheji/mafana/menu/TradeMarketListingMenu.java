package me.tahacheji.mafana.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.ItemType;
import me.tahacheji.mafana.data.TradeMarket;
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

public class TradeMarketListingMenu {

    public PaginatedGui getMarketShopGui(Player player, String noteFilter, String itemFilter, ItemType itemType) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaTradeNetwork All Listings"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
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

        for (TradeMarket listing : MafanaTradeNetwork.getInstance().getTradeMarketData().getAllListings()) {
            String listingItemType = NBTUtils.getString(listing.getItem(), "ItemType");
            if(listing.isClaimed()) {
                continue;
            }
            if ((!noteFilter.isEmpty() && !listing.getNote().contains(noteFilter)) || // Filter by note
                    (!itemFilter.isEmpty() && !listing.getItem().getItemMeta().getDisplayName().contains(itemFilter)) || // Filter by item
                    (itemType != null && !matchesItemType(itemType, listingItemType))) { // Filter by itemType
                continue;
            }
            ItemStack item = getItemStack(listing);
            item = NBTUtils.setString(item, "ListUUID", listing.getUuid().toString());
            gui.addItem(new GuiItem(item, event -> {
                if (event.getClick() == ClickType.RIGHT) {
                    if(!player.getUniqueId().toString().equalsIgnoreCase(listing.getPlayer().getUniqueId().toString())) {

                    }
                    new TradeMarketCreateOfferMenu().getTradeMarketOffer(player, null, listing, "").open(player);
                }
                if (event.getClick() == ClickType.LEFT) {
                    new TradeMarketOfferMenu().getTradeMarketOffer(player, listing, "").open(player);
                }
            }));
        }

        ItemStack noteFilterButton = new ItemStack(Material.FEATHER);
        ItemMeta noteFilterButtonMeta = noteFilterButton.getItemMeta();
        noteFilterButtonMeta.setDisplayName(ChatColor.YELLOW + "Note Filter: " + (noteFilter.isEmpty() ? "None" : noteFilter));
        noteFilterButton.setItemMeta(noteFilterButtonMeta);

        gui.setItem(45, new GuiItem(noteFilterButton, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), noteFilter, itemFilter, true, itemType);
        }));

        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Item Filter: " + (itemFilter.isEmpty() ? "None" : itemFilter));
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openFilterSign((Player) event.getWhoClicked(), noteFilter, itemFilter, false, itemType);
        }));

        return gui;
    }

    @NotNull
    private static ItemStack getItemStack(TradeMarket listing) {
        ItemStack item = listing.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        for (String string : itemMeta.getLore()) {
            itemLore.add(string);
        }
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add("------------------------");
        if (listing.getPlayer().isOnline()) {
            itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getName() + " " + ChatColor.GREEN + "[ONLINE]");
        } else {
            itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getName() + " " + ChatColor.RED + "[OFFLINE]");
        }
        itemLore.add(ChatColor.DARK_GRAY + "Listing UUID: " + listing.getUuid().toString());
        itemLore.add(ChatColor.DARK_GRAY + "Note: " + listing.getNote());
        itemLore.add(ChatColor.DARK_GRAY + "Offers Made: " + MafanaTradeNetwork.getInstance().getTradeOfferData().getTradeMarketTradeOffers(listing).size());
        itemLore.add(ChatColor.DARK_GRAY + "");
        itemLore.add(ChatColor.DARK_GRAY + "Right Click to offer!");
        itemLore.add(ChatColor.DARK_GRAY + "Left Right to view all offers!");
        itemLore.add("------------------------");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    private boolean matchesItemType(ItemType selectedType, String itemTypeTag) {
        if (selectedType == null || itemTypeTag == null) {
            return true;
        }

        if (selectedType == ItemType.ARMOR) {
            return itemTypeTag.contains(ItemType.HELMET.getLore()) ||
                    itemTypeTag.contains(ItemType.CHESTPLATE.getLore()) ||
                    itemTypeTag.contains(ItemType.LEGGGINGS.getLore()) ||
                    itemTypeTag.contains(ItemType.BOOTS.getLore());
        } else {
            return itemTypeTag.contains(selectedType.getLore());
        }
    }

    public void openFilterSign(Player player, String noteFilter, String textFilter, boolean isNoteFilter, ItemType itemType) {
        String filterName = isNoteFilter ? "Note" : "Item";
        SignGUI.builder()
                .setLines(null, filterName + " Filter:", isNoteFilter ? noteFilter : textFilter, "MTM") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> {
                    String filterValue = result.getLineWithoutColor(0);
                    player.closeInventory();
                    if (isNoteFilter) {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getMarketShopGui(player, filterValue, textFilter, itemType).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    } else {
                        return List.of(SignGUIAction.run(() -> {
                            try {
                                getMarketShopGui(player, noteFilter, filterValue, itemType).open(player);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    }
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }


}
