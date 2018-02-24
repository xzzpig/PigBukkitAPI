package com.xzzpig.bukkit.pigapi.pigcommand

import java.util.*


data class PigCommandArgument(val name: String, val describe: String = "", val type: Type = Type.String, val necessary: Boolean = true, val flag: Boolean = false, val defaultValue: (PigCommandContext) -> Any? = {}, internal val limits: List<(String) -> (String?)>) {

    class PigCommandArgumentBuilder(val name: String, var type: Type = Type.String, var describe: String = "") {

        var necessary = true

        internal var flag = false

        private val limitList = LinkedList<(String) -> (String?)>()

        fun limit(block: String.() -> (String?)) {
            limitList.add(block)
        }

        inline fun limits(block: PigCommandArgumentBuilder.() -> Unit) {
            block(this)
        }

        var defaultValue: (PigCommandContext) -> Any? = {}

        fun build(): PigCommandArgument = PigCommandArgument(name, describe, type, necessary, flag, defaultValue, limitList)

        fun default(block: PigCommandContext.() -> Any?) {
            defaultValue = block
        }
    }


    enum class Type(val typeClass: Class<*>) {
        /**
         * 表示标识是否出现
         * Flay only
         */
        Present(Unit::class.java),
        String(kotlin.String::class.java),
        /**
         * 表示剩下所有参数
         * Arg only
         */
        RemainString(kotlin.String::class.java),
        Int(kotlin.Int::class.java),
        Double(kotlin.Double::class.java),
        Player(org.bukkit.entity.Player::class.java),
        World(org.bukkit.World::class.java)
    }
}