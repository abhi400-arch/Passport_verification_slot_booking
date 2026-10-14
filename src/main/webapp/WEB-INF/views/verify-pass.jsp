<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Officer Pass Verifier & Counter Terminal</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div class="hero">
    <div>
      <h1>🛡️ Gate Scanner & Verifier</h1>
      <p>Official counter verification terminal for scanning applicant QR appointment passes, inspecting pre-screened documents, and processing progressive clearance stages.</p>
    </div>
    <div class="iconbox">📷</div>
  </div>

  <c:if test="${not empty sessionScope.successMsg}">
    <div class="alert success">${sessionScope.successMsg}</div>
    <c:remove var="successMsg" scope="session"/>
  </c:if>

  <c:if test="${not empty searchError}">
    <div class="alert error">${searchError}</div>
  </c:if>

  <!-- Pass Search & QR Scanner Input -->
  <div class="card">
    <h2>🔍 Scan or Enter Appointment Pass Reference</h2>
    <form method="get" action="${pageContext.request.contextPath}/admin/verify-pass">
      <div class="form-grid">
        <div class="field">
          <label>Booking Reference / QR Token</label>
          <input type="text" name="ref" id="scanRefInput" data-uppercase value="${param.ref}" placeholder="e.g. PSV-7FDEFE7041" required autofocus>
        </div>
        <div class="field" style="justify-content: flex-end;">
          <div class="actions">
            <button class="btn" type="submit">Verify Pass</button>
            <button class="btn secondary" type="button" onclick="document.getElementById('scanRefInput').value='PSV-7FDEFE7041'">Demo Ref</button>
          </div>
        </div>
      </div>
    </form>
  </div>

  <c:if test="${not empty booking}">
    <!-- Candidate Dossier Card -->
    <div class="card" style="border-left: 6px solid #1a568c;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
        <div>
          <h2 style="margin: 0;">${booking.applicantName}</h2>
          <span class="muted">Passport: <strong>${booking.passportNumber}</strong> · Reference: <strong style="color: #0b1f36;">${booking.bookingReference}</strong></span>
        </div>
        <div>
          <span class="badge ${booking.bookingStatus == 'APPROVED' ? 'success' : (booking.bookingStatus == 'CANCELLED' ? 'danger' : 'gold')}" style="font-size: 14px; padding: 6px 14px;">
            ${booking.bookingStatus}
          </span>
        </div>
      </div>

      <div class="grid three">
        <div class="stat secondary">
          <div class="muted">Appointment Date</div>
          <div class="num" style="font-size: 22px;">${booking.slotDate}</div>
        </div>
        <div class="stat">
          <div class="muted">Time Window</div>
          <div class="num" style="font-size: 22px; color: #00a896;">${booking.startTime} – ${booking.endTime}</div>
        </div>
        <div class="stat gold">
          <div class="muted">Cryptographic Hash</div>
          <div class="num" style="font-size: 18px; font-family: monospace;">${checksum}</div>
        </div>
      </div>
    </div>

    <!-- Document Pre-Screening Status -->
    <div class="card">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h3 style="margin: 0;">📁 Document Pre-Screening Vault Status</h3>
        <span class="badge ${compliance.score >= 75 ? 'success' : 'warning'}">${compliance.score}% Counter-Ready</span>
      </div>
      <div style="margin-top: 14px;">
        <c:choose>
          <c:when test="${not empty compliance.documents}">
            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Category</th>
                    <th>Document</th>
                    <th>Identifier</th>
                    <th>Algorithmic Status</th>
                    <th>Remarks</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="doc" items="${compliance.documents}">
                    <tr>
                      <td><strong>${doc.formattedDocType}</strong></td>
                      <td>${doc.docName}</td>
                      <td><code>${doc.docIdentifier}</code></td>
                      <td>
                        <span class="badge ${doc.verificationStatus == 'VERIFIED' ? 'success' : 'warning'}">
                          ${doc.verificationStatus}
                        </span>
                      </td>
                      <td class="small muted">${doc.remarks}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:when>
          <c:otherwise>
            <p class="muted">Applicant has not pre-registered digital documents in the vault.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- Counter Stage Actions Panel -->
    <div class="card">
      <h3>⚡ Officer Multi-Stage Verification Actions</h3>
      <p class="muted">Progress applicant through the official physical processing stages at this counter.</p>
      
      <div class="grid four" style="margin-top: 15px;">
        <!-- Stage 1: Gate Entry -->
        <div class="stat">
          <strong>1. Gate Entry</strong>
          <p class="small muted">Confirm applicant physical arrival at center.</p>
          <form method="post" action="${pageContext.request.contextPath}/admin/verify-pass">
            <input type="hidden" name="bookingId" value="${booking.bookingId}">
            <input type="hidden" name="ref" value="${booking.bookingReference}">
            <input type="hidden" name="stage" value="GATE_ENTRY">
            <input type="hidden" name="status" value="PASS">
            <button class="btn accent small" type="submit">Mark Check-In</button>
          </form>
        </div>

        <!-- Stage 2: Document Inspection -->
        <div class="stat">
          <strong>2. Document Inspection</strong>
          <p class="small muted">Verify original identity and DOB physical records.</p>
          <form method="post" action="${pageContext.request.contextPath}/admin/verify-pass">
            <input type="hidden" name="bookingId" value="${booking.bookingId}">
            <input type="hidden" name="ref" value="${booking.bookingReference}">
            <input type="hidden" name="stage" value="DOC_VERIFY">
            <input type="hidden" name="status" value="PASS">
            <button class="btn secondary small" type="submit">Verify Docs</button>
          </form>
        </div>

        <!-- Stage 3: Biometrics -->
        <div class="stat">
          <strong>3. Biometrics Capture</strong>
          <p class="small muted">Record finger scan, iris, and digital photo.</p>
          <form method="post" action="${pageContext.request.contextPath}/admin/verify-pass">
            <input type="hidden" name="bookingId" value="${booking.bookingId}">
            <input type="hidden" name="ref" value="${booking.bookingReference}">
            <input type="hidden" name="stage" value="BIOMETRICS">
            <input type="hidden" name="status" value="PASS">
            <button class="btn secondary small" type="submit">Capture Biometrics</button>
          </form>
        </div>

        <!-- Stage 4: Final Clearance -->
        <div class="stat gold">
          <strong>4. Final Decision</strong>
          <p class="small muted">Issue official grant or mark deficiency.</p>
          <form method="post" action="${pageContext.request.contextPath}/admin/verify-pass">
            <input type="hidden" name="bookingId" value="${booking.bookingId}">
            <input type="hidden" name="ref" value="${booking.bookingReference}">
            <input type="hidden" name="stage" value="FINAL_DECISION">
            <input type="hidden" name="status" value="PASS">
            <input type="text" name="notes" placeholder="Officer Remarks" class="small" style="margin-bottom: 6px;">
            <div class="actions">
              <button class="btn success small" type="submit">Grant & Approve</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Audit History -->
    <c:if test="${not empty history}">
      <div class="card">
        <h3>📝 Check-in & Audit Log</h3>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Stage</th>
                <th>Status</th>
                <th>Officer</th>
                <th>Notes</th>
                <th>Timestamp</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="h" items="${history}">
                <tr>
                  <td><strong>${h.stageLabel}</strong></td>
                  <td><span class="badge ${h.status == 'PASS' ? 'success' : 'danger'}">${h.status}</span></td>
                  <td>${h.officerName}</td>
                  <td>${empty h.notes ? '—' : h.notes}</td>
                  <td class="muted small">${h.loggedAt}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </c:if>
  </c:if>
</div>

<div class="footer">
  Passport Verification System &copy; Officer Gate Scanner Terminal
</div>
</body>
</html>
