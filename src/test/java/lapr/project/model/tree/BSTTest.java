package lapr.project.model.tree;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class BSTTest {
    private Integer[] arr = {20, 15, 10, 13, 8, 17, 40, 50, 30, 7};
    private int[] height = {0, 1, 2, 3, 3, 3, 3, 3, 3, 4};
    private Integer[] inorderT = {7, 8, 10, 13, 15, 17, 20, 30, 40, 50};
    private Integer[] preorderT = {20, 15, 10, 8, 7, 13, 17, 40, 30, 50};
    private Integer[] posorderT = {7, 8, 13, 10, 17, 15, 30, 50, 40, 20};

    private BST<Integer> instance;

    void setUp() {
        instance = new BST<>();
        for (int i : arr)
            instance.insert(i);
    }

    /**
     * Test of size method, of class BST.
     */
    @Test
    void testSize() {
        setUp();
        assertEquals(instance.size(), arr.length);
        BST<String> sInstance = new BST<>();
        assertEquals(sInstance.size(), 0);
        sInstance.insert("A");
        assertEquals(sInstance.size(), 1);
        sInstance.insert("B");
        assertEquals(sInstance.size(), 2);
        sInstance.insert("A");
        assertEquals(sInstance.size(), 2);
    }

    /**
     * Test of insert method, of class BST.
     */
    @Test
    void testInsert() {
        int[] arr = {20, 15, 10, 13, 8, 17, 40, 50, 30, 20, 15, 10};
        BST<Integer> instance = new BST<>();
        for (int i = 0; i < 9; i++) {            //new elements
            instance.insert(arr[i]);
            assertEquals(instance.size(), i + 1);
        }
        for (int i = 9; i < arr.length; i++) {    //duplicated elements => same size
            instance.insert(arr[i]);
            assertEquals(instance.size(), 9);
        }
    }

    /**
     * Test of remove method, of class BST.
     */
    @Test
    void testRemove() {
        setUp();

        int qtd = arr.length;
        instance.remove(999);

        assertEquals(instance.size(), qtd);
        for (Integer integer : arr) {
            instance.remove(integer);
            qtd--;
            assertEquals(qtd, instance.size());
        }

        instance.remove(999);
        assertEquals(0, instance.size());
    }

    /**
     * Test of isEmpty method, of class BST.
     */
    @Test
    void testIsEmpty() {
        setUp();

        assertFalse(instance.isEmpty());
        instance = new BST<>();
        assertTrue(instance.isEmpty());

        instance.insert(11);
        assertFalse(instance.isEmpty());

        instance.remove(11);
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of height method, of class BST.
     */
    @Test
    void testHeight() {
        instance = new BST<>();
        assertEquals(instance.height(), -1);
        for (int idx = 0; idx < arr.length; idx++) {
            instance.insert(arr[idx]);
            assertEquals(instance.height(), height[idx]);
        }
        instance = new BST<>();
        assertEquals(instance.height(), -1);
    }

    /**
     * Test of smallestelement method, of class TREE.
     */
    @Test
    void testSmallestElement() {
        setUp();
        assertEquals(new Integer(7), instance.smallestElement());
        instance.remove(7);
        assertEquals(new Integer(8), instance.smallestElement());
        instance.remove(8);
        assertEquals(new Integer(10), instance.smallestElement());
    }

    /**
     * Test of processBstByLevel method, of class TREE.
     */
    @Test
    void testProcessBstByLevel() {
        setUp();
        Map<Integer, List<Integer>> expResult = new HashMap<>();
        expResult.put(0, Collections.singletonList(20));
        expResult.put(1, Arrays.asList(15, 40));
        expResult.put(2, Arrays.asList(10, 17, 30, 50));
        expResult.put(3, Arrays.asList(8, 13));
        expResult.put(4, Collections.singletonList(7));

        Map<Integer, List<Integer>> result = instance.nodesByLevel();

        for (int i = 0; i < result.entrySet().size(); i++)
            expResult.put(1, Arrays.asList(15,40));
            expResult.put(2, Arrays.asList(10,17,30,50));
            expResult.put(3, Arrays.asList(8,13));
            expResult.put(4, Collections.singletonList(7));
        
        for(Map.Entry<Integer,List<Integer>> e : result.entrySet())
            assertEquals(expResult.get(e.getKey()), e.getValue());
    }


    /**
     * Test of inOrder method, of class BST.
     */
    @Test
    void testInOrder() {
        setUp();
        List<Integer> lExpected = Arrays.asList(inorderT);
        assertEquals(lExpected, instance.inOrder());
    }

    /**
     * Test of preOrder method, of class BST.
     */
    @Test
    void testPreOrder() {
        setUp();
        List<Integer> lExpected = Arrays.asList(preorderT);
        assertEquals(lExpected, instance.preOrder());
    }

    /**
     * Test of posOrder method, of class BST.
     */
    @Test
    void testPosOrder() {
        setUp();
        List<Integer> lExpected = Arrays.asList(posorderT);
        assertEquals(lExpected, instance.posOrder());
    }
}
