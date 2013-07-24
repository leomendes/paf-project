package br.com.sinetic.pafecftef.entities;
// Generated 03-Mar-2013 18:19:07 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * PafRegR02 generated by hbm2java
 */
public class PafRegR02  implements java.io.Serializable {


     private int id;
     private String numFabricacao;
     private char mfAdicional;
     private String modeloEcf;
     private int numeroUsuario;
     private int crz;
     private int coo;
     private int cro;
     private Date dataMovimento;
     private Date dataEmissao;
     private BigDecimal vendaBrutaDiaria;
     private char parametroDesconto;

    public PafRegR02() {
    }

    public PafRegR02(int id, String numFabricacao, char mfAdicional, String modeloEcf, int numeroUsuario, int crz, int coo, int cro, Date dataMovimento, Date dataEmissao, BigDecimal vendaBrutaDiaria, char parametroDesconto) {
       this.id = id;
       this.numFabricacao = numFabricacao;
       this.mfAdicional = mfAdicional;
       this.modeloEcf = modeloEcf;
       this.numeroUsuario = numeroUsuario;
       this.crz = crz;
       this.coo = coo;
       this.cro = cro;
       this.dataMovimento = dataMovimento;
       this.dataEmissao = dataEmissao;
       this.vendaBrutaDiaria = vendaBrutaDiaria;
       this.parametroDesconto = parametroDesconto;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getNumFabricacao() {
        return this.numFabricacao;
    }
    
    public void setNumFabricacao(String numFabricacao) {
        this.numFabricacao = numFabricacao;
    }
    public char getMfAdicional() {
        return this.mfAdicional;
    }
    
    public void setMfAdicional(char mfAdicional) {
        this.mfAdicional = mfAdicional;
    }
    public String getModeloEcf() {
        return this.modeloEcf;
    }
    
    public void setModeloEcf(String modeloEcf) {
        this.modeloEcf = modeloEcf;
    }
    public int getNumeroUsuario() {
        return this.numeroUsuario;
    }
    
    public void setNumeroUsuario(int numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }
    public int getCrz() {
        return this.crz;
    }
    
    public void setCrz(int crz) {
        this.crz = crz;
    }
    public int getCoo() {
        return this.coo;
    }
    
    public void setCoo(int coo) {
        this.coo = coo;
    }
    public int getCro() {
        return this.cro;
    }
    
    public void setCro(int cro) {
        this.cro = cro;
    }
    public Date getDataMovimento() {
        return this.dataMovimento;
    }
    
    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }
    public Date getDataEmissao() {
        return this.dataEmissao;
    }
    
    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    public BigDecimal getVendaBrutaDiaria() {
        return this.vendaBrutaDiaria;
    }
    
    public void setVendaBrutaDiaria(BigDecimal vendaBrutaDiaria) {
        this.vendaBrutaDiaria = vendaBrutaDiaria;
    }
    public char getParametroDesconto() {
        return this.parametroDesconto;
    }
    
    public void setParametroDesconto(char parametroDesconto) {
        this.parametroDesconto = parametroDesconto;
    }




}


