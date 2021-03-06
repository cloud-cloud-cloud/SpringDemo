#### :snail: RabbitMQ
##### rabbitmq系统架构
 - broker :提供一种传输服务，他的角色提供生产者和消费者的线路，保证数据按照指定的方式进行传输；（被看作消息中间件，消息转发器）
 - exchange :消息交换体，指定消息按照某个规则，路由到指定的队列
 - queue：消息的载体，每个消息被投递到一个或者多个队里里面；
 - binding:他的作用是将exchange 和queue根据某个路由规则绑定在一起；
 - routing key 路由关键字
 - Vhost:虚拟主机，一个broker里面有多个vhost,用作不同用户的权限分离。
 - producer :消息生产者，发送程序
 - consumer：消息消费者，接受消息的程序；
 - channel：消息通道，在客户端可以建立多个消息通道。
##### 消息分发机制
  -  round-robin 循环分发。
  -　fair dispath分平分发，会将n个消息分发到第N个consumer，n是取余。不过这样会导致有的很忙，有的很闲。

  在nginx里面的公平分发是按照时间越短的优先（fair），另外其他几种分发方式：轮询，weight,ip_hash(session),url_hash(upstream_hash),

 **交换机路由的几种规则**
  - 直接匹配(direct exchange)（通过exchange与routing key进行binding）
   - 主题匹配订阅(topic exchange):将消息绑定到该路由器上面，并且制定的routing key符合规则才能接收；
   - 广播订阅(fanout exchange): 向所有消费这发送消息，但是只有消费者将队列绑定到路由器，才能收到消息，忽略 routing key;
   - 消息头订阅(header exchange)：消息发布前，消息定义一个或者多个键值对的消息头，去订阅消费消息的时候，只有消费体的请求头和消息头匹配，才能接收消息，忽略routingkey

 ##### protoBuf数据类型
 要通信就必须要有协议，否则双方无法理解对方的码流，在protoBUf中协议是一系列的组成。因此最重要的是定义消息结构
 格式：限定修饰符|数据结构|字段名称=|字段编码值|字段默认值  例如说 require  int age=23
 限定修饰符：require必填字段 ； optional 可选字段； repeated 表示该字段有0-N个元素，他的特性和optional一样，不过一次可以包含多个值，可以看作是一个传递数组的值。
