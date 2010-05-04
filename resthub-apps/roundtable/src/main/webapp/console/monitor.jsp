<%-- 
    Document   : monitor
    Created on : 31 mars 2010, 10:10:42
    Author     : Nicolas Carlier
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.jamonapi.MonitorFactory"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Monitoring</title>
    </head>
    <body>
        <h1>Reports</h1>
<%
out.println(MonitorFactory.getReport());
%>
    </body>
</html>
