<#list tables as table>

create table ${table.name}
(
    <#list table.columns as column>
    ${column.columnName} ${column.columnType}(<#if column.isNumber == '1'>${column.columnPrecision!''}<#if column.columnScale?? && column.columnScale != '0'>,${column.columnScale}</#if><#else>${column.columnLength!''}</#if>)<#if column.defaultValue??> default ${column.defaultValue!''}</#if><#if column.nullable?? && column.nullable == 'N'> not null</#if><#if column_has_next>,</#if>
    </#list>
);
comment on table ${table.name} is '${table.comments}';

<#list table.columns as column>
comment on column ${column.tableName}.${column.columnName} is '${column.columnComment!''}';
</#list>

</#list>