<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Available Slots</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><h1>Available verification slots</h1><p class="muted">Select a suitable future date and time.</p>
<c:if test="${empty slots}"><div class="card"><h3>No slots available</h3><p class="muted">Please check again later.</p></div></c:if>
<div class="grid"><c:forEach var="s" items="${slots}"><div class="card"><span class="badge">${s.slotDate}</span><h2>${s.startTime} – ${s.endTime}</h2><p class="muted">${s.capacity - s.bookedCount} appointment(s) remaining</p><form method="post" action="${pageContext.request.contextPath}/app/book" onsubmit="return confirm('Confirm this verification appointment?')"><input type="hidden" name="slotId" value="${s.slotId}"><button class="btn success" type="submit">Book this slot</button></form></div></c:forEach></div>
<div class="card"><h3>JSON availability endpoint</h3><p class="small muted">The system also exposes a JSON representation for available slots.</p><a class="btn secondary" href="${pageContext.request.contextPath}/api/slots" target="_blank">View JSON</a></div>
</div><script src="${pageContext.request.contextPath}/assets/js/app.js"></script></body></html>
