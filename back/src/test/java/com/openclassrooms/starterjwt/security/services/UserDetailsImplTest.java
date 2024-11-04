package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("yogaStudio")
                .firstName("Yoga")
                .lastName("Studio")
                .admin(true)
                .password("test!1234")
                .build();
    }

    @Test
    void shouldReturnEmptyAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities).isEmpty();
    }

    @Test
    void shouldReturnTrueForIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueForIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueForIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueForIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldEqualWhenSameId() {
        UserDetailsImpl userDetailsCopy = UserDetailsImpl.builder()
                .id(1L)
                .username("totototo")
                .firstName("toto")
                .lastName("toto")
                .admin(false)
                .password("differentpassword")
                .build();

        assertThat(userDetails).isEqualTo(userDetailsCopy);
    }

    @Test
    void shouldNotEqualWhenDifferentId() {
        UserDetailsImpl differentUser = UserDetailsImpl.builder()
                .id(2L)
                .username("totototo")
                .firstName("toto")
                .lastName("toto")
                .admin(false)
                .password("differentpassword")
                .build();

        assertThat(userDetails).isNotEqualTo(differentUser);
    }

    @Test
    void shouldReturnCorrectUserDetailsAttributes() {
        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("yogaStudio");
        assertThat(userDetails.getFirstName()).isEqualTo("Yoga");
        assertThat(userDetails.getLastName()).isEqualTo("Studio");
        assertThat(userDetails.getPassword()).isEqualTo("test!1234");
        assertThat(userDetails.getAuthorities()).isInstanceOf(HashSet.class);
    }
}
