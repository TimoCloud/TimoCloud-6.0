package cloud.timo.TimoCloud.bukkit.sockets;

import cloud.timo.TimoCloud.bukkit.TimoCloudBukkit;
import cloud.timo.TimoCloud.bukkit.sockets.handler.*;
import cloud.timo.TimoCloud.common.protocol.Message;
import cloud.timo.TimoCloud.common.protocol.MessageType;
import cloud.timo.TimoCloud.common.sockets.BasicStringHandler;
import io.netty.channel.Channel;

public class BukkitStringHandler extends BasicStringHandler {

    public BukkitStringHandler() {
        addBasicHandlers();
    }

    @Override
    public void handleMessage(Message message, String originalMessage, Channel channel) {
        if (message == null) {
            TimoCloudBukkit.getInstance().severe("Error while parsing json (json is null): " + originalMessage);
            return;
        }
        MessageType type = message.getType();

        getMessageHandlers(type).forEach(messageHandler -> messageHandler.execute(message, channel));
    }

    private void addBasicHandlers() {
        addHandler(new BukkitApiDataHandler());
        addHandler(new BukkitEventFiredHandler());
        addHandler(new BukkitHandshakeSuccessHandler());
        addHandler(new BukkitPluginMessageHandler());
        addHandler(new BukkitServerExecuteCommandHandler());
        addHandler(new BukkitServerStopHandler());
    }
}
