package data_structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphNode {
    private char data;
    private GraphNode nextDataNode;
    private EdgeNode firstEdge;

    public GraphNode(char data) {
        this.data = data;
        this.nextDataNode = null;
        this.firstEdge = null;
    }
}
