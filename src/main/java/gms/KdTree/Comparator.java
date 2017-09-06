package gms.KdTree;

/**
 * Created by Alessandro Zonta on 01/09/2017.
 * PhD Situational Analytics
 * <p>
 * Computational Intelligence Group
 * Computer Science Department
 * Faculty of Sciences - VU University Amsterdam
 * <p>
 * a.zonta@vu.nl
 */
public class Comparator {

    public static final java.util.Comparator<KdTree.XYZPoint> X_COMPARATOR = new java.util.Comparator<KdTree.XYZPoint>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(KdTree.XYZPoint o1, KdTree.XYZPoint o2) {
            if (o1.getX() < o2.getX())
                return -1;
            if (o1.getX() > o2.getX())
                return 1;
            return 0;
        }
    };

    public static final java.util.Comparator<KdTree.XYZPoint> Y_COMPARATOR = new java.util.Comparator<KdTree.XYZPoint>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(KdTree.XYZPoint o1, KdTree.XYZPoint o2) {
            if (o1.getY() < o2.getY())
                return -1;
            if (o1.getY() > o2.getY())
                return 1;
            return 0;
        }
    };
}
