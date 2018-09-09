    #### :snail:  solr使用
    ##### solr 简介
    solr是基于lucene的Java搜索引擎服务。solr提供了层面搜索，命中醒目显示支持多种输出格式（xml/xslt/json）便于安装和配置
    并且附带了HTTP的管理界面。solr包装和扩展了lucene。所以他沿用了lucene的相关术语。更重要的是solr建立的索引和lucene搜索引擎库完全兼容。
    ##### solr安装和访问，
    window环境新版本启动solr密令：进入solr里面的server 执行 java -jar start.jar   --module=http
    linux环境执行密令 ： bin/solr start -e cloud -noprompt

    通过ip:8983/solr 访问界面

    ##### solr简单使用
    服务启动后，目前界面上面是没有任何数据的，你可以同通过posting密令向solr添加（更新）文档，删除文档。在exampledoc目录里面包含了一些实例文件，运行密令：
    java -jar post.jar solr.xml monitor.xml  添加了这两份文档
    添加core