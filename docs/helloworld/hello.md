# hellowolrd サンプル


# Connect to postgres

##　postgresを起動

`$  postgres -D /usr/local/var/postgres`

##　postgresを接続

`$ psql -U postgres postgres`

##　サンプルテーブル作成

```
# 
CREATE TABLE tbl_reserve (
    reserve_id            varchar(8),
    reserve_date         date
);

# 
insert into tbl_reserve(reserve_id,reserve_date) values('00000001',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000002',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000003',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000004',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000005',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000006',NOW());
insert into tbl_reserve(reserve_id,reserve_date) values('00000007',NOW());

# select * from tbl_reserve;
 reserve_id | reserve_date 
------------+--------------
 00000001   | 2021-04-26
 00000002   | 2021-04-26
 00000003   | 2021-04-26
 00000004   | 2021-04-26
 00000005   | 2021-04-26
 00000006   | 2021-04-26
 00000007   | 2021-04-26
```

# Tomcat 設定

JDBC DRIVER をダウンロードする

https://jdbc.postgresql.org/download.html

ダウンロードしたJarファイルを apache-tomcat-7.0.108/lib 配下に置く

## Resource設定

apache-tomcat-7.0.108/conf/context.xml に下記の内容を足す。

```
    <Resource
            name="jdbc/postgresql"
            type="javax.sql.DataSource"
            auth="Container"
            driverClassName="org.postgresql.Driver"
            url="jdbc:postgresql://localhost:5432/postgres"
            username="postgres"
            password="postgres"
            maxActive="20"
            maxIdel="5"
            maxWait="5000"
    />
```

## Web.xml 設定

```
    <resource-ref>
        <description>PostgreSQL connection resource</description>
        <res-ref-name>jdbc/postgresql</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <res-auth>Container</res-auth>
    </resource-ref>
```

## サンプル画面

```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="javax.naming.Context"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.NamingException"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.IOException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
out.println("Hello PostgreSQL");
Connection conn = null;
try {
    Context initContext = new InitialContext();
    Context envContext = (Context)initContext.lookup("java:/comp/env");
    DataSource ds = (DataSource)envContext.lookup("jdbc/postgresql");
    conn = ds.getConnection();

    if (conn != null) {
        out.println("connection is NOT null");
    } else {
        out.println("connection is null");
    }
} catch (Exception ex) {
    out.println(ex.toString());
}

try {
    out.println("<ul>");
    out.println("<li>custom id</li>");

    PreparedStatement statement = conn.prepareStatement("select reserve_id,reserve_date from tbl_reserve");
    ResultSet result=statement.executeQuery();
    while(result.next()){
        out.println("<li>"+result.getString(1)+"</li>");
    }
    out.println("</ul>");
} catch(SQLException ex) {
    out.println(ex.toString());
}

%>

</body>
</html>
```