package pt.up.fe.specs.binarytranslation.tracer;

public class TraceUnitGraph {

    // TODO: other stuff?
    private TraceGraphNode head;
    private TraceGraphNode tail;

    // TODO hashmap of jump targets and originating trace units, for faster edge insertion

    public TraceUnitGraph(TraceGraphNode head) {
        this.head = head;
        this.tail = head;
    }

    public TraceGraphNode getHead() {
        return head;
    }

    public TraceGraphNode getTail() {
        return tail;
    }

    /*
     * Insert a new unit 
     */
    public void insert(TraceGraphNode newNode) {

        // if newnode has no direct parents, connect to head
        if (this.insert(this.head, newNode) == 0)
            head.addChild(newNode);

        this.tail = newNode;
    }

    /*
     * Insert a new unit 
     */
    private int insert(TraceGraphNode parent, TraceGraphNode newNode) {

        if (parent == newNode)
            return 1;

        int insertCount = 0;

        // insert newNode as child of candidate parent, if parent jumps to or precedes newNode
        if (parent.getUnit().jumpsTo(newNode.getUnit())
                || parent.getUnit().precedes(newNode.getUnit())
                || newNode.getUnit().includesTarget(parent.getUnit())) {
            parent.addChild(newNode);
            insertCount++;
        }

        for (var child : parent.getChildren()) {
            insertCount += this.insert(child, newNode);
        }

        return insertCount;
    }
}
