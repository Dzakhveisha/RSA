import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Введите значение p"); //простое число
        long p = scan.nextLong();
        System.out.println("Введите значение q"); //простое число
        long q = scan.nextLong();
        System.out.println("Введите шифруемое слово:");
        scan.nextLine();
        String message = scan.nextLine().toUpperCase();        //подготовка шифруемой строки
        message = message.replaceAll("\\s","");

        long n = p * q;
        long f = (p - 1) * (q - 1);    // функция Эйлера

        long e = calculate_e(f);       // выбираем целое значения открытой экспоненты e

        gcdReturn result = new gcdReturn();
        result = result.gcdWide(f, e);     // Расширенный алгоритм Евклида для нахождения d
        long d = result.y;
        if (d < 0) { d += f;}

        ArrayList<String> encryptMessage = RSA_Endoce(message, d, n);
        System.out.println("Зашифровка: " + encryptMessage.toString());
        String decryptMessage = RSA_Decode(encryptMessage, e, n);
        System.out.println("Зашифровка: " + decryptMessage);
    }

    private static long calculate_e(long f)
    {  // простое, меньше f, взаимно простое с ним
        long e = f - 1;
        for (int i = 2; i <= f; i++)
            if ((f % i == 0) && (e % i == 0)) //если имеют общие делители
            {
                e--;
                i = 1;
            }
        return e;
    }

    public static BigInteger power(BigInteger a, long n) { //алгоритм быстрого возведения в степень
        if (n == 0)
            return BigInteger.valueOf(1);
        if (n % 2 == 1)
            return power (a, n-1).multiply(a);
        else {
            BigInteger b = power (a, n/2);
            return b.multiply(b);
        }
    }

    private static  ArrayList<String> RSA_Endoce(String s, long e, long n)
    {
        ArrayList<String> result =  new ArrayList<String>();
        BigInteger bi;

        for (int i = 0; i < s.length(); i++)
        {
            int index = characters.indexOf(s.charAt(i));
            bi = BigInteger.valueOf(index);
            bi = power(bi, e);
            bi = bi.mod(BigInteger.valueOf((int)n));
            result.add( bi.toString());
        }

        return result;
    }

    private static String RSA_Decode(List<String> input, long d, long n)
    {
        String result = "";
        BigInteger bi;

        for (String item : input)
        {
            bi = BigInteger.valueOf(Long.parseLong(item));
            bi = power(bi, d);

            bi = bi.mod(BigInteger.valueOf(n));
            int index = bi.intValue();
            result += characters.charAt(index);
        }

        return result;
    }

    public static final class gcdReturn { //класс расширенный алгоритм Евклида
        long d;
        long x;
        long y;

        gcdReturn(long d, long x, long y) {
            this.d = d;
            this.x = x;
            this.y = y;
        }

        gcdReturn() {
        }

        public gcdReturn gcdWide(long a, long b) {
            if (b == 0) {
                return new gcdReturn(a, 1, 0);
            } else {

                gcdReturn temp2 = gcdWide(b, a % b);
                gcdReturn temp = new gcdReturn();

                temp.d = temp2.d;
                temp.x = temp2.y;
                temp.y = temp2.x - temp2.y * (a / b);
                return temp;
            }
        }
    }

}
