package io.pfaumc.example

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class RaftPlugin : JavaPlugin(), Listener {
    override fun onEnable() {
        logger.info("BarrelOpenPlugin успешно загружен!")
        server.pluginManager.registerEvents(this, this)
        registerCommand("raft") {
            exec {
                val source = source.sender
                if (source is Player) {
                    val world = server.getWorld("raft_world_${source.uniqueId}")
                    if (world != null) {
                        world.spawnLocation = Location(world, 0.5, 1.0, 0.5)
                        source.teleport(world.spawnLocation)

                        source.gameMode = GameMode.CREATIVE

                        source.sendMessage("ВелCum ")
                    } else {
                        source.sendMessage("Иди нахуй")
                    }
                }
                1
            }
            literal("create") {
                exec {
                    val source = source.sender
                    if (source is Player) {
                        createRaftWorld(source)
                    }

                    1
                }
            }
        }

        registerCommand("menu") {
            exec {
                val sender = source.sender

                if (sender is Player) {
                    sender.openInventory(RaftMenu(this@RaftPlugin).inventory)

                }

                1
            }
        }


        registerCommand("ec") {
            exec {
                val sender = source.sender

                if (sender is Player) {
                    sender.sendMessage("kdkdkdkmasdcfsaf")
                    sender.openInventory(sender.enderChest)


                }

                1
            }
        }
    }

    private fun createRaftWorld(player: Player) {

        val uniqueWorldName = "raft_world_${player.uniqueId}"


        if (server.getWorld(uniqueWorldName) != null) {
            player.sendMessage("Уже есть мир")
            return
        }

        val world: World? = server.createWorld(
            WorldCreator(uniqueWorldName)
                .generator(WaterWorldGen())
        )

        if (world != null) {
            world.spawnLocation = Location(world, 0.5, 1.0, 0.5)
            player.teleport(world.spawnLocation)

            player.gameMode = GameMode.CREATIVE

            player.sendMessage("ВелCum ")
        } else {
            player.sendMessage("Иди нахуй")
        }
    }

    @EventHandler
    fun onPlayer(event: PlayerMoveEvent) {
        val player = event.player


        if (player.location.block.type == Material.WATER && player.health > 0.0) {
           if (player.gameMode == GameMode.CREATIVE) {
                player.gameMode = GameMode.SURVIVAL
            }
            player.health = 0.0
            player.sendMessage("LOSHARA")
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        if (event.clickedBlock?.type == Material.BARREL && event.action == Action.RIGHT_CLICK_BLOCK) {
            player.sendMessage("писяпопа")
        }
    }


}
