// Passport Verification System - Client Interactive Script

document.addEventListener("DOMContentLoaded", () => {
  // Form validation
  document.querySelectorAll("form[data-validate]").forEach(form => {
    form.addEventListener("submit", e => {
      let valid = true;
      form.querySelectorAll("[required]").forEach(el => {
        if (!el.value.trim()) {
          el.style.borderColor = "#d9383a";
          valid = false;
        } else {
          el.style.borderColor = "";
        }
      });
      const pass = form.querySelector("#password");
      const confirm = form.querySelector("#confirmPassword");
      if (pass && confirm && pass.value !== confirm.value) {
        confirm.setCustomValidity("Passwords do not match");
        valid = false;
      } else if (confirm) {
        confirm.setCustomValidity("");
      }
      if (!valid) {
        e.preventDefault();
        alert("Please complete all required fields correctly.");
      }
    });
  });

  // Uppercase auto-formatting for references and identifiers
  document.querySelectorAll("input[data-uppercase]").forEach(input => {
    input.addEventListener("input", () => {
      input.value = input.value.toUpperCase();
    });
  });

  // Admin slot delete handler
  document.querySelectorAll(".delete-slot").forEach(btn => {
    btn.addEventListener("click", async () => {
      if (!confirm("Are you sure you want to delete this slot?")) return;
      try {
        const r = await fetch(btn.dataset.url + "?id=" + btn.dataset.id, { method: "DELETE" });
        const t = await r.text();
        alert(t);
        if (r.ok) location.reload();
      } catch (err) {
        alert("Failed to delete slot: " + err.message);
      }
    });
  });

  // Print Pass handler
  const printBtn = document.getElementById("printPassBtn");
  if (printBtn) {
    printBtn.addEventListener("click", () => {
      window.print();
    });
  }

  // Draw Standalone Vector QR Code on Canvas if canvas exists
  const qrCanvas = document.getElementById("qrCanvas");
  if (qrCanvas) {
    drawVerificationQR(qrCanvas, qrCanvas.dataset.qrText || "PSV-VERIFIED");
  }

  // Polling unread notifications count for live badge
  updateNotificationCount();
});

// Lightweight deterministic 2D matrix barcode/QR simulator for offline rendering
function drawVerificationQR(canvas, text) {
  const ctx = canvas.getContext("2d");
  const size = canvas.width || 160;
  const cells = 25;
  const cellSize = size / cells;

  ctx.fillStyle = "#ffffff";
  ctx.fillRect(0, 0, size, size);
  ctx.fillStyle = "#0b1f36";

  // Pseudo-random hash sequence based on text
  let hash = 0;
  for (let i = 0; i < text.length; i++) {
    hash = ((hash << 5) - hash) + text.charCodeAt(i);
    hash |= 0;
  }

  // Draw finder patterns (top-left, top-right, bottom-left)
  function drawFinder(ox, oy) {
    ctx.fillRect(ox * cellSize, oy * cellSize, 7 * cellSize, 7 * cellSize);
    ctx.fillStyle = "#ffffff";
    ctx.fillRect((ox + 1) * cellSize, (oy + 1) * cellSize, 5 * cellSize, 5 * cellSize);
    ctx.fillStyle = "#0b1f36";
    ctx.fillRect((ox + 2) * cellSize, (oy + 2) * cellSize, 3 * cellSize, 3 * cellSize);
  }

  drawFinder(1, 1);
  drawFinder(cells - 8, 1);
  drawFinder(1, cells - 8);

  // Timing lines
  for (let i = 8; i < cells - 8; i++) {
    if (i % 2 === 0) {
      ctx.fillRect(i * cellSize, 6 * cellSize, cellSize, cellSize);
      ctx.fillRect(6 * cellSize, i * cellSize, cellSize, cellSize);
    }
  }

  // Data modules
  let seed = Math.abs(hash);
  for (let r = 0; r < cells; r++) {
    for (let c = 0; c < cells; c++) {
      // Skip finder zones
      if ((r < 9 && c < 9) || (r < 9 && c > cells - 10) || (r > cells - 10 && c < 9)) continue;
      if (r === 6 || c === 6) continue;

      seed = (seed * 9301 + 49297) % 233280;
      if (seed / 233280 > 0.55) {
        ctx.fillRect(c * cellSize, r * cellSize, cellSize - 0.5, cellSize - 0.5);
      }
    }
  }
}

// Live notification badge fetch
async function updateNotificationCount() {
  const badge = document.getElementById("notifBadge");
  if (!badge) return;
  try {
    const res = await fetch(badge.dataset.apiUrl || "/passport-slot-booking/api/notifications/count");
    if (res.ok) {
      const data = await res.json();
      if (data.unreadCount > 0) {
        badge.textContent = data.unreadCount;
        badge.style.display = "inline-block";
      } else {
        badge.style.display = "none";
      }
    }
  } catch (ignored) {}
}
