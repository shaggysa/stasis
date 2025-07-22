package xyz.shaggysa.stillSky

import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

fun serverEmpty(): Boolean {
    return (Bukkit.getOnlinePlayers().isEmpty())
}

fun freezeDaylight() {
    Bukkit.getWorlds()[0].setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
    StillSky.instance.logger.info("The daylight cycle has paused.")
}

fun unfreezeDaylight() {
    Bukkit.getWorlds()[0].setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
    StillSky.instance.logger.info("The daylight cycle has resumed.")
}

class PlayerListener : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (serverEmpty() || Bukkit.getOnlinePlayers().size == 1) { //playercount updates after event listener
            freezeDaylight()
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (Bukkit.getOnlinePlayers().size == 1) {
            unfreezeDaylight()
        }
    }
}

class StillSky : JavaPlugin() {
    companion object {
        val instance: StillSky by lazy {
            Bukkit.getPluginManager().getPlugin("StillSky") as StillSky
        }
    }

    override fun onEnable() {
        if (serverEmpty()) {
            freezeDaylight()
        }
        server.pluginManager.registerEvents(PlayerListener(), this)
    }

    override fun onDisable() {
        unfreezeDaylight()
    }
}



