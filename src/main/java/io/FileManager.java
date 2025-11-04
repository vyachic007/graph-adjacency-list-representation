package io;

import java.io.*;
import java.util.Scanner;

public class FileManager {

    public static int[][] loadAdjacencyMatrix(String filename) throws FileNotFoundException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        try (Scanner scanner = new Scanner(new File(filename))) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Файл пуст");
            }

            int n = Integer.parseInt(scanner.nextLine().trim());

            if (n <= 0 || n > 1000) {
                throw new IllegalArgumentException("Неверный размер матрицы: " + n);
            }

            int[][] matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                if (!scanner.hasNextLine()) {
                    throw new IllegalArgumentException("Недостаточно строк в файле");
                }

                String line = scanner.nextLine().trim();
                String[] elements = line.split("\\s+");

                if (elements.length != n) {
                    throw new IllegalArgumentException("Строка " + (i + 1) + " имеет неверное количество элементов");
                }

                for (int j = 0; j < n; j++) {
                    int value = Integer.parseInt(elements[j]);
                    if (value != 0 && value != 1) {
                        throw new IllegalArgumentException("Элемент должен быть 0 или 1");
                    }
                    matrix[i][j] = value;
                }
            }

            return matrix;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл не найден: " + filename);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка при чтении числа из файла");
        }
    }
}
