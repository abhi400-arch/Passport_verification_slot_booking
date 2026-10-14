<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Smart Document Vault & Pre-Screening</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div class="hero">
    <div>
      <h1>📁 Smart Document Pre-Screening Vault</h1>
      <p>Ensure zero-rejection on appointment day. Pre-screen and mathematically validate your Aadhaar, Proof of Date of Birth, and Address proofs before visiting the Passport Seva Kendra.</p>
    </div>
    <div class="iconbox">📑</div>
  </div>

  <c:if test="${not empty sessionScope.success}">
    <div class="alert success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
  </c:if>

  <c:if test="${not empty error}">
    <div class="alert error">${error}</div>
  </c:if>

  <!-- Compliance Score Banner -->
  <div class="compliance-banner ${compliance.score >= 100 ? 'certified' : (compliance.score >= 50 ? 'partial' : 'incomplete')}">
    <div class="score-circle">
      <div class="number">${compliance.score}%</div>
      <div class="label">Compliance</div>
    </div>
    <div>
      <h2 style="margin: 0 0 6px; color: var(--primary);">
        <c:choose>
          <c:when test="${compliance.score >= 100}">✅ Zero-Defect Certified Counter-Ready</c:when>
          <c:when test="${compliance.score >= 50}">⚠️ Partially Ready for Verification</c:when>
          <c:otherwise>❌ Action Required — High Rejection Risk</c:otherwise>
        </c:choose>
      </h2>
      <p style="margin: 0; color: var(--muted); font-size: 14px;">
        ${compliance.verifiedCount} of ${compliance.totalRequired} core document categories have passed automated algorithmic integrity checks.
      </p>
    </div>
    <div>
      <a class="btn gold" href="${pageContext.request.contextPath}/app/pass">View Official E-Pass</a>
    </div>
  </div>

  <!-- Document Pre-Screening Form -->
  <div class="card">
    <h2>➕ Pre-Screen & Register Supporting Document</h2>
    <p class="muted">Enter document metadata. The Java compliance engine runs Verhoeff checksums and regex heuristics to ensure accuracy.</p>
    
    <form method="post" action="${pageContext.request.contextPath}/app/documents">
      <div class="form-grid">
        <div class="field">
          <label>Document Category</label>
          <select name="docType" required>
            <option value="IDENTITY_PROOF">Photo Identity Proof (Aadhaar / PAN / Voter ID)</option>
            <option value="DOB_PROOF">Proof of Date of Birth (Birth / 10th Certificate)</option>
            <option value="ADDRESS_PROOF">Proof of Address (Utility Bill / Bank / Rent)</option>
            <option value="NON_ECR_PROOF">Non-ECR Eligible Qualification (Degree / 12th)</option>
          </select>
        </div>

        <div class="field">
          <label>Document Name / Issuing Authority</label>
          <input type="text" name="docName" placeholder="e.g. Aadhaar Card, CBSE Certificate" required>
        </div>

        <div class="field full">
          <label>Document ID / Serial / Certificate Number</label>
          <input type="text" name="docIdentifier" data-uppercase placeholder="e.g. 12-digit Aadhaar, PAN (ABCDE1234F), or Reg Number" required>
          <span class="muted small">Aadhaar numbers undergo mathematical Verhoeff parity validation. PANs undergo format checksum.</span>
        </div>
      </div>

      <div class="actions">
        <button class="btn" type="submit">Verify & Save to Vault</button>
      </div>
    </form>
  </div>

  <!-- Registered Documents List -->
  <div class="card">
    <h2>🗄️ Vault Records & Integrity Status</h2>
    <c:choose>
      <c:when test="${not empty compliance.documents}">
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Category</th>
                <th>Document</th>
                <th>Pre-Screened ID</th>
                <th>Status</th>
                <th>Algorithmic Evaluation</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="d" items="${compliance.documents}">
                <tr>
                  <td><strong>${d.formattedDocType}</strong></td>
                  <td>${d.docName}</td>
                  <td><code>${d.docIdentifier}</code></td>
                  <td>
                    <span class="badge ${d.verificationStatus == 'VERIFIED' ? 'success' : 'warning'}">
                      ${d.verificationStatus}
                    </span>
                  </td>
                  <td class="small muted">${d.remarks}</td>
                  <td>
                    <form method="post" action="${pageContext.request.contextPath}/app/documents" style="display:inline;" onsubmit="return confirm('Remove this document from vault?');">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="docId" value="${d.docId}">
                      <button class="btn danger small" type="submit">Remove</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>
      <c:otherwise>
        <p class="muted">No documents uploaded yet. Add your Aadhaar, DOB, and Address proofs above to certify your application.</p>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Real-Time Compliance Checklist -->
  <div class="card">
    <h2>📋 Official PSK Counter Checklist</h2>
    <div class="grid two">
      <div>
        <h4 style="margin-top: 0;">Verification Status:</h4>
        <ul style="padding-left: 20px; line-height: 1.8;">
          <c:forEach var="c" items="${compliance.checklist}">
            <li class="${c.startsWith('✓') ? 'text-success' : 'muted'}">${c}</li>
          </c:forEach>
        </ul>
      </div>
      <div>
        <h4 style="margin-top: 0; color: #b91c1c;">Pre-Appointment Warnings:</h4>
        <c:choose>
          <c:when test="${not empty compliance.warnings}">
            <ul style="padding-left: 20px; color: #b91c1c; line-height: 1.8;">
              <c:forEach var="w" items="${compliance.warnings}">
                <li>${w}</li>
              </c:forEach>
            </ul>
          </c:when>
          <c:otherwise>
            <p style="color: #166534; font-weight: 700;">
              ✨ Excellent! All mandatory documents are validated with zero errors. Your appointment pass is certified.
            </p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</div>

<div class="footer">
  Passport Verification System &copy; Document Pre-Screening Vault
</div>
</body>
</html>
