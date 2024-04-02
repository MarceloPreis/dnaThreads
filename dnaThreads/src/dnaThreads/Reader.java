package dnaThreads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader implements Runnable {
	private String filePath;
	private int lineCount;
	private int validLineCount;
	private int invalidLineCount;
	private ArrayList<Integer> invalidLines = new ArrayList<Integer>();
	
	Reader(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public void run() {
		try {
            File file = new File("./dna/" + filePath);
            Scanner scanner = new Scanner(file);
            File restulFile = new File("./results/" + filePath);
            
            if (restulFile.exists())
            	restulFile.delete();
            
            try {
            	restulFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        	FileWriter fw = new FileWriter(restulFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();                        
                String newLine = this.transalate(line);
                bw.newLine();
                bw.write(newLine);
                
                this.lineCount++;
            }
            
            scanner.close();
            bw.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
		
		this.showResults();
	}
	
	private void showResults() {
		System.out.println("Arquivo " + this.filePath + " finalizado!");
		System.out.println("Total de linhas do arquivo = " + this.lineCount);
		System.out.println("Total de linhas válidas = " + this.validLineCount);
		System.out.println("Total de linhas inválidas = " + this.invalidLineCount);
		System.out.println("Linhas inválidas = " + this.invalidLines);
	}

	private String transalate(String dnaSequence) {
		
		if (!Reader.isValidDna(dnaSequence)) {
			this.invalidLineCount++;
			this.invalidLines.add(this.lineCount);
			
			return "***FITA INVALIDA - " + dnaSequence; 
		}
		
		StringBuilder rnaSequence = new StringBuilder();
		
        for (char nucleotide : dnaSequence.toCharArray()) {
            switch (nucleotide) {
                case 'A':
                    rnaSequence.append('U');
                    break;
                case 'T':
                    rnaSequence.append('A');
                    break;
                case 'G':
                    rnaSequence.append('C');
                    break;
                case 'C':
                    rnaSequence.append('G');
                    break;
                default:
                    throw new IllegalArgumentException("Invalid DNA nucleotide: " + nucleotide);
            }
        }
        
        this.validLineCount++;
        return rnaSequence.toString();
	}
	
	
    public static boolean isValidDna(String dnaSequence) {
        String dnaRegex = "^[ATGC]*$";
        return dnaSequence.matches(dnaRegex);
    }
}