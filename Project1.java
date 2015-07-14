// Nelson Lee
// Assignment - Project
// CS 311
// 7/13/2015

import java.io.*;
import java.util.*;

public class Project1{

  private ArrayList<ArrayList> machines;

  // Read the file and store the data
  public void readFile(String fileName){

    String line = null;

    try {

       this.machines = new ArrayList<ArrayList>();
       ArrayList<String> machine = new ArrayList<String>();

       FileReader fileReader = new FileReader(fileName);
       BufferedReader bufferedReader = new BufferedReader(fileReader);

       while((line = bufferedReader.readLine()) != null) {
         if(line.isEmpty()){
           machine.add(" ");
           continue;
         }
         if(line.charAt(0) == '!'){
           this.machines.add(machine);
           machine = new ArrayList<String>();
           continue;
         }
         machine.add(line);
       }

       bufferedReader.close();
    }
    catch(FileNotFoundException ex) {
       System.out.println("Unable to open file '" + fileName + "'");
    }
    catch(IOException ex) {
       System.out.println("Error reading file '" + fileName + "'");
    }

  }

  // Fill the multidimension with the dead end state
  public String[][] fillTransition(String[][] transition, String numOfStates, String[] alphabet){

    for(int x = 0; x < Integer.parseInt(numOfStates); x++){
      for(int y = 0; y < alphabet.length; y++){

        transition[x][y] = numOfStates;

      }
    }

    return transition;

  }

  // Get the next symbol
  public String getNextSymbol(String input, int count){

    return input.charAt(count) + "";


  }

  // Get the next state
  public String getNextState(String state, String symbol, String numOfStates, String[] alphabet, String[][] transition){

    for(int x = 0 ; x < Integer.parseInt(numOfStates); x ++){
      for(int y = 0; y < alphabet.length; y++){
        if (state.equals(x+"") && symbol.equals(alphabet[y]+"")){
          return transition[x][y];
        }
      }
    }
    return "";
  }

  // Check if a string is accepted or not
  public void acceptOrRejectString(String numOfStates, String [] fState, String[] alphabet, String[][] transition, String input){

    String state = "0";
    boolean exit = false;

    int count = 0;
    List alphabetList = Arrays.asList(alphabet);
    List fstates = Arrays.asList(fState);

    while(!exit){

      if(count +1 > input.length()){
        break;
      }

      String symbol = getNextSymbol(input, count);
      if(alphabetList.contains(symbol)){

        state = getNextState(state,symbol, numOfStates, alphabet ,transition);
        //System.out.println("state: " + state + "symbol: "+ symbol);
        if(state.equals(numOfStates)){
          exit = true;
          System.out.print("Reject");
        }
        if(count +1 == input.length()){
          if(fstates.contains(state)){
            System.out.print("Accept");
            break;
          }
        }
        if(count +1 == input.length()){
          if(!state.equals(Integer.parseInt(numOfStates)-1)){
            System.out.print("Reject");
          }
        }
      }else{
          exit = true;
          if(fstates.contains(state)){
            System.out.print("Accept");
          }else{
            System.out.print("Reject");
          }
      }
      count++;
    }
  }

  // Collect the info and get ready to pass to a acceptOrRejectString function
  public void stringCheck(ArrayList machine, String input){

    String numOfStates = "";
    String[] finalState = null;
    String[] alphabet = null;
    String[][] transition = null;

    for(int x = 0 ; x < machine.size(); x++){

      if(x == 0){
        numOfStates = machine.get(x).toString();
      }
      if(x == 1){
        finalState = machine.get(x).toString().split(" ");
      }
      if(x == 2){
        alphabet = machine.get(x).toString().split(" ");
        transition = new String[Integer.parseInt(numOfStates)][alphabet.length];
        transition = fillTransition(transition, numOfStates, alphabet);
      }
      if(x >= 3 && x < ((Integer.parseInt(numOfStates) * alphabet.length) + 3)){

          int currentAlphabetIndex = 0;
          String eachTransition = machine.get(x).toString().replace("(","").replace(")","").replace(" ","");
          String currentState = eachTransition.charAt(0) + "";
          String currentAlphabet = eachTransition.charAt(1) + "";
          String nextState = eachTransition.charAt(2) + "";

          for(int y = 0; y < alphabet.length; y++){
            if(alphabet[y].equals(currentAlphabet)){
              currentAlphabetIndex = y;
            }
          }
          transition[Integer.parseInt(currentState)][currentAlphabetIndex] = nextState;
      }
    }

    acceptOrRejectString(numOfStates, finalState, alphabet, transition, input);

  }

  // Prints out exactly as the pdf example
  public void display(){

    int numOfStates=0, totalSymbols=0;

    System.out.println();

    for(int x = 0; x < machines.size(); x++){
      for(int y = 0; y < machines.get(x).size(); y++){
        if(y == 0){
          System.out.println("Finite State Automaton #" + (x + 1));
          System.out.println("1) number of states: " + machines.get(x).get(y));
          numOfStates = Integer.parseInt(machines.get(x).get(y).toString());
        }
        if(y == 1){
          System.out.println("2) final states: " + machines.get(x).get(y));
        }
        if(y == 2){
          totalSymbols = machines.get(x).get(y).toString().replace(" ","").length();
          System.out.println("3) alphabet: " + machines.get(x).get(y));
          System.out.println("4) transitions:");
        }
        if(y >= 3 && y < ((numOfStates * totalSymbols) + 3)){
          String transition = machines.get(x).get(y).toString().replace("(","").replace(")","");
          System.out.println("        " + transition);
        }
        if(y == ((numOfStates * totalSymbols) + 3)){
          System.out.println("5) strings: ");
        }
        if(y >= ((numOfStates * totalSymbols) + 3)){
          System.out.print("        " + machines.get(x).get(y) + "\t\t");
          stringCheck(machines.get(x) , machines.get(x).get(y).toString());
          System.out.println();
        }
      }
      System.out.println();
    }

  }

  public static void main(String args[]){

    // Used the file called machine
    String fileName = "machine.txt";

    // Read file and display the data to screen
    Project1 project1 = new Project1();
    project1.readFile(fileName);
    project1.display();

  }

}
