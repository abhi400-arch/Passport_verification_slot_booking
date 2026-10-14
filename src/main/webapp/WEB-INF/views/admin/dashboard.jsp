<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Administrator Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../includes/header.jspf" %>

<div class="container">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px;">
    <div>
      <h1 style="margin: 0;">Administrator Command Hub 🛡️</h1>
      <p class="muted" style="margin: 4px 0 0;">Oversee passport appointments, counter check-ins, pre-screening vaults, and waitlists.</p>
    </div>
    <div>
      <a class="btn gold" href="${pageContext.request.contextPath}/admin/verify-pass">
        📷 Open Gate Scanner Terminal
      </a>
    </div>
  </div>

  <c:if test="${not empty error}">
    <div class="alert error">${error}</div>
  </c:if>

  <!-- Metric Overview Grid -->
  <div class="grid three" style="margin-bottom: 22px;">
    <div class="stat">
      <div class="muted">Registered Applicants</div>
      <div class="num">${userCount}</div>
    </div>
    <div class="stat secondary">
      <div class="muted">Available Future Slots</div>
      <div class="num">${slotCount}</div>
    </div>
    <div class="stat gold">
      <div class="muted">Confirmed Bookings</div>
      <div class="num">${bookingCount}</div>
    </div>
  </div>

  <div class="grid three" style="margin-bottom: 22px;">
    <div class="stat danger">
      <div class="muted">⚡ Tatkal Active Waitlist</div>
      <div class="num">${waitlistCount}</div>
    </div>
    <div class="stat">
      <div class="muted">📁 Verified Vault Documents</div>
      <div class="num">${verifiedDocsCount}</div>
    </div>
    <div class="stat secondary">
      <div class="muted">🏢 Today's Gate Check-ins</div>
      <div class="num">${todayCheckins}</div>
    </div>
  </div>

  <!-- Operational Action Cards -->
  <div class="grid two">
    <div class="card">
      <h2>🛡️ Counter Gate Scanner & Verifier</h2>
      <p class="muted">Scan or search applicant QR digital passes to verify identities, inspect uploaded document vaults, and process biometric clearances.</p>
      <a class="btn accent" href="${pageContext.request.contextPath}/admin/verify-pass">Launch Scanner Portal</a>
    </div>

    <div class="card">
      <h2>📊 Live Booking & System Analytics</h2>
      <p class="muted">Inspect peak booking hours, popular days, slot capacity utilization, and waitlist metrics.</p>
      <a class="btn secondary" href="${pageContext.request.contextPath}/admin/analytics">View Real-Time Analytics</a>
    </div>
  </div>

  <div class="card">
    <h2>Operations Management</h2>
    <div class="actions">
      <a class="btn" href="${pageContext.request.contextPath}/admin/slots">Manage Slots</a>
      <a class="btn" href="${pageContext.request.contextPath}/admin/bookings">All Bookings</a>
      <a class="btn secondary" href="${pageContext.request.contextPath}/admin/users">Registered Users</a>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Administrator Portal
</div>
</body>
</html>
