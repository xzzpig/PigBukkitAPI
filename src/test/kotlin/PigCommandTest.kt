package com.xzzpig.bukkit.pigapi.pigcommand

import org.junit.Test


class PigCommandTest {

    val manager = PigCommandManager()

    @Test
    fun testAddCommand() {
        manager.apply {
            command("pigapi test") {
                describe = "Test Command"
                permission("com.xzzpig.a")
                flag("a")
                flag("b", "aaa", PigCommandArgument.Type.Int, true)
                argument("name") { type = PigCommandArgument.Type.String }
                argument("world") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi test2") {
                describe = "Test Command2"
                permission("com.xzzpig.b")
                flag("c")
                flag("d", "ddd", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi") {
                describe = "Test Command2"
                flag("c")
                flag("d", "ccc", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi test2 test3") {
                describe = "Test Command2"
                flag("c")
                flag("d", "eee", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi test2 test4") {
                describe = "Test Command2"
                flag("c")
                flag("d", "ddafe", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi test test5") {
                describe = "Test Command2"
                flag("c")
                flag("d", "ariog", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
            command("pigapi2") {
                describe = "Test Command2"
                flag("c")
                flag("d", "fiwaof", PigCommandArgument.Type.Int)
                argument("name2") { type = PigCommandArgument.Type.String }
                argument("world2") { type = PigCommandArgument.Type.Int }
                unnecessary()
                argument("other2") { type = PigCommandArgument.Type.RemainString }
            }
        }
        println(manager.toTreeString())
    }
}