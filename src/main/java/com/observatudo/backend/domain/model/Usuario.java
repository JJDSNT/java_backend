package com.observatudo.backend.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "role", nullable = false, columnDefinition = "varchar(255) default 'user'")
    private String role = "user";  // Valor padrão é 'user'

    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email não pode ser vazio")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EixoUsuario> eixos = new ArrayList<>();

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.role = "user";  // Valor padrão já está sendo definido
    }

    // Métodos convenientes
    public void addEixo(EixoUsuario eixo) {
        eixos.add(eixo);
        eixo.setUsuario(this);
    }

    public void removeEixo(EixoUsuario eixo) {
        eixos.remove(eixo);
        eixo.setUsuario(null);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EixoUsuario> getEixos() {
        return eixos;
    }

    public void setEixos(List<EixoUsuario> eixos) {
        this.eixos = eixos;
    }

    // Sobrescrevendo equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
