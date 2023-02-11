package kz.test.tz.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.test.tz.dto.RequestDto;
import kz.test.tz.dto.UserDto;
import kz.test.tz.pogo.NewUserRequest;
import kz.test.tz.security.jwt.JwtTokenProvider;
import kz.test.tz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "API Аутентификации и регистрации", description = "api для аутентификации и регистрации пользователя")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation("Аутентификация пользователя")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody RequestDto requestDto
    ) {
        try {
            var response = getToken(requestDto);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ApiOperation("Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @Valid @RequestBody NewUserRequest request
    ) {
        var user = userService.registry(request);
        return ResponseEntity.ok(UserDto.from(user));
    }

    private Map<String, String> getToken(RequestDto requestDto) {
        var email = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        var user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        var token = jwtTokenProvider.createToken(email, user.getRoles());
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("token", token);
        return response;
    }
}
