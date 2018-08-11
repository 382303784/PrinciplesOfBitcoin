package com.blockchain.btc.websocket;

import com.blockchain.btc.bin.Accountbook;
import com.blockchain.btc.bin.Block;
import com.blockchain.btc.bin.MessageBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * ClassName:MyService
 * Description:
 */
public class MyService extends WebSocketServer {

    private int port;

    public MyService(int port) {
        super(new InetSocketAddress(port));
        this.port=port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("WS服务器-" + port + "-打开了连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WS服务器-" + port + "-关闭了连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("WS服务器-" + port + "-收到了消息"+message);
        try {
            if("请求区块链信息".equals(message)){
                Accountbook accountbook=Accountbook.getInstance();
                ArrayList<Block> list=accountbook.showlist();

                ObjectMapper objectMapper=new ObjectMapper();
                String blockChainData = objectMapper.writeValueAsString(list);

                MessageBean messageBean = new MessageBean(1, blockChainData);
                String msg = objectMapper.writeValueAsString(messageBean);

                // 广播消息
                broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("WS服务器-" + port + "-发生了错误");
    }

    @Override
    public void onStart() {
        System.out.println("WS服务器-" + port + "-启动成功");
    }

    //启动方法
    public void startServer(){
        new Thread(this).start();
    }
}
