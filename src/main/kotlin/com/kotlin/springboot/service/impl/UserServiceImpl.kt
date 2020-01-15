package com.kotlin.refactoring.service.impl

import com.kotlin.refactoring.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserServiceImpl @Autowired
constructor(private val userRepository: UserRepository) : UserDetailsService {

    private val ROLE_PREFIX = "ROLE_"

    fun getAuthority(user: com.kotlin.refactoring.model.User): List<SimpleGrantedAuthority> =
            user.roles.map { role ->
                SimpleGrantedAuthority("${ROLE_PREFIX}${role.name}")
            }

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)?.let {
            User(it.username, it.password, getAuthority(it))
        } ?: throw UsernameNotFoundException("User ('$username') is not found")
    }
}
