package view;

import java.io.IOException;

import model.Cidade;
import model.ListaCidades;
import model.Previsao;
import model.PrevisaoCidade;
import model.parse.XStreamParser;
import model.service.WeatherForecastService;

public class Main {

	public static void main(String[] args) {
		
		try {	
			String cityName = "Machado";
			String cidadesXML = 
				WeatherForecastService.cidades(cityName);
			
			XStreamParser<PrevisaoCidade, ListaCidades> 
				xspCidades = new XStreamParser();
			
			ListaCidades listaCidades = (ListaCidades) 
					xspCidades.cidades(cidadesXML);
			
			for (Cidade c : listaCidades.getCidades()) {
				
				String previsaoXML = 
						WeatherForecastService.
						previsoesParaSeteDias(c.getId());
				
				XStreamParser<PrevisaoCidade, ListaCidades> 
					xspPrevisoes = new XStreamParser();
				
				PrevisaoCidade pc = (PrevisaoCidade) 
						xspPrevisoes.previsao(previsaoXML);
				
				System.out.printf("Previsão para: %s %s\n", 
						c.getNome(), c.getUf());
				
				for (Previsao p : pc.getPrevisoes()) {
					System.out.printf("Dia: %s, Máx: %s, "
							+ "Min: %s, Tempo: %s \n", p.getDia(), 
							p.getMaxima(), p.getMinima(), 
							p.getTempo());
				}
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	
	}
}