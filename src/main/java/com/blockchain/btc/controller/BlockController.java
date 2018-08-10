package com.blockchain.btc.controller;

import com.blockchain.btc.bin.Accountbook;
import com.blockchain.btc.bin.Block;
import com.blockchain.btc.bin.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * ClassName:BlockController
 * Description:
 */

@RestController
public class BlockController {

    Accountbook accountbook = new Accountbook();

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

        return accountbook.check();

    }
}
