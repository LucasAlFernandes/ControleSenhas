<%@page import="java.util.Iterator"%>
<%@ page import="Dados.*" %>
<%@ page import="Controle.*" %>
<%@ page import="java.io.*" %>
<%
	String path =  getServletContext().getRealPath("/");
	GerenciaLogin gl = (GerenciaLogin) pageContext.getAttribute("gerenciaLogin", pageContext.APPLICATION_SCOPE);	
	if(gl == null)
	{
		File f = new File(path + "gerenciaLogin.ser");
		if(!f.exists())
		{
			gl = GerenciaLogin.getInstancia();
			SalvarRecuperarDados.Salvar(path+"controleUsuarios.ser", gl);
		}
		else
		{
			gl = (GerenciaLogin) SalvarRecuperarDados.Recuperar(path + "gerenciaLogin.ser");
			if(gl == null)
			{
				f.delete();
				SalvarRecuperarDados.Salvar(path+"controleUsuarios.ser", gl);
			}
		}
		pageContext.setAttribute("gerenciaLogin", gl, pageContext.APPLICATION_SCOPE);
	}
	String cadastrado = request.getParameter("cadastroOk");
	if(cadastrado != null)
	{
		Login l = new Login();
		l.setEstado(false);
		l.setNome(request.getParameter("inLogin"));
		l.setSenha(request.getParameter("inSenha"));
		l.setGerente(request.getParameter("Gerencia") != null);
		gl.addLogin(l);
		request.removeAttribute("cadastroOk");
	}
%>
<div id="frmUsuarioSenha">
<br><h1>Criar uma Conta Gerente</h1>
<form method="post" action="cadastro.jsp" >
	<br>
	<label>[Usuario]:</label>&emsp;
	<input type="text" name="inLogin"><br><br>
	<label>[Senha]:</label>&emsp;
	<input type="password" name="inSenha"><br><br>
	<input type="hidden" name="cadastroOk" value="true">
	<input type="checkbox" name="Gerencia"><label>Gerente</label><br><br>
	<input type="submit" name="cadastrar" value="Cadastrar">
</form>
</div>

