package dictionary;

import java.io.*;
import java.util.ArrayList;


/** CompactPrefixTree class, implements Dictionary ADT and
 *  several additional methods. Can be used as a spell checker.
 *  Fill in code and feel free to add additional methods as needed.
 */
public class CompactPrefixTree implements Dictionary {
    private Node root; // the root of the tree
    private Node prevNode = new Node();
    /** Default constructor  */
    public CompactPrefixTree() {}
    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {
        // FILL IN CODE:
        // Read each word from the file, add it to the tree
        try {
            FileReader f = new FileReader(filename);
            BufferedReader br = new BufferedReader(f);
            String line;
            while ((line = br.readLine()) != null){
                add(line);
            }
        } catch (IOException e){
            System.out.println("No file found");
        }
    }

    /** Adds a given word to the dictionary.
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method
    }

    /**
     * Checks if a given word is in the dictionary
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {
        return check(word.toLowerCase(), root); // Calling a private check method
    }

    /**
     * Checks if a given prefix is stored in the dictionary
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling a private checkPrefix method
    }

    /**
     * Returns a human-readable string representation of the compact prefix tree;
     * contains nodes listed using pre-order traversal and uses indentations to show the level of the node.
     * An asterisk after the node means the node's boolean flag is set to true.
     * The root is at the current indentation level (followed by * if the node's valid bit is set to true),
     * then there are children of the node at a higher indentation level.
     *
     * @return returns the entire string in required format
     */
    public String toString() {
        if(root == null){
            return "";
        }
        return treeToString(root, 0, "");
    }

    /**
     * Print out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {
        // FILL IN CODE
        // Uses toString() method; outputs info to a file
        try (PrintWriter printWriter = new PrintWriter (filename)) {
            printWriter.write(toString());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file.");
        }
    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use ot the compact prefix tree.
     *
     * @param word The word to check
     * @param numSuggestions The length of the array to return.  Note that if the word is
     * in the dictionary, this parameter will be ignored, and the array will contain a
     * single world.
     * @return An array of the closest entries in the dictionary to the target word
     */
    public String[] suggest(String word, int numSuggestions){
        // FILL IN CODE
        // Note: you need to create a private suggest method in this class
        // (like we did for methods add, check, checkPrefix)
        ArrayList<String> arr = new ArrayList<>();
        if(check(word)){
            arr.add(word);
        }else{
            checkSuggest(word,"",  numSuggestions, arr, root);
        }
        return arr.toArray(new String[arr.size()]);
    }

    // ---------- Private helper methods ---------------
    /**
     * A recursive helper method for the toString method
     *
     * @param node is the current node which is passed as root
     * @param numIndentations is the number of indentations to add before printing prefix
     * @param str is the string which the entire tree is appended at
     *
     * @return returns the tree in required form
     */
    private String treeToString(Node node, int numIndentations, String str){
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if(node == null){
            return sb.toString();
        }
        for(int i = 0; i < numIndentations; i++){
            sb.append("  ");
        }
        sb.append(node.prefix);
        if(node.isWord){
            sb.append("*");
        }
        sb.append("\n");
        for(Node children : node.children){
            sb.append(treeToString(children, numIndentations + 1, str));
        }
        return sb.toString();
    }

    /**
     * A recursive helper method for the suggest method
     *
     * @param word original word to search suggestions for
     * @param curWord current word prefix
     * @param numSuggestions The length of the array to return.
     * @param arr current array with added suggestions
     * @param node current node
     *
     * @return returns an arraylist of suggestions
     */
    private void checkSuggest(String word, String curWord, int numSuggestions, ArrayList<String> arr, Node node){
        if (node == null || arr.size() == numSuggestions) {
            return;
        }
        String suffix = suffix(word, node.prefix);
        String finalWord = curWord + node.prefix;
        if(!suffix.isEmpty()){
            checkSuggest(suffix, finalWord, numSuggestions, arr, node.children[childIndex(suffix)]);
        }
        childInitial(finalWord, numSuggestions, arr, node, prevNode);
        System.out.println("prev node " +prevNode.prefix);
        prevNode = node;
    }

    /**
     * A recursive helper method for the checkSuggest method
     * Populates array with child suggestions, breaks when enough are found or childNode exhausted
     *
     * @param currentPrefix the prefix to find child for
     * @param numSuggestions The length of the array to return.
     * @param arr current array with added suggestions
     * @param childNode the current node to find the child of
     * @param prevNode the previous node to avoid traversing down that path again
     */
    private void childInitial(String currentPrefix, int numSuggestions, ArrayList<String> arr, Node childNode, Node prevNode) {
        if (childNode == null || arr.size() >= numSuggestions) {
            return;
        }
        if (childNode.isWord){
            arr.add(currentPrefix);
            System.out.println("added: " +currentPrefix);
        }
        for(int i = 0; i < childNode.children.length; i++) {
            if (childNode.children[i] != null) {
                if(childNode == prevNode){
                    continue;
                }
                childInitial(currentPrefix + childNode.children[i].prefix, numSuggestions, arr, childNode.children[i], prevNode);
            }
        }
    }

    /**
     *  A private add method that adds a given string to the tree
     * @param s the string to add
     * @param node the root of a tree where we want to add a new string
     *
     * @return a reference to the root of the tree that contains s
     */
    private Node add(String s, Node node) {
        // FILL IN CODE
        if(node == null){
            return new Node(s, true);
        }
        if(s.equals(node.prefix)){
            if(!node.isWord){
                node.isWord = true;
            }
            return node;
        }
        if(s.startsWith(node.prefix)){                                          //hamster starts with ham
            s = suffix(s, node.prefix);                                         //ster
            int childIndex = childIndex(s);
            node.children[childIndex] = add(s, node.children[childIndex]);
            return node;
        }
        String commonPrefix = prefix(s, node.prefix);                            //ham                          //ham
        String suffix = suffix(commonPrefix, node.prefix);                       //burger                       //burger
        String suffixWord = suffix(commonPrefix, s);                             //ster                         //empty
        Node newNode = new Node(commonPrefix, false);                       //ham NEW NODE                 //ham NEW NODE
        node.prefix = suffix;                                                    //hamburger ----> burger       //hamburger ----> burger
        newNode.children[childIndex(suffix)] = node;                             //newNode[child[b]] = burger   //ham[child[b]] = burger
        if(suffixWord.isEmpty()){
            newNode.isWord = true;                                               //HAM IS NOW TRUE
        }else{
            int index = childIndex(suffixWord);
            newNode.children[index] = add(suffixWord, newNode.children[index]);  //(ster, ham[child[s]])
        }
        return newNode;
    }

    /**
     * A private method to find suffix of two words
     *
     * @param original the first string
     * @param suffix the second string
     *
     * @return the suffix of two words
     */
    private String suffix(String original, String suffix){
        String suf;
        if(original.length() < suffix.length()){
            suf = suffix.substring(original.length());
        }else{
            suf = original.substring(suffix.length());
        }
        return suf;
    }

    /**
     * A private method to find prefix of two words
     *
     * @param first the first string
     * @param second the second string
     *
     * @return the prefix of two words
     */
    private String prefix(String first, String second){
        int i = 0;
        if(first == null || first.isEmpty() || second == null || second.isEmpty()){
            return "";
        }
        int len = Math.min(first.length(), second.length());
        while(i < len){
            if(first.charAt(i) != second.charAt(i)){
                break;
            }
            i++;
        }
        return first.substring(0, i);
    }


    /** A private method to check whether a given string is stored in the tree.
     *
     * @param s the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String s, Node node) {
        // FILL IN CODE
        if(node == null){
            return false;
        }
        if(s.equals(node.prefix) && !node.isWord){
            return false;
        }
        if(!s.startsWith(node.prefix)){
            return false;
        }
        if(s.equals(node.prefix) && node.isWord){
            return true;
        }
        s = suffix(s, node.prefix);
        return check(s, node.children[childIndex(s)]);
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node){
        // FILL IN CODE
        if(node == null){
            return false;
        }
        if(node.prefix.startsWith(prefix)){
            return true;
        }
        if(!prefix.startsWith(node.prefix)){
            return false;
        }
        prefix = suffix(prefix, node.prefix);
        return checkPrefix(prefix, node.children[childIndex(prefix)]);
    }

    /**
     * A private method to find position of first character of string in array
     *
     * @param s string to find child index of
     *
     * @return returns childIndex i.e. the position in array of first character
     */
    private int childIndex(String s) {
        return (int) s.charAt(0) - (int) 'a';
    }

    // You might want to create a private recursive helper method for toString
    // that takes the node and the number of indentations, and returns the tree  (printed with indentations) in a string.
    // private String toString(Node node, int numIndentations)
    // Add a private suggest method. Decide which parameters it should have

    // --------- Private class Node ------------
    // Represents a node in a compact prefix tree
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node(){
            isWord = false;
            prefix = "";
            children = new Node[26]; // initialize the array of children
        }
        // FILL IN CODE: Add other methods to class Node as needed
        Node(String str, boolean flag) {
            isWord = flag;
            prefix = str;
            children = new Node[26]; // initialize the array of children
        }
    }
}
