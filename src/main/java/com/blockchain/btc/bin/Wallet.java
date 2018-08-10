package com.blockchain.btc.bin;


import com.blockchain.btc.utils.RSAUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * ClassName:Wallet
 * Description:
 */
public class Wallet {
    //公钥
    public PublicKey publickey;

    //私钥
    public PrivateKey privatekey;

    //钱包名称
    public String name;

    public Wallet(String name) {
        this.name=name;

        File priFile=new File(name+".pri");
        File pubFile=new File(name+".pub");

        if (!priFile.exists() || priFile.length() == 0 || !pubFile.exists() || pubFile.length() == 0) {
            RSAUtils.generateKeysJS("RSA", name + ".pri", name + ".pub");
        }

//        privatekey=RSAUtils.getPrivateKeyFile("RSA", name + ".pri");
//        publickey=RSAUtils.getPublicKeyFromFile("RSA", name + ".pub");
    }

    public Transaction sendMoney(String receiverPublicKey,String content){

        String singnature = RSAUtils.getSignature("SHA256withRSA", privatekey, content);

        String senderPublicKey= Base64.encode(publickey.getEncoded());

        Transaction transaction=new Transaction(senderPublicKey,receiverPublicKey,content,singnature);
        return transaction;
    }

    public static void main(String[] args) {
        Wallet a=new Wallet("a");
        Wallet b=new Wallet("b");

//        PublicKey publickey = b.publickey;
//
//        String encode = Base64.encode(publickey.getEncoded());
//
//        Transaction transaction=a.sendMoney(encode,"100");
//
//        boolean verify = transaction.verify();
//
//        System.out.println(verify);

    }


}
