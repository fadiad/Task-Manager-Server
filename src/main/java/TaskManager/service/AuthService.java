package TaskManager.service;

import TaskManager.entities.SecurityUser;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Transactional
    public void addUser(User user) {
        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());
        if (fetchedUser.isPresent()) {
            throw new IllegalArgumentException("this email is already in use");
        }
        signUp(user);
    }

    private void signUp(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("bad credentials"));
    }
    @Transactional
    public UserDTO signUpGitUser(User user) {
        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());
        return fetchedUser.map(UserDTO::new).orElseGet(() -> new UserDTO(userRepository.save(user)));
    }

}
