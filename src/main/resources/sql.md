#### :snail: SQL注意要点
 ##### 如何放sql注入
 例如mybatis如何防止sql注入：在查询语句里面表明类型；用#{}提到${}，因${}会被编译的

 如何进行多个参数进行模糊查询，并且防止sql注入：
                                          <if test="name != null and name != ''">
                                          AND name LIKE "%"#{name}"%"
                                          </if>

                                          或者使用字符串拼接函数
                                           <if test="name != null and name != ''">
                                                          AND name LIKE CONCAT(CONCAT('%',#{name},'%'))
                                           </if>

  #####  JAP如何使用，原理是什么





   

