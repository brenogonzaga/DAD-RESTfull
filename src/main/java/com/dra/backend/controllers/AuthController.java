package com.dra.backend.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.dra.backend.dto.auth.CriarUsuarioDTO;
import com.dra.backend.dto.auth.LogarUsuarioDTO;
import com.dra.backend.models.entities.Usuario;
import com.dra.backend.models.responses.HateoasResposta;
import com.dra.backend.services.AuthService;
import com.dra.backend.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth")

@RestController
@RequestMapping("/api/auth")
@SecurityRequirements
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/criar")
    @Operation(summary = "Cria um novo usuário.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Usuário já existe.")
    @ApiResponse(responseCode = "400", description = "Erro ao criar usuário. Por favor, verifique os dados e tente novamente.")
    ResponseEntity<HateoasResposta> criarUsuario(@RequestBody CriarUsuarioDTO contatoDTO) {
        Optional<String> error = CriarUsuarioDTO.validarCampos(contatoDTO);
        HateoasResposta resposta = new HateoasResposta();
        resposta.set_links(Links.of(
                linkTo(methodOn(AuthController.class).criarUsuario(null)).withSelfRel().withType("POST"),
                linkTo(methodOn(AuthController.class).login(null)).withSelfRel()
                        .withType("POST")));
        if (error.isPresent()) {
            resposta.setError(error.get());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
        try {
            Usuario usuario = Usuario.from(contatoDTO);
            Optional<Usuario> user = this.authService.register(usuario);
            if (user.isEmpty()) {
                resposta.setError("Usuário já existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (Exception e) {
            resposta.setError("Erro ao criar usuário. Por favor, verifique os dados e tente novamente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(resposta);
        }
    }

    @PostMapping("/logar")
    @Operation(summary = "Realiza login.")
    @ApiResponse(responseCode = "200", description = "JWT Token")
    @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos.")
    ResponseEntity<HateoasResposta> login(@RequestBody LogarUsuarioDTO usuario) {
        Optional<String> error = LogarUsuarioDTO.validarCampos(usuario);
        HateoasResposta resposta = new HateoasResposta();
        resposta.set_links(
                Links.of(
                        linkTo(methodOn(AuthController.class).login(null)).withSelfRel()
                                .withType("POST"),
                        linkTo(methodOn(AuthController.class).criarUsuario(null)).withSelfRel().withType("POST")));
        if (error.isPresent()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
        try {
            var usernameAndPassword = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
            var auth = authenticationManager.authenticate(usernameAndPassword);
            Usuario user = (Usuario) auth.getPrincipal();
            String jwtToken = this.jwtService.generateToken(user.getEmail());
            resposta.setResposta(jwtToken);
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            resposta.setError("Usuário ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
        }
    }

    @GetMapping("/refresh")
    @Operation(summary = "Atualiza o token.")
    @ApiResponse(responseCode = "200", description = "JWT Token")
    @ApiResponse(responseCode = "401", description = "Token inválido.")
    ResponseEntity<String> refresh(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String jwtToken = this.jwtService.refreshToken(jwt);
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }
    }

}
