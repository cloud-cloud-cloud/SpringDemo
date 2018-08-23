#### :snail: java多线程并发
 ##### java的内存模型
 1.volatile关键字，修饰的共享变量，上一次的写入操作结果对下一次的读取操作是肯定是可见的。在写入volatile变量之后，CPU的缓存中的内容会被写回内存；在读取volatile变量时，CPU缓存中的对应内容会被置为失效，重新从主从中读取。
 volatile不使用锁，所以性能优于synchronize关键字。
   在去库存过程中我们将库存加载到内存中，用volatile关键字设置库存内存可见，在多线程避免出现非原子操作。

 2.final关键字：fina关键字声明的的域的值只能被初始化一次，一般在构造方法里面初始化。（在多线程里面，final的域通常用来实现不可变对象）
 当对象中的共享变量的值不可能发生变化的时候，在多线程中也就不需要同步机制来进行处理，故而在线程里面用尽可能使用不可变对象。
 另外在代码执行时候，final域的值可以被保存在寄存器中，而不用从主从中的频繁重新读取。
 3.java基本类型的原子操作：double,long的赋值，引用是分隔的，必须在synchronize或者volatile操作




  #####  java提供线程同步的方式
  - synchronize关键字
  - Object类的wait（），notify,notifyAll()；（在阻塞队列里面的使用 lock,unlock,wait,notify,）
   调用wait(）方法的对象进入等待池，并释放锁。
   notify()从等待池里面捞一个线程放入睡醒的锁池，当当前线程结束后释放掉锁，锁池中的线程即可竞争对象的锁来获取锁的执行机会。

   1.方法sleep()进入阻塞状态，不会释放对象锁（就是说要睡大家一起睡，所以不要让sleep出现在synchronize里面会造成其他等待锁的线程一直处于等待状态）
   2. join（）则是主线程main 等待子线程执行完毕再往下执行；
   3.interrupt（）中断方法
   4.yield（）尝试让出所占有的CPU资源，让其他线程获取运行机会，对操作系统上面的调度器来说是一个信号不一定立即切换线程。（在实际开发中，测试阶段频繁调用yeid方法使线程切换更频繁，从而让一些多线程相关的错误更容易暴露出来）。

  #####  非阻塞方式


  #####  高级同步对象（信号量samaphore类）
  - 信号量一般用来数量有限的资源，每类资源有一个对象的信号量，信号量的值表示资源的可用数。
  在使用信号量时，需要从改信号量上获取成功，成功获取许可，资源的可用数-1，完成对资源的使用，释放资源，可用资源数+1.
  当资源数为0 的时候需要获取资源的线程以阻塞的方式进行等待，或者过段时间来查询是否有资源。
  java.util.current.samaphore 在创建samaphore类的对象时指定资源的可用数。
  1.acquire()以阻塞的方式获取资源许可；
  2.tryAcquire()以非阻塞方式获取资源许可；
  3.release()释放许可；
  4.AcquireUninteruptibly()，acquire（）方法获取许可的过程中可以被中断，然后不希望被中断使用该方法。
  - 倒数闸门CountDownLatch
  在多线程中一个线程完成任务下一个线程才能继续进行。利用该类指定等待的任务数，当一个任务完成时候调用countDown();

  - 循环屏障（CyclicBarrier）
  循环屏障的作用类似倒数闸门，不过不像countdownLatch是一次性的，他可以循环使用。另外，线程之间是互相平等的。例如说一个线程完成任务，另外一个线程也完成任务，当所有线程都完成某项任务或者各自的任务后，再继续进行下面的任务（类似我们玩游戏升级，多个玩家都要同时做某项任务后，才能进行下一项任务）

  - 对象交换器（Exchanger）
  java.util.current.exchanger 一个线程执行完成后，将结果交给另外一个线程执行

   ##### 数据结构（多线程程序使用高性能的数据结构）
         currentHashMap


##### extend Thread和implements Runnable的区别及使用
  这个就是继承和接口的区别，


###### 为什么我们调用start()方法时会执行run()方法，为什么我们不能直接调用run()方法？
     因为调用start方法等于说线程开启，等待CPU资源，当有资源后就就回调用run方法运行，因为如果直接调用run方法等于说主线程执行run方法，没有开启线程等于说这个run方法还是主线程的一个普通方法。

    ###### extend Thread和implements Runnable的区别及使用
    这个其实就是继承和实现接口的区别
    - 实现接口可以另外再继承其他类，继承只能继承一个类
    - 实现接口的编程会稍微复杂点，如果需要访问当前线程需要 Thread.cuurrentThread()，继承线程，只需要用this即可获取当前线程
    ###### 为什么我们调用start()方法时会执行run()方法，为什么我们不能直接调用run()方法？
         因为如果我们直接调用子线程的run方法，那么这个方法其实还是运行在主线程中的，我们只有在子线程开始后，才能子线程才能执行run方法。

    ###### 使用匿名类来运行线程


    ###### 如何得知一个线程运行完毕？
   run()函数内容执行完毕，或者用isAlive()来判断；

    ###### 我需要有一个变量用于记录某个线程运行从开始到结束一共用了多少时间？


    ###### 线程锁的问答：synchronized和lock的使用（必须2个都知道）


    ###### synchronized的用法


    ###### synchronized method


    ###### synchronized static method是怎么样？和synchronized method的区别在哪？
        synchronized method 的同步对象是指类实例对象，

        synchronized static method 的同步对象指类本身，也就是所有对象共享这个同步对象。

    ###### synchronized{}段

    ###### synchronized(obj)

    ###### 这边这个synchronized(obj)里的obj是个什么东西？
    当前对象


    ###### lock怎么用？lock(obj)时这个obj怎么声明你认为最好？

    ###### future有没有用过？Lazy Future？ Promise？可复用的Future？ Callable是什么，和Future的区别？
      - lazy future:与一般的future不同，lazy future在创建之初不会准备引用的对象，而是等到请求对象时才开始工作。
      本身并不是为了实现并发，而是节约不必要的运算资源为出发点。例如设计某些API的时候需要回一组信息，但是其中某些信息的计算会耗费可观的资源，单是的调用者不一定关系所有的这些信息。
      因此那些需要耗费比较多资源的原始以lazy future形式提供，节约资源。lazy future可以避免过早的引用或锁定资源而产生的不必要的互斥。
      - promise 可以看作是future的一个特殊分支，常见的future一般是由服务调用者致敬触发的异步处理流程（这就像淘宝的订阅生产者和消费者，消费者去主动订阅而消费），promise显式表示那些异步流程并不直接由服务器触发情景。
       promise接口多一个set()或fulfill()接口。
      - 可复用的future：常用的future是一次性 的，当获取异步的结果，就失去了意义，但是经过特殊处理的future也可以实现复用
      ，这对于可多次变更的数据显得非常有用。例如淘宝的分布式订阅框架提供的future，它允许多次调用waitnext()方法（相当于future.get()方法），每次调用是否阻塞取决于是否又有数据发布，或者更新，如果没有数据更新，那么阻塞移植到
      下一次数据发布。
      -  callable与future都可以获取线程执行的结果，callable一般是与ExecutorService配合使用。
        future就是对具体的runnable或callable任务的执行结果进行取消，查询是否完成，获取结构。必要时可以通过get方法获取执行结构，该方法是阻塞的直到任务返回结果。
         callable定义的方法是call，

    ###### jdk1.5后新增的ConCurrent用过吗？
         java.util.concurrent

    ###### 什么是Executors框架？
      ThreadPoolExecutors框架的一个主要实现类，是执行具体任务的实现类。该实现类有executor中的工厂类executors提供的静态工厂方法负责创建。


    ###### 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？


    ##### Subtopic


    ##### ConcurrentHashMap
  - 利用的锁分段技术增加了锁的数目，从而使争夺一般锁的线程数目得到有效的工作。hashmap在并发线程中使用可能导致死循环，因为插入的过程不是原子操作，
   每个hashentry是一个链表结构，很可能在插入的过程中，已经设置了后节点，实际还未插入最终反而插入到后节点之后，造成了
   链中出现了环，破坏了链表的性质，失去了为节点，出现死循环。
   hashtable因为内部采用了synchronize来保证线程的安全，但是在线程竞争激励的情况下hashtable的效率下降很快，多个线程竞争一个锁导致大量线程处于轮询或者阻塞。

   - concurrenthashmap 的get方法是无需要加锁的，因为用到的共享变量都采用了volatile关键字，保证了共享变量在线程之间的可见性
   - concurrentHashMap 是由segment数组和hashentry数组组成。segment是重入锁（ReentranLock）,作为一个数据段的竞争锁，每个hashentry一个链表结构的元素，
   利用hash算法得到索引确定归属的数据段，也就是对应到修改时需要竞争获取的锁。（eg:读取配置文件的时候，因为配置内容是共享的，为了线程安全且减少线程之间的竞争，可以用concurrentHashMap来读取内容）

   - segment的put操作是加锁的，在插入的时候先判断segment里的hashentry数组是否会超过容量（threshold），如果超过需要对数组进行扩容，翻一倍。然后
   在新的数组中重新hash,为了高效，concurrentHashMap只会对需要扩容的单个segment进行扩容。

    ##### Thread.start ()与 Thread.run ()有什么区别？
     - start启动线程，这个时候线程处于就绪状态，一旦得到cup时间片，就开始执行run方法，run方法称为线程提，包含 了这个线程需要执行的内容，run方法执行结束，这个线程也随之结束。
     - run方法只是一个类的普通方法，直接调用run，程序中依然只有一个主线程这一个线程，其程序执行还是需要顺序执行，run方法执行完毕往下执行。
        **总结**：调用start方法是可以启动线程，而run方法只是thread的一个普通的调用，还是在主线程里面执行。把需要并行处理的代码放在run方法中，start方法启动线程将自动调用run方法，这是由jvm
        的内存机制规定的。并且run方法必须是public访问权限，访问值类型为void
    ##### 什么是线程池（thread pool）
        线程池就是提前创建若干个线程，若干有任务需要处理，线程池里面的线程就回处理任务，处理完后线程不被销毁，而是放到线程池里面等待下一个任务。

    ###### 常用的线程池有哪些？
    newFixedThreadPool定长线程池. newScheduledThreadPool定时延迟执行线程池,new CachedThreadPool线程池的大小基本上没有限制，因为很大.newSingleThreadPool默认长度线程池

    ###### 如果回答出了至少fixedThreadPool(常用, SingleThreadExecutor的话进而问:

    ###### newSingleThreadExecutor用法
    创建一个单线程化的executor，即只创建唯一的一个工作线程来执行任务，它只会用唯一的工作线程来执行人，保证所有的任务执行按照指定的顺序（FIFO,LIFO,优先级）
    执行，如果这个线程异常结束，会有另外一个取代它，保准顺序执行。单工作线程最大的特定是可保证顺序的执行各个任务，并且在任意给定的时间不会有多个线程是活动的。。

    ###### newFixedThreadPool用法
     创建一个指定工作线程数量的线程池。每当提交一个任务的时候就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，将提交的任务存入到池队列中。
     FixedThreadPool是一个典型的优秀线程池，它具有线程池提高程序效率和创建线程时所耗费的开销的优点，单是在线程池空闲的时候
     当线程池中没有课可运行的任务的时候，它不会释放工作线程，这会占用系统一定的资源。这点相比于newCachedThreadPool是有一定缺陷；


    ###### newScheduledThreadPool用法
     创建一个定长的线程池，而且支持定时以及周期性的任务执行。

    ###### newCachedThreadPool用法
    创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收的，则新建线程。
    这列线程池的特定：
    - 工作线程的创建数量几乎没有限制（其实也有限制，是Integer.max_value），这样可以灵活的创建线程池中添加线程。
    - 如果长时间没有往线程池中提交任务，如果工作线程空闲了指定的时间，那么该工作线程将自动终止，终止后，如果你又提交了新的任务，那么线程池会重新创建一个姓新的工作线程；
    - 在使用cachedThreadPool时候，一定要主要控制任务的数量，否则，由于大量线程同时运行，会造成系统瘫痪。




     newSingleThreadpool
    ###### 现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行？


    ###### 你将如何使用thread dump？你将如何分析Thread dump？
        thread dump 是非常有用的诊断java的应用问题工具，每一个java虚拟机都有及时生成所有线程的在某一点状态的thread-dump的能力，各个java虚拟机都提供了当前活动线程的快照，虽然微有不同。
        当前活动的快照，及jvm中所有java线程的堆栈跟踪信息，堆栈信息一般包含了完整的类名及所执行的方法，如果可能的话
        还有源码的行数。它的特点：
        - 能在各种操作系统下使用
        - 能在各种java应用服务器上使用；
        - 可以在生产环境下使用不影响性能；
        - 可以将问题直接定位到应用程序的代码行上；
        **thread-dump能诊断的问题：**
        - 查找内存泄漏，常见的程序里load大量数据到缓存；
        - 发现死锁的线程；
        **thread dump 最常用的两种方式**
        thread dump能帮助我们判断CPU峰值，死锁，内存异常，应用反应迟钝，响应时间变长和其他系统问题。
        - jstack :是一个抓取thread dump文件有效的命令行工具，位于jdk目录bin文件，抓取的密令：jstack -l <pid> > <file-path>
           eg: jstack -l 37320 > /opt/tmp/threadDump.txt

        -  kill -3 ：处于安全考虑，有一部分生产机器的环境值包含jre环境，因此就不能使用jstack工具了。这种情况使用kill -3； kill -3 <pid>;

      ##### volatile与用synchronize比有什么区别
      java的jvm内存模型示意图：
      ![](static/images/java_jvm_memory.png)
      - volatile的操作是非原子性的(读取，修改，将值写回内存)，volatile主要用于在多个线程中感知实例变量的更改的场合，从而使得各个线程获得最新的值，他强制线程每次从主内存中读到变量，而不是从线程的私有内存读取变量，从而保证了数据的可见性。
      - volatile是轻量级的，只能修饰变量，synchronize重量级，还可以修饰方法。
      - volatile只能保证数据的可见性，不能用来同步，因为多个线程并发访问volatile修饰的的变量不会阻塞。
      synchronize不仅保证可见性，而且保证原子性，因为，只有获得锁的线程才能进入临界区，从而保证临界区的所有语句都全部执行，
      多个线程争抢synchronize锁对象时，会出现阻塞。
      线程的安全性保函两个方面 ：可见性，原子性。所有对于volatile并不能保证线程安全性，，
      而synchronize则可实现线程的安全性。

