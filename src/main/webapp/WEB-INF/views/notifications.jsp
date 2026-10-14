<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Notifications & Alerts Center</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="includes/header.jspf" %>

<div class="container">
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;">
    <div>
      <h1 style="margin: 0;">🔔 Notification Center</h1>
      <p class="muted" style="margin: 4px 0 0;">Real-time automated alerts for bookings, document pre-screening checks, Tatkal queue matches, and gate check-in milestones.</p>
    </div>
    <c:if test="${unreadCount > 0}">
      <form method="post" action="${pageContext.request.contextPath}/app/notifications">
        <input type="hidden" name="action" value="readAll">
        <button class="btn secondary" type="submit">Mark All as Read</button>
      </form>
    </c:if>
  </div>

  <c:choose>
    <c:when test="${not empty notifications}">
      <c:forEach var="n" items="${notifications}">
        <div class="notif-item ${!n.read ? 'unread' : ''}">
          <div class="notif-icon">
            <c:choose>
              <c:when test="${n.type == 'URGENT'}">⚡</c:when>
              <c:when test="${n.type == 'SUCCESS'}">✅</c:when>
              <c:when test="${n.type == 'WARNING'}">⚠️</c:when>
              <c:otherwise>ℹ️</c:otherwise>
            </c:choose>
          </div>
          <div class="notif-content">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <h3 class="notif-title">${n.title}</h3>
              <c:if test="${!n.read}">
                <span class="badge danger" style="font-size: 10px;">NEW</span>
              </c:if>
            </div>
            <p class="notif-msg">${n.message}</p>
            <div class="notif-meta">
              <span>Timestamp: ${n.createdAt}</span>
              <c:if test="${!n.read}">
                ·
                <form method="post" action="${pageContext.request.contextPath}/app/notifications" style="display:inline;">
                  <input type="hidden" name="notificationId" value="${n.notificationId}">
                  <button type="submit" style="background:none; border:none; color:#1a568c; cursor:pointer; font-weight:700; text-decoration:underline; font-size:11px;">
                    Mark as read
                  </button>
                </form>
              </c:if>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="card center" style="padding: 50px 20px;">
        <div style="font-size: 50px; margin-bottom: 10px;">📭</div>
        <h2>No notifications yet</h2>
        <p class="muted">You're all caught up! You will receive real-time notifications when appointments are booked, documents are validated, or waitlist slots open up.</p>
        <a class="btn" href="${pageContext.request.contextPath}/app/dashboard">Back to Dashboard</a>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<div class="footer">
  Passport Verification System &copy; In-App Notification Center
</div>
</body>
</html>
