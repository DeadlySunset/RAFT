package io.pfaumc.example

import org.bukkit.event.player.PlayerJoinEvent

class RaftSettingsMenu {
    fun Player (event: PlayerJoinEvent){
        val player = event.player

        player.sendMessage("Пошел нахуй ")
    }
}
