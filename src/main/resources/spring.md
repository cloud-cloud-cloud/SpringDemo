
    #### :snail:Spring
    ##### Spring的事务传播机制
    -  propagation_required 当前没有事务，创建事务，有事务 加入到事务中运行
    - propagation_supports 当前有事务，以事务执行，无事务，以非事务的状态进行
    - propagation_mandatory 使用当前事务，没有事务抛出异常
    - propagation_requires_new 新建事务，当前有事务，事务挂起；
    - propagation_not_support  以非事务执行，如果当前存在事务，事务挂起；
    - propagation_never 以非事务执行，有事务抛异常；
    - propagation_nested  如果当前存在事务，则嵌套在事务内执行，如果当前没有事务，则执行与propagation_required类似的操作；


    ##### AOP有几种？
    - 基于代理的模式动态实现AOP（JDK动态代理）：在运行期，目标类加载后，为接口动态生成代理类，将切面注入到代理类中。
    - CGlib生成代理：通过实现MethodInterceptor接口，实现intercept方法生成代理
        在web.xml配置拦截器，例如说springMVC里面和mybaitis拦截mybatis,配置拦截器和拦截地址。
        <mvc:interceptors>
        		<mvc:interceptor>
        			<!-- 需拦截的地址 -->
        			<!-- 一级目录 -->
        			<mvc:mapping path="/**" />
        			<bean class="com.qf.pboc.lib.web.ThirdPartyServiceInterceptor" />
        		</mvc:interceptor>
        </mvc:interceptors>
       在拦截类里面 跨线程，当请求进来的时候，就会发生拦截，将请求里面需要的参数放到MDC里面， 利用拦截器里面的preHandle预处理，postHandle（返回处理后做什么 例如说清除 内容，修改moderandView）
       ， afterHandle后处理 可以进行类似异常日志处理


    - @Aspact注解形式的
    在切面类上面添加@Aspact，  @Component 注解，
     获取数据源判断：跨线程的，根据拦截器里面的上下文内容进行判断应该访问的数据库，
      1.自定义注解 获取数据库的自定义的指定的是哪个库，没有指定哪个库就会指定一个默认库（面对的情况是如果一个数据源里面的有的表是需要多种操作的时候，例如说线上，线下，线下的还是要访问以前的库，线上的访问另外的库）
      2.利用 @Pointcut 这个注解切入点，扫描对应的那些文件 ：@Pointcut("execution(* com.qf.pboc.dao.*Mapper.*(..))")
      3.  @Before("doMethod()")   @After("doMethod()")  ， //@Around("doMethod()")   配置controller环绕通知,使用在方法aspect()上注册的切入点
          @Around @Before @After三个注解的区别@Before是在所拦截方法执行之前执行一段逻辑。@After 是在所拦截方法执行之后执行一段逻辑。@Around是可以同时在所拦截方法的前后执行一段逻辑。
          值得注意的是，Around在拦截方法后，需要返回一个方法执行结果，否则，原方法不能正常执行。

    - 注入形式的Aspact切面
     利用注入的形式 例如说读写分离中如果select方法的，写 insert,update  delete方法开头的
    <!-- 目标类 -->
        <bean id="lina" class="com.tgb.springaop.service.impl.ChenLliNa"/>
        <bean id ="sleepHelper" class="com.tgb.springaop.aspect.SleepHelper02"/>

        <aop:config>
            <aop:aspect ref="sleepHelper">
                 <aop:before method="beforeSleep" pointcut="execution(* *.sleep(..))"/>
                 <aop:after method="afterSleep" pointcut="execution(* *.sleep(..))"/>
            </aop:aspect>
        </aop:config>


    ###### 声明式AOP怎么写？
    在<aop:config>标签下使用<aop:pointcut>声明一个切入点Bean，该切入点可以被多个切面使用，对于需要共享使用的切入点最好使用该方式，
    该切入点使用id属性指定Bean名字，在通知定义时使用pointcut-ref属性通过该id引用切入点，expression属性指定切入点表达式：
     <tx:advice id="txAdvice" transaction-manager="transactionManager">
            <tx:attributes>
                <tx:method name="add*" propagation="REQUIRED" />
                <tx:method name="del*" propagation="REQUIRED" />
                <tx:method name="update*" propagation="REQUIRED" />
                <tx:method name="get*" propagation="NOT_SUPPORTED"
                    read-only="true" />
                <tx:method name="doComplexLogic" propagation="NESTED"
                    isolation="REPEATABLE_READ" timeout="1000" rollback-for="java.lang.Exception"
                    no-rollback-for="com.mysrc.service.CustomRuntimeException" />
            </tx:attributes>
        </tx:advice>

        <!-- Spring AOP config -->
        <aop:config>
            <!-- 切入点 -->
            <aop:pointcut id="studentServicesPointcut"
                expression="execution(* com.mysrc.service.StudentService.*(..))" />
            <!-- <aop:pointcut id="newServicesPointcut2" expression="execution(* com.model.*.*(..))"
                /> -->
            <aop:advisor advice-ref="txAdvice" pointcut-ref="studentServicesPointcut" />
            <!-- <aop:advisor advice-ref="txAdvice" pointcut-ref="newServicesPointcut2"
                /> -->
        </aop:config>

    ###### 注解式AOP怎么写？
    @aspact  @component


    ##### 如何自定义一个Annotation
      定义一个注解  一般包含了:
      @Retention(RetentionPolicy.RUNTIME)   什么时候使用该注解,定义该注解的生命周期
        ●   RetentionPolicy.SOURCE : 在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。
        ●   RetentionPolicy.CLASS : 在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式
        ●   RetentionPolicy.RUNTIME : 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。
      @Target({ElementType.TYPE})  –注解用于什么地方，默认值为任何元素，表示该注解用于什么地方。可用的ElementType参数包括
        ● ElementType.CONSTRUCTOR:用于描述构造器
        ● ElementType.FIELD:成员变量、对象、属性（包括enum实例）
        ● ElementType.LOCAL_VARIABLE:用于描述局部变量
        ● ElementType.METHOD:用于描述方法
        ● ElementType.PACKAGE:用于描述包
        ● ElementType.PARAMETER:用于描述参数
        ● ElementType.TYPE:用于描述类、接口(包括注解类型) 或enum声明
      @Documented  –注解是否将包含在JavaDoc中
      public @interface DataSource {

      	String id() default "";
      }

     @Inherited – 定义该注释和子类的关系
     @Inherited 元注解是一个标记注解，@Inherited阐述了某个被标注的类型是被继承的。
     如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。

    ###### 自定义annotation里的作用范围有几种？（方法、类、字段）
    method  type ,field ,constructor  构造器，package描述包，parameter 参数

    ##### 什么是IOC，IOC的注入有几种，@Resource和@Autowired的区别
    两种，@Resource通过name进行反射获取对象；@Autowired 创建对象通过类型


    ##### BeanFactory是什么？
    beanFactory是IOC最基本的容器 负责生产和管理bean,是为了解耦作用。
    XmlBeanFactory,ApplicationContext 等具体的容器都是实现了BeanFactory


    ##### XMLBeanFactory
     目前已被废弃  替代它用classPathResource("application.xml";)
     在beanfactory启动的时候不会创建bean实例，而是在getBean的时候才会创建bean

    ##### 解释Spring支持的几种bean的作用域（singleton,prototype,request,sesson,global-session）


    ##### 什么是Spring的内部bean？


    ##### 描述RequestMapping和PathVariable两个注解的差别。


    ##### SpringMVC搭建的Rest请求，如何统一指定请求返回的编码格式？


    ##### SpringMVC默认返回的ModelView文件映射类型是jsp文件，如果想指定ModelView的返回类型 为htm文件,如何实现？


    ##### Bean 工厂和 Application contexts 有什么区别？


    ##### 你用spring结合过相关的DAO写法吗？


    ###### 回答hibernate


    ###### 如何通过HibernateDaoSupport将Spring和Hibernate结合起来？说出关键步骤和要点


    ###### 回答mybatis


    ###### 如何把mybatis和spring进行相结合？说出关键步骤和要点
    - 在pom里面加载 mybatis  jar包
    - 配置application-mybatis.properties文件配置数据库信息，配置mybatis 的sqlSessionFactoryBean，和mapperLocation。配置文件扫描
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <property name="dataSource" ref="pboc_dataSource"/>
            <property name="mapperLocations" value="classpath:mapper/*.xml"/>
        </bean>

        <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
            <property name="basePackage" value="com.qf.pboc.dao;com.qf.pboc.lib.dao.mysql" />
            <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        </bean>

        <bean id="pboc_dataSource" class="com.qf.pboc.lib.system.multidatasource.DynamicDataSourceFactoryBean">
            <property name="dataSourceConfigList">
                <list>

                    <bean class="com.qf.pboc.lib.system.multidatasource.DataSourceConfig">
                        <property name="id" value="default" />
                        <property name="url" value="${pboc.datasource.default.url}" />
                        <property name="driverClassName" value="${pboc.datasource.default.driverClassName}"/>
                        <property name="userName" value="${pboc.datasource.default.username}" />
                        <property name="password" value="${pboc.datasource.default.password}" />
                    </bean>

                    <!--<bean class="com.qf.pboc.lib.system.multidatasource.DataSourceConfig">-->
                        <!--<property name="id" value="qyj-online" />-->
                        <!--<property name="url" value="${pboc.datasource.qyj-online.url}" />-->
                        <!--<property name="driverClassName" value="${pboc.datasource.qyj-online.driverClassName}"/>-->
                        <!--<property name="userName" value="${pboc.datasource.qyj-online.username}" />-->
                        <!--<property name="password" value="${pboc.datasource.qyj-online.password}" />-->
                    <!--</bean>-->
                </list>
            </property>
        </bean>

    -  在mapping.xml文件里面映射对应的dao包，里面写具体的crud的sql;
    - 在dao包里面写接口 对应增删查改方法



    ######回答jdbctemplate


    ###### 如何把jdbctemplate和spring结合？说出几个关键步骤和要点


    ##### 平时工作和学习中有没有使用过Spring封装base service类？


    ###### 能否描述 一下你是怎么封装一个base service的？


    ##### 平时工作和学习中有没有使用过Spring封装base dao类


    ###### 能否描述 一下你是怎么封装一个base dao的？


    ##### 如何在spring中实现国际化?


    ##### 有没有用过0 xml配置即spring4.x的写法？


    ###### spring boot有没有用过？


    ###### XML配置和传统spring 的 xml配置的主要区别是什么？

    ##### spring 是怎么进行事务管理的

    ##### spring的dispatcher的流程是怎么样的