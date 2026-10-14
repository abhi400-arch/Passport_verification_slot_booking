<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../includes/header.jspf" %>
<!DOCTYPE html><html><head><title>Manage Slots</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<div class="container"><h1>Manage verification slots</h1>
<c:if test="${not empty error}"><div class="alert error">${error}</div></c:if><c:if test="${not empty sessionScope.success}"><div class="alert success">${sessionScope.success}</div><c:remove var="success" scope="session"/></c:if>
<div class="card"><h2>Add a new slot</h2><form method="post" action="${pageContext.request.contextPath}/admin/slots" data-validate><div class="form-grid">
<div class="field"><label>Date</label><input type="date" name="date" required></div><div class="field"><label>Capacity</label><input type="number" name="capacity" min="1" max="100" value="1" required></div>
<div class="field"><label>Start time</label><input type="time" name="start" required></div><div class="field"><label>End time</label><input type="time" name="end" required></div>
</div><button class="btn" type="submit">Add Slot</button></form></div>
<div class="card"><h2>Future available slots</h2><div class="table-wrap"><table><thead><tr><th>ID</th><th>Date</th><th>Time</th><th>Capacity</th><th>Booked</th><th>Action</th></tr></thead><tbody>
<c:forEach var="s" items="${slots}"><tr><td>${s.slotId}</td><td>${s.slotDate}</td><td>${s.startTime} – ${s.endTime}</td><td>${s.capacity}</td><td>${s.bookedCount}</td><td><button class="btn danger delete-slot" data-id="${s.slotId}" data-url="${pageContext.request.contextPath}/admin/slots">Delete</button></td></tr></c:forEach>
<c:if test="${empty slots}"><tr><td colspan="6">No available slots.</td></tr></c:if></tbody></table></div></div></div><script src="${pageContext.request.contextPath}/assets/js/app.js"></script></body></html>
