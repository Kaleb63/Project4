import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Kaleb Agbobli
 * @author Andrew Pavel
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    @Test
    public final void testConstructor() {
        Set<String> set = this.constructorTest();
        Set<String> setExpected = this.constructorRef();

        assertEquals(setExpected, set);
    }

    @Test
    public final void testAddToEmpty() {
        Set<String> set = this.createFromArgsTest();
        Set<String> setExpected = this.createFromArgsRef("m");

        set.add("m");

        assertEquals(setExpected, set);
    }

    @Test
    public final void testAddSmallerElement() {
        Set<String> set = this.createFromArgsTest("m");
        Set<String> setExpected = this.createFromArgsRef("m", "a");

        set.add("a");

        assertEquals(setExpected, set);
    }

    @Test
    public final void testAddLargerElement() {
        Set<String> set = this.createFromArgsTest("m");
        Set<String> setExpected = this.createFromArgsRef("m", "z");

        set.add("z");

        assertEquals(setExpected, set);
    }

    @Test
    public final void testAddDifferentOrders() {
        Set<String> first = this.createFromArgsTest("m", "a", "z");
        Set<String> second = this.createFromArgsTest("z", "a", "m");
        Set<String> setExpected = this.createFromArgsRef("a", "m", "z");

        assertEquals(setExpected, first);
        assertEquals(setExpected, second);
    }

    /*
     * Insertion order m, a, z. Removing a removes a leaf.
     */
    @Test
    public final void testRemoveLeaf() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("m", "z");

        String result = set.remove("a");

        assertEquals("a", result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testRemoveOnlyElement() {
        Set<String> set = this.createFromArgsTest("m");
        Set<String> setExpected = this.createFromArgsRef();

        String result = set.remove("m");

        assertEquals("m", result);
        assertEquals(setExpected, set);
    }

    /*
     * Insertion order m, b, a. Removing b removes a node with one child.
     */
    @Test
    public final void testRemoveNodeWithOneChild() {
        Set<String> set = this.createFromArgsTest("m", "b", "a");
        Set<String> setExpected = this.createFromArgsRef("m", "a");

        String result = set.remove("b");

        assertEquals("b", result);
        assertEquals(setExpected, set);
    }

    /*
     * Insertion order m, c, t, a, e. Removing c removes a node with two
     * children.
     */
    @Test
    public final void testRemoveNodeWithTwoChildren() {
        Set<String> set = this.createFromArgsTest("m", "c", "t", "a", "e");
        Set<String> setExpected = this.createFromArgsRef("m", "t", "a", "e");

        String result = set.remove("c");

        assertEquals("c", result);
        assertEquals(setExpected, set);
    }

    /*
     * Insertion order m, a, z. Removing m removes the root.
     */
    @Test
    public final void testRemoveRoot() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("a", "z");

        String result = set.remove("m");

        assertEquals("m", result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testRemoveAnySingleton() {
        Set<String> set = this.createFromArgsTest("m");
        Set<String> setExpected = this.createFromArgsRef();

        String result = set.removeAny();

        assertEquals("m", result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testRemoveAnyMultiple() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("m", "a", "z");

        String result = set.removeAny();
        assertEquals(true, setExpected.contains(result));
        setExpected.remove(result);

        assertEquals(setExpected, set);
    }

    @Test
    public final void testRemoveAnyRepeatedly() {
        Set<String> set = this.createFromArgsTest("m", "a", "z", "c", "t");
        Set<String> setExpected = this.createFromArgsRef("m", "a", "z", "c", "t");

        int remaining = setExpected.size();
        while (remaining > 0) {
            String result = set.removeAny();
            assertEquals(true, setExpected.contains(result));
            setExpected.remove(result);
            remaining--;
            assertEquals(remaining, set.size());
            assertEquals(setExpected, set);
        }
    }

    @Test
    public final void testContainsEmpty() {
        Set<String> set = this.createFromArgsTest();
        Set<String> setExpected = this.createFromArgsRef();

        boolean result = set.contains("m");

        assertEquals(false, result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testContainsPresent() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("m", "a", "z");

        assertEquals(true, set.contains("m"));
        assertEquals(true, set.contains("a"));
        assertEquals(true, set.contains("z"));
        assertEquals(setExpected, set);
    }

    @Test
    public final void testContainsAbsent() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("m", "a", "z");

        assertEquals(false, set.contains("0"));
        assertEquals(false, set.contains("b"));
        assertEquals(false, set.contains("zz"));
        assertEquals(setExpected, set);
    }

    @Test
    public final void testSizeEmpty() {
        Set<String> set = this.createFromArgsTest();
        Set<String> setExpected = this.createFromArgsRef();

        int result = set.size();

        assertEquals(0, result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testSizeSingleton() {
        Set<String> set = this.createFromArgsTest("m");
        Set<String> setExpected = this.createFromArgsRef("m");

        int result = set.size();

        assertEquals(1, result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testSizeMultiple() {
        Set<String> set = this.createFromArgsTest("m", "a", "z");
        Set<String> setExpected = this.createFromArgsRef("m", "a", "z");

        int result = set.size();

        assertEquals(3, result);
        assertEquals(setExpected, set);
    }

    @Test
    public final void testSizeBeforeAndAfterOperations() {
        Set<String> set = this.constructorTest();
        Set<String> setExpected = this.constructorRef();

        assertEquals(0, set.size());

        set.add("m");
        setExpected.add("m");
        assertEquals(1, set.size());
        assertEquals(setExpected, set);

        set.add("a");
        setExpected.add("a");
        set.add("z");
        setExpected.add("z");
        assertEquals(3, set.size());
        assertEquals(setExpected, set);

        String removed = set.remove("a");
        setExpected.remove("a");
        assertEquals("a", removed);
        assertEquals(2, set.size());
        assertEquals(setExpected, set);

        String any = set.removeAny();
        assertEquals(true, setExpected.contains(any));
        setExpected.remove(any);
        assertEquals(1, set.size());
        assertEquals(setExpected, set);
    }

}
