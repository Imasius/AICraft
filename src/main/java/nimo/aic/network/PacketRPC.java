package nimo.aic.network;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import nimo.aic.AICraft;

import java.lang.reflect.Method;


public class PacketRPC<T extends GeneratedMessageV3> implements IMessage {

    private static long messageCounter = 0;

    private static long nextId() {
        return messageCounter++;
    }

    protected T message;
    protected Class<T> msgClass;
    protected long messageId = nextId();

    public PacketRPC(Class<T> msgClass) {
        this.msgClass = msgClass;
    }

    public PacketRPC(T message) {
        this.message = message;
        this.msgClass = (Class<T>) message.getClass();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            messageId = buf.readLong();
            byte[] rawData = new byte[buf.readableBytes()];
            buf.readBytes(rawData);

            Method parseMethod = msgClass.getMethod("parseFrom", byte[].class);
            message = (T) parseMethod.invoke(null, rawData);
        } catch (Exception e) {
            AICraft.log.error("Unable to parse protobuf message sent via MC packet.", e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(messageId);
        buf.writeBytes(message.toByteArray());
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
