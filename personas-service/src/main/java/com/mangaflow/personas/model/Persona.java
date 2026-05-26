package com.mangaflow.personas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "personas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "El teléfono debe tener entre 8 y 12 números")
    @Column(nullable = false)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private Boolean activo = true;
}
