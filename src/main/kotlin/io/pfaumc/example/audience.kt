package io.pfaumc.example

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import org.bukkit.SoundCategory

fun Audience.playSound(
    sound: org.bukkit.Sound,
    category: SoundCategory = SoundCategory.MASTER,
    volume: Float = 1_000_000f,
    pitch: Float = 1f
) = playSound(Sound.sound(sound, category.soundSource(), volume, pitch))

fun Audience.playSound(
    sound: org.bukkit.Sound,
    volume: Float = 1_000_000f,
    pitch: Float = 1f
) = playSound(sound, SoundCategory.MASTER, volume, pitch)

fun Iterable<Audience>?.asAudience(): Audience = Audience.audience(this ?: emptyList())
