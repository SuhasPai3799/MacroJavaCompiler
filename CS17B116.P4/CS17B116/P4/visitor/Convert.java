//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class Convert<R> implements GJNoArguVisitor<R> {
   int temp_ctr=1000;
   int lab_ctr = 1000;
   boolean st_exp  = false;
   boolean print_lab = false;
   boolean main = false;
   String ret_exp = "";
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n) {
      R _ret=(R)"";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         String gg = (String)_ret;
         gg += (String)e.nextElement().accept(this);
         _ret = (R)gg;
         _count++;
      }
      return (R)_ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
          R _ret=(R)"";
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            String gg = (String)_ret;
         gg += (String)e.nextElement().accept(this);
         _ret = (R)gg;
            _count++;
         }
         return (R)_ret;
      }
      else
         return (R)"";
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return (R)"";
   }

   public R visit(NodeSequence n) {
      R _ret=(R)"";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         String gg = (String)_ret;
         gg += (String)e.nextElement().accept(this);
         _ret = (R)gg;
         _count++;
      }
      return (R)_ret;
   }

   public R visit(NodeToken n) { return (R)n.tokenImage; }
   public String new_temp()
   {
      temp_ctr++;
      return ( "TEMP " + temp_ctr);

   }
   public String new_lab()
   {
      lab_ctr++;
      return( "LAB " +lab_ctr);
   }
   public void debug(String str)
   {
      System.out.println(str);
   }
   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      String res = "";
      debug( " MAIN ");
      n.f0.accept(this);
      main = true;
      n.f1.accept(this);
      main = false;
      debug ( " END ");
      n.f2.accept(this);
      //debug(res);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
      print_lab = true;
      n.f0.accept(this);
      print_lab = false;
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n) {
      R _ret=null;
      String res = "";
      res += (String)n.f0.f0.tokenImage;
      res += " [ ";
      n.f1.accept(this);
      
      res += (String)n.f2.f0.tokenImage;
      res += " ] \n";
      n.f3.accept(this);
      debug(res);
      debug( " BEGIN ");
      n.f4.accept(this);
      debug( "RETURN " + ret_exp);
      debug( " END ");
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      debug( " NOOP ");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      debug( " ERROR ");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Exp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      String res = " CJUMP ";
      n.f0.accept(this);
      res += (String) n.f1.accept(this) + " ";
      debug(res);
      n.f2.accept(this);
      
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      String res = " JUMP ";
      n.f0.accept(this);
      debug(res);
      n.f1.accept(this);
      
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Exp()
    * f2 -> IntegerLiteral()
    * f3 -> Exp()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      String res = " HSTORE ";
      n.f0.accept(this);
      res += (String)n.f1.accept(this);
      res += " " + (String)n.f2.f0.tokenImage;
      res += (String) n.f3.accept(this);
      debug(res);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Exp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      String res = " HLOAD ";
      n.f0.accept(this);
      res += " " + (String)n.f1.accept(this);
      res += " " + (String)n.f2.accept(this);
      res += " " + (String)n.f3.f0.tokenImage;
      debug(res);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      String res = " MOVE " ;
      n.f0.accept(this);
      res += " " + n.f1.accept(this);
      res += " " + n.f2.accept(this);
      debug(res);
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> Exp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      String res = " PRINT " ;
      n.f0.accept(this);
      res += (String) n.f1.accept(this);
      debug(res); 
      return _ret;
   }

   /**
    * f0 -> StmtExp()
    *       | Call()
    *       | HAllocate()
    *       | BinOp()
    *       | Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(Exp n) {
      R _ret=null;
      String res = "";
      st_exp=true;
      print_lab = true;
      res += " " +(String)n.f0.accept(this);
      print_lab = false;
      st_exp = false;
      return (R)res;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> Exp()
    * f4 -> "END"
    */
   public R visit(StmtExp n) {
      R _ret=null;

      String res = "";
      if(true)
      {
         n.f0.accept(this);
         n.f1.accept(this);
         n.f2.accept(this);
         String t1 = new_temp ();

         res += (String)n.f3.accept(this);
         ret_exp  =  " MOVE " + t1 + " " + res;
         debug (ret_exp);
         ret_exp = t1;
         res = t1;

      }
      else
      {
         n.f1.accept(this);
         res = (String)n.f3.accept(this);
         debug( " RETURN " + res);
      }
      //res += " " n.f4.accept(this);
      return (R)res;
   }

   /**
    * f0 -> "CALL"
    * f1 -> Exp()
    * f2 -> "("
    * f3 -> ( Exp() )*
    * f4 -> ")"
    */
   public R visit(Call n) {
      R _ret=null;
      String res = " CALL ";
      n.f0.accept(this);
      res += " " + (String)n.f1.accept(this);
      res += " ( " ;
      n.f2.accept(this);
      res += (String)n.f3.accept(this);
      res += " ) " ;
      n.f4.accept(this);
      String t1 = new_temp();
      debug ( " MOVE " + t1 + " " + res);
      return (R)t1;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> Exp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      String res = "";
      String t1 = new_temp();
      String t2;
      res += (String)n.f0.accept(this);
      t2 = (String)n.f1.accept(this);
      debug( " MOVE " + " " + t1 + " " + t2);
      res = " HALLOCATE " + t1 + "\n";
      return (R)res;
   }

   /**
    * f0 -> Operator()
    * f1 -> Exp()
    * f2 -> Exp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      String res = "";
      String t1 ;
      String t2 ;
      res += (String)n.f0.accept(this);
      t1 = (String)n.f1.accept(this);
      t2 = (String)n.f2.accept(this);
      String t3 = new_temp();
      String t4 = new_temp();
      debug ( " MOVE " + t3 + " " + t1);
      debug ( " MOVE " + t4 + " " + t2);
      String t5 = new_temp();
      debug( " MOVE " + " " + t5 + " " + res + " " + t3 + " " + t4);
      res = t5;
      return (R)res;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n) {
      R _ret=null;
     
      return (R) n.f0.accept(this);
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n) {
      R _ret=null;
      String res = " TEMP ";
      n.f0.accept(this);
      res += (String)n.f1.f0.tokenImage;
      return (R)res;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      String t1 = new_temp();
      debug( " MOVE " + t1 + " "  + n.f0.tokenImage);
      return (R)t1;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      if(print_lab)
      {
         String t1 = new_temp();
         debug( " MOVE " + t1 + " " + n.f0.tokenImage);
         return (R)t1;
      }
      debug((String)n.f0.tokenImage);
      return (R)n.f0.accept(this);
   }

}
