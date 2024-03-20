package com.yybf.chenrpc.registry;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * $END$
 *
 * @author yangyibufeng
 * @date 2024/3/20
 */
public class EtcdRegistry {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 使用端点创建客户端
        Client client = Client.builder().endpoints("http://localhost:2379")
                .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // 输入 key-value
        kvClient.put(key, value).get();

        // 获取 CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // 从CompletableFuture中获取value
        GetResponse response = getFuture.get();

        // 删除键
        kvClient.delete(key).get();
    }
}