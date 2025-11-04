import data_structures.GraphDescriptor;
import io.FileManager;
import operations.GraphOperations;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class GraphApplication {

    private static final String DEFAULT_FILE = "/Users/vyacheslavborisov/IdeaProjects/LB3/data/graph_matrix.txt";
    private static GraphDescriptor graph;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        try {
            loadGraphFromFile();
            mainMenu();
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void loadGraphFromFile() throws FileNotFoundException {
        int[][] matrix = FileManager.loadAdjacencyMatrix(DEFAULT_FILE);
        graph = GraphOperations.buildGraphFromMatrix(matrix);
        GraphOperations.printGraph(graph);
    }

    private static void mainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n1. Добавить вершину");
            System.out.println("2. Удалить вершину");
            System.out.println("3. Найти ребро");
            System.out.println("4. Вывести граф");
            System.out.println("5. Подробная информация о графе");
            System.out.println("6. Выход");
            System.out.print("Выбор: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addVertex();
                    break;
                case "2":
                    deleteVertex();
                    break;
                case "3":
                    findEdge();
                    break;
                case "4":
                    GraphOperations.printGraph(graph);
                    break;
                case "5":
                    GraphOperations.printGraphDetails(graph);
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор\n");
            }
        }
    }

    private static void addVertex() {
        System.out.print("Введите букву вершины: ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            System.out.println("Ошибка\n");
            return;
        }

        char label = input.charAt(0);
        java.util.List<Character> connected = new ArrayList<>();

        System.out.println("Введите связи (Enter для выхода):");
        while (true) {
            System.out.print("> ");
            String target = scanner.nextLine().trim().toUpperCase();

            if (target.isEmpty()) {
                break;
            }

            if (target.length() == 1 && Character.isLetter(target.charAt(0))) {
                connected.add(target.charAt(0));
            }
        }

        char[] array = new char[connected.size()];
        for (int i = 0; i < connected.size(); i++) {
            array[i] = connected.get(i);
        }

        try {
            GraphOperations.addVertex(graph, label, array);
            System.out.println("Вершина добавлена\n");
            GraphOperations.printGraph(graph);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }
    }

    private static void deleteVertex() {
        System.out.print("Введите букву вершины: ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
            System.out.println("Ошибка\n");
            return;
        }

        char label = input.charAt(0);

        System.out.print("Удалить? (yes/no): ");
        String conf = scanner.nextLine().trim().toLowerCase();

        if (conf.equals("yes") || conf.equals("y")) {
            try {
                GraphOperations.deleteVertex(graph, label);
                System.out.println("Вершина удалена\n");
                GraphOperations.printGraph(graph);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + "\n");
            }
        }
    }

    private static void findEdge() {
        System.out.print("Начальная вершина: ");
        String source = scanner.nextLine().trim().toUpperCase();

        System.out.print("Конечная вершина: ");
        String target = scanner.nextLine().trim().toUpperCase();

        if (source.length() != 1 || target.length() != 1) {
            System.out.println("Ошибка\n");
            return;
        }

        boolean exists = GraphOperations.findEdge(graph, source.charAt(0), target.charAt(0));

        if (exists) {
            System.out.println("Ребро найдено\n");
        } else {
            System.out.println("Ребро не найдено\n");
        }
    }
}
