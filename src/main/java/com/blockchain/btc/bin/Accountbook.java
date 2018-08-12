package com.blockchain.btc.bin;

import com.blockchain.btc.utils.HashUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ClassName:Accountbook
 * Description:
 */
public class Accountbook {

    private ArrayList<Block> list = new ArrayList<>();

    private Accountbook() {
        init();
    }

    public static volatile Accountbook instance;

    public static Accountbook getInstance(){
        if(instance==null){
            synchronized (Accountbook.class) {
                if (instance == null) {
                    instance = new Accountbook();
                }
            }
        }
        return instance;
    }

    //构造的时候把数据读出来
    public void init() {
        try {
            File file = new File("a.json");
            if (file.exists() && file.length() > 0) {
                ObjectMapper objectMapper = new ObjectMapper();

                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                list = objectMapper.readValue(file, javaType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //封面 添加创世区块 需要是第一个
    public void addGenesis(String genesis) {
        if (list.size() > 0) {
            throw new RuntimeException("添加封面的事必须是新账本");
        }

        String prehash="0000000000000000000000000000000000000000000000000000000000000000";

        int nonce=mine(genesis+prehash);

        list.add(new Block(
                list.size() + 1,
                genesis,
                HashUtils.sha256(nonce+genesis+prehash),
                nonce,
                prehash
        ));

        savedisk();
    }

    //挖矿
    private int mine(String content){
        for(int i=0;i<Integer.MAX_VALUE;i++){
            String s = HashUtils.sha256(i + content);
            if(s.startsWith("0000")){
                return i;
            }
        }
        throw new RuntimeException("挖矿失败");
    }

    //添加普通区块
    public void addaccount(String account) {
        if (list.size() < 1) {
            throw new RuntimeException("添加记录的事必须有封面");
        }


        Block block=list.get(list.size()-1);
        String prehash=block.hash;
        int nonce=mine(account+prehash);

        list.add(new Block(
                list.size() + 1,
                account,
                HashUtils.sha256(nonce+account+prehash),
                nonce,
                prehash
        ));

        savedisk();
    }

    //展示记录
    public ArrayList<Block> showlist() {
        return list;
    }

    //保存数据 读取
    public void savedisk() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("a.json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String check() {

        StringBuilder sb = new StringBuilder();

//        for (Block block : list) {
//            if (!block.hash.equals(HashUtils.sha256(block.nonce+block.content+block.prehash))) {
//                sb.append("编号" + block.id + "为的区块数据有问题");
//            }
//        }

        for (int i = 0; i <list.size() ; i++) {
            Block block=list.get(i);
            String hash=block.hash;
            String content=block.content;
            int nonce=block.nonce;
            String preHash=block.prehash;

            //创世区块判断
            if(i==0){
                String jisuanHash=HashUtils.sha256(nonce+content+preHash);
                if(!jisuanHash.equals(hash)){
                    sb.append("编号为"+ block.id +"的区块数据有问题<br>");
                }
            }else{
                String jisuanHash=HashUtils.sha256(nonce+content+preHash);
                if(!jisuanHash.equals(hash)){
                    sb.append("编号为"+ block.id +"的区块数据有问题<br>");
                }

                Block preBlock=list.get(i-1);//上一块
                String preBlockHash=preBlock.hash;//上一块的hash

                if(!preBlockHash.equals(preHash)){
                    sb.append("编号为"+ block.id +"的区块preHash数据有问题<br>");
                }

            }
        }
        return sb.toString();
    }

    // 和本地的区块链进行比较,如果对方的数据比较新,就用对方的数据替换本地的数据
    public void compareData(ArrayList<Block> newList) {
        // 比较长度, 校验
        if (newList.size() > list.size()) {
            list = newList;
            savedisk();
        }
    }
}
