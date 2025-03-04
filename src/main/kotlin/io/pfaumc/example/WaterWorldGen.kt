package io.pfaumc.example

import org.bukkit.Material
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*

class WaterWorldGen : ChunkGenerator() {
    override fun generateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {

        repeat(16) { x ->
            repeat(16) { z ->
                repeat(10) { y ->
                    chunkData.setBlock(x, -y, z, Material.WATER)
                }
            }
        }





        if (0 == chunkX && 0 == chunkZ) {

            repeat(6) { x ->
                chunkData.setBlock(x, 0, 0, Material.DARK_OAK_WOOD)
            }
            repeat(6) { x ->
                chunkData.setBlock(x, 0, 4, Material.DARK_OAK_WOOD)
            }
            repeat(6) { x ->
                repeat(3) { z ->
                    chunkData.setBlock(x, 0, z + 1, Material.STRIPPED_OAK_WOOD)
                }
            }
            repeat(3){y->
                chunkData.setBlock(1,y+1,2, Material.SPRUCE_FENCE)
            }
            repeat(2){y->
                chunkData.setBlock(1,y+4,2, Material.SPRUCE_FENCE.createBlockData("[south=true]"))
            }
            chunkData.setBlock(2,1,1, Material.BARREL.createBlockData("[facing=up]"))
            repeat(3){z->
                chunkData.setBlock(1,4,z+3, Material.RED_WOOL)
            }
            repeat(2){z->
                chunkData.setBlock(1,5,z+3, Material.RED_WOOL)
            }

            println("Генерация чанка $chunkX, $chunkZ")
        }



    }
}
