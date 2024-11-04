package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_shouldMapUserToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("yoga@studio.com");
        user.setFirstName("Yoga");
        user.setLastName("Studio");
        user.setAdmin(true);

        UserDto userDto = userMapper.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    void toEntity_shouldMapUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("yoga@studio.com");
        userDto.setFirstName("Yoga");
        userDto.setLastName("Studio");
        userDto.setAdmin(true);
        userDto.setPassword("test!1234");

        User user = userMapper.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.isAdmin()).isEqualTo(userDto.isAdmin());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
    }

    @Test
    void toEntityList_shouldMapListOfUserDtoToListOfUser() {
        UserDto userDto1 = new UserDto(1L, "yoga@studio.com", "Studio", "Yoga", true, "test!1234", null, null);
        UserDto userDto2 = new UserDto(2L, "toto3@toto.com", "toto", "toto", false, "test!1234", null, null);
        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        List<User> userList = userMapper.toEntity(userDtoList);

        assertThat(userList).hasSize(2);
        assertThat(userList.get(0).getEmail()).isEqualTo(userDto1.getEmail());
        assertThat(userList.get(1).getEmail()).isEqualTo(userDto2.getEmail());
    }

    @Test
    void toDtoList_shouldMapListOfUserToListOfUserDto() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("yoga@studio.com");
        user1.setFirstName("Yoga");
        user1.setLastName("Studio");
        user1.setAdmin(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("toto3@toto.com");
        user2.setFirstName("toto");
        user2.setLastName("toto");
        user2.setAdmin(false);

        List<User> userList = Arrays.asList(user1, user2);

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertThat(userDtoList).hasSize(2);
        assertThat(userDtoList.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(userDtoList.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    void toEntityList_shouldReturnEmptyListWhenUserDtoListIsEmpty() {
        List<User> userList = userMapper.toEntity(Collections.emptyList());

        assertThat(userList).isEmpty();
    }

    @Test
    void toDtoList_shouldReturnEmptyListWhenUserListIsEmpty() {
        List<UserDto> userDtoList = userMapper.toDto(Collections.emptyList());

        assertThat(userDtoList).isEmpty();
    }
}
