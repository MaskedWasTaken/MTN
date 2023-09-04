package me.tahacheji.mafana.menu.offer;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.data.TradeMarket;
import me.tahacheji.mafana.data.TradeOffer;
import me.tahacheji.mafana.menu.TradeMarketMenu;
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

public class TradeMarketCreateOfferMenu {


    int[] openSlots = new int[]{1,2,3,10,11,12,19,20,21};

    public Gui getTradeMarketOffer(Player player, List<ItemStack> offerItems, TradeMarket tradeMarket, String note) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Create Offer"))
                .rows(3)
                .disableAllInteractions()
                .create();
        AtomicBoolean sentOffer = new AtomicBoolean(false);
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

        gui.setPlayerInventoryAction(event -> {
            ItemStack itemStack = event.getCurrentItem();
            if(itemStack != null) {
                if (itemStack.getItemMeta() != null) {
                    if (new TradeUtil().areAllSlotsFilled(openSlots, gui)) {
                        event.setCancelled(true);
                        return;
                    }
                    for (int i : openSlots) {
                        if (gui.getGuiItem(i) != null) {
                            if (gui.getGuiItem(i).getItemStack().getType() == Material.AIR) {
                                gui.updateItem(i, itemStack);
                                offer.add(itemStack);
                                event.setCurrentItem(null);
                                break;
                            }
                        }
                    }
                }
            }
        });
        gui.setDefaultClickAction(e -> {
            if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null) {
                    if (itemStack.getItemMeta() != null) {
                        for (int i : openSlots) {
                            if (e.getSlot() == i) {
                                player.getInventory().addItem(itemStack);
                                gui.updateItem(i, new GuiItem(Material.AIR));
                                offer.remove(itemStack);
                                break;
                            }
                        }
                    }
                }
            }
        });
        gui.setCloseGuiAction(inventoryCloseEvent -> {
            if(!sentOffer.get()) {
                for (int i : openSlots) {
                    if (gui.getGuiItem(i).getItemStack().getType() != Material.AIR) {
                        player.getInventory().addItem(gui.getGuiItem(i).getItemStack());
                    }
                }
            }
        });

        gui.setItem(16, ItemBuilder.from(Material.BARRIER).setName(ChatColor.RED + "Click To Cancel Offer").asGuiItem(event -> {
            new TradeMarketMenu().getTradeMarketMenu(player);
            sentOffer.set(false);
        }));
        gui.setItem(6, new GuiItem(tradeMarket.getItem()));
        gui.setItem(15, ItemBuilder.from(Material.IRON_INGOT).setName(ChatColor.GOLD + "Click To Send Offer").asGuiItem(event -> {
            if(!offer.isEmpty()) {
                MafanaTradeNetwork.getInstance().getTradeOfferData().setTradeOffer(new TradeOffer(tradeMarket, player, offer, note,  "2"));
                sentOffer.set(true);
                new TradeMarketMenu().getTradeMarketMenu(player);
            }
        }));
        gui.setItem(24, ItemBuilder.from(Material.NAME_TAG).setName(ChatColor.YELLOW + "Click To Set Note: " + note).asGuiItem(event -> {
            openNoteSign(player, offer, tradeMarket, note);
            sentOffer.set(true);
        }));
        gui.getFiller().fill(new GuiItem(greystainedglass));
        return gui;
    }

    public void openNoteSign(Player player, List<ItemStack> offerItems, TradeMarket tradeMarket, String note){
        SignGUI.builder()
                .setLines(null, "---------------", note, "MTN") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    if (x.isEmpty()) {
                        return List.of(SignGUIAction.run(() -> getTradeMarketOffer(player,offerItems,tradeMarket, x).open(player)));
                    }
                    return List.of(SignGUIAction.run(() -> getTradeMarketOffer(player,offerItems,tradeMarket,x).open(player)));
                }).callHandlerSynchronously(MafanaTradeNetwork.getInstance()).build().open(player);
    }
}
