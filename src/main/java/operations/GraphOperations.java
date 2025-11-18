package operations;

import data_structures.EdgeNode;
import data_structures.GraphDescriptor;
import data_structures.GraphNode;

import java.util.ArrayList;
import java.util.List;

public class GraphOperations {

    private static void validateGraphNotNull(GraphDescriptor graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Граф не может быть null");
        }
    }

    private static void validateGraphNotEmpty(GraphDescriptor graph, String errorMessage) {
        if (graph == null || graph.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    private static GraphNode validateAndGetVertex(GraphDescriptor graph, char label) {
        GraphNode vertex = findVertexByLabel(graph, label);
        if (vertex == null) {
            throw new IllegalArgumentException("Вершина " + label + " не найдена");
        }
        return vertex;
    }


    private static EdgeNode buildEdgeList(List<GraphNode> targetNodes) {
        if (targetNodes == null || targetNodes.isEmpty()) {
            return null;
        }

        EdgeNode firstEdge = new EdgeNode(targetNodes.get(0));
        EdgeNode lastEdge = firstEdge;

        for (int i = 1; i < targetNodes.size(); i++) {
            EdgeNode newEdge = new EdgeNode(targetNodes.get(i));
            lastEdge.setNextEdge(newEdge);
            lastEdge = newEdge;
        }

        return firstEdge;
    }


    private static GraphNode findLastVertex(GraphDescriptor graph) {
        GraphNode current = graph.getFirstNode();
        while (current != null && current.getNextDataNode() != null) {
            current = current.getNextDataNode();
        }
        return current;
    }

    private static void removeEdgesToVertex(GraphDescriptor graph, GraphNode targetVertex) {
        GraphNode currentNode = graph.getFirstNode();

        while (currentNode != null) {
            removeEdgesFromNode(currentNode, targetVertex);
            currentNode = currentNode.getNextDataNode();
        }
    }

    private static void removeEdgesFromNode(GraphNode node, GraphNode targetVertex) {
        EdgeNode currentEdge = node.getFirstEdge();
        EdgeNode previousEdge = null;

        while (currentEdge != null) {
            if (currentEdge.getTargetNode() == targetVertex) {
                if (previousEdge == null) {
                    node.setFirstEdge(currentEdge.getNextEdge());
                    currentEdge = node.getFirstEdge();
                } else {
                    previousEdge.setNextEdge(currentEdge.getNextEdge());
                    currentEdge = currentEdge.getNextEdge();
                }
            } else {
                previousEdge = currentEdge;
                currentEdge = currentEdge.getNextEdge();
            }
        }
    }


    public static GraphDescriptor buildGraphFromMatrix(int[][] adjacencyMatrix) {
        if (adjacencyMatrix == null || adjacencyMatrix.length == 0) {
            throw new IllegalArgumentException("Матрица не может быть пустой");
        }

        int n = adjacencyMatrix.length;
        GraphNode[] nodes = new GraphNode[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new GraphNode((char) ('A' + i));
        }

        for (int i = 0; i < n - 1; i++) {
            nodes[i].setNextDataNode(nodes[i + 1]);
        }

        for (int i = 0; i < n; i++) {
            List<GraphNode> connectedNodes = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    connectedNodes.add(nodes[j]);
                }
            }

            nodes[i].setFirstEdge(buildEdgeList(connectedNodes));
        }

        GraphDescriptor graph = new GraphDescriptor();
        graph.setFirstNode(nodes[0]);
        graph.setVertexCount(n);

        return graph;
    }


    public static GraphNode findVertexByLabel(GraphDescriptor graph, char label) {
        if (graph == null || graph.isEmpty()) {
            return null;
        }

        GraphNode currentNode = graph.getFirstNode();
        while (currentNode != null) {
            if (currentNode.getData() == label) {
                return currentNode;
            }
            currentNode = currentNode.getNextDataNode();
        }

        return null;
    }


    public static void addEdge(GraphDescriptor graph, char sourceLabel, char targetLabel) {
        validateGraphNotEmpty(graph, "Граф пуст");

        GraphNode sourceVertex = validateAndGetVertex(graph, sourceLabel);
        GraphNode targetVertex = validateAndGetVertex(graph, targetLabel);


        if (findEdge(graph, sourceLabel, targetLabel)) {
            throw new IllegalArgumentException("Ребро " + sourceLabel + " -> " + targetLabel + " уже существует");
        }
        EdgeNode newEdge = new EdgeNode(targetVertex);

        newEdge.setNextEdge(sourceVertex.getFirstEdge());
        sourceVertex.setFirstEdge(newEdge);
    }


    public static void addVertex(GraphDescriptor graph, char vertexLabel, char[] connectedVertices) {
        validateGraphNotNull(graph);

        if (findVertexByLabel(graph, vertexLabel) != null) {
            throw new IllegalArgumentException("Вершина " + vertexLabel + " уже существует");
        }

        GraphNode newVertex = new GraphNode(vertexLabel);

        if (graph.isEmpty()) {
            graph.setFirstNode(newVertex);
            graph.setVertexCount(1);
            return;
        }

        GraphNode lastVertex = findLastVertex(graph);
        lastVertex.setNextDataNode(newVertex);

        if (connectedVertices != null && connectedVertices.length > 0) {
            List<GraphNode> targetNodes = new ArrayList<>();
            for (char label : connectedVertices) {
                GraphNode target = findVertexByLabel(graph, label);
                if (target != null) {
                    targetNodes.add(target);
                }
            }
            newVertex.setFirstEdge(buildEdgeList(targetNodes));
        }

        graph.incrementVertexCount();
    }


    public static void deleteVertex(GraphDescriptor graph, char vertexLabel) {
        validateGraphNotEmpty(graph, "Граф пуст");

        GraphNode vertexToDelete = findVertexByLabel(graph, vertexLabel);
        if (vertexToDelete == null) {
            throw new IllegalArgumentException("Вершина " + vertexLabel + " не найдена");
        }

        // Удаление из списка вершин
        GraphNode currentNode = graph.getFirstNode();
        if (currentNode == vertexToDelete) {
            graph.setFirstNode(currentNode.getNextDataNode());
        } else {
            while (currentNode != null && currentNode.getNextDataNode() != vertexToDelete) {
                currentNode = currentNode.getNextDataNode();
            }
            if (currentNode != null) {
                currentNode.setNextDataNode(vertexToDelete.getNextDataNode());
            }
        }

        removeEdgesToVertex(graph, vertexToDelete);

        graph.decrementVertexCount();
    }


    public static boolean findEdge(GraphDescriptor graph, char sourceVertex, char targetVertex) {
        if (graph == null || graph.isEmpty()) {
            return false;
        }

        GraphNode source = findVertexByLabel(graph, sourceVertex);
        if (source == null) {
            return false;
        }

        GraphNode target = findVertexByLabel(graph, targetVertex);
        if (target == null) {
            return false;
        }

        EdgeNode currentEdge = source.getFirstEdge();
        while (currentEdge != null) {
            if (currentEdge.getTargetNode() == target) {
                return true;
            }
            currentEdge = currentEdge.getNextEdge();
        }

        return false;
    }


    public static void printGraph(GraphDescriptor graph) {
        if (graph == null || graph.isEmpty()) {
            System.out.println("Граф пуст");
            return;
        }

        System.out.println("\nСТРУКТУРА ГРАФА");
        System.out.println("Вершин: " + graph.getVertexCount());

        GraphNode currentNode = graph.getFirstNode();
        while (currentNode != null) {
            System.out.print(currentNode.getData());

            EdgeNode currentEdge = currentNode.getFirstEdge();
            if (currentEdge == null) {
                System.out.println(" -> нет связей");
            } else {
                System.out.print(" -> ");

                while (currentEdge != null) {
                    System.out.print(currentEdge.getTargetNode().getData());
                    currentEdge = currentEdge.getNextEdge();
                    if (currentEdge != null) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }

            currentNode = currentNode.getNextDataNode();
        }
        System.out.println();
    }


    public static int countEdges(GraphDescriptor graph) {
        if (graph == null || graph.isEmpty()) {
            return 0;
        }

        int edgeCount = 0;
        GraphNode currentNode = graph.getFirstNode();

        while (currentNode != null) {
            EdgeNode currentEdge = currentNode.getFirstEdge();
            while (currentEdge != null) {
                edgeCount++;
                currentEdge = currentEdge.getNextEdge();
            }
            currentNode = currentNode.getNextDataNode();
        }

        return edgeCount;
    }


    public static void printGraphDetails(GraphDescriptor graph) {
        if (graph == null || graph.isEmpty()) {
            System.out.println("Граф пуст");
            return;
        }

        System.out.println("\nПОДРОБНАЯ ИНФОРМАЦИЯ О ГРАФЕ");
        System.out.println("Вершин: " + graph.getVertexCount());
        System.out.println("Рёбер: " + countEdges(graph));

        GraphNode currentNode = graph.getFirstNode();
        while (currentNode != null) {
            System.out.println("\nВершина: " + currentNode.getData());

            EdgeNode currentEdge = currentNode.getFirstEdge();
            if (currentEdge == null) {
                System.out.println("  Рёбра: нет");
            } else {
                System.out.println("  Рёбра:");
                while (currentEdge != null) {
                    System.out.println("    " + currentNode.getData() + " -> " +
                            currentEdge.getTargetNode().getData());
                    currentEdge = currentEdge.getNextEdge();
                }
            }

            currentNode = currentNode.getNextDataNode();
        }
        System.out.println();
    }
}
