public class QuadTreeNode {
    Location value;

    //children
    QuadTreeNode topLeft;
    QuadTreeNode topRight;
    QuadTreeNode bottomLeft;
    QuadTreeNode bottomRight;

    //bounding box
    float topLeftx;
    float topLefty;
    float bottomRightx;
    float bottomRighty;

    public QuadTreeNode(float topLeftx, float topLefty,float bottomRightx,float bottomRighty){
        this.topLeftx=topLeftx;
        this.topLefty=topLefty;
        this.bottomRightx=bottomRightx;
        this.bottomRighty=bottomRighty;
    }

    public QuadTreeNode(Location value, float topLeftx, float topLefty,float bottomRightx,float bottomRighty){
        this(topLeftx, topLefty, bottomRightx, bottomRighty);
        this.value=value;
    }
}
