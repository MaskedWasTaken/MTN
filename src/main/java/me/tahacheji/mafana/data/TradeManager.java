package me.tahacheji.mafana.data;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tahacheji.mafana.MafanaTradeNetwork;
import me.tahacheji.mafana.util.TradeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeManager {

    private final Player player;
    private TradeManager player2TradeManager;

    private List<ItemStack> playerItems = new ArrayList<>();

    private boolean playerAccept = false;

    private Gui tradeMenu = null;

    public TradeManager(Player player, TradeManager player2TradeManager) {
        this.player = player;
        this.player2TradeManager = player2TradeManager;
    }

    public TradeManager(Player player) {
        this.player = player;
    }

    int usersAcceptButton = 21;
    int otherUserAcceptButton = 23;
    int[] userSide = new int[]{0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20};
    int[] otherUserSide = new int[]{5, 6, 7, 8, 14, 15, 16, 17, 24, 25, 26};

    boolean tradeFinished = false;

    public void openTradeGUI(boolean open) {
        tradeMenu = Gui.gui()
                .disableAllInteractions()
                .title(Component.text(ChatColor.GOLD + "MafanaTrade"))
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

        tradeMenu.setPlayerInventoryAction(e -> {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null) {
                    if (itemStack.getItemMeta() != null) {
                        if (new TradeUtil().areAllSlotsFilled(userSide, tradeMenu)) {
                            e.setCancelled(true);
                            return;
                        }
                        for (int i : userSide) {
                            if (tradeMenu.getGuiItem(i) != null) {
                                if (tradeMenu.getGuiItem(i).getItemStack().getType() == Material.AIR) {
                                    tradeMenu.updateItem(i, itemStack);
                                    playerItems.add(itemStack);
                                    e.setCurrentItem(null);
                                    updateOtherUsersGUI();
                                    setPlayerAccept(false);
                                    updateAcceptButton(tradeMenu);
                                    updateOtherUsersGUI();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
        tradeMenu.setDefaultClickAction(e -> {
            if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null) {
                    if (itemStack.getItemMeta() != null) {
                        for (int i : userSide) {
                            if (e.getSlot() == i) {
                                player.getInventory().addItem(itemStack);
                                tradeMenu.updateItem(i, new GuiItem(Material.AIR));
                                playerItems.remove(itemStack);
                                updateOtherUsersGUI();
                                setPlayerAccept(false);
                                updateAcceptButton(tradeMenu);
                                updateOtherUsersGUI();
                                break;
                            }
                        }
                    }
                }
            }
        });


        GuiItem acceptButton = ItemBuilder.from(Material.RED_WOOL).name(getTradeStatus()).asGuiItem(e -> {
            toggleTradeAcceptance();
            updateAcceptButton(tradeMenu);
            finishTrade();
            player2TradeManager.finishTrade();
            updateOtherUsersGUI();
        });

        tradeMenu.updateItem(21, acceptButton);

        tradeMenu.setItem(22, ItemBuilder.from(Material.BARRIER).name(Component.text(ChatColor.DARK_RED + "Close Trade")).asGuiItem(e -> {
            tradeMenu.close(player);
        }));

        tradeMenu.setItem(otherUserAcceptButton, ItemBuilder.from(Material.RED_WOOL).name(getOtherUserTradeStatus()).asGuiItem());

        tradeMenu.setCloseGuiAction(e -> {
            if(!tradeFinished) {
                cancelTrade();
                destroyTradeManager();
            }
            tradeMenu.close(player2TradeManager.getPlayer());
        });

        if (open) {
            tradeMenu.open(player);
        }
    }

    public void cancelTrade() {
        for (int i : userSide) {
            if (tradeMenu.getGuiItem(i) != null) {
                player.getInventory().addItem(tradeMenu.getGuiItem(i).getItemStack());
            }
        }
        player.sendMessage(ChatColor.RED + "MafanaTradeNetwork: " + "TRADE_CANCELED");
    }

    public void clearTradeMenu() {
        for (int i : userSide) {
            if (tradeMenu.getGuiItem(i) != null) {
                tradeMenu.updateItem(i, new GuiItem(Material.AIR));
            }
        }
    }

    public void finishTrade() {
        if (playerAccept && player2TradeManager.isPlayerAccept()) {
            tradeFinished = true;
            for (ItemStack itemStack : player2TradeManager.getPlayerItems()) {
                if (itemStack == null) {
                    continue;
                }
                player.getInventory().addItem(itemStack);
            }
            clearTradeMenu();
            player.closeInventory();
            destroyTradeManager();
            player.sendMessage(ChatColor.DARK_GRAY + "MafanaTradeNetwork: Trade Finished: ");
            for (ItemStack itemStack : player2TradeManager.getPlayerItems()) {
                if (itemStack == null) {
                    continue;
                }
                player.sendMessage(ChatColor.DARK_GRAY + "+ " + itemStack.getItemMeta().getDisplayName() + ChatColor.DARK_GRAY + " x" + itemStack.getAmount());
            }
            for (ItemStack itemStack : playerItems) {
                if (itemStack == null) {
                    continue;
                }
                player.sendMessage(ChatColor.DARK_GRAY + "- " + itemStack.getItemMeta().getDisplayName() + ChatColor.DARK_GRAY + " x" + itemStack.getAmount());
            }
        }
    }

    public void updateOtherUsersGUI() {
        Gui otherUserGui = player2TradeManager.getTradeMenu();
        for (int i = 0; i < userSide.length; i++) {
            int userSideIndex = userSide[i];
            int otherUserSideIndex = otherUserSide[i];

            GuiItem currentItem = tradeMenu.getGuiItem(userSideIndex);

            if (currentItem != null) {
                otherUserGui.updateItem(otherUserSideIndex, currentItem);
            } else {
                otherUserGui.updateItem(otherUserSideIndex, new GuiItem(Material.AIR));
            }
        }
        updateOtherAcceptButton(otherUserGui);
    }

    public void destroyTradeManager() {
        MafanaTradeNetwork.getInstance().getTradeManagerList().remove(this);
    }

    public void setPlayer2TradeManager(TradeManager player2TradeManager) {
        this.player2TradeManager = player2TradeManager;
    }

    public void addPlayerItem(ItemStack itemStack) {
        playerItems.add(itemStack);
    }

    public boolean isPlayerAccept() {
        return playerAccept;
    }

    public Player getPlayer() {
        return player;
    }

    public Gui getTradeMenu() {
        return tradeMenu;
    }

    public void setTradeMenu(Gui tradeMenu) {
        this.tradeMenu = tradeMenu;
    }

    public List<ItemStack> getPlayerItems() {
        return playerItems;
    }

    public TradeManager getPlayer2TradeManager() {
        return player2TradeManager;
    }

    public void setPlayerItems(List<ItemStack> playerItems) {
        this.playerItems = playerItems;
    }

    public void setPlayerAccept(boolean playerAccept) {
        this.playerAccept = playerAccept;
    }

    private Component getOtherUserTradeStatus() {
        return player2TradeManager.isPlayerAccept() ? Component.text(ChatColor.GREEN + "Accepted Trade") : Component.text(ChatColor.RED + "Declined Trade");
    }

    private void toggleTradeAcceptance() {
        setPlayerAccept(!isPlayerAccept());
    }

    private void updateAcceptButton(Gui gui) {
        if (getTradeStatus().equals(Component.text(ChatColor.GREEN + "Accepted Trade"))) {
                gui.updateItem(usersAcceptButton, ItemBuilder.from(Material.GREEN_WOOL).name(getTradeStatus()).asGuiItem(e -> {
                    toggleTradeAcceptance();
                    updateAcceptButton(tradeMenu);
                    finishTrade();
                    player2TradeManager.finishTrade();
                    updateOtherUsersGUI();
                }));
        } else {
            gui.updateItem(usersAcceptButton, ItemBuilder.from(Material.RED_WOOL).name(getTradeStatus()).asGuiItem(e -> {
                toggleTradeAcceptance();
                updateAcceptButton(tradeMenu);
                finishTrade();
                player2TradeManager.finishTrade();
                updateOtherUsersGUI();
            }));
        }
    }

    private void updateOtherAcceptButton(Gui gui) {
        if (getTradeStatus().equals(Component.text(ChatColor.GREEN + "Accepted Trade"))) {
            gui.updateItem(otherUserAcceptButton, ItemBuilder.from(Material.GREEN_WOOL).name(getTradeStatus()).asGuiItem(e -> {
            }));
        } else {
            gui.updateItem(otherUserAcceptButton, ItemBuilder.from(Material.RED_WOOL).name(getTradeStatus()).asGuiItem(e -> {
            }));
        }
    }

    private Component getTradeStatus() {
        return isPlayerAccept() ? Component.text(ChatColor.GREEN + "Accepted Trade") : Component.text(ChatColor.RED + "Declined Trade");
    }
}
