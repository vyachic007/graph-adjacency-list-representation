package data_structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphDescriptor {
    private GraphNode firstNode;
    private int vertexCount;

    public GraphDescriptor() {
        this.firstNode = null;
        this.vertexCount = 0;
    }

    public void incrementVertexCount() {
        this.vertexCount++;
    }

    public void decrementVertexCount() {
        if (this.vertexCount > 0) {
            this.vertexCount--;
        }
    }

    public boolean isEmpty() {
        return this.firstNode == null || this.vertexCount == 0;
    }
}
