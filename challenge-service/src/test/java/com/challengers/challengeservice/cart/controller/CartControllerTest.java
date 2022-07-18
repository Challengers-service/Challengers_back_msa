package com.challengers.challengeservice.cart.controller;

import com.challengers.challengeservice.cart.service.CartService;
import com.challengers.challengeservice.common.documentation.Documentation;
import com.challengers.challengeservice.testtool.StringToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
class CartControllerTest extends Documentation {
    @MockBean CartService cartService;

    @Test
    @DisplayName("챌린지를 찜 한다.")
    void addCart() throws Exception {
        doNothing().when(cartService).put(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/cart/{challenge_id}", 1L)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1"))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.addCart());

    }

    @Test
    @DisplayName("챌린지 찜을 취소한다.")
    void deleteCart() throws Exception {
        doNothing().when(cartService).takeOut(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/cart/{challenge_id}", 1L)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1"))
                .andExpect(status().isOk())
                .andDo(CartDocumentation.deleteCart());
    }
}