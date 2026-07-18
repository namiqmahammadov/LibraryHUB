package com.namiq.msuser.service

import com.namiq.msuser.dao.entity.User
import com.namiq.msuser.dao.repository.UserRepository
import com.namiq.msuser.dto.request.AuthLoginRequest
import com.namiq.msuser.dto.request.AuthRegisterRequest
import com.namiq.msuser.dto.request.UpdateUserRequest
import com.namiq.msuser.dto.response.UserResponse
import com.namiq.msuser.enums.Role
import com.namiq.msuser.exception.EmailAlreadyExistsException
import com.namiq.msuser.mapper.AuthMapper
import com.namiq.msuser.mapper.UserMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceSpec extends Specification {
    AuthenticationManager authenticationManager = Mock()
    JwtService jwtService = Mock()
    TokenStorageService tokenStorageService = Mock()
    UserRepository userRepository = Mock()
    PasswordEncoder passwordEncoder = Mock()
    AuthMapper authMapper = Mock()
    UserMapper userMapper = Mock()
    AuthService authService = new AuthService(authenticationManager, jwtService, tokenStorageService, userRepository, passwordEncoder, authMapper);
    UserService userService = new UserService(userRepository, userMapper)

    def "should throw exception when email already exists"() {
        def request = new AuthRegisterRequest(
                username: "namiq",
                email: "namiqms90@gmail.com",
                password: "12345"
        )
        1 * userRepository.existsByEmail(request.email) >> true
        when:
        authService.register(request)
        then:
        def exception = thrown(EmailAlreadyExistsException)
        exception.getMessage() == "Email already exists: ${request.email}"

    }

    def "should register user successfully"() {
        def request = new AuthRegisterRequest(
                username: "namiq",
                email: "namiqms90@gmail.com",
                password: "12345"
        )
        def user = new User(
                username: "namiq",
                email: "namiqms90@gmail.com",
                password: "12345",
                isActive: true,
                role: Role.ROLE_USER
        )
        1 * userRepository.existsByEmail(request.email) >> false
        1 * userRepository.findByUsername(request.username) >> Optional.empty()
        1 * authMapper.toUser(request) >> user
        1 * passwordEncoder.encode(request.password) >> "encodedPassword"
        1 * userRepository.save(user)
        when:
        authService.register(request)
        then:
        noExceptionThrown()
        user.password == "encodedPassword"
        user.role == Role.ROLE_USER
        user.isActive == true


    }

    def "should login successfully"() {
        given:
        def request = new AuthLoginRequest(
                username: "namiq",
                password: "12345"
        )

        def user = new User(username: "namiq")
        def authentication = Mock(Authentication)

        1 * authenticationManager.authenticate(_ as UsernamePasswordAuthenticationToken) >> authentication
        1 * authentication.getPrincipal() >> user

        1 * jwtService.generateAccessToken(user) >> "access-token"
        1 * tokenStorageService.storeAccessToken("namiq", "access-token")
        1 * userRepository.save(user)
        1 * jwtService.getAccessTokenExpiration() >> 15   // nümunə olaraq

        when:
        def response = authService.login(request)

        then:
        response.accessToken == "access-token"
        response.tokenType == "Bearer"
        response.expiresIn == 900
    }

    def "should update profile successfully"() {
        given:
        def request = new UpdateUserRequest(
                fullName: "Namiq Mahammadov",
                email: "namiqms90@gmail.com"
        )
        def user = new User(
                fullName: "Namiq Mahammadov",
                email: "namiqms90@gmail.com"

        )
           def authentication=Mock(Authentication)
        def securityContext=Mock(SecurityContext)
        SecurityContextHolder.context = securityContext
        securityContext.authentication >> authentication
        authentication.name >> "namiq"
        and:
        1 * userRepository.findByUsername("namiq") >> Optional.of(user)
        1 * userRepository.save(user)
        1 * userMapper.toResponse(user)>> new UserResponse()
        when:
         def result=userService.updateUser(request)
        then:
        user.fullName == request.getFullName()
        user.email == request.getEmail()

    }

}
