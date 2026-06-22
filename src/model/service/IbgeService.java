package model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IbgeService {

    // Lista fixa de UFs para agilizar a interface
    public static final String[] ESTADOS = {
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", 
        "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    };

    public static List<String> getCidades(String uf) throws Exception {
        String urlString = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + uf + "/municipios";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // O código IBGE para municípios tem exatamente 7 dígitos. Isso garante que vamos pegar apenas o nome da cidade.
        Pattern pattern = Pattern.compile("\"id\":\\d{7},\"nome\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response.toString());

        List<String> cidades = new ArrayList<>();
        while (matcher.find()) {
            cidades.add(matcher.group(1));
        }
        
        Collections.sort(cidades);
        return cidades;
    }
}