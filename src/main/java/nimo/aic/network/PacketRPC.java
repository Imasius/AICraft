package nimo.aic.network;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nimo.aic.AICraft;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class PacketRPC implements IMessage {

    private static long messageCounter = 0;

    private static long nextSequenceNr() {
        return messageCounter++;
    }

    private static Map<MessageType, Class<?>> messageClassMap = new HashMap<>();
    private static Map<Class<?>, MessageType> classMessageMap = new HashMap<>();

    private static Map<MessageType, Method> handlerCache = new HashMap<>();

    private static final String msgPackage = "nimo.aic.grpc.";

    static {
        for (int i = 0; i < MessageType.values().length; i++) {
            MessageType messageType = MessageType.values()[i];
            try {
                Class<?> messageClass = Class.forName(msgPackage + messageType.toString());
                messageClassMap.put(messageType, messageClass);
                classMessageMap.put(messageClass, messageType);
            } catch (ClassNotFoundException e) {
                AICraft.log.error("Unable to load class for MessageType {}", messageType);
                AICraft.log.catching(e);
            }
        }
    }

    public enum MessageType {
        SetNameRequest,
        SetIdRequest,
        TransferItemStackRequest,
        GetItemStackInfoRequest,
        GetItemStackInfoResponse
    }

    private enum PacketType {
        Request(7, RequestHandler.class),
        Response(8, ResponseHandler.class);

        public final int offset;
        public final Class<?> handlerClass;

        PacketType(int offset, Class<?> handlerClass) {
            this.offset = offset;
            this.handlerClass = handlerClass;
        }
    }

    protected MessageType messageType;
    protected long sequenceNr;
    protected GeneratedMessageV3 message;

    public PacketRPC() {}

    public PacketRPC(GeneratedMessageV3 message) {
        this(message, nextSequenceNr());
    }

    public PacketRPC(GeneratedMessageV3 message, long sequenceNr) {
        this.message = message;
        this.messageType = classMessageMap.get(message.getClass());
        this.sequenceNr = sequenceNr;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            sequenceNr = buf.readLong();
            messageType = MessageType.values()[buf.readInt()];
            byte[] rawData = new byte[buf.readableBytes()];
            buf.readBytes(rawData);

            Method parseMethod = messageClassMap.get(messageType).getMethod("parseFrom", byte[].class);
            message = (GeneratedMessageV3) parseMethod.invoke(null, rawData);
        } catch (Exception e) {
            AICraft.log.error("Unable to parse protobuf message sent via MC packet.", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(sequenceNr);
        buf.writeInt(messageType.ordinal());
        buf.writeBytes(message.toByteArray());
    }

    public long getSequenceNr() {
        return sequenceNr;
    }

    public static class RequestPacketHandler implements IMessageHandler<PacketRPC, IMessage> {
        @Override
        public IMessage onMessage(PacketRPC packet, MessageContext ctx) {
            try {
                Method handler = getHandler(packet.messageType, PacketType.Request);
                handler.invoke(RequestHandler.instance, packet, ctx);
            } catch (Exception e) {
                AICraft.log.error("Unable to call handler method for MessageType {}", packet.messageType);
                AICraft.log.catching(e);
            }
            return null;
        }
    }

    public static class ResponsePacketHandler implements IMessageHandler<PacketRPC, IMessage> {
        @Override
        public IMessage onMessage(PacketRPC packet, MessageContext ctx) {
            try {
                Method handler = getHandler(packet.messageType, PacketType.Response);
                handler.invoke(ResponseHandler.instance, packet, ctx);
            } catch (Exception e) {
                AICraft.log.error("Unable to call handler method for MessageType {}", packet.messageType);
                AICraft.log.catching(e);
            }
            return null;
        }
    }

    private static Method getHandler(MessageType messageType, PacketType packetType) throws NoSuchMethodException {
        Method handler = handlerCache.get(messageType);
        if (handler == null) {
            String methodName = methodNameOf(messageType, packetType.offset);
            handler = packetType.handlerClass.getMethod(methodName, PacketRPC.class, MessageContext.class);
            handlerCache.put(messageType, handler);
        }
        return handler;
    }

    private static String methodNameOf(MessageType messageType, int offset) {
        String messageTypeString = messageType.toString();
        String prefixLetter = messageTypeString.substring(0, 1).toLowerCase();

        String methodNameString = prefixLetter + messageTypeString.substring(1, messageTypeString.length() - offset);
        return methodNameString;
    }
}
