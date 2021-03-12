# dbApi-spring-boot-starter

## 概述
- dbApi-spring-boot-starter 是接口快速开发工具，可以极大的降低代码量，类似于mybatis-plus框架，不需要再编写mapper接口、resultMap、javaBean
- dbApi-spring-boot-starter 是[dbApi](https://gitee.com/freakchicken/db-api) 的spring boot集成
- 通过xml编写sql和数据库配置，可以快速开发接口，支持多数据源，支持动态sql

## 使用案例
- 新建一个springboot的web项目，pom.xml中引入依赖
```
<dependency>
    <groupId>com.jq</groupId>
    <artifactId>dbApi-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

- 在resources目录下创建数据库地址配置ds.xml
```
<datasource>
    <ds id="mysql">
        <url>jdbc:mysql://localhost:3306/story?useSSL=false&amp;characterEncoding=UTF-8</url>
        <username>root</username>
        <password>root</password>
    </ds>

    <ds id="local_mysql">
        <url>jdbc:mysql://localhost:3306/story?useSSL=false&amp;characterEncoding=UTF-8</url>
        <username>root</username>
        <password>root</password>
    </ds>

</datasource>
```

- 在resources目录下创建sql脚本配置sql.xml
```
<sql>
    <select id="getUser" db="mysql">
        select * from user
        <where>
            <if test = "id != null">
                id &lt;= #{id}
            </if>
        </where>
    </select>

    <select id="getUserIn" db="local_mysql">
        select * from user where id in
        <foreach collection="ids" open="(" close=")" separator=",">
            #{item}
        </foreach>

    </select>

</sql>
```

- 在application.properties中配置xml地址
```
dbapi.config.datasource=classpath:ds.xml
dbapi.config.sql=classpath:sql.xml
```

- 新建controller，注入DBApi，通过DBApi就可以执行sql
```
@RestController
public class HomeController {

    @Autowired
    DBApi dbApi;

    @RequestMapping("/hello")
    public ResponseDto hello(@RequestBody Map<String,Object> map) {
        # 第一个参数是sql执行的所有参数，一定要封装成Map<String,Object>类型
        # 第二个参数是上一步sql.xml中sql脚本对应的id
        ResponseDto execute = dbApi.execute(map, "getUser");
        return execute;
    }

    @RequestMapping("/getUserIn")
    public ResponseDto getUserIn(@RequestBody Map<String,Object> map) {

        # 第一个参数是sql执行的所有参数，一定要封装成Map<String,Object>类型
        # 第二个参数是上一步sql.xml中sql脚本对应的id
        ResponseDto execute = dbApi.execute(map, "getUserIn");
        return execute;
    }
}
```

- 这样，对于sql执行类的http api就已经开发完成了，接下来启动springboot应用，访问接口看看结果（我用python访问的）

```
import json
import requests

data = {"ids": [1, 2, 3, 4, 5, 6]}
re = requests.post("http://localhost:8888/getUserIn", json.dumps(data), headers={"Content-Type": "application/json"})
print(re.text)


#执行结果：
#{"msg":"dbApi接口访问成功","data":[{"name":"tom","id":1,"age":12,"height":1.83},{"name":"lily","id":2,"age":45,"height":1.75},{"name":"sss","id":5,"age":2,"height":1.65},{"name":"sss","id":6,"age":34,"height":1.84}],"success":true}
```


```
import json
import requests

data = {"id":3}
re = requests.post("http://localhost:8888/hello", json.dumps(data),headers={"Content-Type": "application/json"})print(re.text)


#执行结果：
#{"msg":"dbApi接口访问成功","data":[{"name":"tom","id":1,"age":12,"height":1.83},{"name":"lily","id":2,"age":45,"height":1.75}],"success":true}

```

- 案例完整代码请查看 [dbApi-spring-boot-starter-demo](https://gitee.com/freakchicken/dbApi-spring-boot-starter-demo.git)

## xml配置详解
### 数据库配置

### sql配置