import syntaxtree.*;
import visitor.*;

public class B1 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         Setup parse1 = new Setup();
         AddAll parse4 = new AddAll();
         LLVM parse2 = new LLVM();
         MAIN parse3 = new MAIN();

         root.accept(parse1);
         root.accept(parse4);
         root.accept(parse2);
         root.accept(parse3);
         
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



