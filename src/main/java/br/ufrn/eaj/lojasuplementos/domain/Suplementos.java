package br.ufrn.eaj.lojasuplementos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Suplementos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String codigoSku;
    String marca;
    Date isDeleted;
    String imgUrl;
    String nome;
    String tipo;
    Double preco;
    LocalDate validade;
}
