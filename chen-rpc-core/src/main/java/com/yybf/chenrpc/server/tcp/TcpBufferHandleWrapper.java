package com.yybf.chenrpc.server.tcp;

import com.yybf.chenrpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * 装饰者模式（通过使用 recordParser 对原有的 buffer 的处理能力进行增强）
 *
 * @author yangyibufeng
 * @date 2024/4/7
 */
public class TcpBufferHandleWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public TcpBufferHandleWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 构造parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            // 初始化，消息头标记
            int size = -1;
            // 储存一次完整的读取（包括消息头和消息体）
            Buffer resultBuffer = Buffer.buffer();
            @Override
            public void handle(Buffer buffer) {
                if (size == -1) { // 读取的是头信息
                    // 读取消息体的长度
                    size = buffer.getInt(13);
                    // 更改截取的长度
                    parser.fixedSizeMode(size);
                    // 将头信息写入结果
                    resultBuffer.appendBuffer(buffer);
//                    resultBuffer.appendBytes(" -- ".getBytes());
                } else { // 读取的是体信息
                    // 写入体信息到结果
                    resultBuffer.appendBuffer(buffer);
                    // 已拼接为完整 Buffer，执行处理
                    bufferHandler.handle(resultBuffer);
                    // 重置
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}