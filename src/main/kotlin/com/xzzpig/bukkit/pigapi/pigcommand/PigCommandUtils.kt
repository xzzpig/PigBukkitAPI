@file:JvmName("PigCommandUtils")

package com.xzzpig.bukkit.pigapi.pigcommand

val PigCommand.subCommands get() = this.subCommandMap.toMap()

fun PigCommand.toTreeString(): StringBuffer = StringBuffer().apply {
    append(name).append(' ')
    for ((_, arg) in arguments_flag) {
        if (!arg.necessary) append('[')
        append('-').append(arg.name)
        arg.type.takeUnless { it == PigCommandArgument.Type.Present }?.run { append('=').append(this.name) }
        if (!arg.necessary) append(']')
        append(' ')
    }
    for (arg in arguments_necessary) {
        append('<').append(arg.name).append('>').append(' ')
    }
    for (arg in arguments_unnecessary) {
        append('[').append(arg.name).append(']').append(' ')
    }
    append('\t').append("d:").append(describe)
    if (permissions.isNotEmpty()) append('\t').append("p:").append(permissions)
    subCommandMap.iterator().run {
        while (hasNext()) {
            val (_, cmd) = next()
            append('\n')
            if (hasNext()) append('|')
            else append('\\')
            append('-')
            cmd.toTreeString().lines().forEachIndexed { index, s ->
                if (index == 0) append(s)
                else append('\n').append(if (hasNext()) '|' else ' ').append(' ').append(' ').append(s)
            }
        }
    }
}

fun PigCommandManager.toTreeString(): StringBuffer = StringBuffer().apply {
    commandMap.values.forEach {
        append(it.toTreeString()).append('\n')
    }
}