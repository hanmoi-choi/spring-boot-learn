package com.kotlin.refactoring.controller

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Tag("SystemTest")
class AuthenticationControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc

    @BeforeAll
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    @Test
    fun `existent user can get token and authentication`() {
        val username = "admin"
        val password = "password1"
        val body = """
           { "username": "$username", "password": "$password" }
        """.trimIndent()

        val result = mvc.perform(
                MockMvcRequestBuilders.post("/token/generate-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE).content(body)
        ).andExpect(status().isOk).andReturn()

        val token = getToken(result)

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .header("Authorization", "Bearer $token"))
                .andExpect(status().isOk)
    }

    @Test
    fun `non-existent user cannot get token`() {
        val username = "admin1"
        val password = "password1"
        val body = """
           { "username": "$username", "password": "$password" }
        """.trimIndent()

        mvc.perform(
                MockMvcRequestBuilders.post("/token/generate-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE).content(body)
        ).andExpect(status().isForbidden)
    }

    private fun getToken(result: MvcResult): String {
        var response = result.response.contentAsString
        response = response.replace("{\"token\": \"", "")
        return response.replace("\"}", "")
    }
}
