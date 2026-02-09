public class FileStatistic {

    private long intElementsCount;
    private Long intValMin;
    private Long intValMax;
    private Long intValSum = 0L;
    private Long intValAvg = 0L;


    private long floatElementsCount;
    private float floatSum;
    private Float floatValMax;
    private Float floatValMin;
    private Float floatValAvg;

    private Long stringElementsCount = 0L;
    private Long stringMaxLetterCount = 0L;
    private Long stringMinLetterCount = Long.MAX_VALUE;

    public void addInteger(String integerVal) {
        long val;

        try {
            val = Long.parseLong(integerVal);
        } catch (Exception e) {
            System.out.println("Integer '" + integerVal + "' cannot be recognized");
            return;
        }

        updateIntegerIfNull(val);

        intElementsCount++;
        intValSum += val;
        intValAvg = intValSum / intElementsCount;
    }
    public void addFloat(String floatVal) {
        float val;

        try {
            val = Float.parseFloat(floatVal);
        } catch (Exception e) {
            System.out.println("A real number '" + floatVal + "' cannot be recognized");
            return;
        }

        updateFloatIfNull(val);

        floatElementsCount++;
        floatSum += val;
        floatValAvg = floatSum / floatElementsCount;
    }

    public void addString(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }

        if (stringMaxLetterCount < string.length())
            stringMaxLetterCount = (long) string.length();

        if (stringMinLetterCount > string.length())
            stringMinLetterCount = (long) string.length();

        stringElementsCount++;
    }



    private void updateIntegerIfNull(Long val) {
        if (intValMin == null)
            intValMin = val;
        else {
            if (intValMin > val)
                intValMin = val;
        }

        if (intValMax == null)
            intValMax = val;
        else {
            if (intValMax < val)
                intValMax = val;
        }
    }

    private void updateFloatIfNull(Float val) {
        if (floatValMin == null)
            floatValMin = val;
        else {
            if (floatValMin > val)
                floatValMin = val;
        }

        if (floatValMax == null)
            floatValMax = val;
        else {
            if (floatValMax < val)
                floatValMax = val;
        }
    }

    public void showFloatFullStats() {
        System.out.println("----------------------------------------------------------");

        System.out.println("Float elements count - " + floatElementsCount);
        System.out.println("Float max value - " + floatValMax);
        System.out.println("Float min value - " + floatValMin);
        System.out.println("Float average value - " + floatValAvg);
        System.out.println("Float sum - " + floatSum);

        System.out.println("----------------------------------------------------------");
    }

    public void showIntegerFullStats() {
        System.out.println("----------------------------------------------------------");

        System.out.println("Integer elements count - " + intElementsCount);
        System.out.println("Integer max value - " + intValMax);
        System.out.println("Integer min value - " + intValMin);
        System.out.println("Integer average value - " + intValAvg);
        System.out.println("Integer sum - " + intValSum);

        System.out.println("----------------------------------------------------------");
    }

    public void showStringFullStats() {
        System.out.println("----------------------------------------------------------");

        System.out.println("String elements count - " + stringElementsCount);

        long max = 0;
        long min = 0;

        if (stringMaxLetterCount != 0) {
            max = stringMaxLetterCount;
            min = stringMinLetterCount;
        }

            System.out.println("String max letter count - " + max);
            System.out.println("String min letter count - " + min);


        System.out.println("----------------------------------------------------------");
    }

    public void showIntegerShortStats() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Integer elements count - " + intElementsCount);
        System.out.println("----------------------------------------------------------");
    }

    public void showFloatShortStats() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Float elements count - " + floatElementsCount);
        System.out.println("----------------------------------------------------------");
    }

    public void showStringShortStats() {
        System.out.println("----------------------------------------------------------");
        System.out.println("String elements count - " + stringElementsCount);
        System.out.println("----------------------------------------------------------");
    }

}
