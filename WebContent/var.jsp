<%@ page language="java"%>
<%@ page import="test.*"%>

<%
	String v = StaticFileVersionHelper.getVersion();
%>

<script type="text/javascript" src="http://localhost/<%=v%>/jquery.js"></script>
<link type="text/css" rel="stylesheet"
	href="http://localhost/<%=v%>/css.css" />