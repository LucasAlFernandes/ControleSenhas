import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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
import Dados.Gerente;
import Dados.Senha;

@WebServlet("/Cliente")
public class Cliente extends HttpServlet
{
	GerenciaLogin gl ;
	controleSenhas cs;
	Login sl;
	Senha s;
	
	HttpSession pageSession;	
	PageContext pageContext;
	
	String pageIndex = "index.jsp";
	String path = "";
	String Menu = "";
	String Site = "";
	
	private void initVar(HttpServletRequest request, HttpServletResponse response)
	{
		pageSession = request.getSession();
	    pageContext = JspFactory.getDefaultFactory().getPageContext(this,	request, response,	null, false, JspWriter.DEFAULT_BUFFER,	true );
	    path = (String)pageSession.getAttribute("startUpPath");
	    sl = (Login)pageSession.getAttribute("Login");
	    s  = (Senha)pageSession.getAttribute("Senha");
	    gl = (GerenciaLogin) pageContext.getAttribute("gerenciaLogin", pageContext.APPLICATION_SCOPE);
	    cs = (controleSenhas) pageContext.getAttribute("controleSenhas", pageContext.APPLICATION_SCOPE);
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(request.getSession().getAttribute("RetServer")== null)
		{
			request.getRequestDispatcher("Server").forward(request, response);
			pageSession.setAttribute("RetServer", null);
			return;
		}
		initVar(request, response);
		
        setMenu();        
        
        String _page = "";
        if(request.getParameter("paginaSolicitada") == null)
        	_page = (String) pageSession.getAttribute("PaginaAtual");
        else        		
        	_page = request.getParameter("paginaSolicitada");
        setPage(_page);
        
        if (!response.isCommitted()){  
     	   RequestDispatcher dispatcher = request.getRequestDispatcher(pageIndex);   
     	   dispatcher.forward(request, response);   
     	}
    }	
	
	/*Dados Visuais Pagina*/
	private void setMenu()
	{
		Menu = "<script type='text/javascript'>"+
					"function _click(obj)"+
					"{"+						
						"document.getElementById('paginaSolicitada').value = obj.id;"+
						"document.frmPagina.submit();"+
					"}"+			
				"</script>"+
				"<a id='Inicio' onclick='_click(this)'>Inicio</a><br>";
		if(sl == null)
		{			
			Menu += "<a id='Login' onclick='_click(this)'>Entrar</a><br>";
			Menu += "<a id='Cadastro' onclick='_click(this)'>Cadastrar</a><br>";			
		}
		else
		{
			if(sl.getGerente())
			{
				Menu += "<a id='Sistema' onclick='_click(this)'>Sistema</a><br>";
			}
			else
			{
				Menu += "<a id='Senha' onclick='_click(this)'>Senhas</a><br>";
			}
			Menu += "<a id='Fila' onclick='_click(this)'>Fila</a><br>";	
			Menu += "<a id='Sair' onclick='_click(this)'>Sair</a><br>";	
		}
		Menu += "<form id='frmPagina' name='frmPagina' action='Cliente' method='post'>";
		Menu += "<input type='hidden' name='paginaSolicitada' id='paginaSolicitada' value='Cadastrar'>"; 
		Menu += "</form>";
		pageSession.setAttribute("Menu", Menu);
	}
	
	private void setPage(String page)
	{
		if(page != null)
		{
			if(page.equals("Cadastro"))
			{
				setPageCadastro();
			}
			else if(page.equals("Login"))
			{
				setPageLogin();
			}
			else if(page.equals("Fila"))
			{
				setPageFila();
			}
			else if(page.equals("Senha"))
			{
				setPageSenha();
			}
			else if(page.equals("Sistema"))
			{
				setPageSistema();
			}
			else if(page.equals("Sair"))
			{
				if(sl != null)
				{
					gl.alterarEstado(sl, false);
					try
					{					
						if(sl.getGerente() && cs.getGerentes().size() > 0)						
							cs.removerGerente(sl.getNome());						
					}
					catch(Exception e)
					{
							pageSession.setAttribute("SereverError", e.getMessage());  
					}
					sl = null;
				}
				pageSession.setAttribute("Login", null);
				pageSession.setAttribute("Senha", null);
				pageSession.setAttribute("PaginaAtual", null);
				setMenu();	
				setPage("Inicio");
			}
			else
			{
				setPageDefault();
			}			
		}
		else
		{
			setPageDefault();
		}
		pageSession.setAttribute("Pagina", Site);
	}
	private void setPageDefault()
	{		
		Site = "";
		pageSession.setAttribute("TimeToRefresh", 3);
		pageSession.setAttribute("Titulo", "Inicio");
		try
		{
			Iterator it = cs.getGerentes().iterator();
			while(it.hasNext())
			{
				Gerente g = (Gerente) it.next();
				Site+="<div id='letreiroExterno'><div id='letreiroInterno'><label>Senha Atual</label><br>"+g.getUsuario().getNome()+" : "+((Senha)g.getSenhas().get(g.getSenhas().size()-1)).getSenha()+"<label></label></div></div><br>";
			}
		}
		catch(Exception e)
		{
			
		}
	}
	private void setPageCadastro()
	{
		pageSession.setAttribute("TimeToRefresh", 9999999);
		Site = "";
		pageSession.setAttribute("Titulo", "Cadastro");		
		Site+= "<div id='frmUsuarioSenha'>"+
				"<br><h1>Criar uma Conta</h1>"+
				"<form method='post' action='Cliente' >"+
					"<br>"+
					"<label>[Usuario]:</label>&emsp;"+
					"<input type='text' name='inLogin'><br><br>"+
					"<label>[Senha]:</label>&emsp;"+
					"<input type='password' name='inSenha'><br><br>"+
					"<input type='hidden' name='cadastroOk' value='true'>"+
					""+
					"<input type='submit' name='cadastrar' value='Cadastrar'>"+
				"</form><br>"+
				"<h3>"+pageSession.getAttribute("msg")+"</h3>"+
				"</div>";
	}
	private void setPageLogin()
	{
		pageSession.setAttribute("TimeToRefresh", 9999999);
		Site = "";
		pageSession.setAttribute("Titulo", "Login");	
		if(sl == null)
		{
			pageSession.setAttribute("Titulo", "Login");		
			Site+=	"<div id=\"frmUsuarioSenha\">"+
					"<br><h1>Logar no Site</h1>"+
					"<form method=\"post\" action=\"Cliente\" id=\"frmLogin\">"+
						"<br>"+
						"<label>[Usuario]:</label>&emsp;"+
						"<input type=\"text\" name=\"inLogin\"><br><br>"+
						"<label>[Senha]:</label>&emsp;"+
						"<input type=\"password\" name=\"inSenha\"><br><br>"+
						"<input type=\"hidden\" name=\"loginOk\" value=\"true\">"+
						"<input type=\"submit\" name=\"logar\" value=\"Login\">"+
					"</form><br>"+
					"<h3>"+pageSession.getAttribute("msg")+"</h3>"+
					"</div>"+
					"<h3 style='color:red; margin-left: 6.5%;'></h3>";
		}
	}
	private void setPageFila()
	{
		pageSession.setAttribute("TimeToRefresh", 3);
		Site = "";
		pageSession.setAttribute("Titulo", "Fila");	
		if(sl != null)
		{
			int maxProxima = 10;
			Senha[] sp = cs.getFilaPreferencial(), 
			        sn = cs.getFilaNormal(),
			        st = cs.getProximas(maxProxima);
			Site += "<table >";
			Site += "<tr>";
			Site += "<td width=\"20%\">";
			Site += "<table>";
			Site += "<tr>";
			Site += " <td style=\"text-align: center;\">";
			Site += " Fila de Senhas";
			Site += "</td>";
			Site += "</tr>";
			Site += "<tr>";
			Site += "<td>";
			Site += "<div class=\"CSSTableGenerator\" id=\"filanorm\">";
			Site += "<table>";
			Site += "<tr>";
			Site += "<td>";
			Site += "Senha";
			Site += "</td>";
			Site += "<td >";
			Site += "Cliente";
			Site += "</td>";
			Site += "</tr>";
			boolean estaNaFila = false;
			Senha ls = null;
			if(sl != null)							
				ls = cs.getSenhaUsuario(sl);			
			for(int i = 0; i < maxProxima; i++)
			{
	        	 if(st[i] != null)
	        	 {
	        		 if(ls != null)
	        		 {
		        		 if(st[i].equals(ls) )
		        		 {
		        			 estaNaFila = true; 
		        			 Site += "<tr>";
			        		 Site += "<td style = 'background: red; color: white;'>";			        		 
		        		 }
		        		 else
		        		 {
		        			 Site += "<tr>";
			        		 Site += "<td >";			        		
		        		 }
	        		 }
	        		 else
	        		 {
		        		 Site += "<tr>";
		        		 Site += "<td >";		        		
	        		 }
	        		 Site += st[i].getSenha() ;
	        		 Site += "</td>";
	        		 Site += "<td>";
	        		 Site += st[i].getUsuario().getNome();
	        		 Site += "</td> ";
	        		 Site += "</tr> ";	
	        	 }
			}
			if(!estaNaFila && sl != null && ls != null)
			{
				if(!ls.getAtrasada() && !ls.getCancelada() && !ls.getChamada())
				{
					Site += "<tr>";
		       		Site += "<td >";
		       		Site += "...";
		       		Site += "</td>";
		       		Site += "<td>";
		       		Site += "...";
		       		Site += "</td> ";
		       		Site += "</tr> ";
		       		Site += "<tr>";
		       		Site += "<td style = 'background: red;'>";
		       		Site += ls.getSenha() ;
		       		Site += "</td>";
		       		Site += "<td>";
		       		Site += ls.getUsuario().getNome();
		       		Site += "</td> ";
		       		Site += "</tr> ";
				}
			}
	        Site += "</table>";
	        Site += "</div> ";
			Site += "</td>";
			Site += "</tr>";
			Site += "</table>";
			Site += "</td>";
			Site += "<td width=\"40%\">";
			Site += "<div class=\"CSSTableGenerator\" id=\"filapref\">";
			Site += "<table >";
			Site += "<tr>";
			Site += "<td>";
			Site += "Senha (Preferencial)";
			Site += "</td>";
			Site += "<td >";
			Site += "Info";
			Site += " </td>";
			Site += "</tr>";
			for(int i = 0; i < cs.getUltimaSenhaP(); i++)
	         {
	        	 if(!sp[i].getAtrasada() && !sp[i].getCancelada() && !sp[i].getChamada())
	        	 {
					Site += "<tr>";
					Site += "<td >";
					Site += sp[i].getSenha();
					Site += "</td>";
					Site += "<td>";
					Site += sp[i].getInfo();
					Site += "</td> ";
					Site += "</tr> ";
	        	 }
	         }
			Site += "</table>";
			Site += "</div> ";
			Site += "</td>";
			Site += "<td width=\"40%\">";
			Site += "<div class=\"CSSTableGenerator\" id=\"filanorm\">";
			Site += "<table >";
			Site += "<tr>";
			Site += "<td>";
			Site += "Senha (Normal)";
			Site += "</td>";
			Site += "<td >";
			Site += "Info";
			Site += " </td>";
			Site += "</tr>";
			for(int i = 0; i < cs.getUltimaSenhaN(); i++)
	         {
	        	 if(!sn[i].getAtrasada() && !sn[i].getCancelada() && !sn[i].getChamada())
	        	 {
					Site += "<tr>";
					Site += "<td >";
					Site += sn[i].getSenha();
					Site += "</td>";
					Site += "<td>";
					Site += sn[i].getInfo();
					Site += "</td> ";
					Site += "</tr> ";
	        	 }
	         }
			Site += "</table>";
			Site += "</div> ";
			Site += "</td>";			
			Site += "</tr>";
			Site += "</table>";
			Site += "</div>";
			
		}
	}
	private void setPageSenha()
	{
		Senha se = cs.getSenhaUsuario(sl);
		pageSession.setAttribute("TimeToRefresh", 9999999);
		Site = "";
		pageSession.setAttribute("Titulo", "Senhas");	
		if(se == null)
		{
			Site += "<h3>Voce ainda não solicitou uma senha para o atendimento!</h3>"+
					"<h3>Para solicitar a senha utilize o formulario abaixo</h3>"+
					"<form method=\"post\" action=\"Cliente\">"+
						"<input type=\"hidden\" name=\"gerarOk\" value=\"true\">"+
						"<input type=\"checkbox\" name=\"Preferencial\"><label>Atendimento Preferencial</label><br><br>"+
						"<input type=\"submit\"  value=\"Gerar Senha\">"+
					"</form>";
		}
		else if(se.getAtrasada())
		{
			Site += "<h3>Sua senha ja foi chamada!</h3>"+
					"<h3>Para entra na fila novamente renove sua senha</h3>"+
					"<form method=\"post\" action=\"Cliente\">"+
						"<input type=\"hidden\" name=\"renovarOk\" value=\"true\">"+
						"<input type=\"submit\" value=\"Renovar senha\">"+
						"</form>"+
						"<h3>ou Para cancelar senha utilize o link abaixo</h3>"+
						"<form method=\"post\" action=\"Cliente\">"+
						"<input type=\"hidden\" name=\"cancelarOk\" value=\"true\">"+
						"<input type=\"submit\" value=\"Cancelar Senha\">"+
						"</form>";
		}
		else if(se.getCancelada() || se.getChamada())
		{
			Site += "<h3>Voce ainda não solicitou uma senha para o atendimento!</h3>"+
					"<h3>Para solicitar a senha utilize o formulario abaixo</h3>"+
					"<form method=\"post\" action=\"Cliente\">"+
						"<input type=\"hidden\" name=\"gerarOk\" value=\"true\">"+
						"<input type=\"checkbox\" name=\"Preferencial\" value=\"disabled\"><label>Atendimento Preferencial</label><br><br>"+
						"<input type=\"submit\"  value=\"Gerar Senha\">"+
					"</form>";
		}		
		else
		{ 
			Site += "<h3>Voce já solicitou uma senha para o atendimento!</h3>"+
					"<h3>Para cancelar senha utilize o link abaixo</h3>"+
					"<form method=\"post\" action=\"Cliente\">"+
						"<input type=\"hidden\" name=\"cancelarOk\" value=\"true\">"+
						"<input type=\"submit\" value=\"Cancelar Senha\">"+						
						"</form>";
		}
	}
	private void setPageSistema()
	{
		pageSession.setAttribute("TimeToRefresh", 9999999);
		Site = "<h3>"+pageSession.getAttribute("msg")+"</h3><br>";
		pageSession.setAttribute("Titulo", "Sistema");	
		if(pageSession.getAttribute("senhaChamada") != null)
		{
			Senha satual = (Senha)pageSession.getAttribute("senhaChamada");
			if(satual != null)
				Site +="<h3>Ultima senha chamada: "+satual.getSenha()+" Info: " +satual.getInfo()+"</h3>";
			Site +="<form method='post' action='Cliente'><input name='actm' type='hidden' value='m'><input type='submit' value='Cliente ausente'></form><br>";
		}
		else
		{
			Site +="<h2>Não ha senhas registradas</h2>";
		}
		Site += "<form method=\"post\" action=\"Cliente\"><input name=\"actrs\" type=\"hidden\" value=\"rt\"><input type=\"submit\" value=\"Reiniciar Sistema\"></form><br>"+
				"<form method=\"post\" action=\"Cliente\"><input name=\"actrc\" type=\"hidden\" value=\"rt\"><input type=\"submit\" value=\"Reiniciar Contagem\"></form><br>";
		Site += "<form method=\"post\" action=\"Cliente\"><input name=\"actc\" type=\"hidden\" value=\"c\"><input type=\"submit\" value=\"Chamar Proxima Senha\"></form>";
	}
	
	
}
