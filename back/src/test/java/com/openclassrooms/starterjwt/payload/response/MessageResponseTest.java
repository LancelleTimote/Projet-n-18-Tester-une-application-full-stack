package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        messageResponse = new MessageResponse("Initial message");
    }

    @Test
    void constructor_ShouldInitializeMessageCorrectly() {
        assertThat(messageResponse.getMessage()).isEqualTo("Initial message");
    }

    @Test
    void setMessage_ShouldUpdateMessage() {
        messageResponse.setMessage("Updated message");
        assertThat(messageResponse.getMessage()).isEqualTo("Updated message");
    }

    @Test
    void getMessage_ShouldReturnCurrentMessage() {
        assertThat(messageResponse.getMessage()).isEqualTo("Initial message");
    }
}
