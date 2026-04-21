import java.time.LocalDateTime;

public class Task {
  private int id;
  private String description;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Task(int id, String description) {
    this.id = id;
    this.description = description;
    this.status="todo";
    this.createdAt=LocalDateTime.now();
    this.updatedAt=LocalDateTime.now();
  }

  public String toJson(){
    return String.format(
            "{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
            id, description, status, createdAt, updatedAt
        );
  }
  
}
