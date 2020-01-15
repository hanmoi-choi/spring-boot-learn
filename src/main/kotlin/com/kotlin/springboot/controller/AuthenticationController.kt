package com.kotlin.refactoring.controller

import com.kotlin.refactoring.config.TokenProvider
import com.kotlin.refactoring.model.AuthToken
import com.kotlin.refactoring.model.dto.LoginUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/token")
class AuthenticationController(
    @Autowired val authenticationManager: AuthenticationManager,
    @Autowired val jwtTokenUtil: TokenProvider
) {

    @PostMapping("/generate-token")
    @Throws(AuthenticationException::class)
    fun register(@RequestBody loginUser: LoginUser): ResponseEntity<*> {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginUser.username, loginUser.password)
        val authentication: Authentication = authenticationManager.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication = authentication

        val token: String = jwtTokenUtil.generateToken(authentication)
        return ResponseEntity.ok<Any>(AuthToken(token))
    }
}
