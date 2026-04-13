package io.github.cursodsousa.libraryapi.controller;


import io.github.cursodsousa.libraryapi.security.CustomAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "Login View Controller")
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    @Operation(summary = "Pagina Home", description = "Retorna detalhes do usuário")
    public String paginaHome(Authentication authentication) {

        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getUsuario());
        }

        return "Olá: " +authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    @Operation(summary = "Obter Authorization Code", description = "Retorna o Authorization Code")
    public String getAuthorizationCode(@RequestParam("code") String code) {
        return "Seu authorization code: " +code;
    }

}
