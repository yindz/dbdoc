package com.apifan.other;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * 数据表元数据导出工具
 *
 * @author yin
 */
public class TableExporter {
    private static final String MYSQL_URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&autoReconnect=true&characterEncoding=utf8";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String ORACLE_URL = "jdbc:oracle:thin:@%s:%s:%s";
    private static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";

    /**
     * 数据库类型(小写形式)
     */
    private String dbType;

    /**
     * 数据库SCHEMA名称
     */
    private String schemaName;

    /**
     * 数据库连接实例
     */
    private Connection connection;

    /**
     * 构造函数
     *
     * @param jdbcParam JDBC参数
     */
    public TableExporter(JdbcParam jdbcParam) {
        if (!"mysql".equalsIgnoreCase(jdbcParam.getDbType()) && !"oracle".equalsIgnoreCase(jdbcParam.getDbType())) {
            throw new IllegalArgumentException("未知的数据库类型" + jdbcParam.getDbType());
        }
        this.dbType = jdbcParam.getDbType();
        this.schemaName = jdbcParam.getSchema();
        String driverName = "", url = "";
        if ("mysql".equalsIgnoreCase(dbType)) {
            driverName = MYSQL_DRIVER;
            url = String.format(MYSQL_URL, jdbcParam.getHost(), jdbcParam.getPort(), jdbcParam.getServiceName());
        } else if ("oracle".equalsIgnoreCase(dbType)) {
            driverName = ORACLE_DRIVER;
            url = String.format(ORACLE_URL, jdbcParam.getHost(), jdbcParam.getPort(), jdbcParam.getServiceName());
        }
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类！");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, jdbcParam.getUsername(), jdbcParam.getPassword());
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
    }

    /**
     * 导出
     *
     * @param outputPath 输出路径
     * @param withSQL    是否输出SQL语句
     */
    public void export(String outputPath, boolean withSQL) throws Exception {
        if (outputPath == null) {
            throw new IllegalArgumentException("输出路径为空");
        }
        if (!outputPath.endsWith(File.separator)) {
            outputPath += File.separator;
        }
        //检查路径
        File outputDir = new File(outputPath);
        if (outputDir.exists()) {
            if (!outputDir.isDirectory()) {
                throw new RuntimeException("路径" + outputPath + "不是一个目录");
            }
        } else {
            if (!outputDir.mkdirs()) {
                throw new RuntimeException("创建目录" + outputPath + "失败");
            }
        }
        Configuration conf = new Configuration(Configuration.VERSION_2_3_28);
        conf.setClassForTemplateLoading(this.getClass(), "/");
        Template docTemplate = conf.getTemplate("doc.ftl");


        List<Map<String, Object>> tables = new ArrayList<>();
        List<String> tableNamesList = getAllTableNames(schemaName.toUpperCase());
        if (tableNamesList == null || tableNamesList.isEmpty()) {
            throw new RuntimeException("没有表");
        }
        for (String tableName : tableNamesList) {
            List<ColumnInfo> columnInfoList = getColumnInfo(tableName);
            if (columnInfoList == null || columnInfoList.isEmpty()) {
                continue;
            }
            //表基本信息
            Map<String, Object> table = new HashMap<>();
            table.put("name", columnInfoList.get(0).getTableName());
            table.put("comments", columnInfoList.get(0).getTableComment());

            //字段
            table.put("columns", columnInfoList);
            tables.add(table);
        }
        //组装数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("tables", tables);

        File docFile = new File(outputPath + this.schemaName + ".html");
        Writer docOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));

        //导出
        docTemplate.process(dataMap, docOut);
        if (withSQL) {
            Template sqlTemplate = conf.getTemplate("sql_" + this.dbType + ".ftl");
            File sqlFile = new File(outputPath + this.schemaName + ".sql");
            Writer sqlOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFile)));
            sqlTemplate.process(dataMap, sqlOut);
        }
        //关闭连接
        connection.close();
        System.out.println(this.schemaName + "下面所有表已导出到 " + outputPath);
    }

    /**
     * 获取所有表名
     *
     * @param schemaName SCHEMA名称
     * @return SCHEMA下面所有表名
     */
    private List<String> getAllTableNames(String schemaName) {
        String sql = "";
        if ("mysql".equalsIgnoreCase(dbType)) {
            sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='" + schemaName + "' AND table_type='BASE TABLE' ORDER BY table_name";
        } else if ("oracle".equalsIgnoreCase(dbType)) {
            sql = "select x.TABLE_NAME from all_tables x where x.OWNER='" + schemaName + "' order by x.TABLE_NAME";
        }
        try (Statement st = this.connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery(sql);
            if (rs == null) {
                return null;
            }
            List<String> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(rs.getString(1));
            }
            rs.close();
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取字段信息
     *
     * @param tableName 表名
     * @return 表所有字段
     */
    private List<ColumnInfo> getColumnInfo(String tableName) {
        List<ColumnInfo> resultList = new ArrayList<>();
        if ("oracle".equalsIgnoreCase(dbType)) {
            String sql = "select c.TABLE_NAME" +
                    ",a.comments as table_comment" +
                    ",c.COLUMN_NAME" +
                    ",c.DATA_TYPE" +
                    ",c.DATA_LENGTH" +
                    ",c.DATA_PRECISION" +
                    ",c.DATA_SCALE" +
                    ",c.NULLABLE" +
                    ",c.CHAR_LENGTH" +
                    ",c.DATA_DEFAULT" +
                    ",d.comments as column_comment " +
                    "from user_tab_columns c " +
                    "left join user_tab_comments a on c.TABLE_NAME=a.table_name " +
                    "left join user_col_comments d on c.TABLE_NAME=d.table_name and c.COLUMN_NAME=d.column_name " +
                    "where c.TABLE_NAME='" + tableName + "'" +
                    "order by c.COLUMN_ID";
            try (Statement st = connection.prepareStatement(sql)) {
                ResultSet rs = st.executeQuery(sql);
                if (rs == null) {
                    return null;
                }
                while (rs.next()) {
                    ColumnInfo col = new ColumnInfo();
                    col.setTableName(rs.getString(1));
                    col.setTableComment(rs.getString(2));
                    col.setColumnName(rs.getString(3));
                    col.setColumnType(rs.getString(4));
                    col.setColumnLength(rs.getString(5));
                    col.setColumnPrecision(rs.getString(6));
                    col.setColumnScale(rs.getString(7));
                    col.setNullable(rs.getString(8));
                    col.setCharLength(rs.getString(9));
                    col.setDefaultValue(rs.getString(10));
                    if (col.getDefaultValue() != null) {
                        col.setDefaultValue(col.getDefaultValue().trim());
                        if ("null".equalsIgnoreCase(col.getDefaultValue()) || col.getDefaultValue().length() == 0) {
                            col.setDefaultValue(null);
                        }
                    }
                    col.setColumnComment(rs.getString(11));

                    //处理
                    if ("CHAR".equalsIgnoreCase(col.getColumnType())
                            || "VARCHAR".equalsIgnoreCase(col.getColumnType())
                            || "NVARCHAR".equalsIgnoreCase(col.getColumnType())
                            || "VARCHAR2".equalsIgnoreCase(col.getColumnType())
                            || "NVARCHAR2".equalsIgnoreCase(col.getColumnType())) {
                        if (col.getDefaultValue() != null) {
                            col.setDefaultValue("'" + col.getDefaultValue().trim() + "'");
                        }
                    }
                    if (col.getColumnPrecision() != null) {
                        col.setIsNumber("1");
                    }
                    resultList.add(col);
                }
                rs.close();
                return resultList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if ("mysql".equalsIgnoreCase(dbType)) {
            String sql = "SELECT c.TABLE_NAME" +
                    ", t.TABLE_COMMENT" +
                    ", c.COLUMN_NAME" +
                    ", c.COLUMN_COMMENT" +
                    ", c.DATA_TYPE" +
                    ", c.NUMERIC_PRECISION" +
                    ", c.NUMERIC_SCALE" +
                    ", c.CHARACTER_MAXIMUM_LENGTH" +
                    ", c.CHARACTER_OCTET_LENGTH" +
                    ", c.COLUMN_DEFAULT" +
                    ", c.IS_NULLABLE" +
                    " FROM information_schema.columns c " +
                    "INNER JOIN information_schema.tables t ON c.TABLE_NAME=t.TABLE_NAME AND c.TABLE_SCHEMA=t.TABLE_SCHEMA " +
                    "WHERE c.table_schema='" + schemaName + "' AND c.table_name='" + tableName + "'" +
                    "ORDER BY c.ORDINAL_POSITION";
            try (Statement st = connection.prepareStatement(sql)) {
                ResultSet rs = st.executeQuery(sql);
                if (rs == null) {
                    return null;
                }
                while (rs.next()) {
                    ColumnInfo col = new ColumnInfo();
                    col.setTableName(rs.getString(1));
                    col.setTableComment(rs.getString(2));
                    col.setColumnName(rs.getString(3));
                    col.setColumnComment(rs.getString(4));
                    col.setColumnType(rs.getString(5));
                    col.setDefaultValue(rs.getString(10));
                    if (col.getDefaultValue() != null) {
                        col.setDefaultValue(col.getDefaultValue().trim());
                        if ("null".equalsIgnoreCase(col.getDefaultValue()) || col.getDefaultValue().length() == 0) {
                            col.setDefaultValue(null);
                        }
                    }
                    if ("bigint".equalsIgnoreCase(col.getColumnType())
                            || "int".equalsIgnoreCase(col.getColumnType())
                            || "smallint".equalsIgnoreCase(col.getColumnType())
                            || "mediumint".equalsIgnoreCase(col.getColumnType())
                            || "tinyint".equalsIgnoreCase(col.getColumnType())
                            || "float".equalsIgnoreCase(col.getColumnType())
                            || "double".equalsIgnoreCase(col.getColumnType())
                            || "decimal".equalsIgnoreCase(col.getColumnType())) {
                        //数字类型
                        col.setIsNumber("1");
                        col.setColumnPrecision(rs.getString(6));
                        col.setColumnScale(rs.getString(7));
                        col.setColumnLength(col.getColumnPrecision());
                    } else if ("char".equalsIgnoreCase(col.getColumnType()) || "varchar".equalsIgnoreCase(col.getColumnType())) {
                        //字符型
                        col.setCharLength(rs.getString(8));
                        col.setColumnLength(col.getCharLength());
                    } else {
                        col.setColumnLength(rs.getString(9));
                    }
                    col.setNullable("YES".equalsIgnoreCase(rs.getString(11)) ? "Y" : "N");
                    resultList.add(col);
                }
                rs.close();
                return resultList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
