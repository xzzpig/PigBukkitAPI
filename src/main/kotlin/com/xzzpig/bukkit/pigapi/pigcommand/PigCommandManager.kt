package com.xzzpig.bukkit.pigapi.pigcommand

import com.xzzpig.bukkit.pigapi.TMessage
import com.xzzpig.pigutils.core.flatToString
import com.xzzpig.pigutils.core.times
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*
import kotlin.collections.ArrayList

class PigCommandManager {

    internal val commandMap = mutableMapOf<String, PigCommand>()

    inline fun command(command: String, block: PigCommand.PigCommandBuilder.() -> Unit = {}) {
        command.split(" ").apply {
            addCommand(PigCommand.PigCommandBuilder(this.last()).apply(block).build(), *this.subList(0, this.size - 1).toTypedArray())
        }
    }

    fun addCommand(command: PigCommand, vararg parents: String) {
        if (parents.isEmpty()) {
            commandMap.getOrPut(command.name, { PigCommand(command.name) }).apply {
                describe = command.describe
                arguments_flag = command.arguments_flag
                arguments_necessary = command.arguments_necessary
                arguments_unnecessary = command.arguments_unnecessary
                executor = command.executor
                permissions.addAll(command.permissions)
                limits.addAll(command.limits)
                command.subCommandMap.forEach { k, v -> this.subCommandMap[k] = v }
            }
            return
        }
        commandMap.getOrPut(parents[0], { PigCommand(parents[0]) }).addSub(command, 1, -1, *parents)
    }

    @Throws(NoSuchCommandException::class, PigCommandArgumentParseException::class, PigCommandDeniedException::class)
    fun execute(context: PigCommandContext) {
        context.state = PigCommandContext.State.Matching
        val command = commandMap.getOrElse(context.rawArgs[0]) {
            throw NoSuchCommandException()
        }
        if (!execute(context, command, 1)) {
            context.commandIndex = 0
            throw NoSuchCommandException()
        }
    }

    private fun execute(context: PigCommandContext, command: PigCommand, index: Int): Boolean {
        context.commandIndex = index
        val argNum = context.rawArgs.size - index
        if (argNum > 0) {
            val arg = context.rawArgs[index]
            val sub = command.subCommandMap[arg]
            if (sub != null) {
                if (execute(context, sub, index + 1)) return true
            }
//            command.subCommandMap[arg]?.run {
//                if (execute(context, this, index + 1)) return true
//            }
        }
        if (argNum < command.arguments_necessary.size) return false
        for ((key, arg) in command.arguments_flag) {
            if (arg.necessary && key !in context.rawFlags) {
                return false
            }
        }

        context.command = command

        //解析参数
        val rawFlagMap = mutableMapOf<String, String>().apply { putAll(context.rawFlags) }
        context.state = PigCommandContext.State.FlagParsing
        for ((key, arg) in command.arguments_flag) {
            context.parasIndex++
            context.argument = arg
            val sValue = rawFlagMap[key]
            if (sValue == null) {
                arg.defaultValue(context)?.run {
                    context.argMap[key] = this
                }
                continue
            }
            context.argMap[key] = transfer(sValue, context)
        }
        context.state = PigCommandContext.State.ArgumentParsing
        context.argumentIndex = index
        context.parasIndex++
        for (arg in command.arguments_necessary) {
            context.parasIndex++
            context.argument = arg
            context.argument = arg
            context.argMap[arg.name] = transfer(context.rawArgs[context.argumentIndex], context)
            context.argumentIndex++
        }
        for (arg in command.arguments_unnecessary) {
            context.parasIndex++
            if (context.argumentIndex >= context.rawArgs.size) {
                arg.defaultValue(context)?.run {
                    context.argMap[arg.name] = this
                }
            } else
                context.argMap[arg.name] = transfer(context.rawArgs[context.argumentIndex], context)
            context.argumentIndex++
        }
        context.parasIndex = -1
        context.argument = null
        context.argumentIndex = -1
        //开始检查限制
        context.state = PigCommandContext.State.LimitChecking
        command.limits.forEach {
            (it(context) ?: "").apply {
                if (this.isBlank()) {
                    throw PigCommandDeniedException(this, context)
                }
            }
        }
        //开始执行
        context.state = PigCommandContext.State.Executing
        command.executor.invoke(context)
        context.state = PigCommandContext.State.Noting
        return true
    }

    private fun transfer(string: String, context: PigCommandContext): Any {
        for (limit in context.argument!!.limits) {
            limit(string)?.takeIf { it.isNotBlank() }?.run {
                throw PigCommandArgumentParseException(string, context, this)
            }
        }
        return when (context.argument!!.type) {
            PigCommandArgument.Type.Present -> true
            PigCommandArgument.Type.String -> string
            PigCommandArgument.Type.RemainString -> context.rawArgs.run { subList(context.argumentIndex, size) }.flatToString(split = " ")
            PigCommandArgument.Type.Int -> string.toIntOrNull()
                    ?: throw PigCommandArgumentParseException(string, context)
            PigCommandArgument.Type.Double -> string.toDoubleOrNull()
                    ?: throw PigCommandArgumentParseException(string, context)
            PigCommandArgument.Type.Player -> Bukkit.getPlayer(string)
                    ?: throw PigCommandArgumentParseException(string, context)
            PigCommandArgument.Type.World -> Bukkit.getWorld(string)
                    ?: throw PigCommandArgumentParseException(string, context)
        }
    }

    fun getHelpMessage(message: TMessage = TMessage(), offset: Int, vararg command: String): TMessage {
        var cmd = commandMap[command[0]]!!
        val sb = StringBuffer("/").append(command[0])
        for (i in 1..offset) {
            cmd = cmd.subCommands[command[i]]!!
            sb.append(' ').append(command[i])
        }
        return cmd.addToTMessage(message.then(sb), false)
    }

    fun getSubHelpMessages(offset: Int, vararg command: String): List<TMessage> {
        var cmd = commandMap[command[0]]!!
        val sb = StringBuffer("/").append(command[0])
        for (i in 1..offset) {
            cmd = cmd.subCommands[command[i]]!!
            sb.append(' ').append(command[i])
        }
        val list = ArrayList<TMessage>(cmd.subCommandMap.size)
        val cmdList = LinkedList<PigCommand>()
        if (!cmd.isUsingDefaultExecutor) {
            list.add(cmd.addToTMessage(TMessage(sb.toString()), false, sb.toString(), false))
            cmdList.add(cmd)
        }
        for ((name, sub) in cmd.subCommandMap) {
            list.add(sub.addToTMessage(TMessage("$sb $name"), false, "$sb $name", false))
            cmdList.add(sub)
        }
        val maxLen = list
                .stream()
                .mapToInt { it.toNoColorString().length }
                .max().orElseGet { 0 }
        for (msg in list) {
            msg
                    .then(" " * (maxLen - msg.toNoColorString().length))
                    .then(cmdList.pollFirst().describe).color(ChatColor.AQUA)
        }
        return list
    }

    /**
     * @return 有效子命令的下标!!
     */
    fun getValidCommandIndex(cmd: Array<String>): Int {
        var offset = -1
        var map = commandMap
        for (sub in cmd) {
            map = (map[sub] ?: break).subCommandMap
            offset++
        }
        return offset
    }

    /**
     * @return 有效子命令的下标!!
     */
    fun getValidCommand(cmd: Array<String>): Pair<Int, PigCommand?> {
        var offset = -1
        var map = commandMap
        var command: PigCommand? = null
        for (sub in cmd) {
            command = map[sub] ?: break
            map = command.subCommandMap
            offset++
        }
        return offset to command
    }

    fun handleTabCompile(context: PigCommandContext, prefix: String = context.rawCommand.label): List<String> {
        val rawArg = context.rawArgs.toTypedArray()
        val (index, command) = getValidCommand(rawArg)
        context.sendMessage("你可以参考以下提示", prefix)
        getSubHelpMessages(index, *rawArg).forEach {
            it.send(context.sender)
        }
        return if (command?.hasArguments() != false) {
            emptyList()
        } else {
            command.subCommandMap.keys.toList()
        }

//        val argIterator = context.rawArgs.iterator()
//        if (!argIterator.hasNext()) return emptyList()
//        var command = commandMap[argIterator.next()] ?: return emptyList()
//        var cmdIndex = 0
//        while (true) {
//            if (!argIterator.hasNext()) break
//            val sub = command.subCommandMap[argIterator.next()] ?: break
//            command = sub
//            cmdIndex++
//        }
//        val restArg = context.rawArgs.size - cmdIndex - 1
//        /**
//         * restArg=1&last is empty
//         * restArg=1&last not empty
//         * restArg>1
//         */
//        when (restArg) {
//            1 -> {
//                val currentArg = (context.rawArgs[cmdIndex + 1])
//                println(currentArg)
//                if (currentArg.isEmpty()) {
//                    context.sendMessage("你可以参考以下提示", prefix)
//                    getSubHelpMessages(getValidCommandIndex(context.rawArgs.toTypedArray()), *context.rawArgs.toTypedArray()).forEach {
//                        it.send(context.sender)
//                    }
//                    return if (command.hasArguments()) {
//                        emptyList()
//                    } else {
//                        command.subCommandMap.keys.toList().apply { println(this) }
//                    }
//                } else {
//
//                }
//            }
//        }
//        return listOf()
    }

    fun handleCommandException(prefix: String, exception: Exception, context: PigCommandContext) {
        val msg = TMessage("[$prefix]").style(ChatColor.WHITE)
        val list = LinkedList<TMessage>()
        list.add(msg)
        when (exception) {
            is NoSuchCommandException -> {
                if (context.commandIndex == -1) {
                    msg.then("未找到此命令:${context.rawArgs[0]},请尝试").color(ChatColor.RED).then("/help").style(ChatColor.WHITE).tooltip("/help").suggest("/help")
                } else {
                    msg.then("无法解析此").color(ChatColor.RED).then("命令").color(ChatColor.YELLOW).style(ChatColor.UNDERLINE).tooltip(context.toRawString().toString()).then(",请尝试以下帮助:").color(ChatColor.RED)
                    val argarr = context.rawArgs.toTypedArray()
                    val offset = getValidCommandIndex(argarr)
                    list.addAll(getSubHelpMessages(offset, *argarr))
                }
            }
            is PigCommandArgumentParseException -> {
                msg.then("无法将").color(ChatColor.RED).then(exception.string).color(ChatColor.YELLOW).then("转换为目标类型").color(ChatColor.RED).then(context.argument?.type?.name
                        ?: "").then(" 原因是:${exception.reason},").then("请检查参数").style(ChatColor.WHITE).then(context.argument?.name
                        ?: "").style(ChatColor.GREEN, ChatColor.UNDERLINE).tooltip(context.argument?.describe ?: "")
                val argarr = context.rawArgs.toTypedArray()
                val offset = getValidCommandIndex(argarr)
                list.add(getHelpMessage(TMessage(), offset, *argarr))
            }
            is PigCommandDeniedException -> {
                context.sendMessage("&4${exception.reason}", prefix)
            }
            else -> {
                msg.then("命令执行出错,请联系op后台查看异常信息").color(ChatColor.RED)
                exception.printStackTrace()
            }

        }
        for (m in list) {
            m.send(context.sender)
        }
    }
}