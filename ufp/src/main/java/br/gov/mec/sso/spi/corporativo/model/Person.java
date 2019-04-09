package br.gov.mec.sso.spi.corporativo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@NamedQueries({
        @NamedQuery(name="getPersonByUsername", query="select p from Person p where p.nuCpfcnpj = :username")
})
@Entity
@Table(name = "TB_PESSOA", schema = "DBCORPORATIVO", catalog = "")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESSOA_SEQ")
    @SequenceGenerator(sequenceName = "DBCORPORATIVO.tb_pessoa_seq", allocationSize = 1, name = "PESSOA_SEQ")
    @Column(name = "CO_PESSOA", nullable = false, precision = 0)
    private Long coPessoa;

    @Basic
    @Column(name = "NO_PESSOA", nullable = false, length = 150)
    private String noPessoa;

    @Basic
    @Column(name = "NU_CPFCNPJ", nullable = true, length = 14)
    private String nuCpfcnpj;

    @Basic
    @Column(name = "DT_CADASTRO", nullable = false)
    private Date dtCadastro = new Date(System.currentTimeMillis());

    @Basic
    @Column(name = "ST_CADASTRO", nullable = true, length = 1)
    private String stCadastro;

    @Basic
    @Column(name = "CO_SITUACAO_CADASTRO", nullable = true, precision = 0)
    private Long coSituacaoCadastro;

    @Basic
    @Column(name = "TP_PESSOA", nullable = false, precision = 0)
    private Long tpPessoa;

    public Long getCoPessoa() {
        return coPessoa;
    }

    public void setCoPessoa(Long coPessoa) {
        this.coPessoa = coPessoa;
    }

    public String getNoPessoa() {
        return noPessoa;
    }

    public void setNoPessoa(String noPessoa) {
        this.noPessoa = noPessoa;
    }

    public String getNuCpfcnpj() {
        return nuCpfcnpj;
    }

    public void setNuCpfcnpj(String nuCpfcnpj) {
        this.nuCpfcnpj = nuCpfcnpj;
    }

    public Long getTpPessoa() {
        return tpPessoa;
    }

    public void setTpPessoa(Long tpPessoa) {
        this.tpPessoa = tpPessoa;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public String getStCadastro() {
        return stCadastro;
    }

    public void setStCadastro(String stCadastro) {
        this.stCadastro = stCadastro;
    }

    public Long getCoSituacaoCadastro() {
        return coSituacaoCadastro;
    }

    public void setCoSituacaoCadastro(Long coSituacaoCadastro) {
        this.coSituacaoCadastro = coSituacaoCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person that = (Person) o;

        if (coPessoa != that.coPessoa) return false;
        if (noPessoa != null ? !noPessoa.equals(that.noPessoa) : that.noPessoa != null) return false;
        if (nuCpfcnpj != null ? !nuCpfcnpj.equals(that.nuCpfcnpj) : that.nuCpfcnpj != null) return false;
        if (tpPessoa != null ? !tpPessoa.equals(that.tpPessoa) : that.tpPessoa != null) return false;
        if (dtCadastro != null ? !dtCadastro.equals(that.dtCadastro) : that.dtCadastro != null) return false;
        if (stCadastro != null ? !stCadastro.equals(that.stCadastro) : that.stCadastro != null) return false;
        if (coSituacaoCadastro != null ? !coSituacaoCadastro.equals(that.coSituacaoCadastro) : that.coSituacaoCadastro != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (coPessoa ^ (coPessoa >>> 32));
        result = 31 * result + (noPessoa != null ? noPessoa.hashCode() : 0);
        result = 31 * result + (nuCpfcnpj != null ? nuCpfcnpj.hashCode() : 0);
        result = 31 * result + (tpPessoa != null ? tpPessoa.hashCode() : 0);
        result = 31 * result + (dtCadastro != null ? dtCadastro.hashCode() : 0);
        result = 31 * result + (stCadastro != null ? stCadastro.hashCode() : 0);
        result = 31 * result + (coSituacaoCadastro != null ? coSituacaoCadastro.hashCode() : 0);
        return result;
    }
}
