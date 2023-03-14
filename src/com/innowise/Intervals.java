package com.innowise;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Intervals {

    public static final String INTERVAL_MIN_2 = "m2";
    public static final String INTERVAL_MAJ_2 = "M2";
    public static final String INTERVAL_MIN_3 = "m3";
    public static final String INTERVAL_MAJ_3 = "M3";
    public static final String INTERVAL_PERF_4 = "P4";
    public static final String INTERVAL_PERF_5 = "P5";
    public static final String INTERVAL_MIN_6 = "m6";
    public static final String INTERVAL_MAJ_6 = "M6";
    public static final String INTERVAL_MIN_7 = "m7";
    public static final String INTERVAL_MAJ_7 = "M7";
    public static final String INTERVAL_PERF_8 = "P8";

    private static final Map<String, Integer[]> INTERVAL_DEGREES_SEMITONES_MAP = Map.ofEntries(
            Map.entry(INTERVAL_MIN_2, new Integer[]{2, 1}),
            Map.entry(INTERVAL_MAJ_2, new Integer[]{2, 2}),
            Map.entry(INTERVAL_MIN_3, new Integer[]{3, 3}),
            Map.entry(INTERVAL_MAJ_3, new Integer[]{3, 4}),
            Map.entry(INTERVAL_PERF_4, new Integer[]{4, 5}),
            Map.entry(INTERVAL_PERF_5, new Integer[]{5, 7}),
            Map.entry(INTERVAL_MIN_6, new Integer[]{6, 8}),
            Map.entry(INTERVAL_MAJ_6, new Integer[]{6, 9}),
            Map.entry(INTERVAL_MIN_7, new Integer[]{7, 10}),
            Map.entry(INTERVAL_MAJ_7, new Integer[]{7, 11}),
            Map.entry(INTERVAL_PERF_8, new Integer[]{8, 12})
    );

    public static final String NOTE_C = "C";
    public static final String NOTE_D = "D";
    public static final String NOTE_E = "E";
    public static final String NOTE_F = "F";
    public static final String NOTE_G = "G";
    public static final String NOTE_A = "A";
    public static final String NOTE_B = "B";

    private static final List<String> NOTE_ORDER_LIST = List.of(NOTE_C, NOTE_D, NOTE_E, NOTE_F, NOTE_G, NOTE_A, NOTE_B);
    private static final Map<String, Integer> NOTE_SEMITONES_TO_NEXT_MAP = Map.of(
            NOTE_C, 2,
            NOTE_D, 2,
            NOTE_E, 1,
            NOTE_F, 2,
            NOTE_G, 2,
            NOTE_A, 2,
            NOTE_B, 1
    );

    public static final String ACCIDENTAL_NONE = "";
    public static final String ACCIDENTAL_SHARP = "#";
    public static final String ACCIDENTAL_DOUBLE_SHARP = "##";
    public static final String ACCIDENTAL_FLAT = "b";
    public static final String ACCIDENTAL_DOUBLE_FLAT = "bb";

    private static final Map<String, Integer> ACCIDENTAL_SEMITONES_MAP = Map.of(
            ACCIDENTAL_NONE, 0,
            ACCIDENTAL_SHARP, 1,
            ACCIDENTAL_FLAT, -1,
            ACCIDENTAL_DOUBLE_SHARP, 2,
            ACCIDENTAL_DOUBLE_FLAT, -2
    );

    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DSC = "dsc";

    public static final int TOTAL_SEMITONES_IN_AN_OCTAVE = 12;
    public static final int TOTAL_DEGREES_IN_AN_OCTAVE = 7;

    private final static String NULL_PARAM_EXCEPTION = "Input param is null";
    private final static String INVALID_PARAM_COUNT_EXCEPTION = "Illegal number of elements in input array";
    private final static String INVALID_INTERVAL_PARAM_EXCEPTION = "Illegal interval param in input array";
    private final static String INVALID_NOTE_PARAM_EXCEPTION = "Illegal note param in input array";
    private final static String INVALID_ORDER_PARAM_EXCEPTION = "Illegal order param in input array";
    private final static String INVALID_INTERVAL_DESCRIPTION_EXCEPTION = "Cannot identify the interval";


    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        return map.entrySet().stream()
                .filter(entry -> value instanceof Integer[] ?
                        Arrays.equals((Integer[]) value, (Integer[]) entry.getValue()) :
                        Objects.equals(value, entry.getValue()))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }

    private static String semitonesToAccidental(int semitoneDifference) {
        return getKeyByValue(ACCIDENTAL_SEMITONES_MAP, semitoneDifference);
    }

    private static int accidentalToSemitones(String accidental) {
        return ACCIDENTAL_SEMITONES_MAP.get(accidental);
    }

    private static String parseAccidental(String noteNameWithAccidentals) {
        String accidentalStr = noteNameWithAccidentals.substring(1);
        return ACCIDENTAL_SEMITONES_MAP.keySet().stream()
                .filter(e -> !e.equals(ACCIDENTAL_NONE))
                .filter(accidentalStr::equals)
                .findAny()
                .orElse(ACCIDENTAL_NONE);
    }

    private static String parseNaturalNote(String noteNameWithAccidentals) {
        String noteName = noteNameWithAccidentals.length() > 1 ?
                noteNameWithAccidentals.substring(0, 1) : noteNameWithAccidentals;
        return NOTE_ORDER_LIST.stream()
                .filter(noteName::equals)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_NOTE_PARAM_EXCEPTION));
    }

    private static void validateArrLengthAndNullArgs(String[] args) {
        if (args == null || args[0] == null || args[1] == null || (args.length == 3 && args[2] == null)) {
            throw new IllegalArgumentException(NULL_PARAM_EXCEPTION);
        }
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException(INVALID_PARAM_COUNT_EXCEPTION);
        }
    }

    private static String validateNoteOrder(String noteOrder) {
        if (!List.of(ORDER_ASC, ORDER_DSC).contains(noteOrder)) {
            throw new IllegalArgumentException(INVALID_ORDER_PARAM_EXCEPTION);
        }
        return noteOrder;
    }

    private static String validateIntervalName(String intervalName) {
        if (!INTERVAL_DEGREES_SEMITONES_MAP.containsKey(intervalName)) {
            throw new IllegalArgumentException(INVALID_INTERVAL_PARAM_EXCEPTION);
        }
        return intervalName;
    }

    private static int getSemitoneDistanceBetweenNaturalNotes(String firstNoteName, String secondNoteName,
                                                              String noteOrder) {
        int firstNoteInd = NOTE_ORDER_LIST.indexOf(firstNoteName);
        int secondNoteInd = NOTE_ORDER_LIST.indexOf(secondNoteName);
        int maxNoteInd = Math.max(firstNoteInd, secondNoteInd);
        int minNoteInd = Math.min(firstNoteInd, secondNoteInd);
        List<String> noteOrderSublist = NOTE_ORDER_LIST.subList(minNoteInd, maxNoteInd);

        int semitonesBetween = noteOrderSublist.stream()
                .mapToInt(NOTE_SEMITONES_TO_NEXT_MAP::get)
                .sum();

        if (secondNoteInd < firstNoteInd) {
            noteOrder = getInvertedOrder(noteOrder);
        }

        if (noteOrder.equals(ORDER_DSC)) {
            semitonesBetween = TOTAL_SEMITONES_IN_AN_OCTAVE - semitonesBetween % TOTAL_SEMITONES_IN_AN_OCTAVE;
        }
        return semitonesBetween;
    }
    private static int getDegreeDistanceBetweenNaturalNotes(String firstNoteName, String secondNoteName, String noteOrder) {
        int firstNoteInd = NOTE_ORDER_LIST.indexOf(firstNoteName);
        int secondNoteInd = NOTE_ORDER_LIST.indexOf(secondNoteName);

        int maxNoteInd = Math.max(firstNoteInd, secondNoteInd);
        int minNoteInd = Math.min(firstNoteInd, secondNoteInd);
        List<String> noteOrderSublist = NOTE_ORDER_LIST.subList(minNoteInd, maxNoteInd);

        int degreesBetween = noteOrderSublist.size();

        if (secondNoteInd < firstNoteInd) {
            noteOrder = getInvertedOrder(noteOrder);
        }

        if (noteOrder.equals(ORDER_DSC)) {
            degreesBetween = (TOTAL_DEGREES_IN_AN_OCTAVE - degreesBetween % TOTAL_DEGREES_IN_AN_OCTAVE);
        }

        return degreesBetween;
    }

    private static String getInvertedOrder(String orderStr) {
        switch (orderStr) {
            case ORDER_DSC:
                return ORDER_ASC;
            case ORDER_ASC:
                return ORDER_DSC;
            default:
                throw new IllegalArgumentException(INVALID_ORDER_PARAM_EXCEPTION);
        }
    }

    private static String getNextNoteByDegreesFromFirst(String firstNoteName, int degreeCountToNextNote, String intervalNoteOrder) {
        if (intervalNoteOrder.equals(ORDER_DSC)) {
            degreeCountToNextNote = -degreeCountToNextNote;
        }

        int secondNoteIndex = Math.floorMod(NOTE_ORDER_LIST.indexOf(firstNoteName) + degreeCountToNextNote, NOTE_ORDER_LIST.size());
        return NOTE_ORDER_LIST.get(secondNoteIndex);
    }

    private static int getDegreeDistanceForInterval(String intervalName) {
        return INTERVAL_DEGREES_SEMITONES_MAP.get(intervalName)[0];
    }

    private static int getSemitoneDistanceForInterval(String intervalName) {
        return INTERVAL_DEGREES_SEMITONES_MAP.get(intervalName)[1];
    }

    public static String intervalConstruction(String[] args) {
        validateArrLengthAndNullArgs(args);
        String intervalName = validateIntervalName(args[0]);

        String firstNoteName = parseNaturalNote(args[1]);
        String firstNoteAccidental = parseAccidental(args[1]);

        String intervalNoteOrder = args.length == 3 ? validateNoteOrder(args[2])  : ORDER_ASC;

        if (intervalName.equals(INTERVAL_PERF_8)) {
            return firstNoteName;
        }

        int degreeCountToNextNote = getDegreeDistanceForInterval(intervalName) - 1;
        String secondNoteName = getNextNoteByDegreesFromFirst(firstNoteName, degreeCountToNextNote, intervalNoteOrder);

        int intervalSemitoneDistance = getSemitoneDistanceForInterval(intervalName);
        int naturalNotesSemitoneDistance = getSemitoneDistanceBetweenNaturalNotes(firstNoteName, secondNoteName, intervalNoteOrder);
        int firstNoteAccidentalSemitones = accidentalToSemitones(firstNoteAccidental);

        if (intervalNoteOrder.equals(ORDER_DSC)) {
            intervalSemitoneDistance = -intervalSemitoneDistance;
            naturalNotesSemitoneDistance = -naturalNotesSemitoneDistance;
        }

        int semitoneDifference = intervalSemitoneDistance - naturalNotesSemitoneDistance + firstNoteAccidentalSemitones;
        String accidentalNeeded = semitonesToAccidental(semitoneDifference);

        return secondNoteName + accidentalNeeded;
    }

    public static String intervalIdentification(String[] args) {
        validateArrLengthAndNullArgs(args);

        String firstNoteName = parseNaturalNote(args[0]);
        String firstNoteAccidental = parseAccidental(args[0]);

        String secondNoteName = parseNaturalNote(args[1]);
        String secondNoteAccidental = parseAccidental(args[1]);

        String intervalNoteOrder = args.length == 3 ? validateNoteOrder(args[2]) : ORDER_ASC;

        if (secondNoteName.equals(firstNoteName)) {
            return INTERVAL_PERF_8;
        }

        // +1 because an interval is inclusive of all its degrees
        int degreesBetweenNotes = getDegreeDistanceBetweenNaturalNotes(firstNoteName,secondNoteName,intervalNoteOrder) + 1;
        int semitonesBetweenNotes = getSemitoneDistanceBetweenNaturalNotes(firstNoteName,secondNoteName,intervalNoteOrder);

        int firstNoteAccidentalSemitones = accidentalToSemitones(firstNoteAccidental);
        int secondNoteAccidentalSemitones = accidentalToSemitones(secondNoteAccidental);

        if (intervalNoteOrder.equals(ORDER_DSC)) {
            semitonesBetweenNotes = semitonesBetweenNotes + firstNoteAccidentalSemitones - secondNoteAccidentalSemitones;
        } else {
            semitonesBetweenNotes = semitonesBetweenNotes - firstNoteAccidentalSemitones + secondNoteAccidentalSemitones;
        }

        Integer[] intervalMapEntry = new Integer[]{degreesBetweenNotes,Math.abs(semitonesBetweenNotes)};

        String intervalName = getKeyByValue(INTERVAL_DEGREES_SEMITONES_MAP,intervalMapEntry);
        if (intervalName == null) {
            throw new IllegalArgumentException(INVALID_INTERVAL_DESCRIPTION_EXCEPTION);
        }

        return intervalName;
    }
}
