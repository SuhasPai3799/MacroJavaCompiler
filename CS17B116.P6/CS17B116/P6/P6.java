import syntaxtree.*;
import visitor.*;

public class P6 {
   public static void main(String [] args) {
      try {
         Node root = new MiniRAParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         MIPS vis = new MIPS();
         
         root.accept(vis); // Your assignment part is invoked here.
         
      }
      catch (ParseException e) {
         
         ;//System.out.println(e.toString());
      }
   }
} 