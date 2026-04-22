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

      switch(command.toLowerCase()){
        case "add":
          if(args.length>1){
            handleAddTask(args[1]);
          }else{
            System.out.println("Error: Missing description for add command.");
          }
          break;
        case "list":
          String filter=(args.length>1)?args[1]:null;
          handleListTasks(filter);
          break;
        default:
          System.out.println("Unknown command. Try 'add' or 'list'.");
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

  public static void handleListTasks(String filter) throws IOException{
    String content=Files.readString(Paths.get(FILE_PATH));

    if(content.equals("[]") ||content.isBlank()){
      System.out.println("No task Found.");
      return;
    }

    String cleaned=content.substring(1,content.length()-1);

    String [] taskStrings=cleaned.split("\\},");

    System.out.println("ID  | Status      | Description");
    System.out.println("-----------------------------------");

    for(String taskStr:taskStrings)
    {
      if(!taskStr.endsWith("}")){
        taskStr+="}";
      }

      String id=extractValue(taskStr,"id");
      String desc=extractValue(taskStr,"description");
      String status=extractValue(taskStr,"status");

      if(filter ==null ||status.equals(filter))
      {
        System.out.printf("%-3s | %-11s | %s%n", id, status, desc);
      }
    }
  }

  public static String extractValue(String json,String key){
    String search="\""+key+"\":";
    int start=json.indexOf(search);
    if(start==-1)return "";

    start+=search.length();

    if(json.charAt(start)=='"'){
      int end=json.indexOf("\"",start+1);
      return json.substring(start+1,end);
    }else{
      int endComma=json.indexOf(",",start);
      int endBrace=json.indexOf("}",start);

      int end = (endComma != -1 && endComma < endBrace) ? endComma : endBrace;
        return json.substring(start, end).trim();
    }




    }

}
