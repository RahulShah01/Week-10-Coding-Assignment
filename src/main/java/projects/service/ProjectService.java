package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {

  private ProjectDao projectDao = new ProjectDao();

  /*
   * This methods called the DAO class to insert a project row.
   */
  public Project addProject(Project project) {
    return projectDao.insertProject(project);
  }

  /*
   * This method calls the project DAO to gather all project rows without the details and returns a
   * list of project records
   */
  public List<Project> fetchAllProjects() {
    return projectDao.fetchAllProjects();
  }

  /*
   * This method calls the project DAO to get all project details using the project ID. If the
   * project ID is invalid, it will throw an exception.
   */
  public Project fetchProjectById(Integer projectId) {
    return projectDao.fetchProjectbyId(projectId).orElseThrow(() -> new NoSuchElementException(
        "Project with project ID=" + projectId + " does not exist."));

  }
}
