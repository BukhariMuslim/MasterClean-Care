package com.TA.MVP.appmobilemember.Model.Array;

import java.util.ArrayList;

/**
 * Created by Zackzack on 12/06/2017.
 */

public class ArrayAgama {
    private ArrayList<String> arrayList;
    public ArrayAgama() {
        arrayList = new ArrayList<String>();
        arrayList.add("Semua");
        arrayList.add("Islam");
        arrayList.add("Kristen");
        arrayList.add("Hindu");
        arrayList.add("Buddha");
        arrayList.add("Atheist");
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}
