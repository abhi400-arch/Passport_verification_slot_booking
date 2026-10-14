<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Passport Verification Digital Appointment Pass</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container pass-wrapper">
  <div class="actions no-print" style="margin-bottom: 20px; justify-content: space-between;">
    <a class="btn secondary" href="${pageContext.request.contextPath}/app/bookings">← Back to My Bookings</a>
    <button id="printPassBtn" class="btn gold">🖨️ Print / Download Official E-Pass</button>
  </div>

  <div class="digital-pass">
    <!-- Pass Header -->
    <div class="pass-header">
      <div class="pass-gov-title">
        <div class="emblem">🏛️</div>
        <div>
          <h3>Passport Seva Kendra Appointment Pass</h3>
          <p>Official Digital Verification Token & Gate Clearance Slip</p>
        </div>
      </div>
      <div class="pass-token">
        <div class="muted" style="color: #cbd5e1; font-size: 11px;">BOOKING REFERENCE</div>
        <div class="ref">${booking.bookingReference}</div>
      </div>
    </div>

    <!-- Stepper Status Tracker -->
    <div class="pass-stepper no-print">
      <div class="stepper-steps">
        <div class="step-item completed">
          <div class="step-dot">✓</div>
          <div>Booked</div>
        </div>
        <div class="step-item ${booking.bookingStatus == 'CHECKED_IN' || booking.bookingStatus == 'DOCS_VERIFIED' || booking.bookingStatus == 'BIOMETRICS_DONE' || booking.bookingStatus == 'APPROVED' ? 'completed' : 'active'}">
          <div class="step-dot">1</div>
          <div>Gate Check-in</div>
        </div>
        <div class="step-item ${booking.bookingStatus == 'DOCS_VERIFIED' || booking.bookingStatus == 'BIOMETRICS_DONE' || booking.bookingStatus == 'APPROVED' ? 'completed' : (booking.bookingStatus == 'CHECKED_IN' ? 'active' : '')}">
          <div class="step-dot">2</div>
          <div>Docs Verified</div>
        </div>
        <div class="step-item ${booking.bookingStatus == 'BIOMETRICS_DONE' || booking.bookingStatus == 'APPROVED' ? 'completed' : (booking.bookingStatus == 'DOCS_VERIFIED' ? 'active' : '')}">
          <div class="step-dot">3</div>
          <div>Biometrics</div>
        </div>
        <div class="step-item ${booking.bookingStatus == 'APPROVED' ? 'completed' : (booking.bookingStatus == 'BIOMETRICS_DONE' ? 'active' : '')}">
          <div class="step-dot">4</div>
          <div>Clearance</div>
        </div>
      </div>
    </div>

    <!-- Pass Body -->
    <div class="pass-body">
      <div>
        <div class="pass-details-grid">
          <div class="pass-detail-item">
            <label>Applicant Name</label>
            <span>${booking.applicantName}</span>
          </div>
          <div class="pass-detail-item">
            <label>Passport Number</label>
            <span>${booking.passportNumber}</span>
          </div>
          <div class="pass-detail-item">
            <label>Appointment Date</label>
            <span style="color: #0b1f36;">📅 ${booking.slotDate}</span>
          </div>
          <div class="pass-detail-item">
            <label>Reporting Time Slot</label>
            <span style="color: #00a896;">⏰ ${booking.startTime} – ${booking.endTime}</span>
          </div>
          <div class="pass-detail-item">
            <label>Verification Center</label>
            <span>PSK Regional Hub (Counter Wing A)</span>
          </div>
          <div class="pass-detail-item">
            <label>Current Status</label>
            <span class="badge ${booking.bookingStatus == 'APPROVED' ? 'success' : (booking.bookingStatus == 'CANCELLED' ? 'danger' : 'gold')}">
              ${booking.bookingStatus}
            </span>
          </div>
        </div>

        <c:if test="${not empty compliance}">
          <div style="margin-top: 18px; padding: 14px; background: #f0fdf4; border: 1px solid #bbf7d0; border-radius: 10px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <strong>🛡️ Document Pre-Screening: ${compliance.status}</strong>
              <span class="badge success">${compliance.score}% Ready</span>
            </div>
            <p class="muted" style="margin: 6px 0 0; font-size: 12px;">
              Zero-defect pre-check active. Bring original documents matching your digital vault records.
            </p>
          </div>
        </c:if>

        <c:if test="${not empty history}">
          <div style="margin-top: 18px;">
            <h4 style="margin: 0 0 8px; font-size: 13px; text-transform: uppercase; color: var(--muted);">Counter Audit Trail</h4>
            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Stage</th>
                    <th>Officer</th>
                    <th>Status</th>
                    <th>Time</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="h" items="${history}">
                    <tr>
                      <td>${h.stageLabel}</td>
                      <td>${h.officerName}</td>
                      <td><span class="badge success">${h.status}</span></td>
                      <td class="small muted">${h.loggedAt}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </c:if>
      </div>

      <!-- Right Column: Live QR Code -->
      <div class="pass-qr-side">
        <div class="qr-frame">
          <canvas id="qrCanvas" width="160" height="160" data-qr-text="${booking.bookingReference}|${checksum}"></canvas>
        </div>
        <div class="muted small" style="font-weight: 700;">SCAN FOR GATE CLEARANCE</div>
        <div class="pass-barcode">|||||||||||||||||||||||||||||||</div>
        <div class="muted small" style="font-size: 10px; margin-top: 6px;">
          Security Hash: <code style="color: #0b1f36;">${checksum}</code>
        </div>
      </div>
    </div>

    <!-- Official Center Guidelines -->
    <div class="pass-instructions">
      <strong>⚠️ Mandatory Center Instructions for Physical Appointment:</strong>
      <ul>
        <li>Arrive at least 15 minutes before your scheduled window (${booking.startTime}). Entry is restricted after slot closing.</li>
        <li>Present this digital pass on your smartphone or carry a printed copy for physical barcode scanning at Counter 1.</li>
        <li>Carry original Aadhaar card, Proof of Date of Birth, and Address proof as verified in your Document Vault.</li>
        <li>Photographs and biometric finger scans will be captured digitally inside the counter booth.</li>
      </ul>
    </div>
  </div>
</div>

<div class="footer no-print">
  Passport Verification System &copy; Official Digital Pass Module
</div>
</body>
</html>
