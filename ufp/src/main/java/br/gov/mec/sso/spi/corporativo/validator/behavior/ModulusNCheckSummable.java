package br.gov.mec.sso.spi.corporativo.validator.behavior;

import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.ICheck;
import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.IReverse;
import br.gov.mec.sso.spi.corporativo.validator.behavior.functional.IWeight;

public interface ModulusNCheckSummable<T> {

    long calculateModulusNCheckSum(T entity, int validLenght, ICheck check, IWeight weight);

    /**
     * CNPJ = Módulo 11 (2 a 9)
     * CPF  = Módulo 11 (2 a N)
     * @param partial
     * @param modulus
     * @param check
     * @param weight
     * @return
     */
    static long calculateModulusNCheckSum(String partial, int modulus, ICheck check, IWeight weight) {
        if (check == null) {
            check = (long remainder) -> {
                return modulus - remainder;
            };
        }

        if (weight == null) {
            weight = (long x) -> {
                return (x + 2) % modulus;
            };
        }

        long reversed = IReverse.reverse(Long.valueOf(partial));
        String reversedString = ""+reversed;
        int size = reversedString.length();
        int[] reversedArray = new int[reversedString.length()];

        for (int i = 0; i < size; i++) {
            reversedArray[i] = reversedString.charAt(i) - '0';
        }

        // retornar apenas uma posição por vez
        IReverse val = (long x) -> {
            return reversedArray[(int)x];
        };

        long res = IWeight.summation(IWeight.weighting(val,weight),0,size);

        // System.out.println("\t\tvalid-length: "+modulus);
        // System.out.println("\t\tsize: "+size);
        // System.out.println("\t\tsum: "+res);
        // System.out.println("\t\tresult: "+(11 - (res % modulus)));

        return check.apply(res % modulus);
    }

}
