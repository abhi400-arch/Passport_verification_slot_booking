<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Applicant Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 15px;">
    <div>
      <h1 style="margin: 0;">Hello, ${sessionScope.user.fullName} 👋</h1>
      <p class="muted" style="margin: 4px 0 0;">Unified passport verification portal with real-time digital pass, pre-screening vault, and priority waitlist.</p>
    </div>
    <div class="actions">
      <c:if test="${not empty activePass}">
        <a class="btn gold" href="${pageContext.request.contextPath}/app/pass?ref=${activePass.bookingReference}">
          🎫 View Digital E-Pass
        </a>
      </c:if>
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

  <!-- Metric Stats Grid -->
  <div class="grid four" style="margin-bottom: 25px;">
    <div class="stat">
      <div class="muted">Available Slots</div>
      <div class="num">${availableSlots}</div>
    </div>
    <div class="stat secondary">
      <div class="muted">Document Vault</div>
      <div class="num">${empty compliance ? 0 : compliance.score}%</div>
    </div>
    <div class="stat gold">
      <div class="muted">Passport Profile</div>
      <div class="num" style="font-size: 22px;">${empty passport ? 'Pending' : 'Ready'}</div>
    </div>
    <div class="stat">
      <div class="muted">Active Bookings</div>
      <div class="num">${bookings.size()}</div>
    </div>
  </div>

  <!-- Active E-Pass Banner if available -->
  <c:if test="${not empty activePass && activePass.bookingStatus != 'CANCELLED'}">
    <div class="card" style="border-left: 6px solid #d4af37; background: linear-gradient(135deg, #fffdfa 0%, #ffffff 100%);">
      <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
        <div>
          <span class="badge gold" style="margin-bottom: 6px;">CONFIRMED APPOINTMENT</span>
          <h2 style="margin: 0; color: #0b1f36;">Ref: ${activePass.bookingReference}</h2>
          <p class="muted" style="margin: 6px 0 0;">
            📅 Date: <strong>${activePass.slotDate}</strong> · ⏰ Window: <strong>${activePass.startTime} – ${activePass.endTime}</strong>
          </p>
        </div>
        <div class="actions">
          <a class="btn gold" href="${pageContext.request.contextPath}/app/pass?ref=${activePass.bookingReference}">
            View & Print Digital Pass
          </a>
          <a class="btn secondary" href="${pageContext.request.contextPath}/app/bookings">
            Manage Booking
          </a>
        </div>
      </div>
    </div>
  </c:if>

  <!-- Two Column Feature Layout -->
  <div class="grid two">
    <!-- Passport Profile Card -->
    <div class="card">
      <h2>🛂 Passport Profile</h2>
      <c:choose>
        <c:when test="${empty passport}">
          <p class="muted">Add your passport profile details before booking an appointment.</p>
          <a class="btn" href="${pageContext.request.contextPath}/app/passport">Add Passport Details</a>
        </c:when>
        <c:otherwise>
          <p><strong>${passport.passportNumber}</strong> · ${passport.applicationType}</p>
          <p class="muted">${passport.nationality} · Date of birth: ${passport.dateOfBirth}</p>
          <p class="muted small">${passport.address}</p>
          <a class="btn secondary" href="${pageContext.request.contextPath}/app/passport">Edit Profile</a>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Innovation 2: Document Vault Card -->
    <div class="card">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 style="margin: 0;">📁 Document Pre-Screening Vault</h2>
        <span class="badge ${compliance.score >= 75 ? 'success' : 'warning'}">${compliance.score}% Ready</span>
      </div>
      <p class="muted" style="margin: 10px 0 16px;">
        Pre-verify Aadhaar, DOB, and Address proofs to ensure zero rejection at the counter.
      </p>
      <div class="actions">
        <a class="btn accent" href="${pageContext.request.contextPath}/app/documents">Open Document Vault</a>
        <a class="btn secondary" href="${pageContext.request.contextPath}/app/smart-assistant">Smart Assistant</a>
      </div>
    </div>
  </div>

  <!-- Innovation 3: Tatkal Waitlist & Booking Actions -->
  <div class="grid two">
    <div class="card">
      <h2>⚡ Tatkal Emergency Waitlist</h2>
      <p class="muted">Slots fully booked? Register in the priority waitlist to automatically receive notifications when cancellations open up.</p>
      <a class="btn secondary" href="${pageContext.request.contextPath}/app/waitlist">View / Join Waitlist</a>
    </div>

    <div class="card">
      <h2>📅 Book Verification Slot</h2>
      <p class="muted">Select from available appointment slots at your designated Passport Seva Kendra.</p>
      <a class="btn" href="${pageContext.request.contextPath}/app/slots">View Available Slots</a>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Applicant Portal
</div>
</body>
</html>
