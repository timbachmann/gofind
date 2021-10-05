package de.tim.gofind.search;

import java.util.ArrayList;

/**
 * TODO
 */
public class HistoricalMaps {

    public static final HistoricalMap MERIAN1615 = new HistoricalMap("Merian 1615", "https://www.basler-bauten.ch/images/stadtplaene/MerianKupfer1615.jpg");
    public static final HistoricalMap MERIANSUED1615 = new HistoricalMap("Merian Süd 1615","https://www.basler-bauten.ch/images/stadtplaene/MerianSued1615.jpg");
    public static final HistoricalMap MERIAN1626 = new HistoricalMap("Merian 1626","https://www.basler-bauten.ch/images/stadtplaene/Merian1626.jpg");
    public static final HistoricalMap RHYNIER1784 = new HistoricalMap("Rhynier 1784","https://www.basler-bauten.ch/images/stadtplaene/Rhynier1784.jpg");
    public static final HistoricalMap MAELYPLAN1845 = new HistoricalMap("Mählyplan 1845","https://www.basler-bauten.ch/images/stadtplaene/maehlyplan_farbig.jpg");
    public static final HistoricalMap TOURIST1888 = new HistoricalMap("Tourist 1888","https://www.basler-bauten.ch/images/stadtplaene/Stadtplan1888.jpg");

    public static CharSequence[] getMapNameList() {
        return new CharSequence[]{MERIAN1615.getName(), MERIANSUED1615.getName(), MERIAN1626.getName(), RHYNIER1784.getName(), MAELYPLAN1845.getName(), TOURIST1888.getName()};
    }

    public static ArrayList<HistoricalMap> getMapList() {
        ArrayList<HistoricalMap> maps = new ArrayList<>();
        maps.add(MERIAN1615);
        maps.add(MERIANSUED1615);
        maps.add(MERIAN1626);
        maps.add(RHYNIER1784);
        maps.add(MAELYPLAN1845);
        maps.add(TOURIST1888);
        return maps;
    }
}
