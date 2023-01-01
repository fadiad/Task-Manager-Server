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

    /**
     * Add user used in user registration process , checks if user email is already used by any user in the db ,
     * if yes it returns an message and stops registration process , else it adds the user to the db .
     *
     * @param user get user details. and add the user to DB.
     */
    @Transactional
    public void addUser(User user) {
        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());
        if (fetchedUser.isPresent()) {
            throw new IllegalArgumentException("this email is already in use");
        }
        signUp(user);
    }

    /**
     * signUp function adds the user to the DB , it used of addUser function.
     *
     * @param user details.
     */
    private void signUp(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        userRepository.save(user);
    }

    /**
     * get user from DB , if user is not existed it will return
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("bad credentials"));
    }

    /**
     * sign up for gitHub users
     *
     * @param user details.
     * @return user DTO
     */
    @Transactional
    public UserDTO signUpGitUser(User user) {
        Optional<User> fetchedUser = userRepository.findByEmail(user.getEmail());

        if (fetchedUser.isPresent()) {
            if (fetchedUser.get().getPassword() != null) {
                throw new IllegalArgumentException("this email already in use");
            }
            return new UserDTO(fetchedUser.get());
        }
        return new UserDTO(userRepository.save(user));
    }

}
