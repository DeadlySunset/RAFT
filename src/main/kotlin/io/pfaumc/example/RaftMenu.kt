package io.pfaumc.example

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class RaftMenu(
    val plugin: RaftPlugin
) : InventoryHolder, Listener {
    private val inventory = plugin.server.createInventory(this, 9 *1)
    override fun getInventory(): Inventory = inventory

    init {



        val SettingRaft = ItemStack(Material.SPRUCE_TRAPDOOR).apply {
            editMeta { meta ->
                meta.displayName(Component.text("Настройка плота"))
            }
        }
        inventory.setItem(4, SettingRaft)

        // Регистрируем наше меню как Listener (т.к. RaftMenu: InventoryHolder, Listener),
        // при создании нового RaftMenu
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    // Дерегистрируем листенер чтобы у нас не скапливалось дохуя одинаковых листенеров
    // и не было утечки памяти.
    fun onClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        // Получаем InventoryHolder у закрытого инвентаря,
        // сравниваем с тем что этот холдер это текущее меню,``
        // Т.к. RaftMenu : InventoryHolder, Listener
        if (holder == this) {
            HandlerList.unregisterAll(this)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        if (holder != this) {
            return // проверяем InventoryHolder у кликнутого инвентаря, если это не наше меню, игнорируем клик
        }
        // Тут уже код будет только для нашего инвентаря меню

        event.isCancelled = true // ОТМЕНЯЕМ (cancel) событие, то есть у нас клика не будет в итоге. Т.е. предмет не возьмется

        val item = event.currentItem // получаем предмет по которому кликнули
        if (item != null && item.hasItemMeta()) { // Проверяем, что предмет не пустой и имеет метаданные
            val displayName = item.itemMeta.displayName()

            when (displayName) {
                Component.text("Настройка плота") -> {
                    // Открываем новое меню для настроек плота

                }
                else -> {
                    // Можно добавить обработку других предметов
                }
            }
        }
    }
}
