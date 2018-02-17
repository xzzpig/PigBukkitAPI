package com.xzzpig.bukkit.pigapi;

import com.xzzpig.bukkit.pigapi.event.StringMatcherEvent;
import com.xzzpig.pigutils.TCalculate;
import com.xzzpig.pigutils.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map.Entry;

public class TStringMatcher {
    public static String buildStr(String str, HashMap<String, Object> data, boolean callEntity) {
        if (callEntity) {
            LivingEntity entity = null;
            if (data.containsKey("entity"))
                entity = (LivingEntity) data.get("entity");
            str = buildStr(str, entity, false);
        } else {
            StringMatcherEvent event = new StringMatcherEvent(str, data);
            Event.callEvent(event);
            str = event.getSouce();
        }
        return str;
    }

    @SuppressWarnings("deprecation")
    public static String buildStr(String str, LivingEntity entity, boolean isInt) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("entity", entity);
        StringMatcherEvent event = new StringMatcherEvent(str, data);
        Event.callEvent(event);
        str = event.getSouce();
        String re = str.replaceAll("</world/>", entity.getWorld().getName())
                .replaceAll("</loc/>", entity.getLocation().toString())
                .replaceAll("</x/>", entity.getLocation().getBlockX() + "")
                .replaceAll("</y/>", entity.getLocation().getBlockY() + "")
                .replaceAll("</z/>", entity.getLocation().getBlockZ() + "")
                .replaceAll("</currenthealth/>", "" + TEntity.getHealth(entity))
                .replaceAll("</maxhealth/>", TEntity.getMaxHealth(entity) + "")
                .replaceAll("</name/>", entity.getCustomName()).replaceAll("</type/>", entity.getType().toString())
                .replaceAll("</online/>", Array.getLength(Bukkit.getServer().getOnlinePlayers()) + "")
                .replaceAll("</worldpvp/>", entity.getWorld().getPVP() + "").replaceAll("</n/>", "\n");
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String colorid;
            if (player.isOp())
                colorid = ChatColor.RED + player.getName();
            else
                colorid = ChatColor.GREEN + player.getName();
            re = re.replaceAll("</fly/>", player.getAllowFlight() + "")
                    .replaceAll("</exp/>", TExp.getTotalExperience(player) + "")
                    .replaceAll("</level/>", TPlayer.ExpToLevel(TExp.getTotalExperience(player)) + "")
                    .replaceAll("</handid/>", player.getItemInHand().getTypeId() + "")
                    .replaceAll("</gamemode/>", player.getGameMode().name()).replaceAll("</op/>", player.isOp() + "")
                    .replaceAll("</sneak/>", "" + player.isSneaking())
                    .replaceAll("</hunger/>", player.getFoodLevel() + "")
                    .replaceAll("</prefix/>", TPrefix.getPrefix(player.getName()))
                    .replaceAll("</id/>", player.getName()).replaceAll("</colorid/>", colorid);
            if (re.contains("</prefix_")) {
                for (Entry<String, String> element : TPrefix.getPrefixMap(player.getName()).entrySet()) {
                    re = re.replaceAll("</prefix_" + element.getKey() + "/>", element.getValue());
                }
            }
        }
        if (isInt)
            re = ((int) TCalculate.getResult(re)) + "";
        re = solve(re);
        return re;
    }

    public static String solve(String ps) {
        return ps.replaceAll("&", TString.s);
    }
}
