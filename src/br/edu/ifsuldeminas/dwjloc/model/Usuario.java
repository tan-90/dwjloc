package br.edu.ifsuldeminas.dwjloc.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToOne(fetch = FetchType.EAGER)
    private Grupo grupo;

    private String fone;
    private String email;

    private String endereco;
    private String bairro;
    private String cidade;
    private String cep;

    @Column(unique = true)
    private String login;
    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<FerramentaAluguel> locacoes;

    public List<FerramentaAluguel> getLocacoes()
    {
        return locacoes;
    }

    public void setLocacoes(List<FerramentaAluguel> locacoes)
    {
        this.locacoes = locacoes;
    }

    public Grupo getGrupo()
    {
        return grupo;
    }

    public void setGrupo(Grupo grupo)
    {
        this.grupo = grupo;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getFone()
    {
        return fone;
    }

    public void setFone(String fone)
    {
        this.fone = fone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco(String endereco)
    {
        this.endereco = endereco;
    }

    public String getBairro()
    {
        return bairro;
    }

    public void setBairro(String bairro)
    {
        this.bairro = bairro;
    }

    public String getCidade()
    {
        return cidade;
    }

    public void setCidade(String cidade)
    {
        this.cidade = cidade;
    }

    public String getCep()
    {
        return cep;
    }

    public void setCep(String cep)
    {
        this.cep = cep;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    @Override
    public String toString()
    {
        return "Usuario [nome=" + nome + ", grupo=" + grupo + ", login=" + login + ", senha=" + senha + "]";
    }


}
