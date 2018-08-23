#### :snail: jvm工作机制和原理
 #####  jvm类加载机制
  类加载系统不仅负责定位和加载类，严格按照以下步骤做了很多事情：
  - 加载：寻找并导入class二进制信息；
  - 连接：进行验证，准备和解析（验证：确保导入的类型是正确性；准备：为类型分配内存并初始化默认值； 解析：为字符引用解析为直接引用）
  - 初始化：调用java代码，将初始值赋予指定值；

  #####  方法区
  （线程共享，存储类加载信息，常量，静态变量等）
  在jvm中类的信息和类的静态变量都是存放在方法区里面，类的信息是由类加载器在类加载的国中中从类文件中提取出来的信息；
   需要注意的是的常量池也是放在方法区里面的。程序中的所有线程多是公用一个方法区的，所有在访问方法区的信息的时候要保证线程的安全。如果有两个线程
   同时访问一个类的时候，只能有一个线程加载这个类，另一个线程需要等待。
   在程序运行时候，方法区的大小是可以改变的，程序在运行时可以扩展。
   方法区也可以被垃圾回收器回收，不过要在该类没有任何应用的情况下，才能被回收；

   jvm常量池也是方法区的一部分；
  #####  类信息包含
  - 类的全名；
  - 类的父类的全名；
  - 该类是一个类或者是一个接口
  - 该类的修饰符（public private protected static ，final， volatile每次都先读取这个变量的值，而不是使用保存在内存的值，任何再写，  transient-修饰的变量不参与实例化）
  - 改类的所有的父接口列表
  - 类的字段信息；
  - 类的方法信息；
  - 所有的静态类变量；
  - 一个指向类加载器的引用；
  - 一个指向class类的引用；
  - 基本类型的常量池；
  （方法去：类型信息，字段信息，方法信息，其他信息）

  #####  方法列表
  为了更高效的访问方法区里面的数据，在方法区中，除了保存上面类的相关信息外，还有一个为了加快存取速度的而设计的数据结构：
   方法列表。每个被加载的非抽象类，java虚拟机都会为他们产生一个方法列表，这个列表保存了这个类可能调用的所有实例方法的引用，父类
   中的调用方法；

   ##### java堆（jvm堆，heap）
  （线程共享，生命周期和虚拟机相同，保存对象实例）
   当java创建一个类的对象或者数组时，都在堆里面为新的对象分配内存，虚拟机中只有一个堆，所有的线程都共享它；
   堆占用的内存空间最多。堆的存取类型是管道类型：先进先出（扩展：队列是先进先出的线性表  ，可用用单链表与数组实现队列先进先出的数据结构 ）；
   在程序运行中，可以动态的分配堆内存的大小；
   堆的内存资源回收是交给jvm gc进行管理；
   - 堆的内存由-xms指定，默认是物理内存的1/64，最大的内存由-Xmx指定，默认是物理的内存的1/4；
   - 默认的空余的堆内存小于40%时候，就回增大，知道-Xmx设置的内存，具体的比例可以由-XX:MinheapFreeRatio指定
   - 空余的内存大于70%时候，就回减少内存，知道-Xms设置的大小，具体有-XX:MaxheapfreeRatio指定；


  #####  java栈（jvm栈，stack）
  （线程私有，使用一段连续的内存空间，存储局部变量表，动态链接，方法出口，操作栈，）
  - 线程私有，生命周期与线程相同
  - 存储基本数据类型
  - 存储引用
  - 存储线程信息
  在java中的栈中只保存基础数据类型（整型-byte short ,int long;浮点型-float ,double；字符型-char
   布尔型-boolean,他们对于的封装类型是Byte,Short ,Integer，Long，Character,Boolean 封装类型是放在堆中 ），
   栈的存取类型类似于水杯，先进后出；
   （ArrayList底层的数据结构使用的是数组结构 顺序结构线性表：特点是查询快，但是增删满
     LinkedList链表结构：查询满，增删快）
     栈内的数据在超出作用域后，后被自动释放掉，它不由jvm gc管理；
     每个线程都包含一个栈区，每个栈中的数据都是私有的，其他栈不能访问；

 #####  程序计数器
 （线程私有，占用内存小，字节码行号）
在jvm概念模型中，字节码解释器工作时就是通过改变计数器的值来选取下一条需要执行的字节码指令。分支，循环，跳转，异常处理，线程恢复等功能都需要依赖这个计数器完成。
     jvm的多线程是通过线程轮流切换并分配处理器执行的时间方式来实现的。为了各条线程之间的切换后计数器能恢复到正确的位置执行，
     所以每条线程都有一个单独的程序计数器。
     程序计数器仅占很小的一块内存空间。
     当线程正在执行一个java方法，程序计数器记录的是正在执行的jvm字节码的地址，如果正在执行的是一个nativefangf ,
     那么这个计数器的值为 空underfined
     程序计数器这个内存区域是唯一一个在jvm规范中没有规定任何outofmemoryError的区域


 ##### 堆和栈分开设计是为了什么？
 - 栈存储了处理逻辑，堆存储了具体的数据，这样隔离设计更为清晰；
 - 堆和栈分离，使得堆可以被多个栈（线程）共享；
 - 栈保存了上下文信息，因此只能向上增长，而堆是动态分配的。
 栈的大小可以通过-xss设置，如果不足的时候会报java.lang.stackOverflowError异常
 栈区：线程私有，生命周期和线程相同。每个方法执行的时候都会创建一个栈区（stack frame）用于存储局部变量表，操作栈，动态链接，方法出口。
 堆：存储对象实例，所有的对象的内存都是在这里分配的，垃圾回收主要在堆里面。


 ##### jvm执行引擎
 java虚拟机相当于一台虚拟的“物理机”，这两种机器都有代码执行能力，其区别主要是物理机执引擎是直接建立在处理器，硬件，指令集和操作系统层面上的。
     而jvm的执行引擎是自己实现的，因此程序员可以自己制定指令集和执行引擎的结构体系，因此能够执行那些不被硬件直接支持的指令集格式；
     在jvm规范中制定了虚拟机字码执行引擎的概念模型，这个模型称之为jvm执行引擎的统一外观。jvm实现中，可能有两种方式：解释执行
     和编译执行（通过即时编译器产生本地代码）。有些虚拟机只采用一种方式，有些则可能采用两种，甚至有坑内包含几个不同级别的编译器执行引擎。
     输入的是字节码文件，处理过程是等效字节码解析过程，输出的是执行结果。这三点每个jvm执行引擎都是一致的。
     
 ##### JNI(JAVA NATIVE INTERFACE本地方法接口)
  它提供了若干的api实现了java和其他语言的通信（主要是c和c++）
     一些旧的库的使用已经用c语言写好了，若干要移植到java上面非常耗时间，而jni可以支持java和c语言编写的库进行交互。
     JNI的使用是java程序失去了java的平台的两大优点：可跨平台； 程序不在是绝对的安全

 ##### JVM的结构体系
 ![](static/images/jvm_structure.png)

 ##### jvm gc垃圾回收机制
 stop-the-word ，意味着jvm在需要执行gc而停止应用程序的执行。当stop-the-word发生时，除gc所需的线程外，所以的线程都进入等待状态，直到gc任务完成。
     GC的优化很多时候就是减少stop-the-word
     JVM GC只回收堆去和方法区内的对象，而栈内的对象，在超过作用域的时候会自动被JVM 自动释放掉，不在JVM GC的管理范围；

###### JVM GC判断对象可以被回收
 - 对象没有被引用；
 - 作用域发生未捕获的异常；程序在作用域正常执行完毕； 程序执行system.exit();
 - 程序被意外终止（被杀线程等）；
 - 在java程序中不能显示的分配和注销缓存，因为这些事情jvm gc帮我们做了； 有些时候我们可以将相关对象设置为null.来显示的清除缓存，单是并不是设置为null就回一定被标记
  有可能會發生逃逸
 - 將對象設置為null至少沒有什麼坏处，但是使用system.gc（）便不可取，使用systm.gc()，并不是马上执行gc,而是会等待一段时间，甚至不执行，执行system.gc，会执行full gc 这会非常影响性能。

###### JVM GC什么时候执行
   当Eden去空间不足存储新对象的时候，执行Minro GC。升到老年代的对象待遇老年代剩余的空间的时候执行FULL GC
   小于的时候被HandlePromotionFailure 参加强制FULL GC.调优主要是减少 full gc的触发次数，可以通过NewRatio控制新生代转换老年代的比例

##### JVM 常量池
jvm常量池也称之为运行时常量池，它是方法区的一部分。用于存放编译期间生产的各种字符量和符号引用（常量池里面存储的是对象的引用，而不是对象本身）。

##### 如果一个在类进行加载的时候如果加载的类不是需要的目标类，但是类名称是一样的，会报什么错



