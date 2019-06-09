<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
    <style type="text/css">
        .table_area {
            margin-top: 50px;
        }

        table, table tr th, table tr td {
            border: 1px solid #9fa8cb;
            padding: 5px;
            font-size: 14px;
        }

        table {
            text-align: center;
            border-collapse: collapse;
            margin-top: 5px;
            table-layout: fixed;
            word-break: break-strict;
            width: 90%;
        }

        th {
            background: #4e71ba;
            color: #FFFFFF;
        }
        tr:hover {
            background-color: #7d97cd;
            color: #fff;
        }

        li {
            list-style: none;
            margin: 4px;
        }

        a:link {
            text-decoration: none;
            color: #2b3c63;
        }
        a:hover{
            text-decoration: underline;
        }

        .back {
            margin-top: 5px;
            font-size: 12px;
        }

        body {
            font-family: "DejaVu Sans Mono", "Droid Sans Mono", Consolas, Menlo, Monaco, "Courier New", "PingFang SC", "Microsoft Yahei", STSong, SimSun, sans-serif;
            color: #12141d;
        }
        .notnull{
            color: #c71585;
        }
    </style>
</head>
<body>

<div id="index">
    <div>目录索引</div>
    <div>
        <ul>
            <#list tables as table>
                <li><a href="#${table.name}">${table.name}（${table.comments}）</a></li>
            </#list>
        </ul>
    </div>

</div>

<div>
    <#list tables as table>
        <div class="table_area" id="${table.name}">
            <div>${table.name}（${table.comments}）</div>
            <div>
                <table>
                    <thead>
                    <th width="27%">字段名</th>
                    <th width="12%">字段类型</th>
                    <th width="8%">长度</th>
                    <th width="6%">是否可空</th>
                    <th width="12%">默认值</th>
                    <th width="35%">注释</th>
                    </thead>
                    <tbody>
                    <#list table.columns as column>
                        <tr>
                            <td>${column.columnName}</td>
                            <td>${column.columnType}</td>
                            <td>
                                <#if column.isNumber == '1'>
                                    ${column.columnPrecision!''}<#if column.columnScale?? && column.columnScale != '0'>,${column.columnScale}</#if>
                                <#else>
                                    ${column.columnLength!''}
                                </#if>
                            </td>
                            <td><span<#if column.nullable?? && column.nullable == 'N'> class="notnull" </#if>>${column.nullable!''}</span></td>
                            <td>${column.defaultValue!''}</td>
                            <td>${column.columnComment!''}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="back">
                <a href="#index">返回目录</a>
            </div>
        </div>
    </#list>
</div>

</body>
</html>