<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Register</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><div class="card auth"><h2>Create applicant account</h2>
<c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>
<form method="post" action="${pageContext.request.contextPath}/register" data-validate>
<div class="field"><label>Full name</label><input name="name" minlength="3" required></div>
<div class="field"><label>Email</label><input type="email" name="email" required></div>
<div class="field"><label>Phone</label><input name="phone" pattern="[0-9]{10,15}" required></div>
<div class="field"><label>Password</label><input id="password" type="password" name="password" minlength="6" required></div>
<div class="field"><label>Confirm password</label><input id="confirmPassword" type="password" required></div>
<button class="btn" type="submit">Register</button></form></div></div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script></body></html>
