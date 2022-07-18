package com.challengers.challengeservice.cart.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class CartDocumentation {

    public static RestDocumentationResultHandler addCart() {

        return document("cart/addCart",
                pathParameters(parameterWithName("challenge_id").description("찜할 챌린지의 ID"))
        );
    }

    public static RestDocumentationResultHandler deleteCart() {
        return document("cart/deleteCart",
                pathParameters(parameterWithName("challenge_id").description("찜을 취소할 챌린지의 ID"))
        );
    }


}
