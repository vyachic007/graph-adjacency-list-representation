package operations;

import data_structures.EdgeNode;
import data_structures.GraphDescriptor;
import data_structures.GraphNode;

public class GraphOperations {

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
            EdgeNode firstEdge = null;
            EdgeNode lastEdge = null;

            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    EdgeNode newEdge = new EdgeNode(nodes[j]);

                    if (firstEdge == null) {
                        firstEdge = newEdge;
                        lastEdge = newEdge;
                    } else {
                        lastEdge.setNextEdge(newEdge);
                        lastEdge = newEdge;
                    }
                }
            }

            nodes[i].setFirstEdge(firstEdge);
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

    public static void addVertex(GraphDescriptor graph, char vertexLabel, char[] connectedVertices) {
        if (graph == null) {
            throw new IllegalArgumentException("Граф не может быть null");
        }

        if (findVertexByLabel(graph, vertexLabel) != null) {
            throw new IllegalArgumentException("Вершина " + vertexLabel + " уже существует");
        }

        GraphNode newVertex = new GraphNode(vertexLabel);

        if (graph.isEmpty()) {
            graph.setFirstNode(newVertex);
            graph.setVertexCount(1);
            return;
        }

        GraphNode currentNode = graph.getFirstNode();
        while (currentNode.getNextDataNode() != null) {
            currentNode = currentNode.getNextDataNode();
        }

        currentNode.setNextDataNode(newVertex);

        if (connectedVertices != null && connectedVertices.length > 0) {
            EdgeNode firstEdge = null;
            EdgeNode lastEdge = null;

            for (char targetLabel : connectedVertices) {
                GraphNode targetVertex = findVertexByLabel(graph, targetLabel);

                if (targetVertex != null) {
                    EdgeNode newEdge = new EdgeNode(targetVertex);

                    if (firstEdge == null) {
                        firstEdge = newEdge;
                        lastEdge = newEdge;
                    } else {
                        lastEdge.setNextEdge(newEdge);
                        lastEdge = newEdge;
                    }
                }
            }

            newVertex.setFirstEdge(firstEdge);
        }

        graph.incrementVertexCount();
    }



    public static void deleteVertex(GraphDescriptor graph, char vertexLabel) {
        if (graph == null || graph.isEmpty()) {
            throw new IllegalArgumentException("Граф пуст");
        }

        GraphNode vertexToDelete = findVertexByLabel(graph, vertexLabel);
        if (vertexToDelete == null) {
            throw new IllegalArgumentException("Вершина " + vertexLabel + " не найдена");
        }

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

        currentNode = graph.getFirstNode();
        while (currentNode != null) {
            EdgeNode currentEdge = currentNode.getFirstEdge();
            EdgeNode previousEdge = null;

            while (currentEdge != null) {
                if (currentEdge.getTargetNode() == vertexToDelete) {
                    if (previousEdge == null) {
                        currentNode.setFirstEdge(currentEdge.getNextEdge());
                        currentEdge = currentNode.getFirstEdge();
                    } else {
                        previousEdge.setNextEdge(currentEdge.getNextEdge());
                        currentEdge = currentEdge.getNextEdge();
                    }
                } else {
                    previousEdge = currentEdge;    //оставляем ребро, если не указ на уд-ю вершину
                    currentEdge = currentEdge.getNextEdge();
                }
            }

            currentNode = currentNode.getNextDataNode();
        }

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
