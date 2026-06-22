package view;

import java.util.HashMap;
import java.util.Map;

public class DicionarioClima {
    private static final Map<String, String> descricoes = new HashMap<>();
    private static final Map<String, String> iconesUrl = new HashMap<>();

    static {
        descricoes.put("ec", "Encoberto c/ Chuvas Isoladas"); descricoes.put("ci", "Chuvas Isoladas");
        descricoes.put("c", "Chuva"); descricoes.put("in", "Instável");
        descricoes.put("pp", "Poss. de Pancadas de Chuva"); descricoes.put("cm", "Chuva pela Manhã");
        descricoes.put("cv", "Chuvisco"); descricoes.put("pt", "Pancadas de Chuva a Tarde");
        descricoes.put("vn", "Variação de Nebulosidade"); descricoes.put("np", "Nublado e Pancadas de Chuva");
        descricoes.put("pc", "Pancadas de Chuva"); descricoes.put("pn", "Parcialmente Nublado");
        descricoes.put("ch", "Chuvoso"); descricoes.put("t", "Tempestade");
        descricoes.put("ps", "Predomínio de Sol"); descricoes.put("e", "Encoberto");
        descricoes.put("n", "Nublado"); descricoes.put("cl", "Céu Claro");
        descricoes.put("nv", "Nevoeiro"); descricoes.put("g", "Geada");
        descricoes.put("ne", "Neve"); descricoes.put("nd", "Não Definido");
        descricoes.put("pnt", "Pancadas de Chuva a Noite"); descricoes.put("psc", "Possibilidade de Chuva");
        descricoes.put("pcm", "Poss. de Chuva pela Manhã"); descricoes.put("pct", "Poss. de Chuva a Tarde");
        descricoes.put("pcn", "Poss. de Chuva a Noite"); descricoes.put("npt", "Nublado c/ Pancadas a Tarde");
        descricoes.put("npn", "Nublado c/ Pancadas a Noite"); descricoes.put("ncn", "Nublado c/ Poss. a Noite");
        descricoes.put("nct", "Nublado c/ Poss. a Tarde"); descricoes.put("ncm", "Nubl. c/ Poss. pela Manhã");
        descricoes.put("npm", "Nublado c/ Pancadas pela Manhã"); descricoes.put("npp", "Nublado c/ Possibilidade de Chuva");

        String sol = "https://openweathermap.org/img/wn/01d@2x.png";
        String solNuvens = "https://openweathermap.org/img/wn/02d@2x.png";
        String nublado = "https://openweathermap.org/img/wn/03d@2x.png";
        String chuva = "https://openweathermap.org/img/wn/10d@2x.png";
        String tempestade = "https://openweathermap.org/img/wn/11d@2x.png";
        String neve = "https://openweathermap.org/img/wn/13d@2x.png";
        String nevoeiro = "https://openweathermap.org/img/wn/50d@2x.png";

        iconesUrl.put("cl", sol); iconesUrl.put("ps", solNuvens);
        iconesUrl.put("pn", solNuvens); iconesUrl.put("vn", solNuvens);
        iconesUrl.put("n", nublado); iconesUrl.put("e", nublado); iconesUrl.put("nd", nevoeiro); iconesUrl.put("nv", nevoeiro);
        iconesUrl.put("c", chuva); iconesUrl.put("ch", chuva); iconesUrl.put("ci", chuva); iconesUrl.put("cv", chuva);
        iconesUrl.put("cm", chuva); iconesUrl.put("ct", chuva);
        iconesUrl.put("t", tempestade); iconesUrl.put("pt", tempestade); iconesUrl.put("pc", tempestade); iconesUrl.put("pp", tempestade);
        iconesUrl.put("in", tempestade); iconesUrl.put("np", tempestade); iconesUrl.put("npt", tempestade); iconesUrl.put("npn", tempestade);
        iconesUrl.put("pnt", tempestade);
        iconesUrl.put("psc", chuva); iconesUrl.put("pcm", chuva); iconesUrl.put("pct", chuva); iconesUrl.put("pcn", chuva);
        iconesUrl.put("g", neve); iconesUrl.put("ne", neve);
    }

    public static String getDescricao(String sigla) {
        return descricoes.getOrDefault(sigla.toLowerCase(), "Não Definido");
    }

    public static String getIconeUrl(String sigla) {
        return iconesUrl.getOrDefault(sigla.toLowerCase(), "https://openweathermap.org/img/wn/03d@2x.png");
    }
}