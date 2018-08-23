#### :snail:Spring  boot
##### spring boot的简介
 spring主要是减少配置，尽快的启动项目
 利用@springBootApplication启动标签（他是一个组合标签包含了@configuration @EnableAutoConfiguration,@ComponentScan指明对象扫描访问），里面有个main方法快速启动项目。
 使用application.properties或者application.yml放置到resouce目录下面
 它支持yaml语言配置文件，yaml是以数据为中心的，在拍照数据的时候具有面对对象的特征。
 例如说在properties里面 ：server.port=8000 server.servlet.context-path=/hello
 在yml里面配置为
 server
 port: 8000
 servlet:
   context-path: /hellp
   文件与yml区别在需要在：后面加一个空格
   另外yml可以在bean里面添加注解,添加属性前缀，避免写的很繁琐@ConfigurationProperties(prefix="test")


   spring boot在pom.xml里面添加了了热部署插件 spring-boot-devtool，这样就可以利用jvm类加载最新的类到虚拟机。这样就不需要重新启动也可以看到修改后的效果。
   当修改代码的时候，控制台就自动启动

   spring  boot缺陷
   - 缺少注册，发现等外围方案
   - 缺少外围监控集成方案；
   - 缺少外围安全管理方案
   - 缺少rest落地的urI规划方案
   所以作为一个微框架，离微服务的实现还是有距离的。


   ##### 集群，分布式，微服务的概念和区别
   - 集群是物理形态，分布式是工作方式；
   - 集群是同一个业务，部署在多个服务器上；
   - 分布式：一个业务拆分为多个子业务，部署到不同的服务器上。（将一个大的系统拆分为多个业务模块，业务模块分布部署到不同的机器上。各个业务模块之间通过接口进行数据交互，区别是分布式是根据不同机器不同业务。，分布式需要做好
   事务管理。分布式是属于微服务的，微服务的意思是将模块拆分为一个独立的服务单元的通过接口来实现数据的交互）
   - 微服务是一种架构风格，一个大型发展的软件应用由一个或者多个微服务组成。系统中的各个微服务可被独立部署，各个微服务之间是松耦合的。每个微服务仅关注
   于完成一件任务并很好的完成该任务，在所有情况下，每个任务代表着一个小的业务能力。
   （微服务的设计是为了不因为某个模块的升级和bug影响现有的系统业务。微服务与分布式细微差别是，微服务应用不一定是酚酸在多个服务器上的，他可以在同一个服务器上。）
   ##### sping boot是如何进行微服务 与分布式有什么区别

   ##### @configuration  ，@componentScan区别
      -  直白的将@component包含了configuration所有的注解，他们头是注解在类上面的。
      -  @configuration的类中的成员变量是@value注解，方法上面的注解是@bean，
      -  @component类中的方法和成员变量没有任何限制。

   从定义来看，@configuration注解的本质HIA是@component，因此<context:component-scan/>或@componentScan都能处理@configuration注解的类
    @configuration标记的类必须符合下面的要求
    - 配置类必须以类的形式提供（不能是工厂方法返回的实例），允许通过生产子类在运行时增强（cglib动态代理）；
    - 配置类不是final类（没办法动态代理）；
    - 配置注解通常为了通过 @bean注解生产spring容器管理的类；
    - 配置类必须是非本地的（既不能在方法中声明，不能是private）；
    - 任何嵌套配置类都必须声明为static；
    -  @bean方法可能不会反过来创建进一步的配置类（也就是返回的bean如果带有@configuration，也不会被特殊处理，只会作为普通的bean）;


    ##### @component注解的类如何实例化？
    利用@Component主鍵的類實例化可以通過 @autowire進行實例化