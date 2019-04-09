package br.gov.mec.sso.spi.corporativo.validator.behavior.functional;

@FunctionalInterface
public interface IWeight {

    long apply(long x);

    static IWeight weighting(IReverse value, IWeight weight) {
        return (long x) -> {
            return value.apply(x) * weight.apply(x);
        };
    }

    static long summation(IWeight weight, int from, int until) {
        long res = 0;

        if (until == 0) until = 1000;

        for (int i = from; i < until; i++) {
            res += weight.apply(i);
        }

        return res;
    }
}
