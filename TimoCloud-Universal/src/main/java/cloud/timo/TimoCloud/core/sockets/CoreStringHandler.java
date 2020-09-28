package cloud.timo.TimoCloud.core.sockets;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.core.commands.CommandHandler;
import cloud.timo.TimoCloud.api.core.commands.CommandSender;
import cloud.timo.TimoCloud.api.events.EventType;
import cloud.timo.TimoCloud.api.implementations.TimoCloudUniversalAPIBasicImplementation;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.objects.*;
import cloud.timo.TimoCloud.api.utils.EventUtil;
import cloud.timo.TimoCloud.common.protocol.Message;
import cloud.timo.TimoCloud.common.protocol.MessageType;
import cloud.timo.TimoCloud.common.sockets.BasicStringHandler;
import cloud.timo.TimoCloud.common.sockets.MessageHandler;
import cloud.timo.TimoCloud.common.utils.DoAfterAmount;
import cloud.timo.TimoCloud.common.utils.EnumUtil;
import cloud.timo.TimoCloud.common.utils.PluginMessageSerializer;
import cloud.timo.TimoCloud.core.TimoCloudCore;
import cloud.timo.TimoCloud.core.objects.Base;
import cloud.timo.TimoCloud.core.objects.Cord;
import cloud.timo.TimoCloud.core.objects.Proxy;
import cloud.timo.TimoCloud.core.objects.Server;
import cloud.timo.TimoCloud.core.sockets.handlers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
public class CoreStringHandler extends BasicStringHandler {
    public CoreStringHandler() {
        addBasicHandlers();
    }

    @Override
    public void handleMessage(Message message, String originalMessage, Channel channel) {
        Communicatable sender = TimoCloudCore.getInstance().getSocketServerHandler().getCommunicatable(channel);
        MessageType type = message.getType();

        List<MessageHandler> messageHandlers  = getMessageHandlers(type);

        boolean handshake = false;
        for(MessageHandler messageHandler : messageHandlers){
            if(messageHandler.getMessageType().toString().contains("HANDSHAKE")) {
                handshake = true;
                messageHandler.execute(message, channel);
            }
        }

        // No Handshake, so we have to check if the channel is registered
        if (!handshake && (sender == null && channel != null)) { // If channel is null, the message is internal (sender is core)
            closeChannel(channel);
            TimoCloudCore.getInstance().severe("Unknown connection from " + channel.remoteAddress() + ", blocking. Please make sure to block the TimoCloudCore socket port (" + TimoCloudCore.getInstance().getSocketPort() + ") in your firewall to avoid this.");
            return;
        }
    }


    private void addBasicHandlers() {
        addHandler(new BaseProxyLogEntryHandler());
        addHandler(new CoreApiDataHandler());
        addHandler(new CoreBaseCheckDeletableHandler());
        addHandler(new CoreBaseHandshakeHandler());
        addHandler(new CoreCordHandshakeHandler());
        addHandler(new CoreFireEventHandler());
        addHandler(new CoreParseCommandHandler());
        addHandler(new CoreProxyHandshakeHandler());
        addHandler(new CoreSendPluginMessageHandler());
        addHandler(new CoreServerHandshakeHandler());
        addHandler(new CoreServerLogEntryHandler());
        addHandler(new ProxyTemplateRequestHandler());
        addHandler(new ServerTemplateRequestHandler());
    }
}
