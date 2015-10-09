import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import Controle.GerenciaLogin;
import Controle.Login;
import Controle.SalvarRecuperarDados;
import Controle.controleSenhas;
import Dados.Senha;

@WebServlet("/Server")
public class Server extends HttpServlet
{
	GerenciaLogin gl ;
	controleSenhas cs;
	Login sl;
	Senha s;
	
	HttpSession pageSession;	
	PageContext pageContext;
	
	String path = "";	
	private void initVar(HttpServletRequest request, HttpServletResponse response)
	{
		pageSession = request.getSession();
	    pageContext = JspFactory.getDefaultFactory().getPageContext(this,	request, response,	null, false, JspWriter.DEFAULT_BUFFER,	true );
	    path = (String)pageSession.getAttribute("startUpPath");
	    sl = (Login)pageSession.getAttribute("Login");
	    s  = (Senha)pageSession.getAttribute("Senha");
	    pageSession.setAttribute("msg", "");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	{		
        initVar(request, response);
        pageSession.setAttribute("SereverError", "");
        try {checkGerenciaLogin();} 
        catch (Exception e) 
        {
        	pageSession.setAttribute("SereverError", e.getMessage());
		}
        try {checkControleSenhas();}
        catch (Exception e) 
        {
        	pageSession.setAttribute("SereverError", e.getMessage());
		}       
        
        String cadastrado = request.getParameter("cadastroOk");
        String login = request.getParameter("loginOk");
        
        if(cadastrado != null)
        {
        	try
        	{
        		cadastrar(request.getParameter("inLogin"), request.getParameter("inSenha"), request.getParameter("Gerencia") != null);
        	}
        	catch(Exception e)
        	{
        		pageSession.setAttribute("SereverError", e.getMessage());        		
        	}
    		request.removeAttribute("cadastroOk");
        }
        if(login != null)
        {
        	try
        	{
        		login(request.getParameter("inLogin"), request.getParameter("inSenha"));
        	}
        	catch(Exception e)
        	{
        		pageSession.setAttribute("SereverError", e.getMessage());        		
        	}
    		request.removeAttribute("loginOk");
        } 
        
        if(sl != null)
		{
			if(!sl.getGerente())
			{
				if(request.getParameter("gerarOk")!= null)
				{
					try
					{
						gerarSenha(request.getParameter("Preferencial"));
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("gerarOk");
				}
				if(request.getParameter("cancelarOk")!= null && cs.getSenhaUsuario(sl) != null)
				{
					try
					{
						cancelarSenha();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("cancelarOk");
				}
				if(request.getParameter("renovarOk")!= null)
				{
					try
					{
						renovarSenha();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("renovarOk");
				}
			}
			else
			{
				if(request.getParameter("actrs")!= null)
				{
					try
					{
						reiniciarSistema();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("actrs");
				}
				if(request.getParameter("actrc")!= null)
				{
					try	
					{
						reiniciarContagem();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("actrc");
				}
				if(request.getParameter("actm")!= null)
				{
					try
					{
						clienteAusente();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("actm");
				}
				if(request.getParameter("actc")!= null)
				{
					try
					{
						proximaSenha();
					}
		        	catch(Exception e)
		        	{
		        		pageSession.setAttribute("SereverError", e.getMessage());        		
		        	}
					request.removeAttribute("actc");
				}
			}
		}          
        
        
        pageSession.setAttribute("RetServer", "");
        if (!response.isCommitted())
        {  
        	  RequestDispatcher dispatcher = request.getRequestDispatcher("Cliente");   
        	  try
        	  {
        		 dispatcher.forward(request, response);   
        	  }
	          catch(Exception e)
	          {
	           	pageSession.setAttribute("SereverError", e.getMessage());        		
	          }
        }  
	}

	/*Logica do Servidor*/		
	private void checkGerenciaLogin() throws ClassNotFoundException, IOException
	{
		gl = (GerenciaLogin) pageContext.getAttribute("gerenciaLogin", pageContext.APPLICATION_SCOPE);
		if(gl == null)
		{
			File f = new File(path + "controleUsuarios.ser");
			if(!f.exists())
			{
				gl = GerenciaLogin.getInstancia();
				SalvarDadosUsuarios();
			}
			else
			{
				gl = (GerenciaLogin) SalvarRecuperarDados.Recuperar(path + "controleUsuarios.ser");
				if(gl == null)
				{
					f.delete();
					checkGerenciaLogin();
				}
			}
			pageContext.setAttribute("gerenciaLogin", gl, pageContext.APPLICATION_SCOPE);
		}	
	}
	private void checkControleSenhas() throws ClassNotFoundException, IOException
	{
		cs = (controleSenhas) pageContext.getAttribute("controleSenhas", pageContext.APPLICATION_SCOPE);		
		if(cs == null)
		{
			File f = new File(path + "controleSenhas.ser");
			if(!f.exists())
			{
				cs = controleSenhas.getInstancia();
				SalvarDadosSenhas();
			}
			else
			{
				cs = (controleSenhas) SalvarRecuperarDados.Recuperar(path+"controleSenhas.ser");
				if(cs == null)
				{
					f.delete();
					checkControleSenhas();
				}
			}
			pageContext.setAttribute("controleSenhas", cs, pageContext.APPLICATION_SCOPE);
		}				
	}
	
	private void SalvarDadosSenhas() throws IOException
	{
		SalvarRecuperarDados.Salvar(path+"controleSenhas.ser", cs);	
	}
	private void SalvarDadosUsuarios() throws IOException
	{
		SalvarRecuperarDados.Salvar(path+"controleUsuarios.ser", gl);
	}
		
	private void cadastrar(String Login, String Senha, boolean gerencia) throws IOException
	{
		Login l = new Login();
		l.setEstado(false);
		l.setNome(Login);
		l.setSenha(Senha);
		l.setGerente(gerencia);
		if(gl.addLogin(l))
			pageSession.setAttribute("msg", "Usuario Cadastrado com Sucesso!");
		else
			pageSession.setAttribute("msg", "Usuario ja existe");
		
		pageSession.setAttribute("PaginaAtual", "Cadastro");
		SalvarDadosUsuarios();
	}
	private void login(String Login, String Senha)
	{
		Login l = new Login();
		l.setNome(Login);
		l.setSenha(Senha);
		Login laux = gl.existeLogin(l);
		if(laux != null)
		{
			gl.alterarEstado(laux, true);
			laux.setEstado(true);
			pageSession.setAttribute("Login", laux);
			if(laux.getGerente())
				cs.inserirGerente(laux);
			pageSession.setAttribute("PaginaAtual", "Inicio");
		}
		else		
		{
			pageSession.setAttribute("PaginaAtual", "Login");
			pageSession.setAttribute("msg", "Usuario ou senha Invalidos!");
		}
	}
	private void gerarSenha(String Preferencial)
	{
		boolean p = Preferencial == null ? false : true;
		cs.GerarSenha(sl, p);		
		pageSession.setAttribute("PaginaAtual", "Senha");
	}
	private void cancelarSenha()
	{
		cs.cancelarSenha(cs.getSenhaUsuario(sl));
		pageSession.setAttribute("PaginaAtual", "Senha");
	}
	private void renovarSenha()
	{
		cs.renovarSenha(cs.getSenhaUsuario(sl));	
		pageSession.setAttribute("PaginaAtual", "Senha");
	}
	private void reiniciarSistema() throws IOException
	{
		File f = new File(path + "controleSenhas.ser");
		f.delete();
		cs = controleSenhas.getInstancia();
		pageContext.setAttribute("controleSenhas", cs, pageContext.APPLICATION_SCOPE);
		SalvarDadosSenhas();
		pageSession.setAttribute("PaginaAtual", "Sistema");
		pageSession.setAttribute("msg", "Sistema Reiniciado");
	}
	private void reiniciarContagem()
	{
		cs.ResetarSenha();
		pageSession.setAttribute("PaginaAtual", "Sistema");
		pageSession.setAttribute("msg", "Contagem Reiniciada");
	}
	private void proximaSenha()
	{
		pageSession.setAttribute("senhaChamada", cs.chamarProximaSenha(sl));
		if(pageSession.getAttribute("senhaChamada") != null)
			cs.InserirSenhaGerente(sl.getNome(),(Senha)pageSession.getAttribute("senhaChamada"));
		else
			pageSession.setAttribute("msg", "Não ha mais senhas na fila!");
		pageSession.setAttribute("PaginaAtual", "Sistema");
	}
	private void clienteAusente()
	{
		cs.atrasarSenha((Senha)pageSession.getAttribute("senhaChamada"));
		pageSession.setAttribute("PaginaAtual", "Sistema");
		pageSession.setAttribute("msg", "Cliente marcado como ausente!");
	}
}
