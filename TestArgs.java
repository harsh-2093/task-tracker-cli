public class TestArgs {
  public static void main(String[] args) {
    System.out.println("number of arguments: "+args.length);
    for(int i=0;i<args.length;i++)
    {
      System.out.println("Argument "+i+"is: "+args[i]);
    }
  }
}
