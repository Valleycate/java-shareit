package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceImplTest {
    private final EntityManager em;
    private final UserServiceImpl service;

    @Test
    @Order(value = 1)
    void shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = service.addUser(userDto);
        TypedQuery<User> query = em.createQuery("Select i from User i where i.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId()).getSingleResult();
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    @Order(value = 2)
    void shouldGetAllUser() {
        List<UserDto> userDtos = service.findAll();
        TypedQuery<User> query = em.createQuery("Select i from User i", User.class);
        List<User> users = query.getResultList();
        assertThat(userDtos.size(), equalTo(users.size()));
    }

    @Test
    @Order(value = 3)
    void shouldFindUserById() {
        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setEmail("test@email");
        userDto = service.addUser(userDto);
        User findUserDto = service.findUserById(userDto.getId());
        TypedQuery<User> query = em.createQuery("Select i from User i where i.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId()).getSingleResult();
        assertThat(user.getId(), equalTo(findUserDto.getId()));
        assertThat(user.getName(), equalTo(findUserDto.getName()));
        assertThat(user.getEmail(), equalTo(findUserDto.getEmail()));
    }
}
