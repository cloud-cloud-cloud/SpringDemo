    #### :snail:  socket编程
    ##### 网络基础知识
    - 两台计算机进行通讯需要三个条件：ip 地址，协议，端口号；
    - tcp/ip协议  tcp：transmission control protocol 传输控制协议
       ip：internet protocol 互联网协议
    - tcp/ip五层模型：物理层-网线，双绞线，网卡等，
                     数据链路层，
                     网络层，
                     传输层-tcp/ip，
                     应用层:http ，ftp,smtp,telnet等；
    - ip地址：为实现通讯，不同计算机之间的通信。每台计算机都必须有一个唯一识别的ip地址，32为二进制
    - 端口 区别一台主机上的多个不同应用程序，端口号范围为0~65535。其中0~1023为系统保留
      例如 http：80，ftp:21,telnet:23;
      ip地址+端口号  组成所谓的socket，socket是王令上运行的程序之间的双向通行链路的终结点，是tcp和udp的基础
    - socket套接字：网络上具有唯一标示的ip地址和端口号组合在一起才能构成唯一能识别的标示符的套接字。
      socket原理机制：通信两端都有socket，网络通信其实就是socket间的通信。数据在两个socket之间近想io传输
    - Java中的网络支持：针对网络的通信的层次不同，java提供了不同的api,提供网络通信的四大类
       inetAddress:用于标识网络上的硬件资源，主要是ip地址（通过这个类获取网络中的ip地址，物理信息。InetAddress.getLoclHost()）
       URL:统一资源定位符，通过url可以直接读取或者写入网络上的数据；
               1 //创建一个URL的实例
                2 URL baidu =new URL("http://www.baidu.com");
                3 URL url =new URL(baidu,"/index.html?username=tom#test");//？表示参数，#表示锚点
                4 url.getProtocol();//获取协议
                5 url.getHost();//获取主机
                6 url.getPort();//如果没有指定端口号，根据协议不同使用默认端口。此时getPort()方法的返回值为 -1
                7 url.getPath();//获取文件路径
                8 url.getFile();//文件名，包括文件路径+参数
                9 url.getRef();//相对路径，就是锚点，即#号后面的内容
                10 url.getQuery();//查询字符串，即参数
       sockets：使用tcp协议实现网络通信，socket相关的类；
       Datagram：使用udp协议，将数据保存到用户数据表中，通过网络进行通信。

    ##### tcp编程
       - tcp协议是面向连接的，可高的，有序的，以字节流的方式发送数据，通过三次握手建立连接，形成传输数据的通道，在连接中进行大量数据的传输，效率会稍低
       - java中基于tcp协议实现的网络通讯类 客户端 socket类，服务端serverSocket
       ![](static/images/socket.png)
       - socket 通讯的步骤
       1.创建serverSocket和socket
       2.打开连接到socket的输入/输入流
       3.按照协议对socket进行 读、写操作
       4.关闭输入输出流，关闭socket；
       - 服务器端：
       1.创建serverSocket对象，绑定监听端口
       2.通过accept方法监听客户端请求；
       3.建立连接后，通过输入流读取客户端的请求；
       4.通过输出流向客户端发送信息；
       5.关闭相关资源；
       - 客户端
       1.创建socket对象，指明需要连接的服务器的地址和端口号
       2.连接创建后，通过输出流向服务器发送请求信息；
       3.通过输入流获取服务端的信息；
       4.关闭资源；
     - 应用多线程实现服务器与多客户端之间的通信
       1. 服务器端创建serverSocket ,循环调用accept方法，等待客户端连接
       2.客户端创建一个socket请求和服务器端连接；
       3.服务器端接受客户端请求，创建socket与该客户创立的专线连接
       4.建立连接的两个socket在一个单独的线程对话；
       5.服务器端等待新的连接；
   ##### UDP编程
   udp(用户数据报协议)是无连接的，不可靠的，无序，速度快。
   进行数据传输时，首先将要传输的数据定义成数据报（dataGram），大小限制到64k,在数据报中明确数据索要达到的socket（主机地址和端口号），
   然后再将数据报发送出去
   DatagramPacket类：表示数据报包；
   datagramSocket类：进行端到端的通信的类
   1、服务器端实现步骤
              ① 创建DatagramSocket，指定端口号
              ② 创建DatagramPacket
              ③ 接受客户端发送的数据信息
              ④ 读取数据
   2、客户端实现步骤

              ① 定义发送信息
              ② 创建DatagramPacket，包含将要发送的信息
              ③ 创建DatagramSocket
              ④ 发送数据

    ##### 注意问题
    - 多线程优先问题，根据实际经验，适当的降低优先级，否则可能会有程序运行效率低的情况；
    - 是否关闭输入和输初流，对于同一个socket，如果关闭了输出流，则与该输出流关联的socket也被关闭，所以一般直接关闭socket；
    - 使用tcp通信传输对象，io中序列化部分；
    - socket编程传递文件，io流部分；



