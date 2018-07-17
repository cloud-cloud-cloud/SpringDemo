#### :snail: NIO
 ##### 一. NIO的简介
    new io,non-blocking io ,nio支持面向缓冲区的，基于管道的io操作，nio以一种更加高效的方式进行读写操作。
    nio随着jdk7发布以后增加了对文件的处理和对文件系统特性的支持；nio已经成为文件处理中越来越重要的部分。

  ##### 二.nio与io区别
  IO:面向流，单向的， 阻塞的 ； NIO:面向缓冲区的，通道是双向的，也可以单向。非阻塞。


  #####  三.NIO的核心
  NIO的核心在于：通道（channel 代表了数据源于io节点【文件，网络socket】之间的连接，负责传输buffer）和缓冲区（buffer ）,若需要使用NIO需要获取NIO设备中的通道以及容纳的数据缓冲区，对数据进行处理的缓冲区底层就是数组。
  简而言之：channel负责传输，buffer负责存储。

  #####  四.buffer常用的属性（存储）
  - 容量capacity；表示buffer最大的容量，一旦声明后，就不能修改，通过buffer中的capacity（）获取，缓冲区的capacity不能为负。
  - 限制limit：第一个不应该读取或写入的数据索引，即位于limit后的数据不能读写，通过buffer的limit（）,缓冲区的limit（）不
  能为负，不能大于capacity；
  - 位置position：当前要读取或者写入数据的索引，通过buffer中的position（）获取，缓冲区里面的position和limit一样不能大于其limit（），并且不能为负数
  - 标记Mark：标记是一个索引，通过buffer中的Mark方法标记当前的位置。之后可以通过reset()方法将position恢复到标记的Mark处。
  - 标记，位置，限制，容量遵循以下不变式：0<=mark<=position<=limit<capacity

    java.nio.buffer (ByteBuffer ,charBuffer,IntBuffer,DoubleBuffer,ShortBuffer,LongBuffer)

  #####  四.channel的介绍
  channel表示io源于目标节点代开的链接，channel本身不能存取数据，只能与buffer进行交互。
  java.nio.channels.Channel
  - FileChannel：用于本地数据传输
  - socketChannel：用于tcp中的网络传输客户端
  - ServerSocketChannel：用于tcp(传输控制层协议，是一种面向连接，可靠的，基于字节流的传输层协议，是有确认ack机制的，在发送和接收的时候都会进行校验正确性，可用MD5认证对数据进行加密)网络传输中的服务器端；
  - DatagramChannel：用于UDP（用户数据协议，在网络中他与tcp一样用于处理数据包，是一种无连接的协议。网络系统在内的众多的客户、服务模式的网络应用都需要udp协议
  ，与所熟知的tcp-传输控制协议  一样，UDP协议直接位于ip协议的顶层，UDP与tcp协议都属于传输层协议）中的网络传输；
  - Pipe.SinkChannel;
  - Pipe.SourceChannel;
