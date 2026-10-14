<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>All Bookings - Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../includes/header.jspf" %>

<div class="container">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <div>
      <h1 style="margin: 0;">All Appointments & Bookings</h1>
      <p class="muted" style="margin: 4px 0 0;">Inspect applicant appointments, monitor live status, and launch counter clearance terminals.</p>
    </div>
    <div>
      <a class="btn gold" href="${pageContext.request.contextPath}/admin/verify-pass">
        📷 Gate Scanner Terminal
      </a>
    </div>
  </div>

  <div class="card">
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Reference</th>
            <th>Applicant Name</th>
            <th>Passport</th>
            <th>Date</th>
            <th>Time Window</th>
            <th>Lifecycle Status</th>
            <th>Officer Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td>
                <strong style="font-family: monospace; color: #0b1f36;">${b.bookingReference}</strong>
              </td>
              <td>${b.applicantName}</td>
              <td>${b.passportNumber}</td>
              <td>📅 ${b.slotDate}</td>
              <td>⏰ ${b.startTime} – ${b.endTime}</td>
              <td>
                <span class="badge ${b.bookingStatus == 'APPROVED' ? 'success' : (b.bookingStatus == 'CANCELLED' ? 'danger' : (b.bookingStatus == 'CHECKED_IN' ? 'secondary' : 'gold'))}">
                  ${b.bookingStatus}
                </span>
              </td>
              <td>
                <div class="actions">
                  <a class="btn accent small" href="${pageContext.request.contextPath}/admin/verify-pass?ref=${b.bookingReference}">
                    🛡️ Scan / Verify
                  </a>
                  <a class="btn secondary small" href="${pageContext.request.contextPath}/app/pass?ref=${b.bookingReference}">
                    🎫 Pass
                  </a>
                </div>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty bookings}">
            <tr>
              <td colspan="7" class="center muted" style="padding: 25px;">No bookings found.</td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Administrator Portal
</div>
</body>
</html>
