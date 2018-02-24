@file:JvmName("Commands")

package com.xzzpig.bukkit.pigapi.pigcommand.plugin

import com.xzzpig.bukkit.pigapi.TMessage
import com.xzzpig.bukkit.pigapi.javascript.JSListener
import com.xzzpig.bukkit.pigapi.pigcommand.PigCommandManager
import com.xzzpig.bukkit.pigapi.plugin.Main
import com.xzzpig.pigutils.TScript
import org.bukkit.ChatColor
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.*
import javax.script.ScriptEngine
import javax.script.ScriptException

val manager = PigCommandManager().apply {
    command("pigapi") { describe = "PigAPI的主命令" }
    command("pigapi reloadscript") {
        describe = "重新加载脚本文件"
        permission("pigapi.command.reloadscript")
        executor {
            JSListener.instance.loadScript()
            sendMessage("[PigAPI]" + ChatColor.GREEN + "JS脚本重载成功")
        }
    }
    command("pigapi runscript") {
        describe = "执行脚本文件"
        permission("pigapi.command.runscript")
        argument("script") { describe = "要执行的脚本" }
        executor {
            if (scriptDir.exists())
                scriptDir.mkdirs()
            val script = getString("script")
            val script_f = File(scriptDir, script)
            var eng: ScriptEngine? = null
            if (script.endsWith(".py")) {
                eng = TScript.getJythonScriptEngine()
                if (eng == null) {
                    sendMessage("&4Jython脚本引擎未加载", "PigAPI")
                    return@executor
                }
            } else if (script.endsWith(".js")) {
                eng = TScript.getJavaScriptEngine()
                if (eng == null) {
                    sendMessage("JavaScript脚本引擎未加载")
                    return@executor
                }
            }
            if (eng == null) {
                sendMessage("无法识别该脚本格式")
                return@executor
            }
            eng.put("plugin", Main.self)
            eng.put("ci", this)
            eng.put("player", sender)
            val fr: FileReader
            try {
                fr = FileReader(script_f)
                eng.eval(fr)
            } catch (e: FileNotFoundException) {
                sendMessage("该文件不存在")
                return@executor
            } catch (e: ScriptException) {
                e.printStackTrace()
                sendMessage("Script执行失败,Reason:" + e.toString())
            }
        }
    }
    command("pigapi chat") { describe = "聊天管理命令" }
    command("pigapi chat desee") { describe = "管理聊天屏蔽" }
    command("pigapi chat desee add") {
        describe = "将某人添加至自己的聊天屏蔽列表"
        argument("player") { describe = "玩家ID" }
        limits { mustPlayer() }
        executor {
            val target = getString("player")
            val desee = deseemap.getOrPut(sender.name, { ArrayList<String>() })//if (deseemap.containsKey(sender)) deseemap.get(sender) else ArrayList<String>()
            desee.add(target)
            sendMessage("已将" + target + "加入你的聊天屏蔽列表")
        }
    }
    command("pigapi chat desee remove") {
        describe = "将某人移出自己的聊天屏蔽列表"
        argument("player") { describe = "玩家ID" }
        limits { mustPlayer() }
        executor {
            val desee = deseemap[sender.name]
            val target = getString("player")
            if (desee == null || target !in desee) {
                sendMessage("&3$target&4不在你的聊天列表中", "PigAPI")
                return@executor
            }
            desee.remove(target)
            sendMessage("已将&3$target&r移出你的聊天屏蔽列表")
        }
    }
    command("pigapi chat desee list") {
        describe = "列出自己的聊天屏蔽列表"
        limits { mustPlayer() }
        executor {
            sendMessage("你的聊天屏蔽列表:")
            (deseemap[sender.name]
                    ?: listOf("空")).forEach { sendMessage(TMessage("[*]").color(ChatColor.GOLD).then(it).color(ChatColor.RED).tooltip("点击移除").command("/pigapi chat desee remove $it")) }
        }
    }
}
internal val scriptDir = File(Main.self.dataFolder, "scripts")
val deseemap: MutableMap<String, MutableList<String>> = HashMap()
