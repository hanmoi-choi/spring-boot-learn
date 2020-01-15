package com.kotlin.refactoring.config

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureException
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    @Autowired val userDetailsService: UserDetailsService,
    @Autowired val jwtTokenUtil: TokenProvider
) : OncePerRequestFilter() {
    private val TOKEN_PREFIX = "Bearer "
    private val AUTH_HEADER_STRING = "Authorization"

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        var username: String? = null
        val authToken: String? = getAuthToken(req)

        try {
            authToken?.let {
                username = jwtTokenUtil.getUsernameFromToken(it)
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException ->
                    logger.error("an error occured during getting username from token", e)
                is ExpiredJwtException ->
                    logger.warn("the token is expired and not valid anymore", e)
                is SignatureException ->
                    logger.error("Authentication Failed. Username or Password not valid.")
                else ->
                    logger.error("Another error", e)
            }
        }

        setSecurityContext(username, authToken, req)
        chain.doFilter(req, res)
    }

    private fun getAuthToken(req: HttpServletRequest): String? {
        val header: String? = req.getHeader(AUTH_HEADER_STRING)
        return header?.let {
            if (header.startsWith(TOKEN_PREFIX))
                header.replace(TOKEN_PREFIX, "")
            else {
                logger.warn("couldn't find bearer string, will ignore the header")
                null
            }
        }
    }

    private fun setSecurityContext(username: String?, authToken: String?, req: HttpServletRequest) {
        username?.let {
            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(it)
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    val authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().authentication, userDetails)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(req)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
    }
}
