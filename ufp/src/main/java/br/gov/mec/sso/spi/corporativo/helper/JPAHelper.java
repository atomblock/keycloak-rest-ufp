package br.gov.mec.sso.spi.corporativo.helper;

import javax.persistence.TypedQuery;
import java.util.List;

// /--------------------------------------------------------------------------------\
// | JPAHelper.java                                                                 |
// |--------------------------------------------------------------------------------|
// | Classe utilitária (Helper) para manipulação de operaoes que envolvam transações|
// | e consultas realizadas pela JPA.                                               |
// |                                                                                |
// \--------------------------------------------------------------------------------/

public class JPAHelper {

    /**
     * Formata um resultado JPQL para um único registro de uma determinada consulta.
     * @param query
     * @param <T>
     * @return T como um resultado único da consulta realizada.
     */
    public static <T> T getSingleResult(TypedQuery<T> query) {
        query.setMaxResults(1);
        List<T> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
