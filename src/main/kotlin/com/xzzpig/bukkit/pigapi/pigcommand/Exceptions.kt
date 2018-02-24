package com.xzzpig.bukkit.pigapi.pigcommand

class NoSuchCommandException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace)
}

class PigCommandArgumentParseException(val string: String, val context: PigCommandContext, val reason: String = "转换失败") :
        Exception()

/**
 * @see PigCommand.PigCommandBuilder.limit
 */
class PigCommandDeniedException(val reason: String, val context: PigCommandContext) :
        Exception()