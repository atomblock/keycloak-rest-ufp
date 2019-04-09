package br.gov.mec.sso.spi.corporativo.model;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name="getUserByUsername", query="select u from User u where u.noLogin = :username"),
        @NamedQuery(name="getUserByEmail", query="select u from User u where u.dsEmail = :email"),
        @NamedQuery(name="getUserCount", query="select count(u.coPessoa) from User u"),
        @NamedQuery(name="getAllUsers", query="select u from User u"),
        @NamedQuery(name="searchForUser", query="select u from User u where ( lower(u.noLogin) like :search or u.dsEmail like :search ) order by u.noLogin")
})
@Entity
@Table(name = "TB_USUARIO", schema = "DBCORPORATIVO", catalog = "")
public class User implements Serializable {

    @Id
    @Column(name = "CO_PESSOA", nullable = false, precision = 0)
    private Long coPessoa;

    @Basic
    @Column(name = "NO_LOGIN", nullable = false, length = 255)
    private String noLogin;

    @Basic
    @Column(name = "DS_SENHA", nullable = false, length = 100)
    private String dsSenha;

    @Basic
    @Column(name = "DS_EMAIL", nullable = true, length = 255)
    private String dsEmail;

    @Basic
    @Column(name = "DS_EMAIL_REGRA", nullable = true, length = 255)
    private String dsEmailRegra;

    @Basic
    @Column(name = "ST_USUARIO_BLOQUEADO", nullable = false, length = 1)
    private String stUsuarioBloqueado;

    @Basic
    @Column(name = "ST_EMAIL_VERIFICADO", nullable = false, length = 1)
    private String stEmailVerificado;

    @Basic
    @Column(name = "ST_ALTERA_SENHA", nullable = false, length = 1)
    private String stAlteraSenha;

    @Basic
    @Column(name = "DS_PRIMEIRO_NOME", nullable = true, length = 255)
    private String firstName;

    @Basic
    @Column(name = "DS_ULTIMO_NOME", nullable = true, length = 255)
    private String lastName;

    public Long getCoPessoa() {
        return coPessoa;
    }

    public void setCoPessoa(Long coPessoa) {
        this.coPessoa = coPessoa;
    }

    public String getNoLogin() {
        return noLogin;
    }

    public void setNoLogin(String noLogin) {
        this.noLogin = noLogin;
    }

    public String getDsSenha() {
        return dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getDsEmailRegra() {
        return dsEmailRegra;
    }

    public void setDsEmailRegra(String dsEmailRegra) {
        this.dsEmailRegra = dsEmailRegra;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStUsuarioBloqueado() {
        return stUsuarioBloqueado;
    }

    public void setStUsuarioBloqueado(String stUsuarioBloqueado) {
        this.stUsuarioBloqueado = stUsuarioBloqueado;
    }

    public String getStEmailVerificado() {
        return stEmailVerificado;
    }

    public void setStEmailVerificado(String stEmailVerificado) {
        this.stEmailVerificado = stEmailVerificado;
    }

    public String getStAlteraSenha() {
        return stAlteraSenha;
    }

    public void setStAlteraSenha(String stAlteraSenha) {
        this.stAlteraSenha = stAlteraSenha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (coPessoa != that.coPessoa) return false;
        if (noLogin != null ? !noLogin.equals(that.noLogin) : that.noLogin != null) return false;
        if (dsSenha != null ? !dsSenha.equals(that.dsSenha) : that.dsSenha != null) return false;
        if (dsEmail != null ? !dsEmail.equals(that.dsEmail) : that.dsEmail != null) return false;
        if (dsEmailRegra != null ? !dsEmailRegra.equals(that.dsEmailRegra) : that.dsEmailRegra != null) return false;
        if (stUsuarioBloqueado != null ? !stUsuarioBloqueado.equals(that.stUsuarioBloqueado) : that.stUsuarioBloqueado != null)
            return false;
        if (stEmailVerificado != null ? !stEmailVerificado.equals(that.stEmailVerificado) : that.stEmailVerificado != null)
            return false;
        if (stAlteraSenha != null ? !stAlteraSenha.equals(that.stAlteraSenha) : that.stAlteraSenha != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = coPessoa.hashCode();
        result = 31 * result + (noLogin != null ? noLogin.hashCode() : 0);
        result = 31 * result + (dsSenha != null ? dsSenha.hashCode() : 0);
        result = 31 * result + (dsEmail != null ? dsEmail.hashCode() : 0);
        result = 31 * result + (stUsuarioBloqueado != null ? stUsuarioBloqueado.hashCode() : 0);
        result = 31 * result + (stEmailVerificado != null ? stEmailVerificado.hashCode() : 0);
        return result;
    }
}
