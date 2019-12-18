/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.tree;

import java.util.List;
import java.util.Map;

/**
 *
 * @author DEI-ESINF
 * @param <E>
 */

public interface BSTInterface<E> {

    boolean isEmpty();
    void insert(E element);
    void remove(E element);

    int size();
    int height();
    
    E smallestElement();
    Iterable<E> inOrder();
    Iterable<E> preOrder();
    Iterable<E> posOrder();
    Map<Integer,List<E>> nodesByLevel();
    
}
