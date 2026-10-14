<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Passport Details</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><div class="card"><h2>Passport verification details</h2><p class="muted">Enter the information needed for your verification appointment.</p>
<c:if test="${not empty error}"><div class="alert error">${error}</div></c:if><c:if test="${not empty sessionScope.success}"><div class="alert success">${sessionScope.success}</div><c:remove var="success" scope="session"/></c:if>
<form method="post" action="${pageContext.request.contextPath}/app/passport" data-validate><div class="form-grid">
<div class="field"><label>Passport number</label><input name="passportNumber" value="${passport.passportNumber}" placeholder="A1234567" required></div>
<div class="field"><label>Date of birth</label><input type="date" name="dob" value="${passport.dateOfBirth}" required></div>
<div class="field"><label>Nationality</label><input name="nationality" value="${empty passport ? 'Indian' : passport.nationality}" required></div>
<div class="field"><label>Application type</label><select name="applicationType" required><option value="NEW" ${passport.applicationType == 'NEW' ? 'selected':''}>New Passport</option><option value="RENEWAL" ${passport.applicationType == 'RENEWAL' ? 'selected':''}>Renewal</option></select></div>
<div class="field full"><label>Address</label><textarea name="address" required>${passport.address}</textarea></div>
</div><button class="btn" type="submit">Save Passport Details</button></form></div></div><script src="${pageContext.request.contextPath}/assets/js/app.js"></script></body></html>
