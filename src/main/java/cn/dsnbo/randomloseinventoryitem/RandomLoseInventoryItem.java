package cn.dsnbo.randomloseinventoryitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * @author DongShaoNB
 */
public final class RandomLoseInventoryItem extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack[] playerInventoryItems = player.getInventory().getContents();
        if (playerInventoryItems.length < getConfig().getInt("lose-number")) {
            if (!(player.getLocation().getY() <= 0)) {
                player.getLocation().getBlock().setType(Material.CHEST);
                Chest chest = (Chest) player.getLocation().getBlock().getState();
                chest.getInventory().setContents(playerInventoryItems);
            }
            player.getInventory().clear();
        } else {
            int loseNumber = getConfig().getInt("lose-number");
            player.getLocation().getBlock().setType(Material.CHEST);
            Random random = new Random();
            playerInventoryItems = Arrays.stream(playerInventoryItems)
                    .filter(Objects::nonNull)
                    .toArray(ItemStack[]::new);
            while (loseNumber > 0) {
                int loseIndex = random.nextInt(playerInventoryItems.length);
                getLogger().info(Arrays.toString(playerInventoryItems));
                getLogger().info(String.valueOf(loseIndex));
                ItemStack itemStack = playerInventoryItems[loseIndex];
                player.getInventory().removeItem(itemStack);
                if (!(player.getLocation().getY() <= 0)) {
                    Chest chest = (Chest) player.getLocation().getBlock().getState();
                    chest.getInventory().addItem(itemStack);
                }
                itemStack.setAmount(0);
                loseNumber--;
            }
        }
    }

}
