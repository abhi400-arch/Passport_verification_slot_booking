<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Admin Booking Analytics</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="../includes/header.jspf" %>

<div class="container">
  <h1>📊 Real-Time System Analytics</h1>
  <p class="muted">Live operational intelligence calculated across bookings, capacity, waitlists, document vaults, and gate verification counters.</p>

  <!-- Metric Grids -->
  <div class="grid three" style="margin-bottom: 20px;">
    <div class="stat">
      <div class="muted">Total Applicants</div>
      <div class="num">${analytics.users}</div>
    </div>
    <div class="stat">
      <div class="muted">Passport Applications</div>
      <div class="num">${analytics.applications}</div>
    </div>
    <div class="stat gold">
      <div class="muted">Active Confirmed Bookings</div>
      <div class="num">${analytics.bookings}</div>
    </div>
  </div>

  <div class="grid three" style="margin-bottom: 20px;">
    <div class="stat secondary">
      <div class="muted">Future Slots Scheduled</div>
      <div class="num">${analytics.slots}</div>
    </div>
    <div class="stat">
      <div class="muted">Available Open Slots</div>
      <div class="num">${analytics.availableSlots}</div>
    </div>
    <div class="stat danger">
      <div class="muted">Cancelled Bookings (Slots Restored)</div>
      <div class="num">${analytics.cancelled}</div>
    </div>
  </div>

  <!-- Innovation Analytics Grid -->
  <div class="grid three" style="margin-bottom: 25px;">
    <div class="stat danger">
      <div class="muted">⚡ Tatkal Waitlist In Queue</div>
      <div class="num">${analytics.waitlistActive}</div>
    </div>
    <div class="stat">
      <div class="muted">📁 Verified Vault Documents</div>
      <div class="num">${analytics.verifiedDocs}</div>
    </div>
    <div class="stat secondary">
      <div class="muted">🏢 Gate Check-ins Today</div>
      <div class="num">${analytics.gateCheckins}</div>
    </div>
  </div>

  <div class="card">
    <h2>📈 Demand & Operational Trends</h2>
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Operational Metric</th>
            <th>Calculated Result</th>
            <th>Significance</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><strong>Busiest Appointment Day</strong></td>
            <td><strong style="color: #1a568c;">${analytics.popularDay}</strong></td>
            <td class="muted small">Day of week with maximum confirmed applicant appointments.</td>
          </tr>
          <tr>
            <td><strong>Peak Time Window</strong></td>
            <td><strong style="color: #00a896;">${analytics.peakTime}</strong></td>
            <td class="muted small">Most requested counter verification hour.</td>
          </tr>
          <tr>
            <td><strong>Future Slot Capacity</strong></td>
            <td>${analytics.capacity} slots</td>
            <td class="muted small">Total potential appointments available in system.</td>
          </tr>
          <tr>
            <td><strong>Current Booked Capacity</strong></td>
            <td>${analytics.utilized} slots</td>
            <td class="muted small">Slots actively claimed by applicants.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Analytics Intelligence Module
</div>
</body>
</html>
