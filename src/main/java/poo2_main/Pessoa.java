//Método molde dos dados e diz "uma pessoa tem um ID, um nome, uma idade e um e-mail"

package poo2_main;

import java.util.Objects;

public class Pessoa {
    private int id;
    private String nome;
    private int idade;
    private String email;

    public Pessoa() {}
    public Pessoa(int id, String nome, int idade, String email) {
        this.id = id; 
        this.nome = nome; 
        this.idade = idade; 
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa)) return false;
        return id == ((Pessoa) o).id;
    }
    
    @Override 
    public int hashCode() { return Objects.hash(id); }
    
    @Override
    public String toString() {
        return "Pessoa{id=" + id + ", nome='" + nome + "', idade=" + idade + ", email='" + email + "'}";
    }
}