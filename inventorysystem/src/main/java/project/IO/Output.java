package project.IO;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Output {

    public static <T> void PrintClassFieldsAsTable(ArrayList<T> _DataList, Class<T> _TypeClass) {
        try {
            // Display Class Name
            System.out.println(_TypeClass.getSimpleName() + " Table");

            Field[] fields = _TypeClass.getDeclaredFields();

            // Header
            printSeparatorLine(fields.length);
            printHeader(fields);
            printSeparatorLine(fields.length);

            // Body 
            for (T data : _DataList) {
                printBodyRow(fields, data);
                printSeparatorLine(fields.length);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void PrintCrudMenu(Class<?> _TypeClass) {
        String className = _TypeClass.getSimpleName();

        printSeparatorLine(1);
        System.out.println(String.format("| %-14s |", "CRUD Operation"));
        printSeparatorLine(1);

        System.out.println(String.format("| %-14s |", "1. Create " + className));
        System.out.println(String.format("| %-14s |", "2. Read " + className));
        System.out.println(String.format("| %-14s |", "3. Update " + className));
        System.out.println(String.format("| %-14s |", "4. Delete " + className));
        System.out.println(String.format("| %-14s |", "5. Exit"));

        printSeparatorLine(1);
    }

    private static void printHeader(Field[] fields) {
        for (Field field : fields) {
            System.out.print(WarpField(field.getName()));
        }
        System.out.println();
    }

    private static <T> void printBodyRow(Field[] fields, T data) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldValue = field.get(data).toString();
            System.out.print(WarpField(fieldValue));
        }
        System.out.println();
    }

    private static void printSeparatorLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("==================");
        }
        System.out.println();
    }

    private static String WarpField(String _fieldName) {
        if (_fieldName.length() > 16) {
            _fieldName = _fieldName.substring(0, 16);
        }
        return String.format("|%-16s|", _fieldName);
    }

    public static void ClearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
