    #### :snail: nginx负载均衡
    user  www www;
    worker_processes 10; # 打开的工作进程数
    #error_log  logs/error.log;
    #error_log  logs/error.log  notice;
    #error_log  logs/error.log  info;
    #pid  logs/nginx.pid;
    #最大文件描述符
    worker_rlimit_nofile 51200;# nginx并发优化，同时连接的数量受限于系统上可用的文件描述符的数量，因为每个套接字将打开一个文件描述符。 如果NGINX尝试打开比可用文件描述符更多的套接字，会发现error.log中出现Too many opened files的信息。
                                           使用ulimit检查文件描述符的数量：
    events {
      use epoll;
      worker_connections 51200;# nginx并发优化，worker进程最大打开的连接数
    }
     http {
      include conf/mime.types;
      default_type  application/octet-stream;
      keepalive_timeout 120;
      cp_nodelay on;
    upstream  www.hws.com  {# 上游 配置tomcat服务，对应nginx  的proxy_pass虚拟域名 分别对应不同的nginx
       server   192.168.1.2:80 wight=2;#配置权重 ，nginx目前支持的四种分配方式 ，wight,fair ,ip_hash ,url_hash
       server   192.168.1.3:80;
       server   192.168.1.4:80;
       server   192.168.1.5:80 backup; #当所有的服务down了，作为备选服务用
    }
    upstream  blog.hws.com  {
        ip_hash;
        server   192.168.1.7:8080;
        server   192.168.1.7:8081;
        server   192.168.1.7:8082;
        }
    server{# 配置外网访问的域名名称 ，通过域名解析到对应的代理proxy_pass，找到对应的upstream 不同的tomcat服务器上面，如果有session需要可以按照ip_hash进行分配
            listen  80;
            server_name  www.hws.com;
    location / {
                proxy_pass        http://www.hws.com;

                proxy_set_header   Host    $host;

                proxy_set_header   X-Real-IP    $remote_addr;

                proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;

                  }

    log_format  www_s135_com  '$remote_addr - $remote_user [$time_local] $request '

    ''$status' $body_bytes_sent '$http_referer' '

    ''$http_user_agent' '$http_x_forwarded_for'';

    access_log  /data1/logs/www.log  www_hws_com;

         }
    server{

            listen  80;

            server_name  blog.hws.com;

    location / {
                proxy_pass   http://blog.hws.com;

                proxy_set_header   Host             $host;

                proxy_set_header   X-Real-IP        $remote_addr;

                proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;

                }
    log_format  blog_s135_com  '$remote_addr - $remote_user [$time_local] $request '

    ''$status' $body_bytes_sent '$http_referer' '

    ''$http_user_agent' '$http_x_forwarded_for'';

    access_log  /data1/logs/blog.log  blog_hws_com;
          }

    }


    ##### nginx优化

     -  修改nginx配置文件，
      nginx.conf增加 work_rlimit_nofile和work_connection数量，并禁用keepalive_timeout.
      worker_process 1；nginx进程数，建议按照cup数目来指定，一般为它的倍数
      worker_rlimit_nofile 20000; 一个nginx进行打开的最多的文件描述数目，理论值应该是最多打开文件数目（ulimit -n）与nginx进程数相除，但是nginx分配请求并不是那么均匀，所以最好是 ulimit -n 值保持一致
      event{
      use epoll; 使用epoll的i/o模型
      worker_connections 20000； 每个进行运行连接的最多数，理论上每台nginx服务器最大的连接数为worker_process* worker_connection;
      multi_accept on
      }
      http{
      keepalive_timeout 0;
      }

    - 系统层面
    1.调整同时打开的最大文件数量 ulimit -n 20480
    2.tcp最大的连接数：  echo 10000>/proc/sys/bet/core/somaxconn
    3.tcp连接立即税收，回用（recycle，reuse）
    echo 1 > /proc/sys/net/ipv4/tcp_tw_reuse
    echo 1 > /proc/sys/net/ipv4/tcp_tw_recycle
    4.不做tcp洪水抵御
    echo 0 > /proc/sys/net/ipv4/tcp_syncookies
    使用至今优化后的配置，在/ect/sysctl.conf中加入：
    et.core.somaxconn = 20480
    net.core.rmem_default = 262144
    net.core.wmem_default = 262144
    net.core.rmem_max = 16777216
    net.core.wmem_max = 16777216
    net.ipv4.tcp_rmem = 4096 4096 16777216
    net.ipv4.tcp_wmem = 4096 4096 16777216
    net.ipv4.tcp_mem = 786432 2097152 3145728
    net.ipv4.tcp_max_syn_backlog = 16384
    net.core.netdev_max_backlog = 20000
    net.ipv4.tcp_fin_timeout = 15
    net.ipv4.tcp_max_syn_backlog = 16384
    net.ipv4.tcp_tw_reuse = 1
    net.ipv4.tcp_tw_recycle = 1
    net.ipv4.tcp_max_orphans = 131072
    net.ipv4.tcp_syncookies = 0
    使用：sysctl -p 生效
    sysctl -p
