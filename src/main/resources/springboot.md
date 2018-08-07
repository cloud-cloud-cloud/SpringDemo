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