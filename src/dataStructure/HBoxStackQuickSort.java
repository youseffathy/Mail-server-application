package dataStructure;

import java.awt.Point;
import java.util.Comparator;

import dataStructure.Stack;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class HBoxStackQuickSort {
    public void swap (HBox[] arr, int i, int j) {
        HBox temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public int partition(HBox a[], int start, int end) {
        HBox pivot = a[end];
        int pIndex = start;
        for (int i = start; i < end; i++)
        {
            if (hboxCompare(a[i], pivot) <= 0)
            {
                swap(a, i, pIndex);
                pIndex++;
            }
        }
        swap (a, pIndex, end);
        return pIndex;
    }
    public void quickSort(HBox[] a) {
        Stack stack = new Stack();
        int start = 0;
        int end = a.length - 1;
        stack.push(new Point(start, end));
        while (!stack.isEmpty()) {
            start = ((Point)stack.peek()).x;
            end = ((Point)stack.peek()).y;
            stack.pop();
            int pivot = partition(a, start, end);
            if (pivot - 1 > start) {
                stack.push(new Point(start, pivot - 1));
            }
            if (pivot + 1 < end) {
                stack.push(new Point(pivot + 1, end));
            }
        }
    }
    private int hboxCompare(HBox h1, HBox h2) {
        String h1Name = ((Label)h1.getChildren().get(3)).getText();
        String h2Name = ((Label)h2.getChildren().get(3)).getText();
        return h1Name.compareToIgnoreCase(h2Name);
    }
}