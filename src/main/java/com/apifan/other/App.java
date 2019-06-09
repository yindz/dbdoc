package com.apifan.other;

/**
 * 入口类
 */
public class App {

    public static void main(String[] args) throws Exception {
        JdbcParam param = new JdbcParam();
        param.setDbType(getString("dbType", null));
        param.setHost(getString("host", null));
        param.setPort(getString("port", null));
        param.setSchema(getString("schema", null));
        param.setUsername(getString("username", null));
        param.setPassword(getString("password", null));
        param.setServiceName(getString("serviceName", null));

        String outPath = getString("outPath", null);
        boolean withSQL = "true".equalsIgnoreCase(getString("withSQL", null));
        TableExporter exporter = new TableExporter(param);
        exporter.export(outPath, withSQL);
    }

    /**
     * 读取命令行参数
     *
     * @param name         属性名
     * @param defaultValue 未找到属性时返回的默认值
     * @return 字符串值
     */
    private static String getString(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }
}
