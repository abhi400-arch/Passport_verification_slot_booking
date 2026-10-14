<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Booking Confirmed</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div class="card center" style="max-width: 650px; margin: 30px auto; padding: 35px;">
    <div style="font-size: 60px; margin-bottom: 10px;">✅</div>
    <h1 style="margin: 0 0 10px; color: var(--primary);">Booking Confirmed!</h1>
    <p class="muted">Your passport verification appointment has been secured successfully.</p>

    <div style="background: #f8fafc; border: 2px dashed #cbd5e1; border-radius: 12px; padding: 20px; margin: 20px 0;">
      <div class="muted small" style="text-transform: uppercase;">Appointment Reference</div>
      <h2 style="margin: 4px 0 15px; color: #0b1f36; font-family: monospace; letter-spacing: 1px;">
        ${booking.bookingReference}
      </h2>

      <div class="grid two" style="text-align: left;">
        <div><strong>Applicant</strong><br><span class="muted">${booking.applicantName}</span></div>
        <div><strong>Passport Number</strong><br><span class="muted">${booking.passportNumber}</span></div>
        <div><strong>Appointment Date</strong><br><span class="muted">📅 ${booking.slotDate}</span></div>
        <div><strong>Time Window</strong><br><span class="muted">⏰ ${booking.startTime} – ${booking.endTime}</span></div>
      </div>
    </div>

    <div class="actions" style="justify-content: center; margin-top: 20px;">
      <a class="btn gold" href="${pageContext.request.contextPath}/app/pass?ref=${booking.bookingReference}">
        🎫 View & Print Digital E-Pass
      </a>
      <a class="btn secondary" href="${pageContext.request.contextPath}/app/documents">
        📁 Verify Documents in Vault
      </a>
      <a class="btn" href="${pageContext.request.contextPath}/app/bookings">
        My Bookings
      </a>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Confirmation
</div>
</body>
</html>
