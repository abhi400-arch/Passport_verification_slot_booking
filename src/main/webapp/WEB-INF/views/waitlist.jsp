<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Emergency Tatkal Priority Waitlist</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div class="hero">
    <div>
      <h1>⚡ Tatkal Emergency Waitlist</h1>
      <p>Need an earlier or urgent appointment? Register in the priority waitlist queue. When cancellations occur or emergency quotas are added, the automated matching engine immediately reserves and notifies you.</p>
    </div>
    <div class="iconbox">⏱️</div>
  </div>

  <c:if test="${not empty sessionScope.success}">
    <div class="alert success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
  </c:if>

  <c:if test="${not empty error}">
    <div class="alert error">${error}</div>
  </c:if>

  <!-- Active Waitlist Queue Overview -->
  <c:forEach var="w" items="${waitlist}">
    <c:if test="${w.status == 'ACTIVE'}">
      <div class="queue-pos-box">
        <div>
          <span class="urgency-badge ${w.urgencyCategory == 'TATKAL_URGENT' ? 'tatkal' : (w.urgencyCategory == 'MEDICAL_EMERGENCY' ? 'medical' : 'education')}">
            ${w.categoryLabel}
          </span>
          <h3 style="margin: 8px 0 2px;">Target Window: ${w.preferredDateFrom} to ${w.preferredDateTo}</h3>
          <p class="muted" style="color: #cbd5e1; margin: 0; font-size: 13px;">Reason: ${w.reason}</p>
        </div>
        <div style="text-align: right;">
          <div class="small" style="color: #94a3b8; text-transform: uppercase;">Queue Position</div>
          <div class="big">#${w.queuePosition}</div>
        </div>
      </div>
    </c:if>
  </c:forEach>

  <!-- Join Waitlist Form -->
  <div class="card">
    <h2>🎯 Request Tatkal / Emergency Appointment Queue</h2>
    <p class="muted">Specify your preferred dates and travel urgency. Requests are dynamically ranked by priority tier and submission timestamp.</p>

    <form method="post" action="${pageContext.request.contextPath}/app/waitlist">
      <div class="form-grid">
        <div class="field">
          <label>Earliest Acceptable Date</label>
          <input type="date" name="fromDate" required>
        </div>

        <div class="field">
          <label>Latest Acceptable Date</label>
          <input type="date" name="toDate" required>
        </div>

        <div class="field">
          <label>Urgency Classification</label>
          <select name="urgencyCategory" required>
            <option value="TATKAL_URGENT">⚡ Tatkal Urgent (Expedited Processing)</option>
            <option value="MEDICAL_EMERGENCY">🏥 Medical Emergency (Overseas Treatment)</option>
            <option value="EDUCATION_STUDY">🎓 Higher Education (Admission / Visa Deadline)</option>
            <option value="GENERAL">Standard Earliest Available Window</option>
          </select>
        </div>

        <div class="field">
          <label>Justification / Travel Reason</label>
          <input type="text" name="reason" placeholder="e.g. Visa interview scheduled on 28th, need urgent slot" required>
        </div>
      </div>

      <div class="actions">
        <button class="btn accent" type="submit">Join Priority Waitlist</button>
      </div>
    </form>
  </div>

  <!-- User's Existing Requests Table -->
  <div class="card">
    <h2>📋 Your Waitlist Requests</h2>
    <c:choose>
      <c:when test="${not empty waitlist}">
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Target Dates</th>
                <th>Category</th>
                <th>Reason</th>
                <th>Position</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="w" items="${waitlist}">
                <tr>
                  <td><strong>${w.preferredDateFrom}</strong> to <strong>${w.preferredDateTo}</strong></td>
                  <td>${w.categoryLabel}</td>
                  <td class="small muted">${w.reason}</td>
                  <td>
                    <c:choose>
                      <c:when test="${w.status == 'ACTIVE'}">
                        <span class="badge gold">#${w.queuePosition} in Queue</span>
                      </c:when>
                      <c:otherwise>—</c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <span class="badge ${w.status == 'ACTIVE' ? 'success' : 'secondary'}">${w.status}</span>
                  </td>
                  <td>
                    <c:if test="${w.status == 'ACTIVE'}">
                      <form method="post" action="${pageContext.request.contextPath}/app/waitlist" style="display:inline;">
                        <input type="hidden" name="action" value="cancel">
                        <input type="hidden" name="waitlistId" value="${w.waitlistId}">
                        <button class="btn danger small" type="submit">Cancel</button>
                      </form>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>
      <c:otherwise>
        <p class="muted">No active waitlist requests. If upcoming slots are fully booked, submit a request above.</p>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Operational Flow Card -->
  <div class="card">
    <h3>💡 How Automated Waitlist Reallocation Works</h3>
    <div class="grid three">
      <div class="stat">
        <strong>1. Slot Released</strong>
        <p class="small muted">When any applicant cancels an appointment, or administrators release emergency quotas, the slot capacity is immediately unlocked.</p>
      </div>
      <div class="stat gold">
        <strong>2. Smart Match Engine</strong>
        <p class="small muted">The backend algorithm filters active waitlist entries matching the slot date, ranked by Tatkal/Medical urgency level and queue order.</p>
      </div>
      <div class="stat secondary">
        <strong>3. Instant Alert</strong>
        <p class="small muted">The matching applicant receives an instant High-Priority Notification with one-click slot claiming access.</p>
      </div>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Tatkal Priority Waitlist Module
</div>
</body>
</html>
