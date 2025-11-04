package data_structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EdgeNode {
    private GraphNode targetNode;
    private EdgeNode nextEdge;

    public EdgeNode(GraphNode targetNode) {
        this.targetNode = targetNode;
        this.nextEdge = null;
    }
}
