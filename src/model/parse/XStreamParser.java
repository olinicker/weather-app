package model.parse;

import com.thoughtworks.xstream.XStream;

import model.Cidade;
import model.ListaCidades;
import model.Previsao;
import model.PrevisaoCidade;

public class XStreamParser<P, C> {
	
	private XStream xstream = new XStream();

	public XStreamParser() {
		xstream = new XStream();

		generalSetup();
	}

	private void generalSetup() {
		Class<?>[] classes = new Class[] {PrevisaoCidade.class, Previsao.class, 
				Cidade.class, ListaCidades.class};
		
		xstream.allowTypes(classes);
	}

	private void previsaoSetup() {
		xstream.alias("cidade", PrevisaoCidade.class);
		xstream.alias("previsao", Previsao.class);
		
		xstream.addImplicitCollection(PrevisaoCidade.class, "previsoes");
	}
	
	private void cidadesSetup() {
		xstream.alias("cidades", ListaCidades.class);
		xstream.alias("cidade", Cidade.class);

		xstream.addImplicitCollection(ListaCidades.class, "cidades");
	}

	public P previsao(String xml) {
		previsaoSetup();
		return (P) xstream.fromXML(xml);
	}
	
	public C cidades(String xml) {
		cidadesSetup();
		return (C) xstream.fromXML(xml);
	}
}
