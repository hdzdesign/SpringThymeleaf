package chc.tfm.udt.auth.filter;

import chc.tfm.udt.auth.service.JWTService;
import chc.tfm.udt.auth.service.JWTServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private JWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    /**
     * Metodo que valida si llega el Token
     * Una vez realizadas las validaciones del Token,  y recuperación de Usuario y Roles se carga en el Contexto de Spring
     * UsernamePasswordAuthenticationToken para poder ser utilizado.
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
        if (!requieresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = null;
        if(jwtService.validate(header)){
            authenticationToken = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header),null, jwtService.getRoles(header));
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
    protected boolean requieresAuthentication(String header){
        if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
