package structures;
import java.util.ArrayList;
import java.util.List;
import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key. This class provides a
 * foundational structure for creating and managing an associative
 * array, including methods for setting, getting, and checking the
 * presence of keys, along with other utility functionalities.
 *
 * @author Rommin Adl
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
    // +-----------+---------------------------------------------------
    // | Constants |
    // +-----------+

    /**
     * The default capacity of the initial array, used to initialize
     * the array size and manage its expansion.
     */
    static final int DEFAULT_CAPACITY = 16;

    // +--------+------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The size of the associative array (the number of key/value pairs),
     * tracking the actual count of stored elements.
     */
    int size;

    /**
   * The capacity of the associative array (space allocated for the array)
   */

    int capacity = DEFAULT_CAPACITY;


    /**
     * The array of key/value pairs, serving as the underlying data structure
     * for storing the associative array's contents.
     */
    KVPair<K, V>[] pairs;

    // +--------------+------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Create a new, empty associative array with an initial capacity.
     * This constructor initializes the array and sets the size to 0,
     * indicating an empty array.
     */
    @SuppressWarnings("unchecked")
    public AssociativeArray() {
        // Initialize the array to hold KVPair objects, casting is necessary due
        // to Java's type erasure with generics.
        this.pairs = (KVPair<K, V>[]) newInstance(KVPair.class, DEFAULT_CAPACITY);
        this.size = 0; // Initially, the associative array is empty.
    } // AssociativeArray()


  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a deep copy of this AssociativeArray. This method creates a new
   * AssociativeArray instance with the same key-value pairs as this instance.
   *
   * @return A new AssociativeArray instance that is a deep copy of this one.
   */
  @SuppressWarnings("unchecked")
  public AssociativeArray<K, V> clone() {
      AssociativeArray<K, V> clonedArray = new AssociativeArray<>();
      clonedArray.size = this.size; // Copy size
      clonedArray.capacity = this.capacity; // Copy capacity
      clonedArray.pairs = (KVPair<K, V>[]) java.lang.reflect.Array.newInstance(KVPair.class, this.capacity);
      for (int i = 0; i < this.size; i++) {
          clonedArray.pairs[i] = new KVPair<>(this.pairs[i].key, this.pairs[i].value); // Copy each KVPair
      }
      return clonedArray;
  }
  
  
  /**
   * Convert the associative array to a string representation.
   * The format of the string is "{ key1: value1, key2: value2, ... }".
   *
   * @return A string representation of the associative array.
   */
  public String toString() {
    if (this.size == 0) {
      return "{}"; // Return empty braces if the array is empty
    }
    StringBuilder builder = new StringBuilder("{ ");
    for (int i = 0; i < this.size; i++) {
      builder.append(this.pairs[i].key).append(": ").append(this.pairs[i].value);
      if (i < this.size - 1) {
        builder.append(", "); // Separate pairs with commas
      }
    }
    builder.append(" }");
    return builder.toString(); // Return the constructed string
  } // toString()



  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value. If the key already exists, update its value.
   */
  public void set(K key, V value) {
    if (key == null) throw new NullPointerException("Key cannot be null.");
    try {
        int index = find(key);
        pairs[index] = new KVPair<>(key, value); // Update existing value
    } catch (KeyNotFoundException e) {
        if (size == capacity) expand(); // Expand array if needed
        pairs[size++] = new KVPair<>(key, value); // Add new key-value pair
    }
}

  /**
   * Get the value associated with key.
   *
   * @throws KeyNotFoundException when the key is not found.
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) throw new IllegalArgumentException("Key cannot be null.");
    int index = find(key); // Find index of the key
    return pairs[index].value; // Return the value associated with the key
  }

  /**
   * Determine if key appears in the associative array. Should return false for the null key.
   */
  public boolean hasKey(K key) {
    if (key == null) return false;
    try {
      find(key); // Attempt to find the key
      return true; // Key found
    } catch (KeyNotFoundException e) {
      return false; // Key not found
    }
  } 
    /**
     * Retrieves a list of all keys in the associative array.
     * 
     * @return A list containing all keys in the associative array.
     */
    public List<K> keys() {
      List<K> keyList = new ArrayList<>();
      for (int i = 0; i < size; i++) {
          if (pairs[i] != null) { // Check to avoid null entries
              keyList.add(pairs[i].key);
          }
      }
      return keyList;
  }
  /**
   * Remove the key/value pair associated with a key. If the key does not appear,
   * does nothing. Future calls to get(key) for this key will throw an exception.
   */
  public void remove(K key) {
    if (key == null) return;
    try {
      int index = find(key); // Find index of the key
      System.arraycopy(pairs, index + 1, pairs, index, size - index - 1); // Shift elements
      pairs[--size] = null; // Decrease size and clear last element
    } catch (KeyNotFoundException e) {
      // Do nothing if key not found
    }
  }

  /**
   * Determine how many key/value pairs are in the associative array.
   */
  public int size() {
    return this.size; // Return the current size
  }

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array to accommodate more elements.
   */
  void expand() {
    capacity *= 2; // Double the capacity
    pairs = java.util.Arrays.copyOf(pairs, capacity); // Copy to new array with updated capacity
  }

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws KeyNotFoundException.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < size; i++) {
      if (pairs[i].key.equals(key)) return i; // Key found
    }
    throw new KeyNotFoundException(); // Key not found
  }
} // class AssociativeArray
