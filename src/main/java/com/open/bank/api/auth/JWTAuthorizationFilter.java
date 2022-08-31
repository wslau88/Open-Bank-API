package com.open.bank.api.auth;

import static com.open.bank.api.constant.SecurityConstants.HEADER_STRING;
import static com.open.bank.api.constant.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTUtil jwtUtil;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JWTUtil jwtUtil) {
        super(authManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        try {
	        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(req, res);
        } catch(SignatureVerificationException ex) {
//            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot verify the signature in the token");
            chain.doFilter(req, res);
    	} catch(TokenExpiredException ex2) {
//    		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired token");
    		chain.doFilter(req, res);
    	} catch(JWTVerificationException ex3) {
//    		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot verify the token");
    		chain.doFilter(req, res);
    	}
        
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        
        if (token != null) {
        	String user = jwtUtil.validateTokenAndRetrieveSubject(token.replace(TOKEN_PREFIX, ""));
        	
            if (user != null) {
                // new arraylist means authorities
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }
}