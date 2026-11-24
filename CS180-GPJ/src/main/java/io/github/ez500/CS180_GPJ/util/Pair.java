package io.github.ez500.CS180_GPJ.util;

/**
 *
 * A generic storage type of two references.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public interface Pair<A, B> {
    A getFirst();
    B getSecond();
    void setFirst(A first);
    void setSecond(B second);
}
