package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if (areListsValid(x, y)) {
            if (x.isEmpty() && y.isEmpty())
                return true;

            if (x.isEmpty())
                return true;

            int requiredItemIndex = 0;
            for (Object o : y) {
                if (o.equals(x.get(requiredItemIndex))) {
                    requiredItemIndex++;
                    if (requiredItemIndex == x.size())
                        return true;
                }
            }
            return false;
        } else
            return false;
    }

    /*
     * Checks is the given lists are valid: not null, not empty and etc.
     */
    private boolean areListsValid(List x, List y) throws IllegalArgumentException {
        if (x == null || y == null)
            throw new IllegalArgumentException ();
        if (y.isEmpty() && !x.isEmpty())
            return false;
        if (x.size() > y.size())
            return false;

        return true;
    }
}
