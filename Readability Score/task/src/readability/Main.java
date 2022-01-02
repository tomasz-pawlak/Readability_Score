package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    static double ari(double numberOfCharacters, double singleWords, double numberOfSentences) {
        return 4.71 * (numberOfCharacters / (double) singleWords) + 0.5 * (singleWords / numberOfSentences) - 21.43;
    }

    static void showAriScore(double a) {


        switch ((int) Math.ceil(a)) {
            case 1:
                System.out.println("This text should be understood by 5-6-year-olds");
                break;
            case 2:
                System.out.println("This text should be understood by 6-7-year-olds");
                break;
            case 3:
                System.out.println("This text should be understood by 7-9-year-olds");
                break;
            case 4:
                System.out.println("This text should be understood by 9-10-year-olds");
                break;
            case 5:
                System.out.println("This text should be understood by 10-11-year-olds");
                break;
            case 6:
                System.out.println("This text should be understood by 11-12-year-olds");
                break;
            case 7:
                System.out.println("This text should be understood by 12-13-year-olds");
                break;
            case 8:
                System.out.println("This text should be understood by 13-14-year-olds");
                break;
            case 9:
                System.out.println("This text should be understood by 14-15-year-olds");
                break;
            case 10:
                System.out.println("This text should be understood by 15-16-year-olds");
                break;
            case 11:
                System.out.println("This text should be understood by 16-17-year-olds");
                break;
            case 12:
                System.out.println("This text should be understood by 17-18-year-olds");
                break;
            case 13:
                System.out.println("This text should be understood by 18-24-year-olds");
                break;
            case 14:
                System.out.println("This text should be understood by 24+-year-olds");
                break;

        }
    }

    static double fk(double words, double sentences, double syllables) {
        double s = (words / sentences);
        double l = (syllables / words);
        return (0.39 * s) + (11.8 * l) - 15.59;
    }

    static double smog(double sentence, double polysyllables) {
        double sqrt = Math.sqrt(polysyllables * (30 / sentence));
        return (1.043 * sqrt) + 3.1291;
    }

    static double cl(double sentences, double characters, double words) {
        double s = sentences / words * 100;
        double l = characters / words * 100;

        return (0.0588 * l) - (0.296 * s) - 15.8;
    }

    private static int countWithRegex(String word) {
        String i = "(?i)[aeiouy][aeiouy]*|e[aeiouy]*(?!d?\\b)";
        Matcher m = Pattern.compile(i).matcher(word);
        int count = 0;

        while (m.find()) {
            count++;
        }
        if (word.endsWith("e")){
            return Math.max(count, 1)-1;
        }else {
            return Math.max(count, 1);
        }
    }

    private static int showAge(double score) {
        int result;
        switch ((int) score) {
            case 1:
                result = 6;
                break;
            case 2:
                result = 7;
                break;
            case 3:
                result = 9;
                break;
            case 4:
                result = 10;
                break;
            case 5:
                result = 11;
                break;
            case 6:
                result = 12;
                break;
            case 7:
                result = 13;
                break;
            case 8:
                result = 14;
                break;
            case 9:
                result = 15;
                break;
            case 10:
                result = 16;
                break;
            case 11:
                result = 17;
                break;
            case 12:
                result = 18;
                break;
            case 13:
                result = 24;
                break;
            case 14:
                result = 24;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + (int) score);
        }
        return result;
    }


    public static void main(String[] args) {

        File file = new File(args[0]);
        DecimalFormat df = new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.US));

        try (Scanner scanner = new Scanner(file)) {

            String sentence = scanner.nextLine();
            System.out.println(sentence);
            String[] singleWords = sentence.split(" ");
            double numberOfSentences = sentence.split("[!.//?]+").length;
            char[] numberOfCharacters = sentence
                    .replace("\n", "")
                    .replace("\t", "")
                    .replace(" ", "")
                    .toCharArray();
            scanner.close();

            double syllables = 0;
            double polysyllables = 0;
            for (int i = 0; i < singleWords.length; i++) {
                syllables += countWithRegex(singleWords[i]);
                if (countWithRegex(singleWords[i])>2){
                    polysyllables ++;
                }
            }

            System.out.println("Words: " + singleWords.length);
            System.out.println("Sentences: " + numberOfSentences);
            System.out.println("Characters: " + numberOfCharacters.length);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polysyllables);


            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();

            double ariScore = ari(numberOfCharacters.length, singleWords.length, numberOfSentences);
            double fkScore = fk(singleWords.length, numberOfSentences, syllables);
            double smogScore = smog(numberOfSentences, polysyllables);
            double clScore = cl(numberOfSentences, numberOfCharacters.length, singleWords.length);

            switch (input) {
                case "ARI":
                    System.out.println("Automated Readability Index: " + df.format(ariScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    break;
                case "FK":
                    System.out.println("Flesch–Kincaid readability tests: " + df.format(fkScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    break;
                case "SMOG":
                    System.out.println("Simple Measure of Gobbledygook: " + df.format(smogScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    break;
                case "CL":
                    System.out.println("Coleman–Liau index: " + df.format(clScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    break;
                case "all":
                    System.out.println("Automated Readability Index: " + df.format(ariScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    System.out.println("Flesch–Kincaid readability tests: " + df.format(fkScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    System.out.println("Simple Measure of Gobbledygook: " + df.format(smogScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    System.out.println("Coleman–Liau index: " + df.format(clScore) + " (about " + showAge(ariScore) + "-year-olds)");
                    break;
            }

            showAriScore(ariScore);
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + file.getPath());
        }


    }
}
