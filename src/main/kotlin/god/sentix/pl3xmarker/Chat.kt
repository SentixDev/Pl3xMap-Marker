package god.sentix.pl3xmarker

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class Chat {

    fun send(audience: Audience, input: String) {
        audience.sendMessage(MiniMessage.get().parse(input))
    }

    fun url(content: String, hoverText: String, openUrl: String): String {
        return "${hoverable(hoverText)}<click:open_url:$openUrl>$content</hover>"
    }

    fun clickable(content: String, hoverText: String, clickAction: String): String {
        return "${hoverable(hoverText)}<click:run_command:$clickAction>$content</hover>"
    }

    fun hoverable(hoverText: String): String {
        return "<hover:show_text:'$hoverText'>"
    }

}