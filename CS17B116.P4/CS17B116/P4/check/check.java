import syntaxtree.*;
import visitor.*;

public class check {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         
         GJNoArguDepthFirst vis = new GJNoArguDepthFirst();
         root.accept(vis); // Your assignment part is invoked here.
         System.out.println("Program parsed successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 

