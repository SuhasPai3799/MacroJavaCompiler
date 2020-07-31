
package visitor;
import syntaxtree.*;
import java.util.*;


public class AddAll implements GJNoArguVisitor<String> {
   //
   // User-generated visitor methods below
   //
  int temp_count = 200;
  int lab_count=0;
  int tc = 200;
  int lc = 0;
  boolean look_up=false;
  boolean main = false;
  String args;
  String main_call = "";
  String class_name = "";
  String curs = "";
  String cur_func = "";
  boolean set_class = false;
  boolean op_vars = false;

  public static HashMap<String,String> type_map =  new HashMap<String,String>();
  public static HashMap<String,Vector> func_arg= new HashMap<String,Vector> ();
  public static HashMap<String,String> func_ret_type = new HashMap<String,String> (); 
  public String new_label()
  {
    lab_count++;
    return("lab" + lab_count);
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
  public String all_types(String str)
  {
    String res = "";
    Vector v = Setup.Vars_Vtable.get(str);
    for(int i = 0 ; i < v.size() ; i++)
    {
      String gg = (String)v.get(i);
      String type_cur  = (String)(Setup.all_var.get(gg).type);
      if(type_cur.equals("int"))
      {
        res += "i32";
      }
      else if(type_cur.equals("int []"))
      {
        res += "[100 x i32]";
      }
      else if(type_cur.equals("boolean"))
      {
        res += "i1";
      }
      else
      {
        res += "i8*";
      }
      if(i!=v.size()-1)
        res += " ,";
    }
    return res;
  }
  public void debug(String str)
  {
    System.out.println(str);
  }
  public String get_temp()
  {
      tc ++;
      return ("%t"+tc);
  }

   //
   // Auto class visitors--probably don't need to be overridden.
   //
   // Checklist 
  // 1. Generate all variables - Vtable
  // 2. Generate address for class variables
  // 3.
   public String visit(NodeList n) {
      String _ret="";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         String t = e.nextElement().accept(this);
         _ret += t;
         _count++;
      }
      return _ret;
   }

   public String visit(NodeListOptional n) {
      if ( n.present() ) {
         String _ret="";
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            String t = e.nextElement().accept(this);
            _ret += t;
            _count++;
         }
         return _ret;
      }
      else
         return "";
   }

   public String visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return "";
   }

   public String visit(NodeSequence n) {
      String _ret="";
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         String t = e.nextElement().accept(this);
         _ret += t;
         _count++;
      }
      return _ret;
   }

   public String visit(NodeToken n) { return ""; }
  
   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public String visit(Goal n) {
      String _ret=null;
      type_map.put("int","i32");
      type_map.put("int []","[100 x i32]");
      type_map.put("boolean","i1");


      String str = "declare i32 @printf(i8*, ...) nounwind \n" + "@.textstr = internal constant [4 x i8] " + "c\"%d\\0a\\00\"\n";
      //debug(str);
      //n.f0.accept(this);
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
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public String visit(MainClass n) {
      String _ret=null;
      //debug("MAIN");
      main=true;
      n.f0.accept(this);
      String str = n.f1.accept(this);
      // if(str.contains("HT3"))
      // {
      //   System.exit(0);
      // }
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
      
      String all = n.f14.accept(this);
      //debug(all);
      main=false;
      n.f15.accept(this);
      n.f16.accept(this);
      //debug("END \n");
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public String visit(TypeDeclaration n) {
      String _ret=null;
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
   public String visit(ClassDeclaration n) {
      String _ret=null;
      String cl_name = n.f1.accept(this);
      curs = cl_name;
      Setup.addStack(cl_name);
      String str = all_types(cl_name);
      //debug("%"+ cl_name + " = type {" + str + "}" );
      n.f2.accept(this);
      //n.f3.accept(this);
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
   public String visit(ClassExtendsDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      String cl_name = n.f1.accept(this);
      curs = cl_name;
      Setup.addStack(cl_name);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      //n.f5.accept(this);
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
   public String visit(VarDeclaration n) {
      String _ret=null;
      //n.f0.accept(this);
      String type = n.f0.accept(this);
      String id = n.f1.accept(this);
      // if(op_vars)
      // {
      // if(type.equals("int []"))
      // {
      //   debug("%"+id +"= " + "alloca i32,i32 100");
      // }
      // else if(type.equals("int"))
      // {
      //   debug("%"+id + "= alloca i32");
      // }
      // else if(type.equals("boolean"))
      // {
      //   debug("%"+id + "= alloca i1");
      // }
      // else
      // {
      //   debug("%"+id + "= alloca %"+type);
      // }
      // } 
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
   public String visit(MethodDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      String res = "define ";
      String type = n.f1.accept(this);
      if(type.equals("class"))
        ;
      else if(type.equals("int") || type.equals("int []") || type.equals("boolean"))
        type = (String)type_map.get(type);
      else
        type = "i8*";
      res += type;

      String method_name = n.f2.accept(this);
      cur_func = curs + "_"+ method_name;
      func_arg.put(cur_func,new Vector());
      func_ret_type.put(cur_func,type);
      res += " @" + curs + "_" + method_name;
      String old_name=Setup.fullStack();
      String full_name=old_name+"_";
      full_name+=method_name;
      
    int g = Setup.function_params.get(full_name).size();
    if (g >= 0){
      g++;  
    }
    //debug(full_name + " [" + g + "]");
    //debug("BEGIN");
      //Setup.addStack(full_name);
      n.f3.accept(this);
      func_arg.get(cur_func).add("i8*");
      String gg = n.f4.accept(this);
      
      
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public String visit(FormalParameterList n) {
      String _ret="";
      String type = n.f0.accept(this);
      
      _ret += type;
      _ret += n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n) {
      String _ret = "";
      String type = n.f0.accept(this);
      if(type.charAt(0)=='i' ||  type.charAt(0)=='b')
      {
        type = (String)type_map.get(type);
      }
      else
      {
        type = "i8*";
      }
      func_arg.get(cur_func).add(type);
      _ret += type;
      _ret += " %" + n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterRest n) {
      String _ret="";
      _ret += ",";
      _ret += n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n) {
      String _ret=null;
      _ret = n.f0.accept(this);
      if(_ret.equals("int") || _ret.equals("int []") || _ret.equals("boolean"))
        return _ret;
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(ArrayType n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return "int []";
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n) {
      String _ret=null;
      n.f0.accept(this);
      return "boolean";
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n) {
      String _ret=null;
      n.f0.accept(this);
      return "int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n) {
      String _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public String visit(Block n) {
      String _ret=null;
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
   public String visit(AssignmentStatement n) {
      String _ret=null;
      look_up=true;
      //String lhs = n.f0.accept(this);

      String id_name = n.f0.f0.tokenImage;
      String id_scope = Setup.get_scope(id_name);

      id_name = id_scope + "_" + id_name;

      String real_scope = Setup.all_var.get(id_name).scope;
      String type = Setup.all_var.get(id_name).type;

      look_up=false;
      n.f1.accept(this);
      String rhs = n.f2.accept(this);

      n.f3.accept(this);
      String res ;
      
      if(real_scope.equals("function_scope") || real_scope.equals("function_arg"))
      {
        res = "";
        if(type.equals("int"))
        {
          res += "store i32 "   + rhs + ", i32* %"+n.f0.f0.tokenImage ;
          //debug(res);
        }
        else if(type.equals("boolean"))
        {
          res += "store i1 "   + rhs + ", i1* %"+n.f0.f0.tokenImage ;
          //debug(res);
        }
        else if(type.equals("int []"))
        {
          ;
        }
        else
        {
          res += "store %"+type + " " + rhs + ", %"+type+"* %" + n.f0.f0.tokenImage;
          //debug(res);
        }
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
      return res;
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
   public String visit(ArrayAssignmentStatement n) {
      String _ret=null;
      look_up = true;
      String arr = n.f0.accept(this);
      look_up = false;
      n.f1.accept(this);
      String off = n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String val = n.f5.accept(this);
      n.f6.accept(this);
      String temp = new_temp();
      String coff = new_temp();
      String res = "";
      res += " MOVE  " + coff + " PLUS 4 " + " TIMES 4 " + off + "\n";
      res += " MOVE " + temp + " PLUS " + coff + " " + arr + "\n";
      res += " HSTORE " + temp + " 0 " + val + "\n";
      return res;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public String visit(IfStatement n) {
      String _ret=null;
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
   public String visit(IfthenStatement n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String expr = n.f2.accept(this);
      n.f3.accept(this);
      String code = n.f4.accept(this);
      String lab = new_label();
      _ret = "\nCJUMP " + expr + " " + lab + " "+ code + " " + lab + " NOOP   "; 
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
   public String visit(IfthenElseStatement n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String exp = n.f2.accept(this);
      n.f3.accept(this);
      
      n.f5.accept(this);
      
      String res ="";
      String lab1 = new_label();
      String lab2 = new_label();
      String lab3 = new_label();
      String tt = get_temp();
      res = "br i1 " + exp + ", label %" + lab1 + ", label %" + lab2;
      
      return res;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public String visit(WhileStatement n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String cond = n.f2.accept(this);
      n.f3.accept(this);
      String exec = n.f4.accept(this);
      String start = new_label();
      String acc = new_label();
      String res = "";
      res += start + " NOOP";
      res += " CJUMP " + cond + " " + acc + " " + exec + " " + " JUMP " + start + " " + acc + " NOOP   "; 


      return res;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public String visit(PrintStatement n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String op = n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String res = "PRINT " + op + "\n";
      return res;
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
   public String visit(Expression n) {
      String _ret=null;
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public String visit(AndExpression n) {
      String _ret=null;
      String e1 = n.f0.accept(this);
      n.f1.accept(this);
      String e2 = n.f2.accept(this);
      String lab1 = new_label();
      //String lab2 = new_label();
      String result = new_label();
      String tt = get_temp();
      String res=tt + "= and i1 " + e1 + " , " + e2;
      //ug(res);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public String visit(OrExpression n) {
      String _ret=null;
      String e1 = n.f0.accept(this);
      n.f1.accept(this);
      String e2 = n.f2.accept(this);
      String res = "";
      String t1 = new_temp();
      String lab1 = new_label();
      String lab2 = new_label();
      String result = new_label();

      res += " BEGIN CJUMP " + e1 + " " + lab1 + "\n MOVE " + t1 + " 1 JUMP " + result + " " + lab1 + " CJUMP " + e2 + " " + lab2 + " MOVE " + t1 + " 1 JUMP " + result;
      res+= " " + lab2 + " MOVE " + t1 + " 0 " + result + " NOOP RETURN " + t1 + " END  ";
      return res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n) {
      String _ret=null;
      String e1 = n.f0.accept(this);
      n.f1.accept(this);
      String e2 = n.f2.accept(this);
      String res = "";
      String tt = get_temp();
      res += tt + " = icmp sle i32 " + e1 + ", " + e2;
      //ug(res);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public String visit(neqExpression n) {
      String _ret=null;
      String e1 = n.f0.accept(this);
      n.f1.accept(this);
      String e2 = n.f2.accept(this);
      String res = "";
      String tt = get_temp();
      res += tt + " = icmp ne i32 " + e1 + ", " + e2;
      //ug(res);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(PlusExpression n) {
      String _ret=null;
      String r1 = n.f0.accept(this);
      String r2 = n.f1.accept(this);
      String r3 = n.f2.accept(this);
      String tt = get_temp();
      //ug(tt + " = " + "add i32 " + r1 + ", " + r3);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n) {
      String _ret=null;
      String r1 = n.f0.accept(this);
      String r2 = n.f1.accept(this);
      String r3 = n.f2.accept(this);
      String tt = get_temp();
      //ug(tt + " = " + "sub i32 " + r1 + ", " + r3);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n) {
      String _ret=null;
      String r1 = n.f0.accept(this);
      String r2 = n.f1.accept(this);
      String r3 = n.f2.accept(this);
      String tt = get_temp();
      //ug(tt + " = " + "mul i32 " + r1 + ", " + r3);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public String visit(DivExpression n) {
      String _ret=null;
      String r1 = n.f0.accept(this);
      String r2 = n.f1.accept(this);
      String r3 = n.f2.accept(this);
      _ret = "DIV " + r1 +" " + r3 +"\n";
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n) {
      String _ret=null;
      String arr = n.f0.accept(this);
      n.f1.accept(this);
      String off = n.f2.accept(this);
      n.f3.accept(this);
      String ret = new_temp();
      String coff = new_temp();
      String temp = new_temp();
      String res = "";
      res += "BEGIN ";
      res += "MOVE " + temp + " PLUS 4 " + " TIMES 4 " + off + "\n";
      res += "MOVE " + coff + " PLUS " + temp + " " + arr + "\n";
      res += " HLOAD " + ret + " " + coff + " 0 ";
      res += " RETURN " + ret + " END \n";
      ////debug(res);
      return res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n) {
      String _ret=null;
      String arr = n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String res ="";
      String len = new_temp();
      res += " BEGIN ";
      res += " HLOAD " + len + " " + arr + " 0 RETURN " + len + " END\n";
      return res;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public String visit(MessageSend n) {
      String _ret=null;
      
      set_class = true;

      String caller = n.f0.accept(this);

      set_class = false;
      String store_class = class_name;
      String fn_origin;
      //String store_class = class_name;
      if(caller.equals("TEMP 0"))
      {
        fn_origin = get_class();

        class_name = get_class();
        //full_class = get_class();

      }
      else
      {
        ////debug(class_name + "lalalla");
        String scope1 = Setup.get_scope(store_class);
        ////debug(caller + "###" + class_name);
        String gg = scope1 + "_"+ store_class;
        ////debug(Setup.all_var.get(gg).type);
        fn_origin = Setup.all_var.get(gg).type;

      }
      //debug("HELLOO " );
      if(caller.equals("TEMP 0"))
        caller = "%this";
      
      n.f1.accept(this);
      String f_name = n.f2.f0.tokenImage;
      n.f3.accept(this);

      //String args =" ";
      args="";  
      args = n.f4.accept(this);
      //debug("hello "+args);
      String [] params = args.split(",");
      //debug("HELLOO " + f_name);
      Vector v = func_arg.get(f_name);

      String ret_type = func_ret_type.get(f_name);

      String res = "call " + ret_type + "@"+fn_origin+"_"+f_name+"(";

      res += (String)v.get(0) + " " + caller;

      for(int i=1;i<v.size();i++)
      {
        res += ", " + (String)v.get(i) + " " + params[i-1];
      }
      res += ")";
      String tt = get_temp();
      //debug(tt + "=" + res); 

      n.f5.accept(this);
      return tt;
      
   }
   // public String visit(MessageSend n) {
   //    String _ret=null;
   //    String res = "";
   //    set_class = true;
   //    String caller = n.f0.accept(this);
   //    set_class = false;
   //    String store_class = class_name;
   //    if(caller.equals("TEMP 0") )
   //    {
   //      caller = "TEMP 0";
   //      class_name = get_class();
   //      ////debug(class_name + "LALALA");
   //    }
   //    n.f1.accept(this);
   //    String f_name = n.f2.f0.tokenImage;
   //    n.f3.accept(this);

   //    //String args =" ";
   //    args="";  
   //    args = n.f4.accept(this);
   //    n.f5.accept(this);

   //    String fn_origin;
   //    String full_class;
   //    if(!main)
   //    {
          
   //        if(caller.equals("TEMP 0"))
   //        {
   //          fn_origin = store_class;
   //          full_class = get_class();

   //        }
   //        else
   //        {
   //          ////debug(class_name + "lalalla");
   //          String scope1 = Setup.get_scope(store_class);
   //          ////debug(caller + "###" + class_name);
   //          String gg = scope1 + "_"+ store_class;
   //          ////debug(Setup.all_var.get(gg).type);
   //          fn_origin = Setup.all_var.get(gg).type;
   //          ////debug( " DONE MESSED UP " + gg);
   //          ////debug(fn_origin);
   //          full_class = fn_origin;
   //        }
   //        ////debug(fn_origin + "lalalla");
   //    }
   //    else
   //    {
   //        fn_origin = main_call;
   //        full_class = main_call;
   //    }
   //    ////debug(" finaa" + fn_origin + " " + main);
   //    String obj = new_temp();
   //    String vpoint = new_temp();
   //    String fp = new_temp();
   //    String fin = new_temp();
   //    // Assign caller to obj
      
   //    String fn_full;
   //    if(main == true)
   //    {
   //        fn_full = fn_origin + "_" + f_name;
   //    }
   //    else if(caller.equals("TEMP 0") )
   //    {
   //      fn_full = get_class()+ "_" + f_name;
   //    }
   //    else
   //    {
        
   //      String find_class = Setup.find_func(fn_origin,f_name);
   //      if(find_class.equals(fn_origin))
   //      {
   //        fn_full = fn_origin + "_"+f_name; 
   //      }
   //      else
   //      {
   //        fn_full = find_class + "_" + f_name;
   //      }
   //    }
   //    int fn_index ;
   //    if(!main)
   //    {
   //        fn_index = Setup.Vtable.get(full_class).indexOf(fn_full);
   //    }
   //    else
   //    {
   //        fn_index =  Setup.Vtable.get(fn_origin).indexOf(fn_full);
   //    }
   //    res+= "BEGIN MOVE " + obj + " " + caller + "\n";
   //    res+= " HLOAD " + vpoint + " " + obj + " 0 \n";
   //    res+= " HLOAD " + fp + " "+ vpoint+ " " + 4*fn_index + " \n" ;
   //    res+= "MOVE " + fin + " CALL "+ fp + " ( " + obj + "  " + args + " ) "  ; 
   //    res+= " RETURN " + fin; 
   //    res += " END";

   //    return res;
   // }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public String visit(ExpressionList n) {
      String _ret="";
      args = " ";
      _ret=n.f0.accept(this);
      //debug("yooo " + _ret);
      ////debug(_ret);
      String lol = n.f1.accept(this);
      
      ////debug(args);
      return _ret+"   " +lol;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionRest n) {
      String _ret=",";
       n.f0.accept(this);

        String exp = n.f1.accept(this);

        args+= "   " + exp;
        
      return "," + exp;
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
   public String visit(PrimaryExpression n) {
      String _ret=null;
      if(!main)
      {
      look_up=true;
      }
      _ret = n.f0.accept(this);
      look_up=false;
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n) {
      String _ret=null;
      
      return  n.f0.tokenImage;
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n) {
      String _ret=null;
      n.f0.accept(this);
      return 1+"";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n) {
      String _ret=null;
      n.f0.accept(this);
      return 0+"";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n) {
      String _ret=null;
      String name = n.f0.accept(this);
      name = n.f0.tokenImage;
      if(set_class)
      {
        class_name = name;
      }
      if(main)
      {
        main_call = name;
        return name;
      }
      else if( look_up)
      {
         String id_scope = Setup.get_scope(name);
         
         String full_name = id_scope + "_" + name;
         String id_cur_scope = Setup.all_var.get(full_name).scope;
         String type = Setup.all_var.get(full_name).type;
         //Setup.Var id_obj = new Setup.Var();
         ////debug(Setup.all_var.get(full_name).scope + " Object check");
        if(id_cur_scope.equals("function_arg"))
        {
          String tt = get_temp();
          if(type.equals("int") || type.equals("int []") || type.equals("boolean"))
          {
            type = type_map.get(type);
          }
          else
          {
            type = "%"+type;
          }
          //debug(tt + " = " + "load " + type + "," + type + "" + "%" +name);
          _ret = "%"+name;
        }
        else
        {
          if(id_cur_scope.equals("function_scope"))
          {
            String tt = get_temp();
            if(type.equals("int") || type.equals("int []") || type.equals("boolean"))
            {
              type = type_map.get(type);
            }
            else
            {
              type = "%"+type;
            }
            //debug(tt + " = " + "load " + type + "," + type + "* " + "%" +name);
            _ret = tt;
          }
          else
          {
            _ret = "BEGIN ";
            String t1 = new_temp();
            String cur_class = get_class();

            ////debug(id_scope + " ___ "+ cur_class + "_________" + name);
            if(id_scope.equals(cur_class))
            {
            _ret += "HLOAD " + t1;
            _ret += " TEMP 0 " + (Setup.Vars_Vtable.get(cur_class).indexOf(full_name)+1)*4 +" RETURN " + t1 + " END";
            }
            else
            {
               _ret += "HLOAD " + t1;
               _ret += " TEMP 0 " + (Setup.Vars_Vtable.get(cur_class).indexOf(cur_class+"_"+full_name)+1)*4 +" RETURN " + t1 + " END";
            }
            
          }
        }
        return _ret;
        ////////debug(name + " Error");
        //return name;
      }
      else
      {
        return name;
      }
      //return _ret;
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n) {
      String _ret=null;
      n.f0.accept(this);
      return "TEMP 0";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(ArrayAllocationExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String size = n.f3.accept(this);
      n.f4.accept(this);
      String res = "";
      String ptr = new_temp();
      String v1 = new_temp();
      String v2 = new_temp();
      res += " BEGIN \n";
      res += " MOVE " + v1 + " PLUS " + size + " " + 1 + "\n";
      res += " MOVE " + v2 + " TIMES " + v1 + " 4 " + "\n";  
      res += " MOVE " + ptr + " HALLOCATE " + v2 + "\n";
      res += " HSTORE " + ptr + " 0 " + size + "\n";
      res += " HSTORE " + ptr + " 4 " + " 0 " + "\n";
      res += " RETURN " + ptr + " END \n";
      return res;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n) {
      String _ret=null;
      n.f0.accept(this);
      look_up=false;
      String cl_name = n.f1.f0.tokenImage;
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
      String tt = get_temp();
      res += tt + " = alloca %" +cl_name; 
      //debug(res);
      return res;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public String visit(NotExpression n) {
      String _ret=null;
      n.f0.accept(this);
      String e1 = n.f1.accept(this);
      String temp = new_temp ();
      String lab1 = new_label();
      String fin = new_label();
      String res = "";
      res += " BEGIN " + " CJUMP " + e1 + "  " + lab1 + " MOVE " + temp + " 0  JUMP " + fin + " " + lab1 + " MOVE " + temp + " 1 " + fin + " NOOP RETURN " + temp + " END\n";  
      return res;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n) {
      String _ret=null;
      n.f0.accept(this);
      _ret = n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public String visit(IdentifierList n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public String visit(IdentifierRest n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

}
