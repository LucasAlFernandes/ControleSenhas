package Dados;

import java.util.Date;

import Controle.Login;

public class Senha implements java.io.Serializable
{
	/*Propriedades*/
	private int 		NumSenha;
	private boolean 	Preferencial;
	private Date 		Data; 
	private Login 		User;
	private boolean  	Cancelada;
	private boolean		Atrasada;
	private boolean  	Chamada;
	private int			pos;
	private String 		NomeGerente;
	
	public Senha(int _NumSenha, boolean _Preferencial, Date _Data, Login _U)
	{
		NumSenha 		=	 _NumSenha;
		Preferencial 	=	 _Preferencial;
		Data 			=	 _Data;
		User			=	 _U;
	}
	
	public void setUsuario(Login l)
	{
		this.User = l;
	}
	
	public Login getUsuario()
	{
		return this.User;
	}
		
	public void setNomeGerente(String NomeGerente)
	{
		this.NomeGerente = NomeGerente;
	}
	
	public void setCancelada(boolean Cancelada)
	{
		this.Cancelada = Cancelada;
	}
	
	public void setAtrasada(boolean Atrasada)
	{
		this.Atrasada = Atrasada;
	}
	
	public void setChamada(boolean Chamada)
	{
		this.Chamada = Chamada;
	}
	
	public void setPos(int pos)
	{
		this.pos = pos;
	}
		
	public boolean getCancelada()
	{
		return Cancelada;
	}
	
	public String getNomeGerente()
	{
		return NomeGerente;
	}
	
	public boolean getAtrasada()
	{
		return Atrasada;
	}
	
	public boolean getChamada()
	{
		return Chamada;
	}
	
	public int getPos()
	{
		return pos;
	}
	
	public boolean getPreferencial()
	{
		return this.Preferencial;
	}
	
	public String getSenha()
	{
		String num = NumSenha < 10  ? "000" : (NumSenha < 100  ? "00" :  "0");
		return Preferencial ? "P" + num + Integer.toString(NumSenha) : "N" + num + Integer.toString(NumSenha); 
	}
	
	public String getInfo()
	{
		return "Senha requisitada para o cliente "+this.User.getNome()+", na data "+this.Data;
	}
}
