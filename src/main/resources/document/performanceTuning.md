#### :snail: 性能调优

    ##### 一般你的调优会涉及到哪些层面？
    对应调优一般就是三个过程：
    - 性能监控：问题没有发生，并不知道需要调优什么，此时需要一些系统，应用监控工具来发现问题；jmeter用于压力和性能测试（jprofiler用于jvm 检查和跟踪系统 ，
    jstack是jvm自带的一种堆栈跟踪工具 jstack -L PID）
    - 性能分析： 问题发生后，并不知道问题出在哪里，此时需要使用工具，经验对系统，应用进行瓶颈分析，以求定位到问题的原因；
    - 性能调优：经过上一步定位到问题所在，需要对问题进行解决，使用代码，配置等手段进行优化

    **定位性能瓶颈**
   - 查看操作系统负载，查看操作系统的CPU利用率，内存使用率，操作系统的io，还有网络的io，网络链接数。
    如果cup利用率不高，但是系统的throughput和latency上不去，这说明程序没有忙于计算，而是忙着其他事，比如io。
    一般来说CPU利用率和io利用率是反着来的，关于io我们要看三个事，一个是磁盘文件io，一个是驱动程序io（如网卡），一个是内存换页率。
    这三个事会影响系统性能。
    - 然后我们看下网络的宽带使用情况。
    - 如果CPU不高，io不高，内存使用率不高，网络带宽使用不高，但是系统的性能伤不起，这说明程序有问题，比如说程序阻塞了，可能是因为等某个锁，或者某个资源，或者切换上下文。
    - 我们可以利用java的profiler测试程序瓶颈，可以让你查看程序中各个模块函数甚至指令的很多东西，如：运行时间，调用次数，CPU利用率等等。

      **性能调优**
      一般来说性能调优有以下几个策略：
      - 用空间换取时间。eg cache,
      - 用时间换空间。有时候，少量的空间可能性能会更好，比如网络传输。如果有一些压缩算法，这样的算法其实很耗时，但是因为瓶颈在网络传输上，所以用时间换取空间反而更省时间。
      - 简化代码：最高效的程序就是不执行任何代码程序，所以代码越少性能越高；如：减少循环的层数，减少递归，在循环中少声明变量。
      少做分配和释放内存的操作，尽量将循环体内的表达式抽到循环体外，条件表达中的多个条件判断的次序，尽量在程序启动时把一些东西准备好，
      注意函数调用的开销（栈上的开销），注意面向对象语言中的临时对象的开销，小心使用异常。
      - 并行处理。如果CPU只有一个核，你去做多线程处理，反而会反应更慢，因为操作系统调度和切换开销很大。CPU多核才能真正体现多线程的优势。
      并行处理需要我们的程序scalability（可扩展性），不能水平或者垂直扩展的程序无法进行并行处理。
    1.算法调优：例如说生成月报表，如果一次性统计那么可能耗时一天，如果我们把每天的统计追加到上一天，这样就大大减少了耗时，这种分而治之的方法堆大数据有很大的帮助就像
    merge排序一样。生气了语句和数据库性能优化也是这一策略，如使用嵌套式的select而不是笛卡尔积的select，使用视图等等。
    2.代码调优；
    **字符串操作**，这是最费系统性能的事，最需要是注意的是字符串匹配。所用能用整型的就用整型。
    **多线程调优** ，多线程的性能瓶颈在于互斥和同步锁上，以及线程切换上线文的成本，怎样少用锁或者不用说是根本，例如说使用乐观锁。
   此外，在读写锁也可以解决大多数读操作并发的性能问题。另外不是说线程越多也好，线程之间的调度和切换上下文是一个很耗时的事情，竟可能在一个线程做，竟可能不要同步线程，这样有很多性能问题。
    **内存分配**,当内存出现碎片的时候系统调用非常耗时。池化技术，如线程池，连接池等。池化技术对一些短作业来说（如HTTP服务）相当有效，。这样可以减少链接建立，线程创建的开销，从而提高性能。
    **异步操作**，文件操作有block，non-block。如果我们的程序是同步的话是非常影响性能的。我们可以改为异步的，但是改为异步会使其程序变得更复杂。一般异步要通过队列，要注意队列的性能问题，另外，异步下的状态通知是一个问题。比如消息时间通知方式，有callback方式
    等。这些方式同样可能影响性能，但是 通常来说，异常操作会让性能的吞吐率得到很大的提升，但是会系统系统的响应时间。
    **语言和代码库**，熟悉语言及其所使用的函数库和类库的性能。例如说empty和size()=0这两个复杂度一个是o(1)一个是O(1),
    还要注意jvm调优需要使用那些参数，jvm gc,尤其是full  gc，它运行的时候这个世界的世界都停止了。
    3.数据库调优（
        数据冗余，
        数据镜像--replication,数据库镜像带来的好处就是可以做负载均衡，把一台数据库的负载均衡的分布到多台，同时又保证数据的一致性。重要的是高可用性，一台挂了，另外一台可以可以继续。
        数据分区：将一些非必须的字段另外放在一张表，同一个商品的库存值可以分配到10张表里面，然后负载均衡，
        把数据按照某种逻辑来分配，例如说火车订票系统可以按照车型分，可以按始发地，目的地划分。）
        - 多表查询主要有三个关键字，join ，exists,in.尽量减少嵌套。
        创建索引，分表
        - 字符串 查询尽可能减少使用字符串，尽可能用工位，时间来替代等。
        - 查询部分结果集 mysql 里面的limit ，oracle里面的rowNum,
        - 不要用select *
        - 全文检索，千万不要用like之类的做全文检索，如果要用全文检索，可以尝试使用Sphinx（全文检索引擎）
        - JOIN操作。有人说，Join表的顺序会影响性能，只要Join的结果集是一样，性能和join的次序无关。
         因为后台的数据库引擎会帮我们优化的。Join有三种实现算法，嵌套循环，排序归并，和Hash式的Join。（MySQL只支持第一种）
          嵌套循环，就好像是我们常见的多重嵌套循环。注意，前面的索引说过，**数据库的索引查找算法用的是B-Tree，这是O(log(n))的算法**，所以，整个算法复法度应该是O(log(n)) * O(log(m)) 这样的。
          Hash式的Join，主要解决嵌套循环的O(log(n))的复杂，使用一个临时的hash表来标记。
          排序归并，意思是两个表按照查询字段排好序，然后再合并。当然，索引字段一般是排好序的。
        -  不要用Having，因为其要遍历所有的记录。性能差得不能再差。
           尽可能地使用UNION ALL  取代  UNION。
           索引过多，insert和delete就会越慢。而update如果update多数索引，也会慢，但是如果只update一个，则只会影响一个索引表

    4.网络调优：TCP ,UDP，网卡 调优。tcp的链接是有很多开销的。一个是会占用文件描述符，另一个会开缓存，一般来说一个系统支持tcp的链接是有限的。
    需要注意的的一个参数keepalive,这个是定义一个世界，如果在链接上没有数据传输，系统会在这个时间传输一个包，如果没有收到响应，就会认为tcp链接是断的，会把链接关闭，这样可以回收系统资源的开销。
  对于像HTTP这样的短链接，设置一个1-2分钟的keepalive非常重要。这可以在一定程度上防止DoS攻击。有下面几个参数（下面这些参数的值仅供参考）：
  net.ipv4.tcp_keepalive_probes = 5
  net.ipv4.tcp_keepalive_intvl = 20
  net.ipv4.tcp_fin_timeout = 30

    5.系统调优:系统性能主要包含两个值(吞吐量Throughput：每秒可以处理的请求书，事务数；  系统延迟Latency：也就是系统在处理请求或者事务时的延迟； 两者
    之间的关系是 throughput越大，latency越差，请求量过大，系统繁忙，响应速度自然慢。latency 越好，throughput越大。)
    可以用jmeter,loadrunner等来制造高强度的throughput。
    - io模型：在java7发布新增了nio
    - 多核CPU调优
    - 文件系统的调优：固态硬盘的读速度超快。为了让文件系统有最大的性能，首先需要分配足够的内存空间，


    ##### 数据库调优时你有没有做过执行计划的分析 ？
    ###### 说一下执行计划看哪几个关键点？
       sql优化的大概流程：
       - 定位执行效率低的sql.
       - 分析为什么这段sql执行效率低（explain）
       - 根据第二步分析的结构采取优化措施
       eg  explain  select t.name from student t
       执行计划中的：id 是以为数字，表示执行select羽箭的顺序，id值大的先执行，相等，在上面的先执行。
       select_type:对应行是简单的还是复杂的select,例如说simple不包含子查询或者union操作查询。
       如果包含任何子查询那么最外层的查询则被标记显示值为primary
       table: 输出行所在的表名称。
       partitions:对应分区表，查询显示分区的id,对于非分区表显示NULL;
       TYPE:例如说system，当表只有一行时使用，表中有且只有一个匹配行时使用，如对主键或唯一索引 查询。
       EXTRY:
       POSSIBLE_KEY:指出mysql能使用的索引来优化查询
       KEY:查询优化器查询实际所使用的的索引，如果没有可用的索引，值为NULL.
       KEY_LEN:表示索引的最大可能长度，这个值根据字段定义计算而来。
       REF:表示那些列或常用量被用于查找索引列上的值。
       ROWS:表示MySQL通过哪些列或常量被用于查找索引列上的值，ROWS值的大小是个统计抽样结果，并不十分准确
       FILTERED:表示返回结果的行数占需读取行数的百分比，Filter列的值越大越好

    ###### hash join, nested loop是什么？区别？
       多表连接。
      -  hash join 优化器使用两个表中较小的表和连接键（join key ）在内存中建立散列表，将列数据存储到hash列表中，然后扫描较大的表，同样对JOIN KEY进行HASH后探测散列表，找出与散列表匹配的行 因为存在当表太大的时候需要分成若干个partition，写入次胖的temporary segment，则会多一个写的代价，会降低效率，所以适合小表。
      -  merge join是先将关联表进行各自的排序，然后从各自的排序表中抽取数据，到另一个排序表中做匹配。
      因为marge join 需要做很多的排序，所以耗费的资源更多。相比较 hash join.
      - nested loop 嵌套循环连接：它的工作方式是循环从一张表读取数据，然后访问另外一张表（被茶盅表 inner table，通常有索引），
      驱动表中的每一行与inner 表中的相应记录join。类似一个嵌套的循环。
      对于被连接的数据子集较小的情况，嵌套循环是一个比较好的选择。适用于驱动表记录集<10000
      而且inner表需要有效的访问方法（index）,并且索引选择性较好的时候。
    #####  j2ee app server有没有做过调优？
    tomcat调优的几个方面：
    - 增加jvm堆内存的大小
    - 修复jre内存泄漏 监听<Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener"/>
    - 线程池的设置:线程池指定Web请求负载的数量，因此，为获得更好的性能这部分应小心处理。可以通过调整连接器属性“maxThreads”完成设置。maxThreads的值应该根据流量的大小，如果值过低，将有没有足够的线程来处理所有的请求，请求将进入等待状态，只有当一个的处理线程释放后才被处理；如果设置的太大，Tomcat的启动将花费更多时间。因此它取决于我们给maxThreads设置一个正确的值。；
      <Connector port="8080"address="localhost"
      maxThreads="250"maxHttpHeaderSize="8192"
      emptySessionPath="true"protocol="HTTP/1.1"
      enableLookups="false"redirectPort="8181"acceptCount="100"
      connectionTimeout="20000"disableUploadTimeout="true"/>
    - 压缩:Tomcat有一个通过在server.xml配置文件中设置压缩的选项。压缩可以在connector像如下设置中完成，
         Java代码

             <Connector port="8080"protocol="HTTP/1.1"
             connectionTimeout="20000"
             redirectPort="8181"compression="500"
             compressableMimeType="text/html,text/xml,text/plain,application/octet-stream"/>

         <Connector port="8080" protocol="HTTP/1.1"connectionTimeout="20000"redirectPort="8181" compression="500"compressableMimeType="text/html,text/xml,text/plain,application/octet-stream" />

         在前面的配置中，当文件的大小大于等于500bytes时才会压缩。如果当文件达到了大小但是却没有被压缩，那么设置属性compression="on"。否则Tomcat默认设置是“off”。接下来我们将看看如何调优数据库。
    - 数据库性能调优：tomcat在等待数据库查询被执行期间性能会降低。例如说我们给数据库设置合理等待等待时间，最大的连接数。
    - tomcat原生库：tomcat原生库基于Apache可移植运行时，给程序员提供了超强的扩展性和性能。
    - 其他项：例如说开启浏览器的缓存，开机的时候启动tomcat
    ###### 调优时你一般会调整哪些参数？
    -Xms – 指定初始化时化的栈内存
    -Xmx – 指定最大栈内存
    -xss 设置每个线程的堆栈大小

    ###### JVM参数你会去调整吗？比如说：-XSS， -SERVER， -d64， --XX:MaxTenuringThreshold=31 -XX:+CMSParallelRemarkEnabled
    当前仅有java hotspot server VM支持64位模式。选择 "-server"选项必须使用-d64；"-client"选项会忽略使用-d64；如果没有指定-d32或者-d64，则默认运行在32位模式。除非仅有64位系统。

    目前在windows平台下jdk1.5,JDK1.6不支持-d32,-d64的选项，jdk1.7支持-d32,-d64的选项
    在linux平台下jdk1.5, 1.6, 1.7均支持-d32,-d64的选项

   --XX:MaxTenuringThreshold=31 虚拟机要求对象年龄达到多大的时候转为老年代。
   -XX:+CMSParallelRemarkEnabled 降低标记停顿
   CMS(content manage system内存管理系统)

    ##### javascript压缩技术有没有使用过？ 前台压缩？用的什么框架？
    一般采用gzip压缩，我们一般采用设置tomcat里面是server.xml配置文件


    ###### 后台压缩？ 用的什么框架？
     一般压缩为gzip,

    #####  LINUX下磁盘IO读写效率如何查看？
    下载安装iotop  ,

   密令  iotop 查看io进行；
   查看状态   sar -b 1 10   /  iostat -x 1 10 查看 从当前时间的1-10秒之间的io状态情况

    ##### 项目如果布署在Linux上一般碰到过什么坑？
   1. liunx默认的jdk版本与开放版本不同，要进行调整。

   2.获取文件的路径不同。
   3.  包版本不对，没有依赖包，配置错误，内存不够，磁盘不够
   4. tomcat http ssl安装配置。
     <Connector port="8443" protocol="org.apache.coyote.http11.Http11AprProtocol" #配置的tomcat  apr 可以应对高并发
                   maxThreads="150" SSLEnabled="true" >
            <UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
           ** <SSLHostConfig>  # 配置ssl传输内容加密，避免数据在传输过程中被有心人截取获取。
                <Certificate certificateKeyFile="conf/localhost-rsa-key.pem"
                             certificateFile="conf/localhost-rsa-cert.pem"
                             certificateChainFile="conf/localhost-rsa-chain.pem"
                             type="RSA" />
            </SSLHostConfig>
            **
        </Connector>


    #####  你们的项目用的是apache还是nginx？
     我们项目使用nginx控制分流，apache作为服务器（apache+nginx搭建集群）
     安装apache,复制多个，tomcat的server.xml配置文件，以不同的端口打开。
     下载nginx，修改nginx.conf配置文件，将tomcat的访问地址，端口配置进去，例如说nginx访问端口号为80 ，启动nginx。
     然后可以通过nginx 访问tomcat可以直接用80端口号访问者这几个tomcat ，不关心具体访问的是哪个tomcat；


    ###### 回答如果是apache
    ###### apache下如何增大它的吞吐量？
     例如说用这样 C=NL/T 平均的并发量数=(login session *  login session 的长度) / 考察的时间

     F=VU*R/T 吞吐量=（虚拟用户数*请求数）/性能测试用的时间

      并发用户数峰值：C’ ≈ C+3*根号C
      在bin/catalina.bat文件中加入下面参数，对JVM进行优化，至于这一大驼参数的作用及说明。

      set JAVA_OPTS=

      -server  #表示服务状态 特别重要。

      -Xms1000M

      -Xmx1000M   #-Xms与-Xmx设成一样的值，避免JVM因为频繁的GC导致性能大起大落

      -Xss512k

      -XX:+AggressiveOpts

      -XX:+UseBiasedLocking

      -XX:PermSize=64M

      -XX:MaxPermSize=300M

      -XX:+DisableExplicitGC

      -XX:MaxTenuringThreshold=31

      -XX:+UseConcMarkSweepGC

      -XX:+UseParNewGC

      -XX:+CMSParallelRemarkEnabled

      -XX:+UseCMSCompactAtFullCollection

      -XX:LargePageSizeInBytes=128m

      -XX:+UseFastAccessorMethods

      -XX:+UseCMSInitiatingOccupancyOnly  -Djava.awt.headless=true

      如何优化Tomcat配置，提升Tomcat性能。
      上述这样的配置，基本上可以达到：

       1.系统响应时间增快

      2.JVM回收速度增快同时又不影响系统的响应率

      3.JVM内存最大化利用

      4.线程阻塞情况最小化

      Tomcat连接参数的优化，主要是针对吞吐量做优化:修改conf/server.xml文件，把原来

      <Connector port="8080" protocol="HTTP/1.1" />

      修改为：<Connector port="8080" protocol="HTTP/1.1"

      URIEncoding="UTF-8"

       minSpareThreads="25" #该Connector先创建25个线程等待客户请求，每个请求由一个线程负责

       maxSpareThreads="75"

       enableLookups="false"#如果为true，则可以通过调用request.getRemoteHost()进行DNS查询来得到远程客户端的实际主机名，若为false则不进行DNS查询，而是返回其ip地址

      disableUploadTimeout="true"#许Servlet容器，正在执行使用一个较长的连接超时值，以使Servlet有较长的时间来完成它的执行，默认值为false

      connectionTimeout="20000"#定义建立客户连接超时的时间.如果为-1,表示不限制建立客户连接的时间

      acceptCount="300" #指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理

      maxThreads="300"　最大线程数，默认值为200

      maxProcessors="1000"

      minProcessors="5"#服务器启动时创建的处理请求的线程数

      useURIValidationHack="false"

      compression="on"# 是否压缩 默认false

      compressionMinSize="2048"

      compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain"

      redirectPort="8443"/> # 指定服务器正在处理http请求时收到了一个SSL传输请求后重定向的端口号

      如何优化Tomcat配置，提升Tomcat性能。

    ###### apache下有一个dns lookup，它会影响性能吗？
    apache http server是Apache软件基金协会的一个开放源码的网页服务器，可以在windows,linux,unix等操作系统中运行，是最流行的文本服务器软件之一，他的反应速度快，运行效率高，
    但值支持HTML等静态页面。
    apache  tomcat 他除了支持htm静态页面外还支持jsp，servlet。

    ###### apache里的keep alive是干什么用的？
     keepalive 是一个布尔值，on代表打开，off代表关闭，配置指令决定当处理用户发起的http请求后是立即关掉tcp，还是断开连接。
     当为on时，当用户完成一次访问后，不会立即断开，会继续在这一次的tcp连接接下来的请求。这样就不用重复建立新的tcp连接和关闭tcp连接，可以提高用户访问的速度。
     不过打开会占用很大的内存。
     总结：在内存非常充足的服务器上，不管是关闭还是打开，服务器性能不会有明显的变化；
     如果服务器内存较少，或者服务器有非常大量的文件系统访问时，或者主要处理动态网页服务，关闭keepalive后可以节省很多内存，而节省出的内存用于文件系统cache，可以提高文件系统访问的性能，并且系统会更加稳定。

   总Apache进程数= KeepAliveTimeout * 每秒种HTTP请求数/ 平均KeepAlive请求
   Apache占用内存= 总Apache进程数* 平均每进程占用内存数

   基于上面的公式,我们就可以推算出当 平均KeepAlive请求<= KeepAliveTimeout 时,关闭KeepAlive 选项是划算的,否则就可以考虑打开。
    ###### 回答如果是:NG


    ###### NG下如何增大它的吞吐量
    TPS系统吞吐量：每秒处理的消息数；
    理论上讲，如果tcp流同http数据紧密打包在一起，而且数据尽可能快的发送出去，那么我们就回获得最大的吞吐量。
    tcp采用了两个基本原则决定何时发送以及发送多少数据：
    - 流量控制是为了确保接受者可以接收到数据；
    - 拥塞控制是为了管理网络宽带；

    1.请求缓存区在nginx请求中起了重要作用，在接受到请求时候，nginx将其写入缓冲区，。
    这些缓存区的数据可以作为nginx变量使用，例如$request_body.，这样就达到了将请求完整的信息一次性传递给后端这样就避免了断断续续的传递（互联网之间连接速度很慢）。
    2.搭建负载均衡，在搭建多台ng集群时候，需要搭建keepalived检查机器是否存活（keepalived需要依赖oppenssl）
     ** keepalived 原理**
      ！[](static/images/keepalived_nginx.png)

    ###### NG的反向代理有几种？
    反向代理的服务的基本原理：
 ！[](static/images/reverse_proxy.png)
 nginx作为web服务器一个重要功能就是反向代理 ，在nginx.conf里面配置。
 **使用反向代理的作用**
 - 提高访问速度：由于目标主机返回的数据会存放在代理服务器的硬盘中，因此下一次客户再访问相同的站点数据时，会直接从代理服务器的硬盘中读取，起到了缓存的作用，尤其对于热门站点能明显提高请求速度
 - 防火墙作用：由于所有的客户机请求都必须通过代理服务器访问远程站点，因此可在代理服务器上设限，过滤某些不安全信息。
 - 通过代理服务器访问不能直接访问目标站点：互联网上有许多开发的代理服务器，客户机在访问受限时，可通过不受限的代理服务器访问目标站点，通俗说，我们使用的翻墙浏览器就是利用了代理服务器，虽然不能出国，但也可直接访问外网。
 -  可以进行负载均衡（又叫反向代理），将用户请求分配到多个服务器。
 **反向代理的流程**
 - 浏览器通过返回nginx配置的server 地址  例如说 localhost,通过本地host文件名解析，找到对应的服务器（在host配置文件配置 域名对应的ip地址）
 - nginx反向代理接受客户机的请求，找到server_name为localhost 的server节点，根据对应的proxy_pass对饮的http路径，将请求路径转到upstream 上，

 #服务器的集群
 	upstream  netitcast.com {  #服务器集群名字
 	   server 127.0.0.1:8070  weight=1;#服务器配置 weight是权重的意思，权重越大，分配的概率越大。
 	   server 127.0.0.1:8090  weight=2;
 	   server 192.168.1.238:8090 backup;#备份服务器，当上面的服务器都不可用 的时候会启动。
 	}
     server {
         listen       80;
         server_name  localhost;

         #charset koi8-r;

         #access_log  logs/host.access.log  main;

         location / {
             proxy_pass http://netitcast.com;
             proxy_redirect default;
         }

         反向代理常用的方式（负载均衡）
         - 1.轮询（默认）每个请求按照时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除。
         upstream backserver {
             server 192.168.0.14;
             server 192.168.0.15;
         }
         - 2.weight 指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。
         upstream backserver {
             server 192.168.0.14 weight=3;
             server 192.168.0.15 weight=7;
         }

         - 3.ip_hash指令（在负载均衡中，假如某个用户子在某台服务器上面访问登录了，那么该用户第二次请求的时候，因为是负载均衡系统，每次请求都会重新定位到服务器集群中的某一个，这样就 会导致该用户的登录信息可能丢失）
          每个请求按照ip的hash结果进行分配，这样每个访客固定访问后端服务器。
          upstream backserver {
              ip_hash;
              server 192.168.0.14:88;
              server 192.168.0.15:80;
          }
         - 4.fair(第三方)：按照后端服务器的响应时间来分配请求，响应时间短的有限分配。（rabbitmq默认的分配方式是用的fair ,会将第n个message分发给第N个consumer,n是取余，这样就会导致有的consumer很忙，有的则很闲。）
         upstream backserver {
             server server1;
             server server2;
             fair;
         }
         - 5.url_hash ：按照访问url的hash结果进行分配请求，让每个url定向到同一个后端服务器，后端服务器缓存时比较有效。
         upstream backserver {
             server squid1:3128;
             server squid2:3128;
             hash $request_uri;
             hash_method crc32;
         }

       #####  什么叫TOMCAT NATIVE APR？
       APR(Apache portable runtime apche可移植运动库，是apache http服务器的支持库。Tomcat将以JNI的形式调用Apache HTTP服务器的核心动态链接库来处理文件读取或网络传输操作，从而大大地提高Tomcat对静态文件的处理性能。 Tomcat apr也是在Tomcat上运行高并发应用的首选模式。)
       JNI是Java Native Interface的缩写，它提供了若干的API实现了Java和其他语言的通信（主要是C&C++）
       **linux下配置tomcat+apr+native应对高并发**
       在网络比较慢的时候tomcat线程开到300以上，如果不陪APR,基本上很快就用满了，以前的请求就进不来了，配置APR,tomcat将以JNId的形式
       调用apache http 服务器核心动态链接库来处理文件读取或网络传输操作，这时候的并发数就会马上降低到只有几十个，新的请求会无阻碍进来。

       linux 配置tomcat native apr 需要下载tomcat（tomcat/bin 下面包含了tomcat-native 压缩包） ,apr jdk1.7及以上，已经配置环境变量
       配置路径访问地址：https://www.cnblogs.com/zishengY/p/7101616.html?utm_source=itdadao&utm_medium=referral

    ###### JBOSS NATIVE APR用过没？


    #####  有没有做过分区表？
     当表数量很大的时候 ，做查询的时候效率非常低，这个时候如果已经**分表**了，因为数据条数的原因查询还是很慢
     那么就考虑做分区，例如说将1-100条放到A表，101-200放到B表 201-300.。。。放到c表这样
   **分区的sql**
    CREATE PARTITION SCHEME partitionSchemeArea
    AS PARTITION partitionFunArea
    TO (
        Area01,
        Area02,
        Area03,
        Area04)

        **如何将普通表转换为分区表**
        - 1.exprot/import method:导入导出，首先在源库建立分区表，然后将数据导出，然后导入到新建的分表去即可
        1）mysql里面在mysql服务器的进入/bin下面 执行：mysqldump -h 172.29.20.46 -u root -p dac-dev email>D:\email.sql
          导出一张表
        2) 删除表 drop  table email;
        3) 重建分区表定义：create table email(id number(10),name varchar2(12))
                         partition by range (id)(partition p1 values less than (501),
                         partition  p2 values less than (maxvalue));
        4) 利用ignore=y(ignore=y 表示忽略创建错误，继续后面的操作)来导入分区表：imp usr/pswd files=exp.dmp ignore=y
          利用原表重建分区表的特点是：方法简单，采用DML,不会产生undo,且只有少量的redo,效率相对 来说比较高，而且建表完成后数据已经在分布到各个分区中了。
          不足：对于数据的一致性方面还需要额外的考虑，由于几乎没有方法锁定表的方法保证一致性。在执行create  table 语句和
        - 2.insert with a subquery method

        - 3. partition exchange method

         - 4.DBMS_REDEFINITION  dbms_redefinition

         ** UNDO表空间管理**
         undo主要是oracle独有的表空间，撤销（undo）数据是反转DML语句结果所需的信息，撤销数据通常称为回滚数据。以前oracle版本，回滚数据和撤销数据可以交替使用，现在在oracle9i上面管理方式有所不同，功能虽然一样。
         ** Redo**
         REDO是记录日志用的。undo是记录数据的备份用的
         例如说当你update 语句后，oracle会详见更改前后的信息放到redo（当满足一定条件后由日志写入日志文件）
         然后将更新前的数据镜像copy到undo中；
         当用户rollback后，oracle将undo里面的数据覆盖回去。
         用户commit后，oracle可以根据redo的信息进行数据恢复。当然也可以利用undo进行flashback.




    ###### DML
    数据操纵语言（data manipulation language,DML）是sql语言中，负责对数据库对象运行数据访问工作的指令集，
    以insert，update ,delete 三种指令为核心，分别代表出任，修改，删除。是开发以数据为中心的应用程序必定会使用到的指令。因此很多开发人员把加上select的语句的四大指令用“crud”


    ##### 如何玩转千万级别数据
        - 做表分区；
        - 用xml类型代替主从表设计，从而达到提高查询性能。 例如说如果我们做一个会议记录 会涉及到会议信息，参与人员，会议的内容。这样可以划分多个表，不过在查询的时候会做关联查询这样就会影响数据库性能。在修改表数据的时候性能也够呛。
        用xml类型替代主从表设计，这样一个会议的内容存储到一个表中的一条记录中，这样数据看起来更直观。
        获取会议详细信息，只需要我要做的是只需用C#的Linq.Xml来解析查询出来的XML字符串即可。
        修改操作时，只需要重新组合xml数据，update下就更新了会议相关的内容。


    ##### linux目录结构
    ！[](static/images/linux_dir.png)
    /- 根
    etc 配置文件        etc/profile 环境配置文件
    bin 用户二进制文件
    sbin  系统二进制文件
    dev  设备文件
    proc  进行信息
    var 变量文件
    tmp 临时文件
    usr 用户程序
    home   home目录 所有用户用home目录存储他们的个人档案
    boot 加载程序文件
    bin 系统库
    opt 可选的附加应用晨曦
    mnt  挂载目录  临时安置目录，系统管理员可以挂载文件系统
    media 可移动媒体设备
    srv  服务数据

