package com.xzzpig.bukkit.pigapi.pigcommand

import com.xzzpig.bukkit.pigapi.TMessage
import org.bukkit.ChatColor
import org.bukkit.entity.Player

data class PigCommand(val name: String, internal var describe: String = "") {

    object DefaultExecutor : (PigCommandContext) -> Unit {
        override fun invoke(p1: PigCommandContext) {
            throw NoSuchCommandException()
        }
    }

    internal var arguments_necessary = mutableListOf<PigCommandArgument>()
    internal var arguments_unnecessary = mutableListOf<PigCommandArgument>()
    internal var arguments_flag = mutableMapOf<String, PigCommandArgument>()
    internal var executor: ((PigCommandContext) -> Unit) = DefaultExecutor
    internal val permissions = mutableListOf<String>()
    internal val limits = mutableListOf<(PigCommandContext) -> String?>()

    internal val subCommandMap = mutableMapOf<String, PigCommand>()

    internal fun addSub(command: PigCommand, index: Int = 0, end: Int = -1, vararg parents: String) {
        val e = if (end == -1) parents.size else end
        if (e == index) {
            subCommandMap.getOrPut(command.name, { PigCommand(command.name) }).apply {
                describe = command.describe
                arguments_flag = command.arguments_flag
                arguments_necessary = command.arguments_necessary
                arguments_unnecessary = command.arguments_unnecessary
                executor = command.executor
                permissions.addAll(command.permissions)
                command.subCommandMap.forEach { k, v -> this.subCommandMap[k] = v }
            }
            return
        }
        subCommandMap.getOrPut(parents[index], { PigCommand(parents[index]) }).addSub(command, index + 1, e, *parents)
    }

    fun addToTMessage(message: TMessage = TMessage(), addName: Boolean = true, suggest: String = "", addDescribe: Boolean = true): TMessage {
        if (addName) message.then(name)
        message.tooltip(StringBuffer(describe).apply {
            if (permissions.isNotEmpty()) {
                append('\n').append("需要权限:")
                for (permission in permissions) {
                    append('\n').append(permission)
                }
            }
        }.toString())
        if (suggest.isNotBlank()) message.suggest(suggest)
        message.then(" ")
        for ((_, arg) in arguments_flag) {
            val buffer = StringBuffer()
            if (!arg.necessary) buffer.append('[')
            buffer.append('-').append(arg.name)
            arg.type.takeUnless { it == PigCommandArgument.Type.Present }?.run { buffer.append('=').append(this.name) }
            if (!arg.necessary) buffer.append(']')
            message.then(buffer.toString()).tooltip(arg.describe).color(ChatColor.GRAY)
            message.then(" ")
        }
        for (arg in arguments_necessary) {
            StringBuffer().apply {
                append('<').append(arg.name).append('>').append(' ')
            }.run { message.then(this.toString()) }.tooltip(arg.describe).style(ChatColor.WHITE)
        }
        for (arg in arguments_unnecessary) {
            StringBuffer().apply {
                append('[').append(arg.name).append(']').append(' ')
            }.run { message.then(this.toString()) }.tooltip(arg.describe).style(ChatColor.WHITE)
        }
        if (addDescribe)
            message.then(" -$describe").color(ChatColor.AQUA)
        return message
    }

    fun toStringBuffer(vararg parents: String): StringBuffer = StringBuffer().apply {
        for (parent in parents) {
            append(parent).append(' ')
        }
        append(this@PigCommand.toString())
    }

    override fun toString(): String = StringBuffer(name).apply {
        for ((_, arg) in arguments_flag) {
            append(' ')
            if (!arg.necessary) append('[')
            append('-').append(arg.name)
            arg.type.takeUnless { it == PigCommandArgument.Type.Present }?.run { append('=').append(this.name) }
            if (!arg.necessary) append(']')
        }
        for (arg in arguments_necessary) {
            append(' ').append('<').append(arg.name).append('>')
        }
        for (arg in arguments_unnecessary) {
            append(' ').append('[').append(arg.name).append(']')
        }
    }.toString()

    fun hasArguments(includeFlag: Boolean = true): Boolean {
        if (arguments_necessary.isNotEmpty()) return true
        if (arguments_unnecessary.isNotEmpty()) return true
        if (includeFlag && arguments_flag.isNotEmpty()) return true
        return false
    }

    val isUsingDefaultExecutor: Boolean get() = executor == DefaultExecutor

    class PigCommandBuilder(val name: String, var describe: String = "") {
        private val arguments_necessary = mutableListOf<PigCommandArgument>()
        private val arguments_unnecessary = mutableListOf<PigCommandArgument>()
        private val arguments_flag = mutableMapOf<String, PigCommandArgument>()

        private val permissions = mutableListOf<String>()
        private val limits = mutableListOf<(PigCommandContext) -> String?>()

        private var executor: ((PigCommandContext) -> Unit) = DefaultExecutor

        private var necessary = true

        /**
         * 添加限制，如果block执行返回null或空字符串则通过，否则禁止执行
         * @param block 返回值将将作为异常原因抛出
         * @see PigCommandManager.execute
         * @see PigCommandDeniedException
         */
        fun limit(block: PigCommandContext.() -> String?) {
            limits.add(block)
        }

        fun argument(name: String, block: PigCommandArgument.PigCommandArgumentBuilder.() -> Unit = {}) {
            val arg = PigCommandArgument.PigCommandArgumentBuilder(name).apply(block).apply {
                flag = false
                necessary = false
            }.build()
            (if (necessary) arguments_necessary else arguments_unnecessary).add(arg)
        }

        fun flag(name: String, describe: String = "", type: PigCommandArgument.Type = PigCommandArgument.Type.Present, necessary: Boolean = false, defaultValue: ((PigCommandContext) -> Any?) = {}) {
//            val arg = PigCommandArgument(name, describe, type)
            val arg = PigCommandArgument.PigCommandArgumentBuilder(name, type, describe).apply {
                this.necessary = necessary
                this.defaultValue = defaultValue
                if (type == PigCommandArgument.Type.Present) {
                    this.necessary = false
                }
            }.build()
            arguments_flag[name] = arg

        }

        inline fun limits(block: PigCommandBuilder.() -> Unit) {
            block(this)
        }

        /**
         * 添加限制:必须为玩家执行此命令
         */
        fun mustPlayer() {
            limit { if (sender is Player) "" else "此命令只能由玩家执行" }
        }

        /**
         * 添加限制:必须为控制台执行此命令
         */
        fun mustConsole() {
            limit { if (sender !is Player) "" else "此命令只能由控制台执行" }
        }

        /**
         * 添加限制为必须为OP玩家执行此命令
         */
        fun mustOP() {
            limit { if (sender is Player && sender.isOp) "" else "此命令只能由OP执行" }
        }

        fun unnecessary() {
            necessary = false
        }

        fun permission(vararg per: String) {
            permission(true, *per)
        }

        fun permission(must: Boolean = true, vararg per: String) {
            permissions.addAll(per)
            if (must)
                per.forEach {
                    limit {
                        if (sender.hasPermission(it)) null
                        else "你没有权限:$it"
                    }
                }
        }

        fun executor(block: PigCommandContext.() -> Unit) {
            this.executor = block
        }

        fun build(): PigCommand {
            return PigCommand(name, describe).apply {
                this.arguments_flag = this@PigCommandBuilder.arguments_flag
                this.arguments_necessary = this@PigCommandBuilder.arguments_necessary
                this.arguments_unnecessary = this@PigCommandBuilder.arguments_unnecessary
                this.executor = this@PigCommandBuilder.executor
                this.permissions.addAll(this@PigCommandBuilder.permissions)
                this.limits.addAll(this@PigCommandBuilder.limits)
            }
        }
    }
}
