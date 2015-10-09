package Controle;

public class Login implements java.io.Serializable
{
	private String nome;
	private String senha;
	private boolean logado;
	private boolean gerente;
	
	public boolean getGerente()
	{
		return gerente;
	}
	
	public void setGerente(boolean gerente)
	{
		this.gerente = gerente;
	}
	
	public String getNome()
	{
		return this.nome;
	}
	
	public boolean getEstado()
	{
		return this.logado;
	}
	
	public void setEstado(boolean logado)
	{
		this.logado = logado;
	}
	
	public String getSenha()
	{
		return this.senha;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	public void setSenha(String senha)
	{
		this.senha = senha;
	}
	
	public boolean equals(Object o)
	{
		Login l = (Login)o;
		return this.nome.equals(l.getNome()) && this.senha.equals(l.getSenha()); 
	}
	
	public boolean exists(Object o)
	{
		Login l = (Login)o;
		return this.nome.equals(l.getNome()); 
	}
	
	
}
