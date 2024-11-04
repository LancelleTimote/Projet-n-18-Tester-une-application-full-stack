package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse(
                "sampleToken",
                1L,
                "yogaStudio",
                "Yoga",
                "Studio",
                true
        );
    }

    @Test
    void constructor_ShouldInitializeAllFieldsCorrectly() {
        assertThat(jwtResponse.getToken()).isEqualTo("sampleToken");
        assertThat(jwtResponse.getType()).isEqualTo("Bearer");
        assertThat(jwtResponse.getId()).isEqualTo(1L);
        assertThat(jwtResponse.getUsername()).isEqualTo("yogaStudio");
        assertThat(jwtResponse.getFirstName()).isEqualTo("Yoga");
        assertThat(jwtResponse.getLastName()).isEqualTo("Studio");
        assertThat(jwtResponse.getAdmin()).isTrue();
    }

    @Test
    void setToken_ShouldUpdateToken() {
        jwtResponse.setToken("newToken");
        assertThat(jwtResponse.getToken()).isEqualTo("newToken");
    }

    @Test
    void setId_ShouldUpdateId() {
        jwtResponse.setId(2L);
        assertThat(jwtResponse.getId()).isEqualTo(2L);
    }

    @Test
    void setUsername_ShouldUpdateUsername() {
        jwtResponse.setUsername("newUsername");
        assertThat(jwtResponse.getUsername()).isEqualTo("newUsername");
    }

    @Test
    void setFirstName_ShouldUpdateFirstName() {
        jwtResponse.setFirstName("toto");
        assertThat(jwtResponse.getFirstName()).isEqualTo("toto");
    }

    @Test
    void setLastName_ShouldUpdateLastName() {
        jwtResponse.setLastName("toto");
        assertThat(jwtResponse.getLastName()).isEqualTo("toto");
    }

    @Test
    void setAdmin_ShouldUpdateAdmin() {
        jwtResponse.setAdmin(false);
        assertThat(jwtResponse.getAdmin()).isFalse();
    }

    @Test
    void setType_ShouldUpdateType() {
        jwtResponse.setType("CustomType");
        assertThat(jwtResponse.getType()).isEqualTo("CustomType");
    }
}
