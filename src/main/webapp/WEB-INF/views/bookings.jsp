<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>My Bookings</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <div>
      <h1 style="margin: 0;">My Bookings</h1>
      <p class="muted" style="margin: 4px 0 0;">View your appointment status, download official E-Passes, or reschedule appointments.</p>
    </div>
    <div>
      <a class="btn" href="${pageContext.request.contextPath}/app/slots">Book New Slot</a>
    </div>
  </div>

  <c:if test="${not empty sessionScope.success}">
    <div class="alert success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
  </c:if>

  <c:if test="${not empty sessionScope.error}">
    <div class="alert error">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
  </c:if>

  <div class="card">
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Reference</th>
            <th>Passport</th>
            <th>Appointment Date</th>
            <th>Time Window</th>
            <th>Status</th>
            <th>Digital Pass & Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td>
                <strong style="font-family: monospace; color: #0b1f36;">${b.bookingReference}</strong>
              </td>
              <td>${b.passportNumber}</td>
              <td>📅 ${b.slotDate}</td>
              <td>⏰ ${b.startTime} – ${b.endTime}</td>
              <td>
                <span class="badge ${b.bookingStatus == 'APPROVED' ? 'success' : (b.bookingStatus == 'CANCELLED' ? 'danger' : 'gold')}">
                  ${b.bookingStatus}
                </span>
              </td>
              <td>
                <div class="actions">
                  <c:if test="${b.bookingStatus != 'CANCELLED'}">
                    <a class="btn gold small" href="${pageContext.request.contextPath}/app/pass?ref=${b.bookingReference}">
                      🎫 View E-Pass
                    </a>
                    <form method="post" action="${pageContext.request.contextPath}/app/cancel-booking" style="display:inline;" onsubmit="return confirm('Cancel this appointment? Your slot will be released back to the pool.');">
                      <input type="hidden" name="bookingId" value="${b.bookingId}">
                      <button class="btn danger small" type="submit">Cancel</button>
                    </form>
                  </c:if>
                  <c:if test="${b.bookingStatus == 'CANCELLED'}">
                    <span class="muted small">Slot Released</span>
                  </c:if>
                </div>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty bookings}">
            <tr>
              <td colspan="6" class="center muted" style="padding: 30px;">
                No bookings yet. <a href="${pageContext.request.contextPath}/app/slots" style="color: #1a568c; font-weight: 700;">Find an available slot</a>
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Booking Management Module
</div>
</body>
</html>
