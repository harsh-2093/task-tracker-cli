import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaskTracker {
  private static final String FILE_PATH="task.json";


  public static void main(String[] args) {
    if(args.length<1)
    {
      System.out.println("Usage: java TaskTracker <command> [arguments]");
      return;
    }

    String command=args[0];

    try{
      ensureFileExists();

      if(command.equalsIgnoreCase("add")&& args.length>1){
        handleAddTask(args[1]);
      }else{
        System.out.println("Unknown command or missing description.");
      }
    }catch(Exception e){
      System.out.println("Error: " + e.getMessage());
    }

  }

  public static void ensureFileExists() throws IOException{
    Path path=Paths.get(FILE_PATH);
    if(!Files.exists(path))
    {
      Files.writeString(path,"[]");
    }
  }

  public static void handleAddTask(String description) throws IOException{
    String content =Files.readString(Paths.get(FILE_PATH));

    int id=(int)(System.currentTimeMillis()%1000);

    Task newTask=new Task(id, description);
    String taskJson=newTask.toJson();

    String UpdatedContent;
    if(content.equals("[]")){
      UpdatedContent="["+taskJson+"]";
    }
    else
    {
      UpdatedContent=content.substring(0,content.length()-1)+","+taskJson+"]";
    }

    Files.writeString(Paths.get(FILE_PATH),UpdatedContent);
    System.out.println("Task added successfully (ID: " + id + ")");

  }
}
