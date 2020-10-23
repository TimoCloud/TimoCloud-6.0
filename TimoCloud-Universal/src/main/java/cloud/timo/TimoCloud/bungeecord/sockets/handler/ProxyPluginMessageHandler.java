package cloud.timo.TimoCloud.bungeecord.sockets.handler;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.implementations.TimoCloudMessageAPIBasicImplementation;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.common.protocol.Message;
import cloud.timo.TimoCloud.common.protocol.MessageType;
import cloud.timo.TimoCloud.common.sockets.MessageHandler;
import cloud.timo.TimoCloud.common.utils.PluginMessageSerializer;
import io.netty.channel.Channel;

import java.util.Map;

public class ProxyPluginMessageHandler extends MessageHandler {
    public ProxyPluginMessageHandler() {
        super(MessageType.ON_PLUGIN_MESSAGE);
    }

    @Override
    public void execute(Message message, Channel channel) {
        AddressedPluginMessage addressedPluginMessage = PluginMessageSerializer.deserialize((Map) message.getData());
        ((TimoCloudMessageAPIBasicImplementation) TimoCloudAPI.getMessageAPI()).onMessage(addressedPluginMessage);
    }
}
