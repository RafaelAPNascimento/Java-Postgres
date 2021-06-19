package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column
    @NotNull @Size(min = 5, max = 15, message = "Nome deve ter entre 5 e 15 carateres")
    private String name;
    @Column
    @Min(value = 18, message = "Idade deve ser maior ou igual a 18")
    private int age;
}
