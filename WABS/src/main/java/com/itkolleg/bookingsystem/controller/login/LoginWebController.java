package com.itkolleg.bookingsystem.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Der LoginWebController ist verantwortlich für die Verarbeitung von HTTP-Anforderungen, die mit der Benutzeranmeldung
 * und -authentifizierung im Zusammenhang stehen, und steuert den Ablauf des Anmelde- und Authentifizierungsprozesses.
 */
@Controller
@RequestMapping("/web")
public class LoginWebController {
    private final AuthenticationManager authenticationManager;

    /**
     * Erzeugt eine neue Instanz von LoginWebController.
     *
     * @param authenticationManager ein AuthenticationManager-Objekt zur Authentifizierung des Benutzers.
     */
    public LoginWebController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Zeigt das Login-Formular an.
     *
     * @return der Name des HTML-Formulars, das das Login-Formular darstellt.
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("error", "");
        return "login/login";
    }

    /**
     * Verarbeitet das Login-Formular und authentifiziert den Benutzer.
     *
     * @param request das HTTP-Request-Objekt, das die Benutzeranmeldedaten enthält.
     * @return eine Weiterleitung auf die Begrüßungsseite, falls die Authentifizierung erfolgreich war, oder auf
     * die Login-Fehlerseite, falls die Authentifizierung fehlgeschlagen ist.
     */
    @PostMapping("/login")
    public String processLoginForm(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            model.addAttribute("error", "Benutzername oder Passwort ungültig");
            return "login/login";
        }
        return null; // Rückgabewert kann null sein, da die Weiterleitung bereits in der Security-Konfiguration festgelegt wird
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "login/login";
    }


    /**
     * Zeigt die Login-Fehlerseite an.
     *
     * @return der Name des HTML-Dokuments, das die Login-Fehlerseite darstellt.
     */
    @GetMapping("/login-error")
    public String loginError() {
        return "login/login-error";
    }
}