package Dados;

import java.util.ArrayList;

import Controle.Login;

public class Gerente  implements java.io.Serializable
{
	Login usu;
	ArrayList	senhas = new ArrayList();
	
	public void setUsuario(Login usu)
	{
		this.usu = usu;
	}
	
	public Login getUsuario()
	{
		return usu;
	}
	
	public void addSenha(Senha s)
	{
		this.senhas.add(s);
	}
	
	public ArrayList getSenhas()
	{
		return senhas;
	}
}
