package me.justeli.coins;

import me.justeli.coins.Coins;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import java.util.logging.Level;

public class CustomFlags {
    // Define the flag with State.ALLOW and State.DENY instead of true and false
    public static final StateFlag COINS_DROP_FLAG = new StateFlag("coins-drop", State.ALLOW);  // Default state to DENY

    public static void register() {
        try {
            // Register flags with WorldGuard's FlagRegistry
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            registry.register(COINS_DROP_FLAG);
            
            // Log successful registration
            Coins.getInstance().getLogger().log(Level.INFO, "Custom WorldGuard flags registered successfully.");
        } catch (Exception e) {
            // Handle any error that might occur
            Coins.getInstance().getLogger().log(Level.WARNING, "Failed to register custom WorldGuard flags", e);
        }
    }
}
