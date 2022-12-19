import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Decrypter1 {
    public static void main(String[] args) throws FileNotFoundException {
        //region key length finder

        // creates scanner to bring in our word
        Scanner scan = new Scanner(new File("FUN.dat"));

        // stores our original encrypted text
        String encrypted_txt = scan.next();

        // length of encrypted text
        int text_length = encrypted_txt.length();

        // ArrayList that stores our occurrences
        ArrayList<Integer> occurrenceCounter = new ArrayList<>();


        // This for loop counts our occurrences

        // the first for loop loops through our text
        for (int i = 1; i < text_length; i++){

            // occurrence's counts the coincidences in each line
            int occurences = 0;

            // this stores the original string with (i) amount of letters removed from the end
            String newString = encrypted_txt.substring(0, text_length - i);

            // this loops through our new shifted string and compares it to our original
            for (int x = 0; x <newString.length(); x++){

                // we compare each index of our shifted string to the adjacent index of our original string
                if (newString.substring(x, x+1).equals(encrypted_txt.substring(x + i, x + i + 1))){
                    // if they match we add one to occurrences
                    occurences++;
                }

            }
            // we add the total amount of occurrences to our list
            occurrenceCounter.add(occurences);


        }

        // initializes sum to later use for finding average of all occurrences in our list.
        double sum = 0;
        // for loop to loop through each element in occurrenceCounter list.
        for (int x: occurrenceCounter){
            // add each occurrence to our sum.
            sum += x;}

        // finding the average of occurrences in our list.
        double avg = sum/occurrenceCounter.size();

        // looping backwards from end of list to the start
        for (int i = occurrenceCounter.size()-1; i >= 0; i--){

            // if statement to remove outliers in our occurrenceCounter list by comparing if they are less than the average.
            if (occurrenceCounter.get(i) < avg){
                // remove the outlier from the list if it's less than the average of occurrences.
                occurrenceCounter.remove(i);}
        }

        double highest = Double.MIN_VALUE;
        int key_length = 0;

        for (int i = 3; i < 8; i++){
            // sum of the multiples of the sentence
            int why_did_she_leave_me = 0;
            // used to find how many numbers were added to find the average
            int loop_counter = 0;
            // adding the numbers with the certain multiple
            for (int x = i; x < occurrenceCounter.size()-1; x += i){
                why_did_she_leave_me += occurrenceCounter.get(x-1);
                loop_counter++;}

            //finding the key length from the highest average
            double temp_average = (double)why_did_she_leave_me/((double)occurrenceCounter.size())/loop_counter;
            if (temp_average > highest){
                highest = temp_average;
                key_length = i;
            }

        }
        // endregion

        //region key finder

        String key = "";
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        //repeats the find key letter loop for each letter in key
        for (int n = 0; n < key_length; n++){

            Scanner freq_scanner = new Scanner(new File("freq.dat"));
            ArrayList<Double> freq = new ArrayList<>();

            // adds the frequency to an arraylist
            while (freq_scanner.hasNextLine()){
                String freqSting = freq_scanner.nextLine();
                freq.add(Double.valueOf(freqSting));
            }


            // takes every (key_length)th letter from the sentence from out encrypted text
            String encrypted_trimmed = "";
            for (int x = n; x < encrypted_txt.length(); x+=key_length){
                encrypted_trimmed += (encrypted_txt.substring(x,x+1));
            }

            char[] original_trimmed_list = encrypted_trimmed.toCharArray();

            ArrayList<Integer> imsosorry = new ArrayList<>();
            ArrayList<Double> freqOfLetterInEncrypt = new ArrayList<>();


            for (char letter:alphabet) {
                int letter_amts = 0;
                for (char letter2 : original_trimmed_list) {
                    if (letter == letter2){
                        letter_amts++;
                    }
                }
                imsosorry.add(letter_amts);
            }

            int total = 0;
            for (int num:imsosorry){
                total += num;
            }

            for (int num:imsosorry){
                if (total != 0){
                    freqOfLetterInEncrypt.add((double)num/(double)total);
                }
                else {freqOfLetterInEncrypt.add(0.0d);}

            }

            ArrayList<Double> sumOfLetterPlacement = new ArrayList<>();

            // multiplying each row and shifting it down one
            for (int i = 0; i < freq.size(); i++) {
                double line = 0;
                for (int y = 0; y < 26; y++) {
                    line += (freq.get(y) * freqOfLetterInEncrypt.get(y));
                }
                freqOfLetterInEncrypt.add(freqOfLetterInEncrypt.get(0));
                freqOfLetterInEncrypt.remove(0);
                sumOfLetterPlacement.add(line);

            }

            // finding which has the highest sum
            double highestNumber = Double.MIN_VALUE;
            for (double anotherforloop : sumOfLetterPlacement){
                if (anotherforloop > highestNumber){
                    highestNumber = anotherforloop;
                }
            }

            // turning the index to a character/letter
            char keyLetter = alphabet[sumOfLetterPlacement.indexOf(highestNumber)];
            key += String.valueOf(keyLetter);
        }
        System.out.println("Key: \n" + key);

        //endregion

        //region decrypting with key
        String newKey = "";
        for (int i = 0; i < encrypted_txt.length() / key_length; i++){
            newKey += key;
        }

        newKey += key.substring(0, encrypted_txt.length() % key_length);
        key = newKey;

        String decrypted_txt = "";
        for (int i = 0; i < encrypted_txt.length() && i < key.length(); i ++){
            int x = (encrypted_txt.charAt(i) - key.charAt(i) + 26) % 26;

            x += 'A';
            decrypted_txt += (char)(x);
        }

        System.out.println("Decrypted Text: \n" + decrypted_txt);

        //endregion



    }


}
