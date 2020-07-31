import syntaxtree.*;
import visitor.*;

public class P3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         Setup parse1 = new Setup();
         IR parse2 = new IR();
         root.accept(parse1);
         root.accept(parse2);
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



