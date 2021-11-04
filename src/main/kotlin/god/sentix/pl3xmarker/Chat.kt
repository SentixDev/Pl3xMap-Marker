package god.sentix.pl3xmarker

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class Chat {

    fun send(recepient: Audience, input: String) {
        recepient.sendMessage(MiniMessage.get().parse(input))
    }

    inner class Audiences {

        fun all(): Audience {
            return ForwardingAudience {
                Bukkit.getOnlinePlayers()
            }
        }

        fun permission(permission: String): Audience {
            return ForwardingAudience {
                Bukkit.getOnlinePlayers().stream().filter { p ->
                    p.hasPermission(permission)
                }.toList()
            }
        }

    }

}