package com.dra.backend.controllers;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.dra.backend.models.responses.HateoasResposta;
import com.dra.backend.models.responses.ListarContato;
import com.dra.backend.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuario")
@ApiResponses(value = {
                @ApiResponse(responseCode = "403", description = "Acesso negado")
})
@RequestMapping("/api/usuario")
@RestController
public class UsuarioController {
        @Autowired
        UsuarioService usuarioService;

        @GetMapping
        @Operation(summary = "Lista todos os usuarios.")
        @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso.")
        ResponseEntity<HateoasResposta> listarContatos() {
                List<ListarContato> usuario = usuarioService.listarUsuarios();
                HateoasResposta response = new HateoasResposta();
                response.setResposta(usuario);
                response.set_links(Links.of(
                                linkTo(methodOn(UsuarioController.class).listarContatos()).withSelfRel()
                                                .withType("GET"),
                                linkTo(methodOn(UsuarioController.class).listarContatoPorEmail("email"))
                                                .withSelfRel()
                                                .withType("GET"),
                                linkTo(methodOn(UsuarioController.class).deletarContato("email")).withSelfRel()
                                                .withType("DELETE")));
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{email}")
        @Operation(summary = "Lista um usuario pelo email.")
        @ApiResponse(responseCode = "200", description = "Contato listado com sucesso.")
        @ApiResponse(responseCode = "404", description = "Contato não encontrado.")
        ResponseEntity<HateoasResposta> listarContatoPorEmail(@PathVariable String email) {
                Optional<ListarContato> usuario = usuarioService.listarUsuario(email);
                if (!usuario.isPresent()) {
                        return ResponseEntity.notFound().build();
                }
                HateoasResposta response = new HateoasResposta();
                response.setResposta(List.of(usuario.get()));
                response.set_links(
                                Links.of(
                                                linkTo(methodOn(UsuarioController.class).listarContatoPorEmail(email))
                                                                .withSelfRel()
                                                                .withType("GET"),
                                                linkTo(methodOn(UsuarioController.class).listarContatos()).withSelfRel()
                                                                .withType("GET"),
                                                linkTo(methodOn(UsuarioController.class).deletarContato(email))
                                                                .withSelfRel().withType(email)));

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{email}")
        @Operation(summary = "Deleta um usuario pelo email.")
        @ApiResponse(responseCode = "204", description = "Contato deletado com sucesso.")
        @ApiResponse(responseCode = "403", description = "Não é possível deletar o usuario de outro usuário.")
        ResponseEntity<HateoasResposta> deletarContato(@PathVariable String email) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String emailLogado = authentication.getName();
                HateoasResposta response = new HateoasResposta();
                response.set_links(
                                Links.of(
                                                linkTo(methodOn(UsuarioController.class).deletarContato(email))
                                                                .withSelfRel()
                                                                .withType("DELETE"),
                                                linkTo(methodOn(UsuarioController.class).listarContatos())
                                                                .withRel("usuario").withType("GET"),
                                                linkTo(methodOn(UsuarioController.class).listarContatoPorEmail(email))
                                                                .withRel("usuario")
                                                                .withType("GET")));
                if (!emailLogado.equals(email)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                }
                usuarioService.deletarUsuario(emailLogado);
                return ResponseEntity.ok().body(response);
        }

}
