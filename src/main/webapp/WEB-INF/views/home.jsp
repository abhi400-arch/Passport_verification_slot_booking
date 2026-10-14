<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Passport Verification & Appointment System</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <section class="hero">
    <div>
      <span class="badge gold" style="margin-bottom: 10px;">GOV E-SERVICES • JAKARTA SERVLETS • JDBC</span>
      <h1>Next-Gen Passport Verification & Slot Booking</h1>
      <p>Modernized passport verification portal equipped with paperless QR Digital Appointment Passes, algorithmic Document Pre-Screening Vaults, and intelligent Tatkal Emergency Waitlists.</p>
      <div class="actions" style="margin-top: 20px;">
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <a class="btn gold" href="${pageContext.request.contextPath}/app/dashboard">Go to Dashboard</a>
            <a class="btn secondary" href="${pageContext.request.contextPath}/app/pass">My Digital Pass</a>
          </c:when>
          <c:otherwise>
            <a class="btn gold" href="${pageContext.request.contextPath}/register">Create Account</a>
            <a class="btn secondary" href="${pageContext.request.contextPath}/login">Sign In</a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div class="iconbox">
      <div>
        <div style="font-size: 65px;">🛂</div>
        <div class="small" style="font-weight: 700; color: #0b1f36; margin-top: 6px;">Zero-Defect Verification</div>
      </div>
    </div>
  </section>

  <!-- Real-World Innovation Highlights Grid -->
  <h2 style="margin: 35px 0 15px; color: var(--primary);">Real-World Digital Innovations</h2>
  <div class="grid three">
    <div class="card" style="border-top: 4px solid #d4af37;">
      <div style="font-size: 32px; margin-bottom: 10px;">🎫</div>
      <h3>Dynamic Digital E-Pass & Gate Terminal</h3>
      <p class="muted">
        Instant printable appointment pass featuring cryptographic QR codes, security verification hashes, barcode tokens, and a counter terminal for officer gate check-in and biometric capture.
      </p>
    </div>

    <div class="card" style="border-top: 4px solid #00a896;">
      <div style="font-size: 32px; margin-bottom: 10px;">📁</div>
      <h3>Smart Document Pre-Screening Vault</h3>
      <p class="muted">
        Algorithmic validation engine with mathematical Verhoeff Aadhaar parity validation, PAN regex checks, and real-time Document Compliance Scores for zero counter rejection.
      </p>
    </div>

    <div class="card" style="border-top: 4px solid #d9383a;">
      <div style="font-size: 32px; margin-bottom: 10px;">⚡</div>
      <h3>Tatkal Priority Waitlist & Alert Hub</h3>
      <p class="muted">
        Automated reallocation engine. When users cancel bookings or emergency quotas are released, the system immediately matches waitlisted applicants and dispatches in-app priority alerts.
      </p>
    </div>
  </div>

  <div class="grid three" style="margin-top: 15px;">
    <div class="card">
      <div style="font-size: 28px; margin-bottom: 8px;">🧠</div>
      <h4>Smart Assistant</h4>
      <p class="muted small">Intelligent readiness scoring and algorithmic slot recommendation based on user schedule preferences.</p>
    </div>
    <div class="card">
      <div style="font-size: 28px; margin-bottom: 8px;">📊</div>
      <h4>Operations Analytics</h4>
      <p class="muted small">Live analytics on peak verification hours, popular days, capacity utilization, and waitlist queues.</p>
    </div>
    <div class="card">
      <div style="font-size: 28px; margin-bottom: 8px;">🌐</div>
      <h4>REST JSON APIs</h4>
      <p class="muted small">Secure JSON endpoints for slot availability (<code>/api/slots</code>), passport profiles (<code>/api/passport</code>), and alerts (<code>/api/notifications/count</code>).</p>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification Slot Booking Management System &copy; Republic Portal
</div>
</body>
</html>
