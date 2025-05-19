package ru.nastya.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nastya.entity.User;
import ru.nastya.exception.AllreadyExistsException;
import ru.nastya.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
   // private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Transactional
    public User registerUser(String login, String firstName, String lastName, String patronymic, String password, String phone) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new AllreadyExistsException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymic(patronymic);
 //       user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        user.setPhone(phone);

        user = userRepository.save(user);

        accountService.createDefaultUserAccount(user);

        return user;

    }
}
