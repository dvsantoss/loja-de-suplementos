package br.ufrn.eaj.lojasuplementos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuplementosForm {
    @NotBlank(message = "O nome do suplemento é obrigatório.")
    private String nome;

    @NotBlank(message = "O tipo (ex: Creatina, Whey, Pré Treino..) não pode estar em branco.")
    private String tipo;

    @NotBlank(message = "O código SKU é obrigatório.")
    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$", message = "O código deve seguir o padrão: 3 letras maiúsculas, um hífen e 4 números (Ex: SUP-1234).")
    private String codigoSku;

    @NotBlank(message = "A marca é obrigatória.")
    private String marca;

    @NotNull(message = "O preço é obrigatório.")
    private Double preco;

    private Long id; // Preciso para ajudar a identificar se já existe, caso exista, é pq ta editando um Suplemento
}

// Bin validation são as anotações em cima do atributo para não vim nulo, vazio..
// Conceito de DTO ta sendo aplicado neste arquivo. O código antes de enviar pra nossa model passa por uma rigorosa validacao de canpos
// @Pattern é para regex