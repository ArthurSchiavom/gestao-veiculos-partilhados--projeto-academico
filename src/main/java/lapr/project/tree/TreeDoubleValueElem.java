package lapr.project.tree;

/**
 * Representa um valor de uma 2D-Tree.
 *
 * @param <A> Tipo 1 de valor
 * @param <B> Tipo 2 de valor
 */
public interface TreeDoubleValueElem<A extends Comparable<A>, B extends Comparable<B>> {
    A getElemA();
    B getElemB();
}
