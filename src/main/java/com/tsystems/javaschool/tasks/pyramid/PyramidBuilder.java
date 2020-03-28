package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;

/*
 * This is PyramidBuilder implementation.
 * First it checks the given list for null items. This process has linear complexity. Throws CannotBuildException if there is any null item in the given list;
 * Then it creates a template (a two dimensional array without any values - with zeros by default). But it checks if the size of given list is proper according to arithmetical progression.
 * Then it sorts a given list using Collections.sort() method.
 * After calling fillPyramid() method it fills the template by single row.
 */

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */

    public int[][] buildPyramid(List<Integer> inputNumbers) {
        checkArrayForNullItems(inputNumbers);
        int[][] pyramidTemplate = createPyramidTemplate(inputNumbers.size());
        sortArray(inputNumbers);
        return fillPyramid(pyramidTemplate, inputNumbers);
    }

    private void checkArrayForNullItems(List<Integer> inputNumbers) throws CannotBuildPyramidException {
        for (Integer value : inputNumbers) {
            if (value == null)
                throw new CannotBuildPyramidException();
        }
    }

    private int[][] createPyramidTemplate(Integer arraySize) throws CannotBuildPyramidException{
        int sum = 0;
        int subArraysCounter = 0;

        for (int i = 1; sum < arraySize; i++) {
            if ((sum += i) > 0) {
                subArraysCounter++;
            } else {
                throw new CannotBuildPyramidException();
            }
        }
        if (sum == arraySize) {
            return new int[subArraysCounter][(subArraysCounter*2)-1];
        } else {
            throw new CannotBuildPyramidException();
        }
    }

    private void sortArray(List<Integer> array) {
        Collections.sort(array);
    }

    private int[][] fillPyramid(int[][] template, List<Integer> input) {
        int itemIndex = input.size()-1;

        for (int row = template.length, leftOffset = 0; row >= 1; row--, leftOffset++) {
            fillRow(template[row-1], input, ((itemIndex-row) + 1), row, leftOffset);
            itemIndex -= row;
        }
        return template;
    }

    private void fillRow(int[] row, List<Integer> input, int itemIndex, int quantityToFill, int leftOffset) {
        for (int i = 0; i < quantityToFill; i++, itemIndex++) {
            Integer item = input.get(itemIndex);
            if (item == null)
                throw new CannotBuildPyramidException();

            if (i == 0) {
                row[leftOffset] = input.get(itemIndex);
            } else {
                row[leftOffset] = input.get(itemIndex);
            }
            leftOffset += 2;
        }
    }


}
