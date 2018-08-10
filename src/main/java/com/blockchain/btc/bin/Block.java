package com.blockchain.btc.bin;

/**
 * ClassName:Block
 * Description:
 */
public class Block {
    public int id;
    public String content;
    public String hash;
    public int nonce;
    public String prehash;

    public Block() {
    }

    public Block(int id, String content, String hash, int nonce, String prehash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.prehash = prehash;
    }
}
