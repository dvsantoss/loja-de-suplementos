package br.ufrn.eaj.lojasuplementos.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Sempre que esta exceção acontecer, chama este método
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public String tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex, Model model) {

        // Passa a mensagem ("Suplemento não encontrado com o ID: 3")
        model.addAttribute("mensagemErro", ex.getMessage());

        return "erro";
    }
}