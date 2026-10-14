package com.passport.servlet;

import com.passport.model.DocumentComplianceResult;
import com.passport.model.User;
import com.passport.service.DocumentService;
import com.passport.service.impl.DocumentServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/documents")
public class DocumentVaultServlet extends HttpServlet {
    private final DocumentService service = new DocumentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            DocumentComplianceResult result = service.getComplianceResult(user.getUserId());
            req.setAttribute("compliance", result);
            req.getRequestDispatcher("/WEB-INF/views/documents.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int docId = Integer.parseInt(req.getParameter("docId"));
                service.deleteDocument(docId, user.getUserId());
                req.getSession().setAttribute("success", "Document removed from pre-screening vault.");
            } else {
                String docType = req.getParameter("docType");
                String docName = req.getParameter("docName");
                String docIdentifier = req.getParameter("docIdentifier");

                service.registerDocument(user.getUserId(), docType, docName, docIdentifier);
                req.getSession().setAttribute("success", "Document registered and pre-screened successfully!");
            }
            resp.sendRedirect(req.getContextPath() + "/app/documents");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
