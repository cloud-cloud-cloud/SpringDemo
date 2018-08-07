
    #### :snail: NoSql


    ##### 有没有用过Redis?


    ##### 访问Redis用的是Jedis还是Spring Data


    ##### 回答jedis


    ##### 如何关闭一个Redis的连接
     直接close关闭

    ##### 如何创建一个jedis pool
    直接 now JedisPool(host,port,timeout)

    ##### 有没有使用过shardConnectionPool


    ##### 回答spring data


    ##### 如何声明一个connection pool

    ##### 如何用redistemplate存一个java object


    #####有没有使用过spring 的 @cach自定义一个cach redis对象的annotation？即我只需要在get/set方法或者是在filed上放上一个自定义的@cach就可以使用业务字段值存入redis的功能


    ##### 做过redhs的集群吗？


    ##### 是HA还是CLUSTER？


    ##### 回答是cluster


    ##### 用的是redis自带cluster?还是什么？
     自动cluster 在redis3.0以上的版本自带集群管理工具

    ##### Redis里的分布式锁是什么样的？你用过吗？什么场景 ？
      在多线程里面分布式锁

    ##### 有没有用过MogoDB或者相关的如：cassandra


    ##### 在mogodb里如果我要存一个人在该论坛内所有接收到的消息（系统推送、互发）用mogodb怎么存？能否说一下你的设计？
    创建一个对象包含（用户标示，创建时间，修改时间，{系统推送内容，创建时间}，[{发送的内容，对方发送的内容，时间}，{}]）


    #####  我想要设计每个人只能看近3个月的消息？又该怎么设计？
      - 如果是数据库要求不存在这部分数据 那么可以给createDate设置索引，设置过期时间
      db.collection.createIndex({'create_time':1},{expireAfterSeconds:3600})
      - 或者在查询模板里面只允许当前时间推前3个月的时间可以查询
     db.collection.find({"$and":[{"createTime":{"$gt":"2014-07-29 0:0:0"},{"createDate":{"lt":"2014-10-29 0:0:0"}}]})


    ##### cassandra里我要在一笔1000万条记录中查某个用户的到期日期设计在2014-01-01到2015-01-01间的那批记录，怎么查？遇到过什么坑吗？