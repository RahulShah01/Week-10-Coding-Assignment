package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

/*
 * This class is a menu drive application that accepts user input from the console. It performs CRUD
 * operation on the project table.*
 */

public class ProjectsApp {

  private Scanner scanner = new Scanner(System.in);
  private ProjectService projectService = new ProjectService();
  private Project curProject;


  /*
   * This is the list of available operations that can be performed.
   */

  //@formatter:off
  private List<String> operations = List.of(
      "1) Add a project",
      "2) List of projects",
      "3) Select a project"
    );
  //@formatter:off
  
  // The java application entry point. 
  
  public static void main(String[] args) {
   new ProjectsApp().processUserSelections();
     }

/*
 * This methods prints the application operation,  gets user selection and performs operations. 
 */
  private void processUserSelections() {
    boolean done = false;
    
    while(!done) {
      try {
        int selection = getUserSelection();
        switch (selection) {
          case -1:
          done = exitMenu();
          break;
          
          case 1:
            createProject();
            break;
          
          case 2:
            listProjects();
            break;
           
          case 3:
            selectProject();
            break;
          
        default:
          System.out.println("\n" + selection + " is not a valid selection. Try again.");
        }
      }
        catch(Exception e) {
          System.out.println("\nError: " + e + " Try again.");
      }
    }
  }
  
/*
 * This methods sets and displays the current project. 
 */
  
private void selectProject() {
  listProjects();
  Integer projectId = getIntInput("Enter a project ID to select a project");
  
  curProject = null;
  
  curProject = projectService.fetchProjectById(projectId);
  
}

/*
 * This method provides list of all projects, prints the project ID and the names. 
 */

private void listProjects() {
  List<Project> projects = projectService.fetchAllProjects();
  
  System.out.println("\nProjects:");
  
  projects.forEach(project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
  
}

/*
 * The method below collected user input to create a project and then call the project service to create the row. 
 */
  private void createProject() {
    String projectName = getStringInput("Enter the project name");
    BigDecimal estimatedhours = getDecimalInput("Enter the estimated hours");
    BigDecimal actualHours = getDecimalInput("Enter the actual hours");
    Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
    String notes = getStringInput("Enter the project notes");
    
    Project project = new Project();
    
    project.setProjectName(projectName);
    project.setEstimatedHours(estimatedhours);
    project.setActualHours(actualHours);
    project.setDifficulty(difficulty);
    project.setNotes(notes);
    
    Project dbProject = projectService.addProject(project);
    System.out.println("You have successfully created project:" + dbProject);
    
  }

  /*
   * This method gets user input from the console and converts it to BigDecimal. 
   */
  private BigDecimal getDecimalInput(String prompt) {
    
    String input = getStringInput(prompt);
    
    if(Objects.isNull(input)) {
      return null;
    }
    
    try {
      return new BigDecimal(input).setScale(2);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid decimal number.");
    }
    
  }

  /*
   * This method is called when the user wants to exit the application. 
   */
  private boolean exitMenu() {
    System.out.println("\nExiting the menu.");
    return true;
  }

  /*
   * This method prints the available selection option for the user. 
   */
  private int getUserSelection() {
    printOperations();
    
    Integer input = getIntInput("Enter a menu selection");
    
    return Objects.isNull(input) ? -1 : input;
    
  }

  /*
   * This methods prompts the user to input a selection, the converts the input to an integer value. 
   */
  private Integer getIntInput(String prompt) {
    String input = getStringInput(prompt);
    
    if(Objects.isNull(input)) {
      return null;
    }
    
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid number.");
    }
    
  }

  /*
   * This methods prompts the user for an input, if the user input is nothing, nothing is returned,
   * otherwise, the trimmed input is returned. 
   */
  private String getStringInput(String prompt) {
    System.out.println(prompt + ": ");
    String input = scanner.nextLine();
    
    return input.isBlank() ? null : input.trim();
  }

  /*
   * This methods prints menu selection
   */
  private void printOperations() {
    System.out.println("\nThese are the available selections, Press the Enter key to quit:");
    
    operations.forEach(line -> System.out.println(" " + line));
    
    if(Objects.isNull(curProject)) {
      System.out.println("\nYou are not working with a project.");
    }
    else {
      System.out.println("\nYou are working with a project: " + curProject);
    }
  }
}
