package com.xzzpig.bukkit.pigapi.pigcommand

import com.xzzpig.bukkit.pigapi.TMessage
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PigCommandContext(val sender: CommandSender, val rawCommand: Command, internal val label: String, internal val args: Array<String>) {

    enum class State {
        /**
         * 不在执行
         */
        Noting,
        /**
         * 正在匹配命令
         */
        Matching,
        /**
         * 正在解析flag
         */
        FlagParsing,
        /**
         * 正在解析argument
         */
        ArgumentParsing,
        /**
         * 正在检查限制
         * @see PigCommand.PigCommandBuilder.limit
         */
        LimitChecking,
        /**
         * 正在执行
         */
        Executing
    }

    internal val rawFlags = mutableMapOf<String, String>()
    internal var rawArgs: List<String>

    //    internal val flagMap = mutableMapOf<String, Any>()
    internal val argMap = mutableMapOf<String, Any>()

    /**
     * 正在解析的命令
     */
    var command: PigCommand? = null
        internal set(value) {
            field = value
        }

    /**
     * 命令执行状态
     * @see State
     */
    var state: State = State.Noting
        internal set(value) {
            field = value
        }

    /**
     * 正在解析的flag/arg的下标
     */
    var parasIndex: Int = -1
        internal set(value) {
            field = value
        }

    /**
     * 正在解析的参数的下标
     */
    var argumentIndex: Int = -1
        internal set(value) {
            field = value
        }

    /**
     * 正在解析/执行的命令的参数下标
     */
    var commandIndex: Int = -1
        internal set(value) {
            field = value
        }

    /**
     * 正在解析的参数
     */
    var argument: PigCommandArgument? = null
        internal set(value) {
            field = value
        }

    init {
        val sb = StringBuffer()
        sb.append(rawCommand.label)
        for (arg in args) {
            if (arg.startsWith('-')) {
                arg.substring(1).split(Regex(":"), 2).run {
                    if (this.size == 1) rawFlags[this[0]] = ""
                    else rawFlags[this[0]] = this[1]
                }
            } else {
                sb.append(' ').append(arg)
            }
        }
        rawArgs = sb.split(Regex(" "))
    }

    fun toRawString(): StringBuffer = StringBuffer().apply {
        append(label)
        for (arg in args) {
            append(' ').append(arg)
        }
    }

    fun sendMessage(msg: String, prefix: String = "") {
        if (prefix.isNotBlank())
            msg.replace('&', ChatColor.COLOR_CHAR).split("\n".toRegex()).forEach {
                sender.sendMessage("[$prefix]$it")
            }
        else {
            msg.replace('&', ChatColor.COLOR_CHAR).split("\n".toRegex()).forEach {
                sender.sendMessage(it)
            }
        }
    }

    fun getString(key: String): String = argMap[key]!! as String
    fun getInt(key: String): Int = argMap[key]!! as Int
    fun getDouble(key: String): Double = argMap[key]!! as Double
    fun getPlayer(key: String): Player = argMap[key]!! as Player
    fun getWorld(key: String): World = argMap[key]!! as World

    fun hasFlag(key: String): Boolean = key in argMap

    operator fun contains(key: String): Boolean = key in argMap

    operator fun get(key: String): Any? = argMap[key]

    fun sendMessage(vararg message: TMessage) {
        message.forEach {
            it.send(sender)
        }
    }
}