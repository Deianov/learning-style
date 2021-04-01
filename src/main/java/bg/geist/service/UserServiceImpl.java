package bg.geist.service;

import bg.geist.constant.Constants;
import bg.geist.domain.entity.UserEntity;
import bg.geist.domain.entity.UserRoleEntity;
import bg.geist.domain.entity.enums.UserRole;
import bg.geist.domain.model.service.UserRegistrationModel;
import bg.geist.exception.FieldAlreadyExistsException;
import bg.geist.exception.FieldName;
import bg.geist.repository.UserRepository;
import bg.geist.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRoleRepository userRoleRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRoleRepository userRoleRepository, UserDetailsServiceImpl userDetailsService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public String getFullname(String username) {
        return repository.findByUsername(username).map(UserEntity::getFullName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(Constants.USER_WITH_NAME_NOT_FOUND, username)));
    }

    @Override
    public void register(UserRegistrationModel model, boolean login) {

        if (repository.existsByUsername(model.getUsername())) {
            throw new FieldAlreadyExistsException(FieldName.username.toString());
        }

        if (repository.existsByEmail(model.getEmail())) {
            throw new FieldAlreadyExistsException(FieldName.email.toString());
        }

        UserEntity newUser = modelMapper.map(model, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(model.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.USER)
                .orElseThrow(() -> new IllegalStateException(Constants.USER_ROLE_NOT_FOUND));

        newUser.addRole(userRole);
        newUser = repository.save(newUser);

        if (login) {
            login(newUser.getUsername(), newUser.getPassword());
        }
    }

    private void login(String username, String password) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}