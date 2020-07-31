
package visitor;
import syntaxtree.*;
import java.util.*;


public class IR<R> extends GJNoArguDepthFirst<R>{
   //
   // User-generated visitor methods below
   //
  int temp_count = 2000;
  int lab_count=0;
  boolean temp_name=false;
  boolean main = false;
  String args;
  String main_call = "";
  String class_name = "";
  boolean set_class = false;
  boolean rec_class = false;
  String rec_class_name = "";
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

  public R visit(NodeToken n) { return (R)""; }
  public String new_label()
  {
    lab_count++;
    return("LAB" + lab_count);
  }
  public String get_class()
  {
      String classes = (String)Setup.stack.lastElement();
      while(!Setup.all_class.contains(classes))
      {
        classes=Setup.parent_map.get(classes);
      }
      return classes;
  }
  public String new_temp()
  {
    temp_count++;
    return ("TEMP " + temp_count);
  }
  public void debug(String str)
  {
    System.out.println(str);
  }
  
   
   
   // Checklist 
  // 1. Generate all variables - Vtable
  // 2. Generate address for class variables
  // 3. Make sure message send picks up right function
   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }


   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "R"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n) {
      R _ret=(R)"";
      debug("MAIN");
      main=true;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      
      String all = (String)n.f14.accept(this);
      debug(all);
      main=false;
      n.f15.accept(this);
      n.f16.accept(this);
      debug("END \n");
      rec_class = false;
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n) {
      R _ret=(R)"";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n) {
      R _ret=(R)"";
      String cl_name = (String)n.f1.accept(this);
      Setup.addStack(cl_name);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      Setup.remStack();
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n) {
      R _ret=(R)"";
      n.f0.accept(this);
      String cl_name = (String)n.f1.accept(this);
      Setup.addStack(cl_name);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      Setup.remStack();
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n) {

      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      String method_name = (String)n.f2.accept(this);
      String old_name=Setup.fullStack();
    String full_name=old_name+"_";
    full_name+=method_name;
    
    int g = Setup.function_params.get(full_name).size();
    if (g >= 0){
      g++;  
    }
    debug(full_name + " [" + g + "]");
    debug("BEGIN");
      Setup.addStack(full_name);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);

      String all_st = (String)n.f8.accept(this);
      debug(all_st);
      n.f9.accept(this);
      //debug("Hello");
      String ret = (String)n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      debug("RETURN " + ret);
      debug("END");
      Setup.remStack();
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n) {
      R _ret=(R)"";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n) {
      R _ret=(R)"";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n) {
      R _ret=(R)"";
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n) {
      R _ret=(R)"";
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n) {
      R _ret=(R)"";
      n.f0.accept(this);
      _ret = n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n) {
      try{
      R _ret=(R)"";
      temp_name=true;
      //debug("Hello");
      String lhs = (String)n.f0.accept(this);

      String id_name = (String)n.f0.f0.tokenImage;
      String id_scope = Setup.get_scope(id_name);
      id_name = id_scope + "_" + id_name;
      String real_scope = Setup.all_var.get(id_name).scope;
      temp_name=false;
      n.f1.accept(this);
      String rhs = (String)n.f2.accept(this);
      n.f3.accept(this);
      String res ;
      if(real_scope.equals("function_scope") || real_scope.equals("function_arg"))
      {
        res = "\nMOVE " + lhs  + "  " + rhs +"\n";
      }
      else
      {
        String t1 = new_temp();
        String t2 = new_temp();
        String cl_cur = get_class();
        String fin_name;
        if(id_scope.equals(cl_cur))
        {
          fin_name = id_name;
        }
        else
        {
          fin_name = cl_cur+"_"+id_name;
        }
        res = "\nHSTORE " + "TEMP 0 " + 4*(Setup.Vars_Vtable.get(cl_cur).indexOf(fin_name) +1) + "  " + rhs + " ";
        
      }
      return (R)res;
      }
      catch(Exception e )
      {
        //debug("Suhas");
        return (R)"";
      }
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n) {
      R _ret=null;
      temp_name = true;
      String arr = (String)n.f0.accept(this);
      temp_name = false;
      n.f1.accept(this);
      String off = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String val = (String)n.f5.accept(this);
      n.f6.accept(this);
     
      
      String res = "";
      String coff = new_temp();
      res += " MOVE  " + coff + " PLUS 4 " + " TIMES 4 " + off + "\n";
      String temp = new_temp();
      res += " MOVE " + temp + " PLUS " + coff + " " + arr + "\n" +  " HSTORE " + temp + " 0 " + val + "\n";
      return (R)res;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n) {
      R _ret=(R)"";
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(IfthenStatement n) {
      R _ret=(R)"";
      String lab = new_label();
      n.f0.accept(this);
      n.f1.accept(this);
      String expr = (String)n.f2.accept(this);
      n.f3.accept(this);
      String code = (String)n.f4.accept(this);
      
      _ret = (R)("\nCJUMP " + expr + " " + lab + " "+ code + " " + lab + " NOOP   "); 
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfthenElseStatement n) {
      R _ret=(R)"";
      String lab1 = new_label();
      String lab2 = new_label();
      n.f0.accept(this);
      n.f1.accept(this);
      String exp = (String)n.f2.accept(this);
      n.f3.accept(this);
      String st1 = (String)n.f4.accept(this);
      n.f5.accept(this);
      String st2 = (String)n.f6.accept(this);
      String res ="";
      
      res += "CJUMP " + exp + " "+ lab1 + " "+ st1 + " JUMP "+ lab2 + " " + lab1  + " NOOP "+ " " + st2 + " " + lab2 + " NOOP ";  // check for NOOP
      return (R)res;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n) {
      R _ret=(R)"";
      String start = new_label();
      String acc = new_label();
      n.f0.accept(this);
      n.f1.accept(this);
      String cond = (String)n.f2.accept(this);
      n.f3.accept(this);
      String exec = (String)n.f4.accept(this);
      
      String res = "";
      res += start + " NOOP";
      res += " CJUMP " + cond + " " + acc + " " + exec + " " + " JUMP " + start + " " + acc + " NOOP   "; 


      return (R)res;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n) {
      R _ret=(R)"";
      n.f0.accept(this);
      n.f1.accept(this);
      String op = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String res = " PRINT " + op + "\n";
      return (R)res;
   }

   /**
    * f0 -> OrExpression()  //Done 
    *       | AndExpression() //DOne 
    *       | CompareExpression() //DOne
    *       | neqExpression() //Done
    *       | PlusExpression() //Done 
    *       | MinusExpression() // Done 
    *       | TimesExpression() // Done 
    *       | DivExpression() // Done
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n) {
      R _ret=(R)"";
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n) {
      String lab1 = new_label();
      //String lab2 = new_label();
      String result = new_label();
      String t1 = new_temp();
      String _ret=null;
      String e1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String e2 = (String)n.f2.accept(this);
      
      String res="";
      res+= "BEGIN CJUMP " + e1 + " " + lab1 + "\nCJUMP " + e2 + " " +lab1 + " MOVE "+ t1 + " 1 JUMP " + result + " " + lab1 + " MOVE " + t1 + " 0 ";
      res+= result + " NOOP RETURN " + t1 + " END  "; 
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n) {
      String t1 = new_temp();
      String lab1 = new_label();
      String lab2 = new_label();
      String result = new_label();
      String _ret=null;
      String e1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String e2 = (String)n.f2.accept(this);
      String res = "";
      

      res += " BEGIN CJUMP " + e1 + " " + lab1 + "\n MOVE " + t1 + " 1 JUMP " + result + " " + lab1 + " CJUMP " + e2 + " " + lab2 + " MOVE " + t1 + " 1 JUMP " + result;
      res+= " " + lab2 + " MOVE " + t1 + " 0 " + result + " NOOP RETURN " + t1 + " END  ";
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n) {
      String ret_val  = new_temp();
      String lab1 = new_label();
      String fin = new_label();
      String _ret=null;
      String e1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String e2 = (String)n.f2.accept(this);
      String res = "";
      
      res+= " BEGIN CJUMP " + " LE " + e1 + "  " + e2 +" " + lab1 + "\n";
      res += " " + "MOVE " + ret_val + " " + " 1 JUMP " + fin + " "+ lab1 + " NOOP MOVE " + ret_val + " " + " 0 " + fin + " NOOP RETURN " + ret_val + " END \n"; 
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n) {
      String ret_val = new_temp();
      String lab1 = new_label();
      String fin = new_label();
      String _ret=null;
      String e1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String e2 = (String)n.f2.accept(this);
      String res = "";
      
      res += "BEGIN CJUMP " + " NE " + e1 + " " + e2 + " " + lab1 + "\n";
      res += " " + "MOVE " + ret_val + " 1 JUMP " + fin + " " + lab1 + " NOOP MOVE " + ret_val + " " + " 0 " + fin + " NOOP RETURN " + ret_val + " END \n"; 
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n) {
      String _ret=null;
      String r1 = (String)n.f0.accept(this);
      String r2 = (String)n.f1.accept(this);
      String r3 = (String)n.f2.accept(this);
      _ret = "PLUS " + r1 +" " + r3 + "\n";
      return (R)_ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n) {
      String _ret=null;
      String r1 = (String)n.f0.accept(this);
      String r2 = (String)n.f1.accept(this);
      String r3 = (String)n.f2.accept(this);
      _ret = "MINUS " + r1 +" " + r3 + "\n";
      return (R)_ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n) {
      String _ret=null;
      String r1 = (String)n.f0.accept(this);
      String r2 = (String)n.f1.accept(this);
      String r3 = (String)n.f2.accept(this);
      _ret = "TIMES " + r1 +" " + r3 +"\n";
      return (R)_ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n) {
      String _ret=null;
      String r1 = (String)n.f0.accept(this);
      String r2 = (String)n.f1.accept(this);
      String r3 = (String)n.f2.accept(this);
      _ret = "DIV " + r1 +" " + r3 +"\n";
      return (R)_ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n) {
      String ret = new_temp();
      String coff = new_temp();
      String temp = new_temp();
      String _ret=null;
      String arr = (String)n.f0.accept(this);
      n.f1.accept(this);
      String off = (String)n.f2.accept(this);
      n.f3.accept(this);
      
      String res = "";
      res += "BEGIN " + "MOVE " + temp + " PLUS 4 " + " TIMES 4 " + off + "\n" + "MOVE " + coff + " PLUS " + temp + " " + arr + "\n" + " HLOAD " + ret + " " + coff + " 0 " + " RETURN " + ret + " END \n";
      //debug(res);
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n) {
      String _ret=null;
      String arr = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String res ="";
      String len = new_temp();
      res += " BEGIN ";
      res += " HLOAD " + len + " " + arr + " 0 RETURN " + len + " END\n";
      return (R)res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n) {
      String _ret=null;
      //debug( " " + rec_class);
      String res = "";
      //debug("Hello");
      set_class = true;
      String caller = (String)n.f0.accept(this);
      set_class = false;
      String store_class = class_name;
      if(caller.equals("TEMP 0") )
      {
        caller = "TEMP 0";
        class_name = get_class();
        //debug(class_name + "LALALA");
      }
      n.f1.accept(this);
      String f_name = (String)n.f2.f0.tokenImage;
      n.f3.accept(this);
       
      //String args =" ";
      args="";  
      args = (String)n.f4.accept(this);
      n.f5.accept(this);

      String fn_origin;
      String full_class;
      if(!main)
      {
          
          if(caller.equals("TEMP 0"))
          {
            fn_origin = store_class;
            full_class = get_class();

          }
          else if(!rec_class)
          {
            //debug(class_name + "lalalla");
            String scope1 = Setup.get_scope(store_class);
            //debug(caller + "###" + class_name);
            String gg = scope1 + "_"+ store_class;
            //debug(Setup.all_var.get(gg).type);
            fn_origin = Setup.all_var.get(gg).type;
            //debug( " DONE MESSED UP " + gg);
            //debug(fn_origin);
            full_class = fn_origin;
            //debug ( scope1 );
            //debug(store_class);
            //debug(fn_origin);
          }
          else
          {
            full_class = rec_class_name;
            fn_origin = rec_class_name;
          }
          //debug(fn_origin + "lalalla");
      }
      else
      {
          fn_origin = main_call;
          full_class = main_call;
      }
      //debug(" finaa" + fn_origin + " " + main);
      String obj = new_temp();
      String vpoint = new_temp();
      String fp = new_temp();
      String fin = new_temp();
      // Assign caller to obj
      
      String fn_full;
      if(main == true)
      {
          fn_full = fn_origin + "_" + f_name;
      }
      else if(caller.equals("TEMP 0") )
      {
        Vector v = Setup.Vtable.get(full_class);
        String cl = get_class();
        for(int i=0;i<v.size();i++)
        {
          String lol = (String)v.get(i);
          String [] spl = lol.split("_",2);
          if(spl[1].equals(f_name))
          {
            cl=spl[0];
          }
        }
        fn_full = cl+ "_" + f_name;
      }
      else
      {
        if(!rec_class)
        {
        String find_class = Setup.find_func(fn_origin,f_name);
        if(find_class.equals(fn_origin))
        {
          fn_full = fn_origin + "_"+f_name; 
        }
        else
        {
          fn_full = find_class + "_" + f_name;
        }
        }
        else
        {
          Vector v = Setup.Vtable.get(rec_class_name);
          String cl = rec_class_name;
          for(int i=0;i<v.size();i++)
          {
            String lol = (String)v.get(i);
            String [] spl = lol.split("_",2);
            if(spl[1].equals(f_name))
            {
              cl=spl[0];
            }
          }
          fn_full = cl+ "_" + f_name;
        }
        //debug(fn_full + " ########## ");
      }
      //debug(full_class + "#######" + fn_full + "#######" + rec_class_name);
      int fn_index ;
      if(!main)
      {
          fn_index = Setup.Vtable.get(full_class).indexOf(fn_full);
      }
      else
      {
          fn_index =  Setup.Vtable.get(fn_origin).indexOf(fn_full);
      }
      try{
      if(Setup.all_class.contains((String)Setup.all_var.get(fn_full).ret_type))
      {
        //debug((String)Setup.all_var.get(fn_full).ret_type);
        rec_class = true;
        rec_class_name = ((String)Setup.all_var.get(fn_full).ret_type);
      }
      else
      {
        rec_class = false;
      }
      }
      catch(Exception e)
      {
        rec_class = false;
      }

      res+= "BEGIN MOVE " + obj + " " + caller + "\n" + " HLOAD " + vpoint + " " + obj + " 0 \n" + " HLOAD " + fp + " "+ vpoint+ " " + 4*fn_index + " \n" +
      " RETURN " + " CALL "+ fp + " ( " + obj + "  " + args + " ) "  + " END";

      return (R)res;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n) {
      String _ret="";
      args = " ";
      _ret=(String)n.f0.accept(this);
      //debug(_ret);
      String lol = (String)n.f1.accept(this);
      
      //debug(args);
      return (R)(_ret+"   " +lol);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n) {
      String _ret=",";
       n.f0.accept(this);

        String exp =(String) n.f1.accept(this);

        args+= "   " + exp;
        
      return (R)(" " + exp);
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public R visit(PrimaryExpression n) {
      String _ret=null;
      if(!main)
      {
      temp_name=true;
      }
      _ret = (String)n.f0.accept(this);
      temp_name=false;
      return (R)_ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
     
      return (R)n.f0.tokenImage;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)(1+"");
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)(0+"");
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n) {
      String _ret=null;
      String name = (String)n.f0.accept(this);
      name = n.f0.tokenImage;
      if(set_class)
      {
        class_name = name;
      }
      if(main)
      {
        main_call = name;
        return (R)name;
      }
      else if( temp_name)
      {
         String id_scope = Setup.get_scope(name);
         
         String full_name = id_scope + "_" + name;
         String id_cur_scope = Setup.all_var.get(full_name).scope;
         //Setup.Var id_obj = new Setup.Var();
         //debug(Setup.all_var.get(full_name).scope + " Object check");
        if(id_cur_scope.equals("function_arg"))
        {
          _ret = "TEMP " + (Setup.all_var.get(full_name).ind);
        }
        else
        {
          if(id_cur_scope.equals("function_scope"))
          {
            _ret = "TEMP " + Setup.all_var.get(full_name).ind;
          }
          else
          {
            _ret = "BEGIN ";
            String t1 = new_temp();
            String cur_class = get_class();

            //debug(id_scope + " ___ "+ cur_class + "_________" + name);
            if(id_scope.equals(cur_class))
            {
            _ret += "HLOAD " + t1;
            _ret += " TEMP 0 " + (Setup.Vars_Vtable.get(cur_class).indexOf(full_name)+1)*4 +" RETURN " + t1 + " END \n ";
              
            }
            else
            {
               _ret += "HLOAD " + t1;
               _ret += " TEMP 0 " + (Setup.Vars_Vtable.get(cur_class).indexOf(cur_class+"_"+full_name)+1)*4 +" RETURN " + t1 + " END \n ";
              
            }
            
          }
        }
        return (R)_ret;
        //debug(name + " Error");
        //return name;
      }
      else
      {
        return (R)name;
      }
      //return _ret;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)"TEMP 0";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n) {
      String ptr = new_temp();
      String v1 = new_temp();
      String v2 = new_temp();
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String size = (String)n.f3.accept(this);
      n.f4.accept(this);
      String res = "";
      
      res += " BEGIN \n";
      res += " MOVE " + v1 + " PLUS " + 1000 + " " + 2 + "\n";
      res += " MOVE " + v2 + " TIMES " + v1 + " 4 " + "\n" + " MOVE " + ptr + " HALLOCATE " + v2 + "\n" + " HSTORE " + ptr + " 0 " + size + "\n";
      for(int i=1;i<200;i++)
      {
       res += " HSTORE " + ptr + " " + (4*i) + " 0 " + "\n";
      }
      res += " RETURN " + ptr + " END \n";
      //debug(res);
      return (R)res;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n) {
      String _ret=null;

      n.f0.accept(this);
      temp_name=false;
      String cl_name = (String)n.f1.f0.tokenImage;
      if(main==true)
      {
        main_call = cl_name;
      }

      n.f2.accept(this);
      n.f3.accept(this);
      int vtable_size = 1 + Setup.Vars_Vtable.get(cl_name).size();
      int zero_size = Setup.Vtable.get(cl_name).size();
      String fspace = new_temp();
      String var_space = new_temp();
      String res = "";

      res+= "BEGIN MOVE " + var_space + " HALLOCATE " + (vtable_size)*4 + " \n";  // Setup initial v table for all
      Vector v = Setup.Vtable.get(cl_name);
      

      res+= " MOVE " + fspace + " HALLOCATE " + (zero_size)*4 + "\n";
      for (int i=0 ; i<v.size() ; i++)
      {
          res += " HSTORE " + fspace + " " + 4*i + " " + v.get(i) +"\n"; 
      }
      for(int i=1;i<vtable_size;i++)
      {
        res+= " HSTORE " + var_space + "  " + 4*i + " 0 ";
      }
      res += " HSTORE " + var_space + " 0 " + fspace + "\n" + " RETURN " + var_space + " END \n";
      String r1 = (String)((R)res);
      

      return (R)res;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n) {
      String _ret=null;
      n.f0.accept(this);
      String e1 = (String)n.f1.accept(this);
      String temp = new_temp ();
      String lab1 = new_label();
      String fin = new_label();
      String res = "";
      res += " BEGIN " + " CJUMP " + e1 + "  " + lab1 + " MOVE " + temp + " 0  JUMP " + fin + " " + lab1 + " MOVE " + temp + " 1 " + fin + " NOOP RETURN " + temp + " END\n";  
      return (R)res;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n) {
      String _ret=null;
      n.f0.accept(this);
      _ret = (String)n.f1.accept(this);
      n.f2.accept(this);
      return (R)_ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public R visit(IdentifierList n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public R visit(IdentifierRest n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

}

