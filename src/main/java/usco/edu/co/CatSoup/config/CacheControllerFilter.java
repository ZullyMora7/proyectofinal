package usco.edu.co.CatSoup.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CacheControllerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 🚫 Bloquear caché completamente
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");

        HttpSession session = req.getSession(false);

        boolean isLoggedIn = session != null &&
                session.getAttribute("SPRING_SECURITY_CONTEXT") != null;

        String uri = req.getRequestURI();

        // Rutas públicas
        boolean isPublic =
                uri.equals("/") ||
                uri.startsWith("/login") ||
                uri.startsWith("/register") ||
                uri.startsWith("/css") ||
                uri.startsWith("/js") ||
                uri.startsWith("/images");

        // 🔥 Si intenta acceder a una página protegida sin sesión → Login
        if (!isLoggedIn && !isPublic) {
            res.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, res);
    }
}
