package br.ufrn.eaj.lojasuplementos.exception;

// A classe precisa herdar de RuntimeException para o Spring conseguir capturar depois
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}