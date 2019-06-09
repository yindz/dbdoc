package com.apifan.other;

/**
 * 列基本属性
 *
 * @author yinzhili
 */
public class ColumnInfo {

    /**
     * 所在表名称
     */
    private String tableName;

    /**
     * 所在表注释
     */
    private String tableComment;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列注释
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 列长度
     */
    private String columnLength;

    /**
     * 列精度
     */
    private String columnPrecision;

    /**
     * 列小数位数
     */
    private String columnScale;

    /**
     * 是否可空 Y/N
     */
    private String nullable;

    /**
     * 字符类型长度
     */
    private String charLength;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否数字
     */
    private String isNumber = "0";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(String columnPrecision) {
        this.columnPrecision = columnPrecision;
    }

    public String getColumnScale() {
        return columnScale;
    }

    public void setColumnScale(String columnScale) {
        this.columnScale = columnScale;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getCharLength() {
        return charLength;
    }

    public void setCharLength(String charLength) {
        this.charLength = charLength;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getIsNumber() {
        return isNumber;
    }

    public void setIsNumber(String isNumber) {
        this.isNumber = isNumber;
    }
}
