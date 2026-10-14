<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Error</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><div class="card center"><h1>Something went wrong</h1><div class="alert error">${error}</div><a class="btn" href="${pageContext.request.contextPath}/home">Return Home</a></div></div></body></html>
