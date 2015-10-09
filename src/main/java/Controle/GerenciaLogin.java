package Controle;

import java.util.ArrayList;
import java.util.Iterator;

public class GerenciaLogin implements java.io.Serializable
{
	private ArrayList listaLogin = new ArrayList();
	private static GerenciaLogin gl = new GerenciaLogin();
		
	public static GerenciaLogin getInstancia()
	{
		return GerenciaLogin.gl;
	}
	
	public void alterarEstado(Login l, boolean estado)
	{
		Iterator it = this.listaLogin.iterator();
		int i  = 0;
		while(it.hasNext())
		{			
			if(l.equals(it.next()))
			{
				break;
			}
			i++;
		}
		Login laux = (Login)this.listaLogin.get(i);
		this.listaLogin.remove(i);
		laux.setEstado(estado);
		this.addLogin(laux);
	}
	
	public boolean addLogin(Login l)
	{
		if(this.existeCadastro(l) == null)
		{
			this.listaLogin.add(l);
			return true;
		}
		return false;
	}
	
	public Login existeCadastro(Login l)
	{
		Iterator it = this.listaLogin.iterator();
		while(it.hasNext())
		{
			Login laux = (Login)it.next();
			if(l.exists(laux))
				return laux;
		}
		return null;			
	}
	
	public Login existeLogin(Login l)
	{
		Iterator it = this.listaLogin.iterator();
		while(it.hasNext())
		{
			Login laux = (Login)it.next();
			if(l.equals(laux) && !laux.getEstado())
				return laux;
		}
		return null;			
	}
	
	public ArrayList getListaLogin()
	{
		return this.listaLogin;
	}
}
