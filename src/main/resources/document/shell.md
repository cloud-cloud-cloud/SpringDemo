    #### :snail: shell

    ##### shell语言的概念
     shell是用C语言编写的程序，它是用户使用linux的桥梁，shell既是一种命令语言，又是一种程序设计语言。
     shell是指一种应用程序，这个应用程序提供了一个界面，用户通过这个访问操作系统的内核的服务。

      ##### shell脚本
      shell脚本是一种编写的脚本程序，业界所说的shell通常是指shell脚本，但是shell和shell script是两个不同的概念。
      shell的环境：shell编程和java，PHP编程一样，只要有一个能编写代码的文本和一个能执行的脚本执行器就可以了。
      linux的shell种类众多，常见的：
      - bourne  shell(/usr/bin/sh或/bin/sh)
      - **bourne again shell(/bin/bash)**
      - C shell(/usr/bin/csh)
      - K shell(/usr/bin/ksh)
      - shell for root(/sbin/sh)
      bash 在日常工作中被广泛使用，同时bash也是大多数linux系统默认的shell；
      在一般情况下，人们不区分bourne shell  和bourne again  shell，所以想 #!/bin/sh，他同样可以修改为
      #!/bin/bash
      #!告诉系统其后路径所指定的程序既是解释此脚本文件的shell程序、
      #!/bin/bash   #是一个约定的标记，告诉系统这个脚本需要什么解释器来执行，指明使用哪一种shell
      echo "hello word"  #echo命令用于向窗口输出文本。

      ##### 运行shell脚本的两种方法
      1.作为可执行程序，在上面的代码保存为test.sh 并cd到响应的目录：
      chmod + x ./test.sh #使脚本具有执行权限
      ./test.sh  #执行脚本
      注意：一定要写成./test.sh而非test.sh，运行其他二进制程序也是一样，直接写成test.shlinux系统会去path目录里面寻找有没有叫test.sh的，而只有
      /bin,/sbin,/usr/bin等在path李，你的当前目录通常不在path，所以写成./test.sh告诉系统，就在当前目录找。
      2.作为解释器参数
      这种运行方式是，直接运行解释器，其参数就是shell脚本的文件名，如：
      /bin/sh test.sh
      /bin/php test.php
      这种方法运行的脚本，不需要在第一行指定解释器的信息，写了也没用。

      ##### shell变量
      定义变量的时候，变量名不需要用美元符号（$,php语言中需要），如
      your_name="rubbo.com"
      注意，变量名和等号之间不能有空格，真坑你和你所熟悉的编程语言不一样，同时变量名需要遵守以下：
      - 命名只能用户英文字母，数字，和下划线，曙光字符不能以数字开头；
      - 中间不能有空格，可以使用下划线；
      - 不能使用标点符号；
      - 不能使用bash里面的关键字（可用help查看保留关键字）

      除了显式的直接赋值，还可用用羽箭给变量赋值，如：
      for  file  in 'ls /etc' 或者 for file in $(ls /etc)  #将/etc下的目录文件名循环出来
        **使用变量**
        使用一个定义过的变量，只要在变量名前面加美元符号即可，如：

        your_name="qinjx"
        echo $your_name
        echo ${your_name}
        变量名外面的花括号是可选的，加不加都行，加花括号是为了帮助解释器识别变量的边界，比如下面这种情况：

        for skill in Ada Coffe Action Java; do
            echo "I am good at ${skill}Script"
        done

        只读变量：
        my_url="www.baidu.com"
        readonly my_url
        删除变量
        unset variable_name

       ##### 变量类型
        运行shell时候，会同时存在三种变量：
        1.局部变量 局部变量在脚本或命令定义中，仅仅在当前shell实例中有效，其他shell启动的时候不能访问这个局部变量；
        2.环境变量 所有程序，包括shell启动的程序，都能访问环境变量，有些程序需要环境变量来保证其正常运行，必要的 时候shell也可以定义环境变量
        3. shell变量 shell变量是由shell程序设置的特殊变量，shell变量中有一部分是环境变量，有一部分是局部变量，这些变量保证了shell正常运行。

       shell字符串：字符串是shell编程中最常用的最有用的数据类型（除了数字和字符串，也没有其他类型好用了），字符串可以用单引号，也可用用双引号，也可以不用引号。
       - 单引号是原样输入，里面的变量也会失效，会原样输出内存
       - 双引号 里面可以有变量，可以出现转义字符
           your_name='runoob'
           str="Hello, I know you are \"$your_name\"! \n"
           echo $str

         获取字符串长度： string="abc" echo ${#string} #输出字符长度4
         提前子字符串： string="runoob is a great site" echo ${string:1:4} #从第二位字符截取，获取4 四个字符串：unoo ;
         查找子字符串：查找字符 i 或 o 的位置(哪个字母先出现就计算哪个)：string="runoob is a great site" echo `expr index "$string" io`  # 输出 4 注意： 以上脚本中 ` 是反引号，而不是单引号 '，不要看错了哦。

     ##### shell数组
     ###### 定义数组  array_name=(val0 val1 val2 val3)或
     array_name=(
     val0
     val1
     val2
     val3
     )
     单独定义各个分量 array_name[0]=val0      array_name[1]=val1 array_name[n]=valn #可以不使用连续的下标，而且下标的范围没有限制。
     读取数组：valn=${array_name[n]}
     可用用@符号获取数组中的所有元素：echo  ${array_name[@]};
     获取数组的长度：length=${#array_name[@]}或者 length=${#array_name[*]}
     获取数组单个元素的长度：lengthn=${#array_name[n]}

     ##### shell注释
     以#开头的行都是注释，会被解释器忽略
     **多行注释可用**
     :>>EOF
     注释内容
     注释内容
     注释内容
     EOF
     或者
     :>>'
      注释内容
      注释内容
      注释内容
     '
     或者
     :>>!
       注释内容
       注释内容
       注释内容
     !
   ##### 运算符
   shell和其他编程一样支持多种元算法：包含
   - 算数运算符 expr   :var=`expr 2 + 2`   # 注意：表达式和运算符之间要有空格，例如说2+2是错误的，必须写成2 + 2；完整的表达式要用``包含，这个字符串不是常用的单引号，而是esc下面的键。
    算数运算包含了+ - * / %取余 == #[ $a == $b ] 返回 false。  = #a=$b 将把变量 b 的值赋给 a。 !=
   注意：1.乘号*前面必须加反斜杠\才能实现乘法运算；
        2.if..then..fi是条件语句，
        3在mac中shell中的expr语法是：$((表达式)) ，此处的表达中的* 不需要转移字符串\

   - 关系运算符
    1.-eq判断相等
    2.-ne判断不相等
    3.-gt判断左边是否大于右边的值
    4.-lt判断左边是否小于右边，如果是，返回ture
    5.-ge，-le 判断左边是否大于后者等于右边；  判断左边是否小于或者等于右边；
   - 布尔运算符
     1. ！非运算  [!false] 返回true;
     2. -o 或运算 [$a -lt 20 -o $b -gt 100];
     3.-a与运算，两个都为true返回true
   - 字符串运算符
     1.= 判断字符串相等 ，
     2.！=，
     3.-z判断字符串长度是否为0,是则返回true;
     4.-n判断字符串长度，非0则返回true；
     5.str判断字符串是否为非空，不为空返回true;
   - 文件测试运算符:文件测试运算符用于检测Unix文件的各种属性
     1. -b file 检测文件是否是设备文件，如果是返回true;
     2. -c file 检测文件是否是字符设备文件，如果是返回true;
     3. -d file 检测文件是否是目录，如果是返回true;
     4. -f file 检测文件是否为普通文件（既不是目录，也不是设备文件），如果是返回true;
     5. -g file 检测文件是否设置了SGID位，如果是返回true ;
     6. -k file 检测文件是否设置了粘着为（sticky bit）如果是返回true;
     7. -p file 检测文件是否有名管道，如果是返回true；
     8. -u file 检测文件是否设置了suid为，如果是返回true  [-u $file]
     9. -r file检测文件是否可读
     10.- w file 检测文件是否可写
     11. -x file 检测文件是否可执行；
     12. -s file 检测文件是否为空（文件大小是否大于0）
     13. -e file检测文件（包含目录）是否存在
   - 逻辑运算符  #&& 逻辑and , ||逻辑or

   ##### printf命令
   printf命令消费c程序库（library）里的printf（）程序，printf脚本被echo移植性好。printf使用引用文本或者空格分隔参数，
   外面可以在printf中使用格式字符串，还可以指定字符串的宽度，左右对其的方式等。
   默认的printf不会像echo自动添加换行符，我们可以手动添加\n.
   printf format-string [arguments...]
   format-string：为格式控制字符串
   arguments:为参数列表
   eg： printf "%-10s %-8s %-4s\n"
        printf "%-10s %-8s %-4.2f\n"
        %s %c %d %f 都是格式替代符
        %-10s指一个宽度为10个字符串（-表示左对齐，没有则表示右对齐），任何字符都以10个字符长度显示，没有则以空格补齐，超过也会将内容全部显示出来；
        %-4.2f表示格式化小数，.2表示保留2为小数。
        **单引号和双引号效果一样**
        printf "%d %s\n" 1 "abc" # 1 abc
        printf '%d %s\n' 1 "abc" # 1 abc
        # 如果没有 arguments，那么 %s 用NULL代替，%d 用 0 代替
        printf "%s and %d \n"   #  and 0
  ###### printf 的转义字符
         $ printf "a string, no processing:<%s>\n" "A\nB"
         a string, no processing:<A\nB>

         $ printf "a string, no processing:<%b>\n" "A\nB"
         a string, no processing:<A
         B>

         $ printf "www.runoob.com \a"
         www.runoob.com $                  #不换行

        %d %s %c %f 格式替代符详解:
        d: Decimal 十进制整数 -- 对应位置参数必须是十进制整数，否则报错！
        s: String 字符串 -- 对应位置参数必须是字符串或者字符型，否则报错！
        c: Char 字符 -- 对应位置参数必须是字符串或者字符型，否则报错！
        f: Float 浮点 -- 对应位置参数必须是数字型，否则报错！
        如：其中最后一个参数是 "def"，%c 自动截取字符串的第一个字符作为结果输出。
        $  printf "%d %s %c\n" 1 "abc" "def"
        1 abc d

     ##### shell流程控制
     在sh/bash如果else分支里面没有执行语句，就不用写else，这个同PHP不同
     ###### if else
     if condition
     then
         command1
         command2
         ...
         commandN
     fi
     写成一行 if[ $(ps -ef |grep -c "ssh") -gt 1]; then echo "true";fi
     末尾的fi就是if倒过来的拼写；

    ###### if else-if else
     if else-if else 语法格式：

     if condition1
     then
         command1
     elif condition2
     then
         command2
     else
         commandN
     fi
     ###### for循环
     for var in item1 item2 ... itemN
     do
         command1
         command2
         ...
         commandN
     done
     写成一行： for  var in itme1 item2... itemN; do common1; common2...done;
     for loop in 1 2 3 4 5
     do
         echo "The value is: $loop"
     done
     输出结果：
     The value is: 1
     The value is: 2
     The value is: 3
     The value is: 4
     The value is: 5
     顺序输出字符串中的字符：

     for str in 'This is a string'
     do
         echo $str
     done
     输出结果： This is a string
     ###### while语句
     while循环用于不断执行一系列命令，也用于从输入文件中读取数据；命令通常为测试添加，格式为
     while condition
     do
        commond
     done
     eg:
     #!/bin/bash
     int=1
     while(($int<=5))
     do
       echo $int
       let "int++"
      done
      while循环可用于读取键盘信息。下面信息被设置为file变量，按<ctrl-d>结束循环
      echo '按下 <CTRL-D> 退出'
      echo -n '输入你最喜欢的网站名: '
      while read FILM
      do
          echo "是的！$FILM 是一个好网站"
      done
      运行脚本，输出类似下面：
      按下 <CTRL-D> 退出
      输入你最喜欢的网站名:菜鸟教程
      是的！菜鸟教程 是一个好网站
      ###### 无限循环 while :
                     do
                        commond
                     done
                     或者
                     while ture
                     do
                       commond
                     done
                    或者
                    for(( ; ; ))
      ###### until循环
             until循环执行一系列命令知道条件true位置；他执行刚好和while相反。
                    一般情况下while循环优于until循环，单是在极少数清清裤下，until循环更加有用
                    until condition
                    do
                      command

                    done
               condition 一般为条件表达式，如果返回值为 false，则继续执行循环体内的语句，否则跳出循环。

               以下实例我们使用 until 命令来输出 0 ~ 9 的数字：

               #!/bin/bash

               a=0

               until [ ! $a -lt 10 ]
               do
                  echo $a
                  a=`expr $a + 1`
               done
               运行结果：

               输出结果为：

               0
               1
               2
               3
               4
               5
               6
               7
               8
               9
         ###### case
               shell里面case羽箭为多选择羽箭，可以用case羽箭匹配一个值与一个陌生，如果匹配成功，执行相匹配的内容
               case  值 in
               模式1）
                  command1
                  command2
                  。。。
                  commandN
                  ;;
                模式2）
                  command1
                  command2
                  。。。
                   commandN
                   ;;
                esac
       ###### 跳出循环 continue（跳出本次循环） break（跳出所有循环）
       ######  esac case的语法和c family语法差别很大，它需要一个esac(就是case反过来)
       作为结束标记，每个case 分别用右括号，用两个番号表示break。

       ##### shell函数
       #!/bin/bash
       # author:菜鸟教程
       # url:www.runoob.com

       funWithParam(){
           echo "第一个参数为 $1 !"
           echo "第二个参数为 $2 !"
           echo "第十个参数为 $10 !"
           echo "第十个参数为 ${10} !"
           echo "第十一个参数为 ${11} !"
           echo "参数总数有 $# 个!"
           echo "作为一个字符串输出所有参数 $* !"
       }
       funWithParam 1 2 3 4 5 6 7 8 9 34 73
       输出结果：

       第一个参数为 1 !
       第二个参数为 2 !
       第十个参数为 10 !
       第十个参数为 34 !
       第十一个参数为 73 !
       参数总数有 11 个!
       作为一个字符串输出所有参数 1 2 3 4 5 6 7 8 9 34 73 !
       参数处理	说明
       $#	传递到脚本的参数个数
       $*	以一个单字符串显示所有向脚本传递的参数
       $$	脚本运行的当前进程ID号
       $!	后台运行的最后一个进程的ID号
       $@	与$*相同，但是使用时加引号，并在引号中返回每个参数。
       $-	显示Shell使用的当前选项，与set命令功能相同。
       $?	显示最后命令的退出状态。0表示没有错误，其他任何值表明有错误。

       ##### shell输入，输出 重定向
       大多数Unix系统命令从你的终端接受输入并将所产出的输出发送到你的终端，一个命令通常从一个叫标准输入的地方读取输入，默认情况下
       这恰好是你的终端
       命令           说明
       command>file  将输出重定向到file
       command<file  将输入重定向到file
       command>>file 将输出以追加的形式重定向到file
       n > file      将文静描述符n的文件重定向到file
       n >> file     将文件描述符为n的文件追加到重定向文件file
       n >& m        将输出文件m和n合并  (放在>后面的&，表示重定向的目标不是一个文件，而是一个文件描述符)
       n <& m        将输入文件m和n合并
       << tag        将开始标记tag和结束标记tag之间的内容作为输入
       需要注意的是文件描述符 0 通常是标准输入（STDIN），1 是标准输出（STDOUT），2 是标准错误输出（STDERR）
       实例
       执行下面的 who 命令，它将命令的完整的输出重定向在用户文件中(users):
       $ who > users

       $ echo "菜鸟教程：www.runoob.com" >> users
       $ cat users
       菜鸟教程：www.runoob.com
       菜鸟教程：www.runoob.com

       在命令行中通过 **wc -l 命令计算 Here Document 的行数**：
       $ wc -l << EOF
           欢迎来到
           菜鸟教程
           www.runoob.com
       EOF
       3          # 输出结果为 3 行
       $

       ###### /dev/null 文件
              如果希望执行某个命令，但又不希望在屏幕上显示输出结果，那么可以将输出重定向到 /dev/null：

              $ command > /dev/null
              /dev/null 是一个特殊的文件，写入到它的内容都会被丢弃；如果尝试从该文件读取内容，那么什么也读不到。但是 /dev/null 文件非常有用，将命令的输出重定向到它，会起到"禁止输出"的效果。

              如果希望屏蔽 stdout 和 stderr，可以这样写：

              $ command > /dev/null 2>&1

      ##### shell里面行末尾的&含义
              表示后台执行该行命令，从而后面的命令在该行命令执行完成并返回前就能得到执行。
              若不加&，则执行方式为阻塞式，必须等到当前命令执行完成并返回后才执行下面的命令

     ps ef|grep java|grep ndes.jar|awk '{print $2}'  # awk里面{}命令代码块，里面包含一条或者多条命令， ''包含引用代码块内容

     #! /bin/bash
     pid=`ps -ef | grep java | grep n-des.jar | awk '{print $2}'`
     if [[ "$pid" != "" ]]; then
       kill -9 $pid
     fi