package me.tahacheji.mafana.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerShiftRightClickEvent implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        // Check if the player is sneaking (shift) and right-clicking another player
        if (player.isSneaking() && event.getRightClicked() instanceof Player) {
            Player targetPlayer = (Player) event.getRightClicked();

            // Perform the /mt trade {playerName} command
            String tradeCommand = "/mt trade " + targetPlayer.getName();
            Bukkit.dispatchCommand(player, tradeCommand);
        }
    }

}
