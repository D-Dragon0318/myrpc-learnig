package io.spridra.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.spridra.rpc.common.utils.SerializationUtils;
import io.spridra.rpc.constants.RpcConstants;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.enumeration.RpcType;
import io.spridra.rpc.protocol.header.RpcHeader;
import io.spridra.rpc.protocol.request.RpcRequest;
import io.spridra.rpc.protocol.response.RpcResponse;
import io.spridra.rpc.serialization.api.Serialization;

import java.util.List;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 10:13
 * @Describe: 解码
 * @Version: 1.0
 */

public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {


    /**
     * ctx：Netty 的上下文对象。
     * in：输入的字节缓冲区。
     * out：输出的对象列表，Netty 会自动传递给下一个 handler。
     */
    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 如果可读字节数小于协议头部总长度（比如 20 字节），则暂不处理，等待更多数据。
        if (in.readableBytes() < RpcConstants.HEADER_TOTAL_LEN) {

            return;

        }
        //保存当前读指针位置，如果后续解析失败可以回退
        in.markReaderIndex();

        short magic = in.readShort();
        if (magic != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal," + magic);
        }

        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();

        //读取16位的序列化类型
        ByteBuf serializationTypeByteBuf =
                in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNR);
        //去除填充的0
        String serializationType = SerializationUtils.subString(serializationTypeByteBuf.toString(CharsetUtil.UTF_8));
        //获取body长度
        int dataLength = in.readInt();
        //如果当前可读字节数不够，则重置读指针并返回，等待下一次数据到来
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        //读取实际数据body内容
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        //获取消息类型枚举
        RpcType msgTypeEnum = RpcType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }
        //构建RpcHeader
        RpcHeader header = new RpcHeader();
        header.setMagic(magic);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgType(msgType);
        header.setSerializationType(serializationType);
        header.setMsgLen(dataLength);

        Serialization serialization = getJdkSerialization();

        switch (msgTypeEnum) {
            //请求类型
            case REQUEST:
                RpcRequest request = serialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    //加入 out 列表，交给下一个 handler 处理
                    out.add(protocol);
                }
                break;
            //响应类型
            case RESPONSE:
                RpcResponse response = serialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                break;
        }

    }
}
