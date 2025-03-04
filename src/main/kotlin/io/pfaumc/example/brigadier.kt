@file:Suppress("UnstableApiUsage")

package io.pfaumc.example

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import io.papermc.paper.command.brigadier.argument.VanillaArgumentProviderImpl
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import net.minecraft.commands.SharedSuggestionProvider
import org.bukkit.plugin.Plugin
import kotlin.time.Duration

fun Plugin.registerCommand(
    literal: String,
    builder: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralCommandNode<CommandSourceStack> {
    val command = Commands.literal(literal).apply(builder).build()
    lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
        event.registrar().register(command)
    }
    return command
}

fun <T : ArgumentBuilder<CommandSourceStack, T>, A> ArgumentBuilder<CommandSourceStack, T>.argument(
    name: String,
    argumentType: ArgumentType<A>,
    builder: RequiredArgumentBuilder<CommandSourceStack, A>.() -> Unit
) = then(Commands.argument(name, argumentType).apply(builder))

fun <S, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.exec(ctx: CommandContext<S>.() -> Int) {
    executes {
        ctx(it)
    }
}

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.literal(
    name: String,
    builder: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
) = then(Commands.literal(name).apply(builder))


@OptIn(DelicateCoroutinesApi::class)
fun <S> RequiredArgumentBuilder<S, *>.suggest(candidates: suspend CommandContext<S>.(SuggestionsBuilder) -> Iterable<String>) {
    suggests { ctx, suggestionsBuilder ->
        GlobalScope.async {
            SharedSuggestionProvider.suggest(candidates(ctx, suggestionsBuilder), suggestionsBuilder).await()
        }.asCompletableFuture()
    }
}

fun RequiredArgumentBuilder<*, *>.suggest(candidates: Iterable<String>) {
    suggests { _, suggestionsBuilder ->
        SharedSuggestionProvider.suggest(candidates, suggestionsBuilder)
    }
}

fun RequiredArgumentBuilder<*, *>.suggest(vararg candidates: String) {
    suggests { _, suggestionsBuilder ->
        SharedSuggestionProvider.suggest(candidates, suggestionsBuilder)
    }
}

/*
    BOOL
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.boolArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Boolean>.() -> Unit
) = argument(name, BoolArgumentType.bool(), builder)

fun <S> CommandContext<S>.getBool(name: String) = BoolArgumentType.getBool(this, name)

/*
    DOUBLE
 */

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.doubleArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Double>.() -> Unit
) = argument(name, DoubleArgumentType.doubleArg(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.doubleArg(
    name: String,
    min: Double,
    builder: RequiredArgumentBuilder<CommandSourceStack, Double>.() -> Unit
) = argument(name, DoubleArgumentType.doubleArg(min), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.doubleArg(
    name: String,
    min: Double,
    max: Double,
    builder: RequiredArgumentBuilder<CommandSourceStack, Double>.() -> Unit
) = argument(name, DoubleArgumentType.doubleArg(min, max), builder)

fun <S> CommandContext<S>.getDouble(name: String) = DoubleArgumentType.getDouble(this, name)

/*
    FLOAT
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.floatArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Float>.() -> Unit
) = argument(name, FloatArgumentType.floatArg(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.floatArg(
    name: String,
    min: Float,
    builder: RequiredArgumentBuilder<CommandSourceStack, Float>.() -> Unit
) = argument(name, FloatArgumentType.floatArg(min), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.floatArg(
    name: String,
    min: Float,
    max: Float,
    builder: RequiredArgumentBuilder<CommandSourceStack, Float>.() -> Unit
) = argument(name, FloatArgumentType.floatArg(min, max), builder)

fun <S> CommandContext<S>.getFloat(name: String) = FloatArgumentType.getFloat(this, name)

/*
    INT
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.intArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Int>.() -> Unit
) = argument(name, IntegerArgumentType.integer(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.intArg(
    name: String,
    min: Int,
    builder: RequiredArgumentBuilder<CommandSourceStack, Int>.() -> Unit
) = argument(name, IntegerArgumentType.integer(min), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.intArg(
    name: String,
    min: Int,
    max: Int,
    builder: RequiredArgumentBuilder<CommandSourceStack, Int>.() -> Unit
) = argument(name, IntegerArgumentType.integer(min, max), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.intArg(
    name: String,
    range: IntRange,
    builder: RequiredArgumentBuilder<CommandSourceStack, Int>.() -> Unit
) = argument(name, IntegerArgumentType.integer(range.first, range.last), builder)

fun <S> CommandContext<S>.getInt(name: String) = IntegerArgumentType.getInteger(this, name)

/*
    LONG
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.longArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Long>.() -> Unit
) = argument(name, LongArgumentType.longArg(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.longArg(
    name: String,
    min: Long,
    builder: RequiredArgumentBuilder<CommandSourceStack, Long>.() -> Unit
) = argument(name, LongArgumentType.longArg(min), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.longArg(
    name: String,
    min: Long,
    max: Long,
    builder: RequiredArgumentBuilder<CommandSourceStack, Long>.() -> Unit
) = argument(name, LongArgumentType.longArg(min, max), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.longArg(
    name: String,
    range: LongRange,
    builder: RequiredArgumentBuilder<CommandSourceStack, Long>.() -> Unit
) = argument(name, LongArgumentType.longArg(range.first, range.last), builder)

fun <S> CommandContext<S>.getLong(name: String) = LongArgumentType.getLong(this, name)

/*
    STRING
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.wordArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, String>.() -> Unit
) = argument(name, StringArgumentType.word(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.stringArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, String>.() -> Unit
) = argument(name, StringArgumentType.string(), builder)

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.greedyStringArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, String>.() -> Unit
) = argument(name, StringArgumentType.greedyString(), builder)

fun <S> CommandContext<S>.getString(name: String) = StringArgumentType.getString(this, name)

/*
    DURATION
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.durationArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, Duration>.() -> Unit
) = argument(name, DurationArgumentType.durationArg()) {
    suggest(DurationArgumentType.EXAMPLES)
    builder(this)
}

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.durationArg(
    name: String,
    min: Duration,
    builder: RequiredArgumentBuilder<CommandSourceStack, Duration>.() -> Unit
) = argument(name, DurationArgumentType.durationArg(min)) {
    suggest(DurationArgumentType.EXAMPLES)
    builder(this)
}

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.durationArg(
    name: String,
    min: Duration,
    max: Duration,
    builder: RequiredArgumentBuilder<CommandSourceStack, Duration>.() -> Unit
) = argument(name, DurationArgumentType.durationArg(min, max)) {
    suggest(DurationArgumentType.EXAMPLES)
    builder(this)
}

fun <S> CommandContext<S>.getDuration(name: String) = DurationArgumentType.getDuration(this, name)

/*
    PLAYER
 */
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.playerArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver>.() -> Unit
) = argument(name, VanillaArgumentProviderImpl().player(), builder)

fun CommandContext<CommandSourceStack>.getPlayer(name: String) =
    getArgument(name, PlayerSelectorArgumentResolver::class.java).resolve(source).firstOrNull()

fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.playersArg(
    name: String,
    builder: RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver>.() -> Unit
) = argument(name, VanillaArgumentProviderImpl().player(), builder)

fun CommandContext<CommandSourceStack>.getPlayers(name: String) =
    getArgument(name, PlayerSelectorArgumentResolver::class.java).resolve(source)


class DurationArgumentType private constructor(
    val min: Duration,
    val max: Duration
) : CustomArgumentType<Duration, String> {
    override fun getNativeType(): ArgumentType<String> = StringArgumentType.string()

    override fun getExamples(): Collection<String> = EXAMPLES

    override fun parse(reader: StringReader): Duration {
        val start = reader.cursor
        val string = nativeType.parse(reader)
        val duration = Duration.parseOrNull(string) ?: throw READER_INVALID_DURATION.createWithContext(reader, string)
        if (duration < min) {
            reader.cursor = start
            throw DURATION_TOO_SMALL.createWithContext(reader, duration, min)
        } else if (duration > max) {
            reader.cursor = start
            throw DURATION_TOO_BIG.createWithContext(reader, duration, max)
        } else {
            return duration
        }
    }

    override fun toString(): String = when {
        min == Duration.ZERO && max == Duration.INFINITE -> "durationArg()"
        max == Duration.INFINITE -> "durationArg($min)"
        else -> "durationArg($min, $max)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DurationArgumentType) return false

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        return result
    }

    companion object {
        private val READER_INVALID_DURATION = DynamicCommandExceptionType({
            LiteralMessage("Invalid Duration '$it'")
        })
        private val DURATION_TOO_SMALL = Dynamic2CommandExceptionType({ found, min ->
            LiteralMessage("Duration must not be less than $min, found $found")
        })
        private val DURATION_TOO_BIG = Dynamic2CommandExceptionType({ found, max ->
            LiteralMessage("Duration must not be more than $max, found $found")
        })
        val EXAMPLES = listOf("5s", "10m", "15d")

        @JvmStatic
        fun durationArg(): DurationArgumentType = DurationArgumentType(Duration.ZERO, Duration.INFINITE)

        @JvmStatic
        fun durationArg(min: Duration) = DurationArgumentType(min, Duration.INFINITE)

        @JvmStatic
        fun durationArg(min: Duration, max: Duration) = DurationArgumentType(min, max)

        @JvmStatic
        fun getDuration(context: CommandContext<*>, name: String): Duration {
            return context.getArgument(name, Duration::class.java)
        }
    }
}
