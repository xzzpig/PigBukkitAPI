package com.xzzpig.bukkit.pigapi

inline fun TMessage.then(obj: Any, block: TMessage.() -> Unit): TMessage {
    then(obj)
    block(this)
    return this
}