<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Login</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><div class="card auth"><h2>Welcome back</h2><p class="muted">Sign in to manage your verification appointment.</p>
<c:if test="${not empty error}"><div class="alert error">${error}</div></c:if><c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login" data-validate>
<div class="field"><label>Email</label><input type="email" name="email" required></div>
<div class="field"><label>Password</label><input type="password" name="password" required></div>
<button class="btn" type="submit">Login</button></form><p class="small muted">New applicant? <a href="${pageContext.request.contextPath}/register" style="color:#20639b">Create an account</a></p>
</div></div><script src="${pageContext.request.contextPath}/assets/js/app.js"></script></body></html>
