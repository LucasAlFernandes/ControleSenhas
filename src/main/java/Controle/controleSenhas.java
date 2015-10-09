package Controle;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import Dados.Gerente;
import Dados.Senha;


public class controleSenhas implements java.io.Serializable
{
	private Senha[] 		FilaSenhasNormal;
	private Senha[] 		FilaSenhasPreferencial;
	
	private int   		PUltimaSenhaNormal;
	private int   		PUltimaSenhaPreferencial;
	
	private int   		PUltimaSenhaNormalChamada;
	private int   		PUltimaSenhaPreferencialChamada;
		
	private ArrayList Gerentes = new ArrayList();	
	private static controleSenhas cs = new controleSenhas();
			
	public static controleSenhas getInstancia()
	{
		return controleSenhas.cs;
	}
	
	public void inserirGerente(Login l)
	{
		Gerente g = new Gerente();
		g.setUsuario(l);
		this.Gerentes.add(g);
	}
	
	public void removerGerente(String Nome)
	{
		Iterator it = this.Gerentes.iterator();
		int i =0;
		while(it.hasNext())
		{
			Gerente g = (Gerente)it.next();
			if(Nome == g.getUsuario().getNome())
			{
				break;
			}				
			i++;
		}
		this.Gerentes.remove(i);
	}
	
	public void InserirSenhaGerente(String Nome, Senha s)
	{
		Iterator it = this.Gerentes.iterator();
		int i =0;
		Gerente gaux = null;
		while(it.hasNext())
		{
			Gerente g = (Gerente)it.next();
			if(Nome == g.getUsuario().getNome())
			{
				gaux = g;
				gaux.addSenha(s);
				break;
			}				
			i++;
		}
		this.Gerentes.remove(i);
		this.Gerentes.add(gaux);		
	}
	
	public boolean usuarioContemSenha(Login l)
	{
		for(int i = 0; i < this.PUltimaSenhaPreferencial; i++)
		{
			if(    !this.FilaSenhasPreferencial[i].getChamada() 
			   &&  !this.FilaSenhasPreferencial[i].getCancelada()
			   &&   this.FilaSenhasPreferencial[i].getUsuario().getNome().equals(l.getNome()))
			{				
				return true;
			}
		}
		
		for(int i = 0; i < this.PUltimaSenhaNormal; i++)
		{
			if(    !this.FilaSenhasNormal[i].getChamada() 
					   &&  !this.FilaSenhasNormal[i].getCancelada()
					   &&   this.FilaSenhasNormal[i].getUsuario().getNome().equals(l.getNome()))
					{				
						return true;
					}
		}
		return false;
	}
	
	public Senha getSenhaUsuario(Login l)
	{
		for(int i = 0; i < this.PUltimaSenhaPreferencial; i++)	
			if(this.FilaSenhasPreferencial[i].getUsuario() != null)
				if( this.FilaSenhasPreferencial[i].getUsuario().equals(l))						
					return this.FilaSenhasPreferencial[i];	
		
		for(int i = 0; i < this.PUltimaSenhaNormal; i++)	
			if(this.FilaSenhasNormal[i].getUsuario() != null)
				if( this.FilaSenhasNormal[i].getUsuario().equals(l))							
					return this.FilaSenhasNormal[i];		
		
		return null;
	}
	
	public ArrayList getGerentes()
	{
		return this.Gerentes;
	}
	
	public int getUltimaSenhaP()
	{
		return this.PUltimaSenhaPreferencial;
	}
		
	public Senha chamarProximaSenha(Login g)
	{
		for(int i = 0; i < this.PUltimaSenhaPreferencial; i++)
		{
			if(!this.FilaSenhasPreferencial[i].getChamada() && !this.FilaSenhasPreferencial[i].getCancelada())
			{
				this.FilaSenhasPreferencial[i].setNomeGerente(g.getNome());
				this.FilaSenhasPreferencial[i].setChamada(true);
				this.PUltimaSenhaPreferencialChamada = i;
				return this.FilaSenhasPreferencial[i];
			}
		}
		
		for(int i = 0; i < this.PUltimaSenhaNormal; i++)
		{
			if(!this.FilaSenhasNormal[i].getChamada() && !this.FilaSenhasNormal[i].getCancelada())
			{
				this.FilaSenhasNormal[i].setNomeGerente(g.getNome());
				this.FilaSenhasNormal[i].setChamada(true);
				this.PUltimaSenhaNormalChamada = i;
				return this.FilaSenhasNormal[i];
			}
		}		
		return null ;
	}

	public void renovarSenha(Senha s)
	{
		s.setAtrasada(false);
		s.setCancelada(false);
		s.setChamada(false);
		if(s.getPreferencial())	
		{
			int pos = this.PUltimaSenhaPreferencialChamada;
			int multa = pos - s.getPos();			
			this.FilaSenhasPreferencial[s.getPos()] = null;
			for(int i = 0; i < multa && i+s.getPos() < this.PUltimaSenhaPreferencialChamada; i++)
			{
				int _pos = i+s.getPos();
				this.FilaSenhasPreferencial[_pos] = this.FilaSenhasPreferencial[_pos+1];
			}
			s.setPos(multa);
			this.FilaSenhasPreferencial[s.getPos()] = s;
		}
		else
		{
			int pos = this.PUltimaSenhaNormalChamada;
			int multa = pos - s.getPos();
			this.FilaSenhasNormal[s.getPos()] = null;						
			for(int i = 0; i < multa && i+s.getPos() < this.PUltimaSenhaNormalChamada; i++)
			{
				int _pos = i+s.getPos();
				this.FilaSenhasNormal[_pos] = this.FilaSenhasNormal[_pos+1];
			}
			s.setPos(multa);			
			this.FilaSenhasNormal[s.getPos()] = s;
		}
	}
	
	public void atrasarSenha(Senha s)
	{
		if(s.getPreferencial())		
			this.FilaSenhasPreferencial[s.getPos()].setAtrasada(true);
		else
			this.FilaSenhasNormal[s.getPos()].setAtrasada(true);
	}
	
	public void cancelarSenha(Senha s)
	{
		if(s.getPreferencial())		
		{
			this.FilaSenhasPreferencial[s.getPos()].setCancelada(true);
			FilaSenhasPreferencial[s.getPos()].setUsuario(null);
		}
		else
		{
			this.FilaSenhasNormal[s.getPos()].setCancelada(true);
			FilaSenhasNormal[s.getPos()].setUsuario(null);
		}
	}
		
	public int getUltimaSenhaN()
	{
		return this.PUltimaSenhaNormal;
	}
	
	public controleSenhas()
	{
		this.FilaSenhasNormal 			= new Senha[9999];
		this.FilaSenhasPreferencial 	= new Senha[9999];		
		this.PUltimaSenhaNormal 		= 0;
		this.PUltimaSenhaPreferencial 	= 0;		
	}
		
	public void ResetarSenha()
	{
		this.FilaSenhasNormal 			= new Senha[9999];
		this.FilaSenhasPreferencial 	= new Senha[9999];		
		this.PUltimaSenhaNormal 		= 0;
		this.PUltimaSenhaPreferencial 	= 0;		
	}
		
	public Senha[] getFilaPreferencial()
	{
		return this.FilaSenhasPreferencial;
	}
	
	public Senha[] getFilaNormal()
	{
		return this.FilaSenhasNormal;
	}
	
	public Senha[] getProximas(int num)
	{
		Senha[] proximas = new Senha[num];
		int index = 0;
		
		for(int i = 0; i < this.PUltimaSenhaPreferencial; i++)
		{
			if(		!this.FilaSenhasPreferencial[i].getChamada() 
					&& !this.FilaSenhasPreferencial[i].getCancelada()
					&& !this.FilaSenhasPreferencial[i].getAtrasada())
			{
				proximas[index] = this.FilaSenhasPreferencial[i];
				index++;
				if(index == num)
					return proximas;
			}
		}
		
		for(int i = 0; i < this.PUltimaSenhaNormal; i++)
		{
			if(!this.FilaSenhasNormal[i].getChamada() 
					&& !this.FilaSenhasNormal[i].getCancelada()
					&& !this.FilaSenhasNormal[i].getAtrasada()
					)
			{
				proximas[index] = this.FilaSenhasNormal[i];
				index++;
				if(index == num)
					return proximas;
			}
		}
		return proximas;
	}
	
	public Senha[] recadastrarSenhas(Senha[] _s)
	{
		Senha[] s = new Senha[9999];
		int pos = -1;
		for(int i = 0; i < _s.length; i++)
		{
			if(_s[i] != null)
				if(!_s[i].getCancelada() || (_s[i].getChamada() && !_s[i].getAtrasada()))				
					s[pos+=1] = _s[i];					
		}
		return s;
	}
	
	public Senha GerarSenha(Login u, boolean Preferencial)
	{
		if(this.PUltimaSenhaNormal == 9999)
		{
			this.FilaSenhasNormal = recadastrarSenhas(this.FilaSenhasNormal);
			this.PUltimaSenhaNormal = 1;
			while(this.FilaSenhasNormal[this.PUltimaSenhaNormal] != null){this.PUltimaSenhaNormal++;}
		}
		if(this.PUltimaSenhaPreferencial == 9999)
		{
			this.FilaSenhasPreferencial = recadastrarSenhas(this.FilaSenhasPreferencial);
			this.PUltimaSenhaPreferencial = 1;
			while(this.FilaSenhasPreferencial[this.PUltimaSenhaPreferencial] != null){this.PUltimaSenhaPreferencial++;}
		}
		Senha s  = null;
		if(this.getSenhaUsuario(u)!= null)
			this.cancelarSenha(this.getSenhaUsuario(u));
		if(!Preferencial)
		{
			
			s = new Senha(this.PUltimaSenhaNormal, Preferencial, new Date(), u);
			s.setPos(this.PUltimaSenhaNormal);
			this.FilaSenhasNormal[this.PUltimaSenhaNormal] = s;
			this.PUltimaSenhaNormal += 1;
		}
		else
		{
			s = new Senha(this.PUltimaSenhaPreferencial, Preferencial, new Date(), u);
			s.setPos(this.PUltimaSenhaPreferencial);
			this.FilaSenhasPreferencial[this.PUltimaSenhaPreferencial] = s;	
			this.PUltimaSenhaPreferencial += 1;
		}		
		return s;
	}
}
