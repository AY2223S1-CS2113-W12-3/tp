package seedu.duke.records.biometrics;

import seedu.duke.exception.IllegalValueException;

import java.util.ArrayList;

public class WeightAndFatList {

    private ArrayList<WeightAndFat> weightAndFatArrayList;

    public WeightAndFatList() {
        weightAndFatArrayList = new ArrayList<>();
    }

    public void addWeightAndFat(WeightAndFat weightAndFat) {
        weightAndFatArrayList.add(weightAndFat);
        weightAndFatArrayList.sort(new WeightAndFatComparator());
    }

    public boolean isMostRecent(WeightAndFat weightAndFat) {
        assert weightAndFatArrayList.size() > 0;
        return weightAndFat == weightAndFatArrayList.get(0);
    }

    public void removeWeightAndFat(int index) throws IllegalValueException {
        if (index < 0 || index >= weightAndFatArrayList.size()) {
            throw new IllegalValueException("Weight and fat record does not exist");
        }
        weightAndFatArrayList.remove(index);
    }

    public int getSize() {
        return weightAndFatArrayList.size();
    }

    public ArrayList<WeightAndFat> getWeightAndFatList() {
        return weightAndFatArrayList;
    }
}
