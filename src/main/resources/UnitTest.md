    #### :snail: 单元测试

    ##### 你写不写单元测试？（你们觉得不会写单元测试的人是不是高级？）


    ##### 你用的是JUNIT写的对吧？
    ##### 如何在JUNIT里MOCKUP一个REQUEST？
     mock 需要什么不能自己创建的，就mock()出来，然后用when()给它增加需要的模拟行为。
     例如说mock(request)
      request = mock(HttpServletRequest.class);
      response = mock(HttpServletResponse.class);
      when(request.getParameter("name")).thenReturn("123");

    ##### 如何在JUNIT里访问SPRING的一个SERVICE方法并对该SERVICE方法进行单元测试？
         在junit里面对service进行单元测试  主要是创建对象，加载spring的上下文，所以我们构建了一个基础测试类BaseTest里面引入注解
         @RunWith(SpringJUnit4ClassRunner.class)
         @ContextConfiguration(locations = {"classpath:applicationContext-test.xml" })//上下文配置加载  可以加载测试项的内容，或者加载运行环境的配置内容
         public class BaseTest {

            public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

         }

    ##### spock有没有用过？
    需要了解groovy语言，所以这个框架没有用过

    ##### 在spock中如何访问spring？


    ##### 如何生成覆盖率报告？你用的是什么组件？


    ##### 如何生成单测试报告？你用的是什么组件？


    ##### 如果回答ANT
        ant管理jar库

    ##### 核心命令是什么？


    ##### 如果回答MAVEN
      直接通过 运行密令可以执行生产单元测试报告  默认执行 mvn test
      等同于mvn test surefire-report:report   在target下面生成xml,txt文件
      在pom里面配置的插件是 maven-surefire-plugin ，配置test类包含的内容，以及测试失败忽略继续构建项目
      <plugin>
      				<artifactId>maven-surefire-plugin</artifactId>
      				<configuration>
      					<testFailureIgnore>true</testFailureIgnore>	<!-- /////maven中如何忽略单元测试中的错误继续构建工程/////// -->
      					<includes>
      						<include>**/*Test.java</include>	<!-- //////////// -->
      					</includes>
      					<excludes>
      						<!-- -->
      					</excludes>
      				</configuration>
      			</plugin>


    ##### 核心命令/组件又是什么？

    ##### mvn install时如何跳过单元测试步骤

    mvn  install -DskipTests