package gms.KdTree;

import gms.GraphML.InfoNode;
import lgds.trajectories.Point;

import java.util.*;
import java.util.Comparator;

import static gms.KdTree.Comparator.*;

/**
 * A k-d tree (short for k-dimensional tree) is a space-partitioning data
 * structure for organizing points in a k-dimensional space. k-d trees are a
 * useful data structure for several applications, such as searches involving a
 * multidimensional search key (e.g. range searches and nearest neighbor
 * searches). k-d trees are a special case of binary space partitioning trees.
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/K-d_tree">K-D Tree (Wikipedia)</a>
 * <br>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class KdTree<T extends KdTree.XYZPoint> implements Iterable<T> {

    private int k = 2;
    private KdNode root = null;

    protected static final int X_AXIS = 0;
    protected static final int Y_AXIS = 1;

    /**
     * Default constructor.
     */
    public KdTree() { }

    /**
     * Constructor for creating a more balanced tree. It uses the
     * "median of points" algorithm.
     *
     * @param list of XYZPoints.
     */
    public KdTree(List<XYZPoint> list) {
        super();
        root = createNode(list, k, 0);
    }

    /**
     * Constructor for creating a more balanced tree. It uses the
     * "median of points" algorithm.
     *
     * @param list of XYZPoints.
     * @param k of the tree.
     */
    public KdTree(List<XYZPoint> list, int k) {
        super();
        root = createNode(list, k, 0);
    }

    /**
     * Creates node from list of XYZPoints.
     *
     * @param list of XYZPoints.
     * @param k  of the tree.
     * @param depth  depth of the node.
     * @return node created.
     */
    private static KdNode createNode(List<XYZPoint> list, int k, int depth) {
        if (list == null || list.size() == 0)
            return null;

        int axis = depth % k;
        if (axis == X_AXIS)
            list.sort(X_COMPARATOR);
        else
            list.sort(Y_COMPARATOR);


        KdNode node = null;
        List<XYZPoint> less = new ArrayList<XYZPoint>(list.size());
        List<XYZPoint> more = new ArrayList<XYZPoint>(list.size());
        if (list.size() > 0) {
            int medianIndex = list.size() / 2;
            node = new KdNode(list.get(medianIndex), k, depth);
            // Process list to see where each non-median point lies
            for (int i = 0; i < list.size(); i++) {
                if (i == medianIndex)
                    continue;
                XYZPoint p = list.get(i);
                // Cannot assume points before the median are less since they could be equal
                if (KdNode.compareTo(depth, k, p, node.id) <= 0) {
                    less.add(p);
                } else {
                    more.add(p);
                }
            }

            if ((medianIndex-1 >= 0) && less.size() > 0) {
                node.lesser = createNode(less, k, depth + 1);
                node.lesser.parent = node;
            }

            if ((medianIndex <= list.size()-1) && more.size() > 0) {
                node.greater = createNode(more, k, depth + 1);
                node.greater.parent = node;
            }
        }

        return node;
    }

    /**
     * Adds value to the tree. Tree can contain multiple equal values.
     *
     * @param value T to add to the tree.
     * @return True if successfully added to tree.
     */
    public boolean add(T value) {
        if (value == null)
            return false;

        if (root == null) {
            root = new KdNode(value);
            return true;
        }

        KdNode node = root;
        while (true) {
            if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                if (node.lesser == null) {
                    KdNode newNode = new KdNode(value, k, node.depth + 1);
                    newNode.parent = node;
                    node.lesser = newNode;
                    break;
                }
                node = node.lesser;
            } else {
                // Greater
                if (node.greater == null) {
                    KdNode newNode = new KdNode(value, k, node.depth + 1);
                    newNode.parent = node;
                    node.greater = newNode;
                    break;
                }
                node = node.greater;
            }
        }

        return true;
    }

    /**
     * Does the tree contain the value.
     *
     * @param value T to locate in the tree.
     * @return True if tree contains value.
     */
    public boolean contains(T value) {
        if (value == null || root == null)
            return false;

        KdNode node = getNode(this, value);
        return (node != null);
    }

    /**
     * Locates T in the tree.
     *
     * @param tree to search.
     * @param value to search for.
     * @return KdNode or NULL if not found
     */
    private static final <T extends KdTree.XYZPoint> KdNode getNode(KdTree<T> tree, T value) {
        if (tree == null || tree.root == null || value == null)
            return null;

        KdNode node = tree.root;
        while (true) {
            if (node.id.equals(value)) {
                return node;
            } else if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                if (node.lesser == null) {
                    return null;
                }
                node = node.lesser;
            } else {
                // Greater
                if (node.greater == null) {
                    return null;
                }
                node = node.greater;
            }
        }
    }

    /**
     * Removes first occurrence of value in the tree.
     *
     * @param value T to remove from the tree.
     * @return True if value was removed from the tree.
     */
    public boolean remove(T value) {
        if (value == null || root == null)
            return false;

        KdNode node = getNode(this, value);
        if (node == null)
            return false;

        KdNode parent = node.parent;
        if (parent != null) {
            if (parent.lesser != null && node.equals(parent.lesser)) {
                List<XYZPoint> nodes = getTree(node);
                if (nodes.size() > 0) {
                    parent.lesser = createNode(nodes, node.k, node.depth);
                    if (parent.lesser != null) {
                        parent.lesser.parent = parent;
                    }
                } else {
                    parent.lesser = null;
                }
            } else {
                List<XYZPoint> nodes = getTree(node);
                if (nodes.size() > 0) {
                    parent.greater = createNode(nodes, node.k, node.depth);
                    if (parent.greater != null) {
                        parent.greater.parent = parent;
                    }
                } else {
                    parent.greater = null;
                }
            }
        } else {
            // root
            List<XYZPoint> nodes = getTree(node);
            if (nodes.size() > 0)
                root = createNode(nodes, node.k, node.depth);
            else
                root = null;
        }

        return true;
    }

    /**
     * Gets the (sub) tree rooted at root.
     *
     * @param root of tree to get nodes for.
     * @return points in (sub) tree, not including root.
     */
    private static final List<XYZPoint> getTree(KdNode root) {
        List<XYZPoint> list = new ArrayList<XYZPoint>();
        if (root == null)
            return list;

        if (root.lesser != null) {
            list.add(root.lesser.id);
            list.addAll(getTree(root.lesser));
        }
        if (root.greater != null) {
            list.add(root.greater.id);
            list.addAll(getTree(root.greater));
        }

        return list;
    }

    /** 
     * Searches the K nearest neighbor.
     *
     * @param K  Number of neighbors to retrieve. Can return more than K, if
     *            last nodes are equal distances.
     * @param value to find neighbors of.
     * @return Collection of T neighbors.
     */
    @SuppressWarnings("unchecked")
    public Collection<T> nearestNeighbourSearch(int K, T value) {
        if (value == null || root == null)
            return Collections.EMPTY_LIST;

        // Map used for results
        TreeSet<KdNode> results = new TreeSet<KdNode>(new EuclideanComparator(value));

        // Find the closest leaf node
        KdNode prev = null;
        KdNode node = root;
        while (node != null) {
            if (KdNode.compareTo(node.depth, node.k, value, node.id) <= 0) {
                // Lesser
                prev = node;
                node = node.lesser;
            } else {
                // Greater
                prev = node;
                node = node.greater;
            }
        }
        KdNode leaf = prev;

        if (leaf != null) {
            // Used to not re-examine nodes
            Set<KdNode> examined = new HashSet<KdNode>();

            // Go up the tree, looking for better solutions
            node = leaf;
            while (node != null) {
                // Search node
                searchNode(value, node, K, results, examined);
                node = node.parent;
            }
        }

        // Load up the collection of the results
        Collection<T> collection = new ArrayList<T>(K);
        for (KdNode kdNode : results)
            collection.add((T) kdNode.id);
        return collection;
    }

    private static final <T extends KdTree.XYZPoint> void searchNode(T value, KdNode node, int K, TreeSet<KdNode> results, Set<KdNode> examined) {
        examined.add(node);

        // Search node
        KdNode lastNode = null;
        Double lastDistance = Double.MAX_VALUE;
        if (results.size() > 0) {
            lastNode = results.last();
            lastDistance = lastNode.id.euclideanDistance(value);
        }
        Double nodeDistance = node.id.euclideanDistance(value);
        if (nodeDistance.compareTo(lastDistance) < 0) {
            if (results.size() == K && lastNode != null)
                results.remove(lastNode);
            results.add(node);
        } else if (nodeDistance.equals(lastDistance)) {
            results.add(node);
        } else if (results.size() < K) {
            results.add(node);
        }
        lastNode = results.last();
        lastDistance = lastNode.id.euclideanDistance(value);

        int axis = node.depth % node.k;
        KdNode lesser = node.lesser;
        KdNode greater = node.greater;

        // Search children branches, if axis aligned distance is less than
        // current distance
        if (lesser != null && !examined.contains(lesser)) {
            examined.add(lesser);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (axis == X_AXIS) {
                nodePoint = node.id.getX();
                valuePlusDistance = value.getX() - lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.id.getY();
                valuePlusDistance = value.getY() - lastDistance;
            }
            boolean lineIntersectsCube = (valuePlusDistance <= nodePoint);

            // Continue down lesser branch
            if (lineIntersectsCube)
                searchNode(value, lesser, K, results, examined);
        }
        if (greater != null && !examined.contains(greater)) {
            examined.add(greater);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (axis == X_AXIS) {
                nodePoint = node.id.getX();
                valuePlusDistance = value.getX() + lastDistance;
            } else if (axis == Y_AXIS) {
                nodePoint = node.id.getY();
                valuePlusDistance = value.getY() + lastDistance;
            }
            boolean lineIntersectsCube = (valuePlusDistance >= nodePoint);

            // Continue down greater branch
            if (lineIntersectsCube)
                searchNode(value, greater, K, results, examined);
        }
    }



    /** 
     * Adds, in a specified queue, a given node and its related nodes (lesser, greater).
     * 
     * @param node  Node to check. May be null.
     * 
     * @param results Queue containing all found entries. Must not be null.
     */
    @SuppressWarnings("unchecked")
    private static <T extends XYZPoint> void search(final KdNode node, final Deque<T> results) {
        if (node != null) {
            results.add((T) node.id);
            search(node.greater, results);
            search(node.lesser, results);
        }
    }










    /**
     * Euclidean comparator between two nodes
     */
    protected static class EuclideanComparator implements Comparator<KdNode> {

        private final XYZPoint point;

        public EuclideanComparator(XYZPoint point) {
            this.point = point;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(KdNode o1, KdNode o2) {
            Double d1 = point.euclideanDistance(o1.id);
            Double d2 = point.euclideanDistance(o2.id);
            if (d1.compareTo(d2) < 0)
                return -1;
            else if (d2.compareTo(d1) < 0)
                return 1;
            return o1.id.compareTo(o2.id);
        }
    }

    /** 
     * Searches all entries from the first to the last entry.
     * 
     * @return Iterator allowing to iterate through a collection containing all found entries.
     */
    public Iterator<T> iterator() {
        final Deque<T> results = new ArrayDeque<T>();
        search(root, results);
        return results.iterator();
    }

    /** 
     * Searches all entries from the last to the first entry.
     * 
     * @return Iterator allowing to iterate through a collection containing all found entries.
     */
    public Iterator<T> reverse_iterator() {
        final Deque<T> results = new ArrayDeque<T>();
        search(root, results);
        return results.descendingIterator();
    }












    /**
     *  Class implementing the node for the kd tree
     */
    public static class KdNode implements Comparable<KdNode> {
        private final XYZPoint id;
        private final int k;
        private final int depth;

        private KdNode parent = null;
        private KdNode lesser = null;
        private KdNode greater = null;

        /**
         * Constructor one parameter
         * @param id {@link XYZPoint} point
         */
        public KdNode(XYZPoint id) {
            this.id = id;
            this.k = 2;
            this.depth = 0;
        }

        /**
         * Constructor three parameters
         * @param id {@link XYZPoint} point
         * @param k int k
         * @param depth int depth
         */
        public KdNode(XYZPoint id, int k, int depth) {
            this.id = id;
            this.k = k;
            this.depth = depth;
        }

        /**
         * Compare method
         * @param depth depth
         * @param k k
         * @param o1 {@link XYZPoint} point
         * @param o2 {@link XYZPoint} point
         * @return integer value
         */
        public static int compareTo(int depth, int k, XYZPoint o1, XYZPoint o2) {
            int axis = depth % k;
            if (axis == X_AXIS)
                return X_COMPARATOR.compare(o1, o2);
            else
                return Y_COMPARATOR.compare(o1, o2);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return 31 * (this.k + this.depth + this.id.hashCode());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof KdNode))
                return false;

            KdNode kdNode = (KdNode) obj;
            return this.compareTo(kdNode) == 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(KdNode o) {
            return compareTo(depth, k, this.id, o.id);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("k=").append(k);
            builder.append(" depth=").append(depth);
            builder.append(" id=").append(id.toString());
            return builder.toString();
        }
    }













    /**
     * Class that implements the point for the KdTree
     *
     * Extend the normal {@link gms.GraphML.InfoNode}
     */
    public static class XYZPoint implements Comparable<XYZPoint> {
        private InfoNode node;

        /**
         * Constructor with one parameter
         *
         * @param x double value
         * @param y double value
         */
        public XYZPoint(Double x, Double y) {
            this.node = new InfoNode(new Point(y,x));
            //point has y latitude and x longitude
        }


        /**
         * Constructor with one parameter
         * @param info {@link InfoNode} node
         */
        public XYZPoint(InfoNode info){
            this.node = info;
        }


        /**
         * Getter for x
         * @return double value
         */
        public Double getX() {
            return this.node.getLon();
        }

        /**
         * Getter for y
         * @return double value
         */
        public Double getY() {
            return this.node.getLat();
        }

        /**
         * Computes the Euclidean distance from this point to the other.
         *
         * @param o1 other point.
         * @return euclidean distance.
         */
        public double euclideanDistance(XYZPoint o1) {
            return this.node.euclideanDistance(o1.node);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof XYZPoint))
                return false;

            XYZPoint xyzPoint = (XYZPoint) obj;
            if (Double.compare(this.getX(), xyzPoint.getX())!=0)
                return false;
            if (Double.compare(this.getY(), xyzPoint.getY())!=0)
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(XYZPoint o) {
            int xComp = X_COMPARATOR.compare(this, o);
            if (xComp != 0)
                return xComp;
            return Y_COMPARATOR.compare(this, o);
        }


        /**
         * Get the original node
         * @return {@link InfoNode} node
         */
        public InfoNode getNode() {
            return this.node;
        }
    }

}
