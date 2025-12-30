package me.justeli.coins.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import java.util.logging.Level;

public class CustomFlags {
    // Define all the flags you need
    public static final StateFlag COINS_DROP_FLAG = new StateFlag("coins-drop", true);

    // You can add more flags as needed, like:
    // public static final StateFlag MY_CUSTOM_FLAG = new StateFlag("my-custom-flag", false);

    public static void register() {
        try {
            // Register flags with WorldGuard's FlagRegistry
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            registry.register(COINS_DROP_FLAG);
            
            // Register any other flags you may have
            // registry.register(MY_CUSTOM_FLAG);

            // Log successful registration
            WorldGuard.getInstance().getLogger().log(Level.INFO, "Custom WorldGuard flags registered successfully.");
        } catch (Exception e) {
            // Handle any error that might occur
            WorldGuard.getInstance().getLogger().log(Level.WARNING, "Failed to register custom WorldGuard flags", e);
        }
    }
}
