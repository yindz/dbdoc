# 数据库文档工具
将数据库中所有表字段及注释等信息输出为便于阅读的HTML网页。

## 编译打包
```
mvn clean package
```

## 运行
### mysql
```
cd target/
java -Dfile.encoding=UTF-8 -DdbType=mysql -Dhost=localhost -Dport=3306 -Dschema=mydb -DserviceName=mydb -Dusername=myuser -Dpassword=123456 -DoutPath=E:\tmp -DwithSQL=true -jar dbdoc-1.0.0-SNAPSHOT.jar
```
### oracle
```
cd target/
java -Dfile.encoding=UTF-8 -DdbType=oracle -Dhost=localhost -Dport=1521 -Dschema=mydb -DserviceName=orcl -Dusername=myuser -Dpassword=123456 -DoutPath=E:\tmp -DwithSQL=true -jar dbdoc-1.0.0-SNAPSHOT.jar
```
### 参数说明
- dbType 数据库类型（目前支持mysql或oracle，小写形式）
- host 数据库主机名或IP
- port 数据库端口号
- schema 数据库schema名称（mysql数据库则是数据库名称）
- serviceName 数据库服务名称（例如：oracle数据库为SID，mysql数据库则是数据库名称）
- username 数据库用户名
- password 数据库密码
- outPath 结果输出目录完整路径
- withSQL 是否也同时输出建表语句


