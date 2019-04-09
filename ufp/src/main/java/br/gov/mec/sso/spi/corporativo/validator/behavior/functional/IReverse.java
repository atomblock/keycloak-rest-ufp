package br.gov.mec.sso.spi.corporativo.validator.behavior.functional;

@FunctionalInterface
public interface IReverse {

    long apply(long n);

    /**
     * Reverte um determinado número.
     * @param n
     * @return long
     */
    static long reverse(long n) {
        long num = n;
        long result = 0;

        while(num != 0) {
            result = result * 10;
            result = result + num%10;
            num = num/10;
        }

        return result;
    }
}
