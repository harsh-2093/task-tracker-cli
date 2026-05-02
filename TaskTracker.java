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
        case "update":
          // We need 3 things now: ID, Field, and the New Value
          if (args.length > 3) {
            String idToUpdate = args[1];
            String field = args[2];
            String newValue = args[3];
            
            handleMasterUpdate(idToUpdate, field, newValue);
          } else {
            System.out.println("Error: Missing arguments.");
            System.out.println("Usage: java TaskTracker update <id> <status/description> \"new value\"");
          }
          break;
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
        case "delete":
          if (args.length > 1) {
                handleDeleteTask(args[1]);
          }else{
            System.out.println("Error: Missing ID. Usage: java TaskTracker delete <id>");
          }
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

  public static void handleDeleteTask(String targetIdStr) throws IOException{
    String content=Files.readString(Paths.get(FILE_PATH));

    if(content.equals("[]")||content.isBlank()){
      System.out.println("No tasks to delete.");
      return;
    }

    String cleaned =content.substring(1,content.length()-1);
    String [] taskStrings=cleaned.split("\\},");

    StringBuilder newJson=new StringBuilder("[");
    boolean taskFound=false;
    boolean isFirstTaskToKeep=true;

    for(String taskstr:taskStrings){

      if(!taskstr.endsWith("}"))
      {
        taskstr+="}";
      }

      String currentId=extractValue(taskstr,"id");

      if(currentId.equals(targetIdStr))
      {
        taskFound=true;
        continue;
      }

      if(!isFirstTaskToKeep)
      {
        newJson.append(",");
      }
      newJson.append(taskstr);
      isFirstTaskToKeep=false;
    }
    newJson.append("]");

    if(taskFound){
      Files.writeString(Paths.get(FILE_PATH), newJson.toString());
      System.out.println("Task " + targetIdStr + " deleted successfully.");
    }
    else {
      System.out.println("Task ID " + targetIdStr + " not found.");
    }
    }

    public static void handleMasterUpdate(String targetIdStr,String fieldToUpdate,String newValue) throws IOException
    {
      String content = Files.readString(Paths.get(FILE_PATH));
      
      if (content.equals("[]") || content.isBlank()) {
      System.out.println("No tasks to update.");
      return;
      }

      fieldToUpdate = fieldToUpdate.toLowerCase();
      if (!fieldToUpdate.equals("status") && !fieldToUpdate.equals("description")) {
      System.out.println("Error: Invalid field. You can only update 'status' or 'description'.");
      return;
      }
      String cleaned = content.substring(1, content.length() - 1);
      String[] taskStrings = cleaned.split("\\},");

    StringBuilder newJson = new StringBuilder("[");
    boolean taskFound = false;
    boolean isFirstTaskToKeep = true;
    for (String taskStr : taskStrings) {
      if (!taskStr.endsWith("}")) {
        taskStr += "}";
      }

      String currentId = extractValue(taskStr, "id");

      if (currentId.equals(targetIdStr)) {
        taskFound = true;

        String oldValue = extractValue(taskStr, fieldToUpdate);

        String targetToReplace = "\"" + fieldToUpdate + "\":\"" + oldValue + "\"";
        String replacement = "\"" + fieldToUpdate + "\":\"" + newValue + "\"";

        String updatedTaskStr = taskStr.replace(targetToReplace, replacement);

        if (!isFirstTaskToKeep) {
            newJson.append(",");
        }
        newJson.append(updatedTaskStr);
        isFirstTaskToKeep = false;
        continue; 
      }

      // NO MATCH: Keep the task exactly as it was
      if (!isFirstTaskToKeep) {
        newJson.append(",");
      }
      newJson.append(taskStr);
      isFirstTaskToKeep = false;
    }

    newJson.append("]");

    if (taskFound) {
      Files.writeString(Paths.get(FILE_PATH), newJson.toString());
      System.out.println("Task " + targetIdStr + " successfully updated (" + fieldToUpdate + " -> " + newValue + ").");
    } else {
      System.out.println("Task ID " + targetIdStr + " not found.");
    }

    }

}
