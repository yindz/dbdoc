<#list tables as table>

CREATE TABLE `${table.name}`
(
    <#list table.columns as column>
    `${column.columnName}` ${column.columnType}(<#if column.isNumber == '1'>${column.columnPrecision!''}<#if column.columnScale?? && column.columnScale != '0'>,${column.columnScale}</#if><#else>${column.columnLength!''}</#if>)<#if column.nullable?? && column.nullable == 'N'> NOT NULL</#if><#if column.defaultValue?? && column.defaultValue != 'null'> DEFAULT <#if column.columnType == 'NUMBER'>${column.defaultValue!''}<#else>'${column.defaultValue!''}'</#if></#if> COMMENT '${column.columnComment!''}'<#if column_has_next>,</#if>
    </#list>
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='${table.comments}';

</#list>