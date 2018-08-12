# -Bitcoin-principle-project-based-on-java-implementation
项目简介：

 1.这是一个实现比特币底层原理的项目，使用springBoot搭建框架，使用了bootStrap搭建了前端页面，使用webSocket模拟各个节点点对点广播和通信，使用Ajax进行前后端通信，使用RSA非对称加密；
 
 2.内置功能模块有加密工具类，密钥对生成，添加创世区块，添加普通区块，校验区块，json字符串序列化和反序列化，数字钱包简单模型并实现转账；
  
 3.使用springBoot创建多个实例，模拟出多节点，相互添加注册；
 
 4.钱包功能是使用发送方的私钥加密转账内容，生成数字签名，将发送方的公钥、数字签名、原文发送至接收方的地址（接收方公钥），接收方验证消息是否发生变化。
  
  
  Project Description:
  
  1. This is a project to implement the underlying principle of Bitcoin. It uses springBoot to build the framework, uses bootStrap to build the front-end page, uses webSocket to simulate point-to-point broadcast and communication of each node, uses Ajax for front-end communication, and uses RSA asymmetric encryption.
  
  2. Built-in function module has encryption tool class, key pair generation, add creation block, add common block, check block, json string serialization and deserialization, digital wallet simple model and realize transfer;
   
  3. Use springBoot to create multiple instances, simulate multiple nodes, and add registrations to each other;
   
  4. The wallet function uses the sender's private key to encrypt the transfer content, generate a digital signature, and send the sender's public key, digital signature, and content to the recipient's address (receiver's public key), and the recipient verifies that the message has changed. .
