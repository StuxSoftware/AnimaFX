package net.stuxcrystal.jass.overrides;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains a list of override tags.<p />
 *
 * To be able to parse the list as fast as possible, this
 * list uses a tree approach to store each type in a tree.
 */
public class AssOverrideTagList {

    /**
     * Trepresents a node in the tree.
     */
    private static class Tag {

        /**
         * The char for the next override tags.
         */
        char c;

        /**
         * The parent tag.
         */
        Tag parent;

        /**
         * The name of the tag in the current state.
         */
        String name;

        /**
         * The type of the override tag.
         */
        AssOverrideTagType type = null;

        /**
         * Any tag with a name starting with this value.
         */
        Map<Character, Tag> sub = new HashMap<>();

    }

    /**
     * The root tag.
     */
    private Tag root = new Tag();

    /**
     * A list with all override tags.
     */
    public AssOverrideTagList() {}

    /**
     * Adds a new tag.
     * @param type The type to add
     */
    public void put(AssOverrideTagType type) {
        String name = type.getName();
        Tag current = this.root;

        for (int i = 0; i<name.length(); i++) {

            char c = name.charAt(i);
            Tag tag;

            if (!current.sub.containsKey(c)) {
                tag = new Tag();
                tag.parent = current;
                tag.name = name.substring(0, i);
                tag.c = c;
                current.sub.put(c, tag);
            } else {
                tag = current.sub.get(c);
            }

            current = tag;
        }

        current.type = type;
    }

    /**
     * Returns the type of an override tag using its name.
     * @param reader The reader to read the next name from.
     */
    public AssOverrideTagType get(PushbackReader reader) throws IOException {
        Tag current = this.root;

        // Traverse the tree as far as possible.
        while (true) {

            int cChar = reader.read();
            if (cChar == -1)
                break;

            Tag tag = current.sub.get((char) cChar);
            if (tag == null) {
                reader.unread((char) cChar);
                break;
            }

            current = tag;



        }

        // Traverse the node back until a override tag was found.
        while (current.type == null) {
            reader.unread(current.c);

            // If the node is unknown, ignore it.
            if (current.parent == null)
                break;

            current = current.parent;
        }

        return current.type;
    }

    /**
     * Returns all override tags in this list.
     * @return All override tags in this list.
     */
    public List<AssOverrideTagType> getAll() {
        List<AssOverrideTagType> types = new ArrayList<>();
        this.getAll(types, this.root);
        return types;
    }

    /**
     * Returns all override tags.
     * @param node    The current node to traverse.
     * @param result  The list to add the values to.
     */
    private void getAll(List<AssOverrideTagType> result, Tag node) {

        for (Tag value : node.sub.values()) {
            if (value.type != null)
                result.add(value.type);

            if (value.sub.size() > 0)
                this.getAll(result, value);
        }

    }
}
