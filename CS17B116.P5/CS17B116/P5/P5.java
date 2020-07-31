import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         LabelMap vis = new LabelMap();
         Liveness vis1 = new Liveness();
         OP vis2 = new OP();
         root.accept(vis); // Your assignment part is invoked here.
         root.accept(vis1);
         root.accept(vis2);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 