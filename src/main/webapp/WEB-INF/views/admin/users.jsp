<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Users</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><h1>Registered users</h1><div class="card"><div class="table-wrap"><table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th></tr></thead><tbody>
<c:forEach var="u" items="${users}"><tr><td>${u.userId}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.phone}</td><td><span class="badge">${u.role}</span></td></tr></c:forEach>
</tbody></table></div></div></div></body></html>
