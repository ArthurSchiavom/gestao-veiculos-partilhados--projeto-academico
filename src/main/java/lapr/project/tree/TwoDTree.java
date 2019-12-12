package lapr.project.tree;

/**
 * Árvore 2D
 *
 * @param <A> Tipo do primeiro elemento a ser usado para ordenação
 * @param <B> Tipo do segundo elemento a ser usado para ordenação
 * @param <C> Tipo do elemento a ser guardado
 */
public class TwoDTree<
        A extends Comparable<A>
        , B extends Comparable<B>
        , C extends TreeDoubleValueElem<A, B> & Comparable<C>
        >
        extends BST<C> {

    @Override
    public void insert(C elemento) {
        if (root == null)
            root = new Node<>(elemento, null, null);

        insert(elemento, root, true);
    }

    /**
     * Inserir novo elemento na árvore.
     *
     * @param elemento elemento a insirir
     * @param node node a ser atualmente analisada
     * @param compararA se se deve comparar o primeiro elemento (segundo caso contrário)
     */
    public void insert(C elemento, Node<C> node, boolean compararA) {
        A elemA = elemento.getElemA();
        B elemB = elemento.getElemB();
        A nodeA = node.getElement().getElemA();
        B nodeB = node.getElement().getElemB();

        int compared;
        if (compararA)
            compared = elemA.compareTo(nodeA);
        else
            compared = elemB.compareTo(nodeB);

        if (compared > 0) {
            if (node.getRight() == null)
                node.setRight(new Node<>(elemento, null, null));
            else
                insert(elemento, node.getRight(), !compararA);
        } else if (compared < 0) {
            if (node.getLeft() == null)
                node.setLeft(new Node<>(elemento, null, null));
            else
                insert(elemento, node.getLeft(), !compararA);
        }
    }

    /**
     * Encontrar elemento atráves dos seus dois elementos de comparação.
     *
     * @param a Elemento A (primeiro)
     * @param b Elemento B (segundo)
     * @return elemento que possui os dois elementos de comparação fornecidos
     */
    public C find(A a, B b) {
        return find(a, b, root, true);
    }

    /**
     * Encontrar elemento atráves dos seus dois elementos de comparação.
     *
     * @param a Elemento A (primeiro)
     * @param b Elemento B (segundo)
     * @param node node a ser atualmente analisada
     * @param compararA se se deve comparar o primeiro elemento (segundo caso contrário)
     * @return elemento que possui os dois elementos de comparação fornecidos
     */
    public C find(A a, B b, Node<C> node, boolean compararA) {
        if (node == null)
            return null;

        C nodeElem = node.getElement();
        A nodeA = nodeElem.getElemA();
        B nodeB = nodeElem.getElemB();
        if (nodeA.compareTo(a) == 0 && nodeB.compareTo(b) == 0)
            return nodeElem;

        int comparado;
        if (compararA)
            comparado = a.compareTo(nodeA);
        else
            comparado = b.compareTo(nodeB);

        if (comparado > 0)
            return find(a, b, node.getRight(), !compararA);
        else
            return find(a, b, node.getLeft(), !compararA);
    }

    /**
     * Desnecessário para a resolução dos exercícios
     */
    @Override
    protected Node<C> find(C element, Node<C> node) {
        throw new UnsupportedOperationException(); // Implementação diferente
    }

    /**
     * Desnecessário para a resolução dos exercícios
     */
    @Override
    public C smallestElement() {
        throw new UnsupportedOperationException();
    }

    /**
     * Desnecessário para a resolução dos exercícios
     */
    @Override
    public void remove(C element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Desnecessário para a resolução dos exercícios
     */
    @Override
    protected C smallestElement(Node<C> node) {
        throw new UnsupportedOperationException();
    }

    /**
     * Desnecessário para a resolução dos exercícios
     */
    @Override
    public C find(C element) {
        throw new UnsupportedOperationException();
    }
}
