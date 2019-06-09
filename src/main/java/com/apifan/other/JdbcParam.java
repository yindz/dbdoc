package com.apifan.other;

/**
 * JDBC参数
 *
 * @author yinzhili
 */
public class JdbcParam {

    /**
     * 数据库类型:mysql/oracle
     */
    private String dbType;

    /**
     * 主机名或IP
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 数据库实例名
     */
    private String serviceName;

    /**
     * SCHEMA名称
     */
    private String schema;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
