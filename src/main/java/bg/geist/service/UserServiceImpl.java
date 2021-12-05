package bg.geist.service;

import bg.geist.constant.Constants;
import bg.geist.domain.entity.UserEntity;
import bg.geist.domain.entity.UserProfile;
import bg.geist.domain.entity.UserRoleEntity;
import bg.geist.domain.entity.enums.UserRole;
import bg.geist.domain.model.service.UserAdministrationModel;
import bg.geist.domain.model.service.UserProfileModel;
import bg.geist.domain.model.service.UserRegistrationModel;
import bg.geist.domain.model.view.SimpleProfileView;
import bg.geist.domain.model.view.UserAdministrationView;
import bg.geist.exception.FieldAlreadyExistsException;
import bg.geist.exception.FieldName;
import bg.geist.repository.UserProfileRepository;
import bg.geist.repository.UserRepository;
import bg.geist.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRoleRepository userRoleRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserRoleRepository userRoleRepository, UserProfileRepository userProfileRepository, UserDetailsServiceImpl userDetailsService, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRoleRepository = userRoleRepository;
        this.userProfileRepository = userProfileRepository;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public final boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public final boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public SimpleProfileView currentProfile(){
        UserEntity userEntity = getBy(SecurityContextHolder.getContext().getAuthentication().getName());
        UserProfileModel profileModel = map(userEntity);
        return modelMapper.map(profileModel, SimpleProfileView.class);
    }

    @Override
    public UserProfileModel profile(final Long id) {
        return map(getByProfile(id));
    }

    @Override
    public UserProfileModel profile(final String username, final String password) {
        UserEntity userEntity = getBy(username);
        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            return map(userEntity);
        }
        return null;
    }

    @Override
    public UserAdministrationModel userAdministrationModel(final UserEntity userEntity) {
        return new UserAdministrationModel(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getFullname(),
                userEntity.getProfile().getImageUrl(),
                userEntity.getProfile().getId(),
                userEntity.getRoles().stream().map(r -> r.getRole().toString()).collect(Collectors.toList()),
                userEntity.hasRole(UserRole.ADMIN));
    }

    @Override
    public void register(final UserRegistrationModel model, boolean login) {

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

        UserProfile userProfile = userProfileRepository.save(new UserProfile(model.getImageUrl()));
        newUser.setProfile(userProfile);

        newUser.addRole(userRole);
        newUser = repository.save(newUser);

        if (login) {
            login(newUser.getUsername(), newUser.getPassword());
        }
    }

    private void login(final String username, final String password) {
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void updateProfile(@NotNull final UserProfileModel model) {
        UserEntity userEntity = getBy(model.getUsername());
        UserProfile userProfile = userEntity.getProfile();
        boolean isChangedEntity = false;
        boolean isChangedProfile = false;

        if (model.getFullname() != null && !userEntity.getFullname().equals(model.getFullname())) {
            userEntity.setFullname(model.getFullname());
            isChangedEntity = true;
        }

        if (model.getPassword() != null && !model.getPassword().isBlank() &&
            model.getPassword().matches(Constants.PASSWORDS_REGEX)) {
            userEntity.setPassword(passwordEncoder.encode(model.getPassword()));
            isChangedEntity = true;
        }

        if(model.getImageUrl() != null) {
            userProfile.setImageUrl(model.getImageUrl());
            isChangedProfile = true;
        }

        if (isChangedProfile) {
            userProfileRepository.save(userProfile);
        }

        if (isChangedEntity) {
            repository.save(userEntity);
        }
    }

    @Override
    public void roleAdd(@NotNull final Long userId, @NotNull final String role) {

        if (isAdminLogged() && !isRootAdmin(userId)) {
            UserEntity userEntity = getBy(userId);
            UserRole roleToAdd = getRoleBy(role);

            // already has the role
            if (userEntity.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole() == roleToAdd)) {
                throw new BadCredentialsException(Constants.USER_ROLE_FOUND);
            }

            // not found in database
            UserRoleEntity userRoleEntity = userRoleRepository.findByRole(roleToAdd)
                    .orElseThrow(() -> new BadCredentialsException(Constants.USER_ROLE_NOT_FOUND));

            userEntity.getRoles().add(userRoleEntity);
            repository.save(userEntity);
        }
    }

    @Override
    public void roleRemove(@NotNull final Long userId, @NotNull final String role) {
        if (isRootAdmin(userId)) {
            throw new BadCredentialsException("Unable to demote root admin.");
        }
        if (isAdminLogged()) {
            UserRole roleToRemove = getRoleBy(role);
            UserEntity userEntity = getBy(userId);
            userEntity.getRoles().removeIf(roleEntity -> roleEntity.getRole() == roleToRemove);
            repository.save(userEntity);
        }
    }

    private UserRole getRoleBy(String role) {
        try {
            return UserRole.valueOf(role);

        } catch (IllegalArgumentException ex) {
            // not found in enum
            throw new BadCredentialsException(Constants.USER_ROLE_NOT_FOUND);
        }
    }

    private boolean isRootAdmin(Long id) {
        return id == 1;
    }

    private boolean isAdminLogged() {
        UserEntity userEntity = getBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return userEntity.hasRole(UserRole.ADMIN);
    }

    @Override
    public final boolean isAuthorized(final Authentication authentication, final Long profileId) {
        UserEntity userEntity = getBy(authentication.getName());
        boolean isOwner = userEntity.getProfile().getId().equals(profileId);
        boolean isAdmin = userEntity.hasRole(UserRole.ADMIN);
        return (isOwner || isAdmin);
    }

    @Override
    public final boolean checkUserPassword(final String username, @NotNull final String oldPassword) {
        UserEntity userEntity = getBy(username);
        return passwordEncoder.matches(oldPassword, userEntity.getPassword());
    }

    private UserProfileModel map(final UserEntity userEntity) {
        UserProfileModel profileModel = modelMapper.map(userEntity.getProfile(), UserProfileModel.class);
        if (profileModel.getImageUrl() == null) {
            profileModel.setImageUrl(Constants.PROFILE_DEFAULT_AVATAR);
        }
        profileModel.setEmail(userEntity.getEmail());
        profileModel.setUsername(userEntity.getUsername());
        profileModel.setFullname(userEntity.getFullname());
        profileModel.setAdmin(userEntity.hasRole(UserRole.ADMIN));
        return profileModel;
    }

    private UserEntity getBy(final String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(Constants.USER_WITH_NAME_NOT_FOUND, username)));
    }

    private UserEntity getBy(final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadCredentialsException(Constants.USER_BAD_CREDENTIALS));
    }

    private UserEntity getByProfile(final Long id) {
        return repository.findByProfileId(id)
                .orElseThrow(() -> new BadCredentialsException(
                        String.format(Constants.USER_WITH_PROFILE_NOT_FOUND, id)));
    }
}