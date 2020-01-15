package com.kotlin.refactoring.service.impl

import com.kotlin.refactoring.repository.UserRepository
import javax.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Tag("IntegrationTest")
internal class UserDetailsServiceIntegrationTest @Autowired constructor(
    userRepository: UserRepository
) {
    val userDetailsService = UserServiceImpl(userRepository)

    @Test
    fun `loadUserByUsername should return Spring Security User`() {
        val expectedUser = userDetailsService.loadUserByUsername("admin")

        assertThat(expectedUser.username).isEqualTo("admin")
        assertThat(expectedUser.authorities).isEqualTo(setOf(SimpleGrantedAuthority("ROLE_ADMIN")))
    }

    @Test
    fun `loadUserByUsername throw exception when user is not found`() {
        assertThatThrownBy { userDetailsService.loadUserByUsername("admin1") }
                .isInstanceOf(UsernameNotFoundException::class.java)
                .hasMessageContaining("User ('admin1') is not found")
    }
}
