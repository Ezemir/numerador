package br.gov.ce.cgd.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Chart {

    public static Map<String, List<?>> separarNomesEQuantidades(List<Object[]> dadosGraficos) {
        List<String> nomes = dadosGraficos.stream()
            .map(d -> (String) d[0])
            .collect(Collectors.toList());

        List<Long> quantidades = dadosGraficos.stream()
            .map(d -> ((Number) d[1]).longValue())
            .collect(Collectors.toList());

        Map<String, List<?>> resultado = new HashMap<>();
        resultado.put("nomes", nomes);
        resultado.put("quantidades", quantidades);

        return resultado;
    }
}
