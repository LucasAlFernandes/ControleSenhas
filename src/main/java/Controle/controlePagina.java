package Controle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class controlePagina implements java.io.Serializable
{
	String[] DadosPagina;
	boolean  PaginaEncontrada;
	String pasta;
	
	public controlePagina(String path, String folder) throws IOException
	{
		File f = new File(path);
		pasta = folder;
		if(f.exists() && !f.isDirectory())
		{ 			
			BufferedReader lerArq = new BufferedReader(new FileReader(path));
			DadosPagina = lerArq.readLine().split(";");
			PaginaEncontrada = true;
			return;
		}
		PaginaEncontrada = false;		
	}
	
	public String TituloPagina()
	{
		return PaginaEncontrada ? DadosPagina[0] : "Inicio";
	}
	
	public String Pagina() throws IOException
	{
		return PaginaEncontrada ? "sub/"+DadosPagina[1] : "sub/default.jsp";
	}
	
	public int getTime()
	{
		return PaginaEncontrada ? Integer.parseInt(DadosPagina[2]) : 3;
	}
	
}
