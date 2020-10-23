package cloud.timo.TimoCloud.bungeecord.sockets.handler;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.events.EventType;
import cloud.timo.TimoCloud.api.implementations.TimoCloudUniversalAPIBasicImplementation;
import cloud.timo.TimoCloud.api.implementations.managers.EventManager;
import cloud.timo.TimoCloud.api.utils.EventUtil;
import cloud.timo.TimoCloud.bungeecord.TimoCloudBungee;
import cloud.timo.TimoCloud.common.protocol.Message;
import cloud.timo.TimoCloud.common.protocol.MessageType;
import cloud.timo.TimoCloud.common.sockets.MessageHandler;
import cloud.timo.TimoCloud.common.utils.EnumUtil;
import io.netty.channel.Channel;

public class ProxyEventFiredHandler extends MessageHandler {
    public ProxyEventFiredHandler() {
        super(MessageType.EVENT_FIRED);
    }

    @Override
    public void execute(Message message, Channel channel) {
    try {
        EventType eventType = EnumUtil.valueOf(EventType.class, (String) message.get("eT"));
        ((EventManager) TimoCloudAPI.getEventAPI()).callEvent(((TimoCloudUniversalAPIBasicImplementation) TimoCloudAPI.getUniversalAPI()).getObjectMapper().readValue((String) message.getData(), EventUtil.getClassByEventType(eventType)));
    } catch (Exception e) {
        System.err.println("Error while parsing event from json: ");
        TimoCloudBungee.getInstance().severe(e);
    }
    }
}
