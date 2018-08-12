package com.blockchain.btc.controller;

import com.blockchain.btc.BtcApplication;
import com.blockchain.btc.bin.Accountbook;
import com.blockchain.btc.bin.Block;
import com.blockchain.btc.bin.MessageBean;
import com.blockchain.btc.bin.Transaction;
import com.blockchain.btc.websocket.MyClient;
import com.blockchain.btc.websocket.MyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.PostConstruct;

/**
 * ClassName:BlockController
 * Description:
 */

@RestController
public class BlockController {

    private Accountbook accountbook = Accountbook.getInstance();

    @RequestMapping(value = "/addGenesis", method = RequestMethod.POST)
    public String addGenesis(String genesis){
        try {
            accountbook.addGenesis(genesis);
            return "封面添加成功";
        } catch (Exception e) {
            return "添加封面失败"+e.getMessage();
        }
    }

    @RequestMapping(value = "/addaccount", method = RequestMethod.POST)
    public String addaccount(Transaction transaction){
        try {
            if (transaction.verify()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String transactionString = objectMapper.writeValueAsString(transaction);

                //广播交易数据
                MessageBean messageBean = new MessageBean(2, transactionString);
                String msg = objectMapper.writeValueAsString(messageBean);

                myService.broadcast(msg);
                accountbook.addaccount(transactionString);
                return "添加记录成功";
            }else {
                throw new RuntimeException("交易数据校验失败");
            }
        } catch (Exception e) {
            return "添加记录失败"+e.getMessage();
        }
    }

    @RequestMapping(value = "/showlist", method = RequestMethod.GET)
    public ArrayList<Block> showlist(){

            return accountbook.showlist();

    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check(){

        String check = accountbook.check();

        if (StringUtils.isEmpty(check)) {
            return "数据是安全的";
        }

        return accountbook.check();

    }

    private MyService myService;

    @PostConstruct//执行构造的时候调用这个方法
    public void init(){
        myService=new MyService(Integer.parseInt(BtcApplication.port)+1);
        myService.startServer();
    }

    private HashSet<String> set=new HashSet<>();

    //节点注册
    @RequestMapping("/regist")
    public String regist(String node){
        set.add(node);
        return "注册成功";
    }

    private ArrayList<MyClient> client=new ArrayList<>();

    //连接
    @RequestMapping("/conn")
    public String conn(){
        try {
            for (String s : set) {
                URI uri=new URI("ws://localhost:"+s);
                MyClient myClient=new MyClient(uri,s);
                myClient.connect();
            }
            return "连接成功";
        } catch (URISyntaxException e) {
            return "连接失败"+e.getMessage();
        }

    }

    //广播
    @RequestMapping("/broadcast")
    public String broadcast(String msg){
        myService.broadcast(msg);
        return "广播成功";
    }

    //同步
    @RequestMapping("/syncData")
    public String syncData() {

        for (MyClient myClient : client) {
            myClient.send("请求区块链信息");
        }

        return "广播成功";
    }
}
