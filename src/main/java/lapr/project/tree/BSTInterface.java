/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.tree;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DEI-ESINF
 * @param <E>
 */

public interface BSTInterface<E> {

    public boolean isEmpty();
    public void insert(E element);
    public void remove(E element);

    public int size();
    public int height();
    
    public E smallestElement();
    public Iterable<E> inOrder();
    public Iterable<E> preOrder();
    public Iterable<E> posOrder();
    public Map<Integer,List<E>> nodesByLevel();
    
}
