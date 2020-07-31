
package visitor;
import syntaxtree.*;
import java.util.*;


public class LLVM implements GJNoArguVisitor<String> {
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
  boolean lhs_flag = false;
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
      int ctr = 0;
      while(!Setup.all_class.contains(classes))
      {
        classes=Setup.parent_map.get(classes);
        ctr++;
        if(ctr>100)
          break;
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
        res += "%"+type_cur;
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
  // 1. Array Lookup
  // 2. Array Store
  // 3. Normal element lookup
   public String add_v(Vector v)
   {
    String res = "";
    for(int i=0;i<v.size();i++)
    {
      if(i == (v.size())-1)
      {
        res += (String)v.get(i);
      }
      else
      {
        res += (String)v.get(i) + " , ";
      }
    }
    return res;
   }
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

      String gg = "declare i8* @calloc(i32, i32)";
      debug(gg);
      String str = "declare i32 @printf(i8*, ...) nounwind \n" + "@.textstr = internal constant [4 x i8] " + "c\"%d\\0a\\00\"\n";
      debug(str);
      String str1 = "@.truetxt = internal constant [6 x i8] " + "c \"true\\0A\\00\"\n";
      debug(str1);
      String str2 = "@.falsetxt = internal constant [7 x i8] " + "c \"false\\0A\\00\"\n";
      debug(str2);
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
      String res = "@.";
      res += cl_name + "_vtable = global ";
      Vector v = (Vector)Setup.Vtable.get(cl_name);
      int siz = v.size();
      res += " [ " + siz + " x i8* ] [";
      
      for(int i=0;i<v.size();i++)
      {
        String str1 = (String)v.get(i);
        String [] spl = str1.split("_");
        res += " i8* bitcast ( " + AddAll.func_ret_type.get(str1) + " (";
        Vector v1 = AddAll.func_arg.get(str1);
        String tot = add_v (v1);
        res += tot + ")* @"+str1 + " to i8* )";
        if(i!=v.size()-1)
        {
          res += ",";
        }
      }
      res += "]";
      debug(res);
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
   public String visit(ClassExtendsDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      String cl_name = n.f1.f0.tokenImage;
      Setup.addStack(cl_name);
      curs = cl_name;
      
      String res = "@.";
      res += cl_name + "_vtable = global ";
      Vector v = (Vector)Setup.Vtable.get(cl_name);
      int siz = v.size();
      res += " [ " + siz + " x i8* ] [";
      
      for(int i=0;i<v.size();i++)
      {
        String str1 = (String)v.get(i);
        String [] spl = str1.split("_");
        res += " i8* bitcast ( " + AddAll.func_ret_type.get(str1) + " (";
        Vector v1 = AddAll.func_arg.get(str1);
        String tot = add_v (v1);
        res += tot + ")* @"+str1 + " to i8* )";
        if(i!=v.size()-1)
        {
          res += ",";
        }
      }
      res += "]";
      debug(res);
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
   public String visit(VarDeclaration n) {
      String _ret=null;
      //n.f0.accept(this);
      String type = n.f0.accept(this);
      String id = n.f1.accept(this);
      if(op_vars)
      {
      if(type.equals("int []"))
      {
        debug("%"+id +"= " + "alloca i32*");
      }
      else if(type.equals("int"))
      {
        debug("%"+id + "= alloca i32");
      }
      else if(type.equals("boolean"))
      {
        debug("%"+id + "= alloca i1");
      }
      else
      {
        debug("%"+id + "= alloca i8*");
      }
      } 
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

      cur_func = method_name;
      //func_arg.put(cur_func,new Vector());
      //func_ret_type.put(cur_func,type);
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
      Setup.addStack(full_name);
      n.f3.accept(this);
      //func_arg.get(cur_func).add("%"+curs+"*");
      String gg = n.f4.accept(this);
      String all_pars []  = gg.split(",");
      //  debug(all_pars.length + "Hello");

      res += "(" + "i8* %this";
      if(gg.equals(""))
        ;
      else
        res += "," + gg;
      res += ") nounwind {";
      debug(res);
      for(int i=0;i<all_pars.length;i++)
      {
        try{
        String arg = all_pars[i];
        String spl []= arg.split(" ");
        String t = spl[0];
        String name = spl[1];
        String sp []= name.split("%");
        String fin  = sp[1];
        debug("%."+fin+" = alloca " + t);
        debug("store " + t + " %" + fin + ", "+ t + "*" + " %."+fin);
        }
        catch(Exception e)
        {
          //debug(all_pars[i]);
          //debug("####");
          ;
        }
      }
      
      n.f5.accept(this);
      n.f6.accept(this);
      op_vars = true;
      n.f7.accept(this);
      op_vars = false;
      String all_st = n.f8.accept(this);
      //debug(all_st);
      n.f9.accept(this);  
      String ret = n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      debug("ret " + type + " " + ret);
      //debug("END");
      Setup.remStack();
      debug("}");
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
      //func_arg.get(cur_func).add(type);
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
      String lhs = "";
      if(real_scope.equals("function_scope") || real_scope.equals("function_arg"))
        ;
      else
        lhs = n.f0.accept(this);
      look_up=false;
      n.f1.accept(this);
      String rhs = n.f2.accept(this);

      n.f3.accept(this);
      String res ;
      
      if(real_scope.equals("function_scope") )
      {
        res = "";
        if(type.equals("int"))
        {
          res += "store i32 "   + rhs + ", i32* %"+n.f0.f0.tokenImage ;
          debug(res);
        }
        else if(type.equals("boolean"))
        {
          res += "store i1 "   + rhs + ", i1* %"+n.f0.f0.tokenImage ;
          debug(res);
        }
        else if(type.equals("int []"))
        {
          String gg = get_temp();
          debug("store i32* " + rhs + ", i32 ** %" + n.f0.f0.tokenImage  );
        }
        else
        {
          String t1 = get_temp();

          if(rhs.equals("TEMP 0"))
            rhs = "%this";
          res += "store i8* " + rhs + ", i8** %" + n.f0.f0.tokenImage;
          debug(res);
        }
      }
      else if(real_scope.equals("function_arg"))
      {
        res = "";
        String tt = get_temp();
        if(type.equals("int"))
        {
          //debug(tt + " = alloca i32");
          //debug("store i32 %" +n.f0.f0.tokenImage + ", i32* " + tt);
          res += "store i32 "   + rhs + ", i32* %."+n.f0.f0.tokenImage ;
          debug(res);
        }
        else if(type.equals("boolean"))
        {
          //debug(tt + " = alloca i1");
          //debug("store i1 %" +n.f0.f0.tokenImage + ", i1* " + tt);
          res += "store i1 "   + rhs + ", i1* %."+n.f0.f0.tokenImage ;
          debug(res);
        }
        else if(type.equals("int []"))
        {
          // debug(tt + " = alloca i32*");
          // debug("store i32* %" +n.f0.f0.tokenImage + ", i32** " + tt);
          // String gg = get_temp();
          debug("store i32* " + rhs + ", i32** %." + n.f0.f0.tokenImage );
        }
        else
        {
          
          if(rhs.equals("TEMP 0"))
            rhs = "%this";
          res += "store i8* " + rhs + ", i8** %." + n.f0.f0.tokenImage;
          debug(res);

        }
      }
      else
      {
        look_up = true;
        lhs_flag = true;
        String ptr = n.f0.accept(this);
        look_up = false;
        lhs_flag = false;
        res = "";
        if(type.equals("int"))
        {
          res += "store i32 "   + rhs + ", i32* "+ptr ;
          debug(res);
        }
        else if(type.equals("boolean"))
        {
          res += "store i1 "   + rhs + ", i1* "+ptr ;
          debug(res);
        }
        else if(type.equals("int []"))
        {
          String gg = get_temp();
          
          debug("store i32* " + rhs + ", i32 **" + lhs );
        }
        else
        {
          if(rhs.equals("TEMP 0"))
            rhs = "%this";
          res += "store i8* " + rhs + ",    i8** " + ptr;
          debug(res);
        }
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
      String noff = get_temp();
      String tt = get_temp();
      debug(noff + " = add i32 1, " + off);
      String t1 = get_temp();
      debug(t1 + " = load i32*, i32** "+arr);
      debug(tt + " = getelementptr i32, i32* "+ t1 + ", i32 " + noff);
      debug("store i32 " + val + " ,i32* " + tt);
      return "";
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
      String lab1 = new_label();
      String lab2 = new_label();
      String res;
      res = "br i1 " + expr + ", label %" + lab1 + ", label %" + lab2;
      debug(res);
      debug(lab1+":");
      String code = n.f4.accept(this);
      debug("br label %" + lab2);
      debug(lab2 + ":");
      
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
      debug(res);
      debug(lab1+":");
      String st1 = n.f4.accept(this);
      //debug(st1);
      debug("br label %" + lab3);
      debug(lab2+":");
      String st2 = n.f6.accept(this);
      //debug(st2);
      debug("br label %" + lab3);
      debug(lab3 + ":");
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
      
      n.f3.accept(this);
      //String exec = n.f4.accept(this);
      String start = new_label();
      String end = new_label();
      String lab1 = new_label();
      debug("br label %"+start);
      debug(start + ":");
      String cond = n.f2.accept(this);
      String res = "";
      res = "br i1 " + cond + ", label %" + lab1 + ", label %" + end;
      debug(res);
      debug(lab1+":");
      String exec = n.f4.accept(this);
      debug("br label %" + start);
      debug(end + ":");


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
      
      n.f0.accept(this);
      n.f1.accept(this);
      String op = n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String tt = get_temp();
      debug(tt + "=" +  "call i32 (i8*, ...) @printf(i8* getelementptr([4 x i8], [4 x i8]* @.textstr, i32 0, i32 0), i32 " + op + ")" );
      
      return tt;
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
      debug(res);
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
      String lab1 = new_label();
      //String lab2 = new_label();
      String result = new_label();
      String tt = get_temp();
      String res=tt + "= or i1 " + e1 + " , " + e2;
      debug(res);
      return tt;
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
      debug(res);
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
      debug(res);
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
      debug(tt + " = " + "add i32 " + r1 + ", " + r3);
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
      debug(tt + " = " + "sub i32 " + r1 + ", " + r3);
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
      debug(tt + " = " + "mul i32 " + r1 + ", " + r3);
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
      String tt = get_temp();
      debug(tt + " = " + "udiv i32 " + r1 + ", " + r3);
      return tt;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n) {
      String _ret=null;
      look_up = true;
      String arr = n.f0.accept(this);
      look_up = false;
      n.f1.accept(this);
      String off = n.f2.accept(this);
      n.f3.accept(this);
      String ret = new_temp();
      String coff = new_temp();
      String temp = new_temp();
      String res = "";
      String tt = get_temp();
      String tt1 = get_temp();
      String noff = get_temp();
      String gg = get_temp();
      res += noff + " = " + " add i32 1 , "+ off + "\n";
      res += tt + " = load i32*, i32** " + arr + "\n";
      res += tt1 + " = getelementptr i32, i32* " + tt + " ,i32 " + noff + "\n";
      res += gg + " = load i32 , i32* " + tt1;
      debug(res);

      ////debug(res);
      return gg;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n) {
      String _ret=null;
      lhs_flag = true;
      String arr = n.f0.accept(this);
      lhs_flag = false;
      n.f1.accept(this);
      n.f2.accept(this);
      String res ="";
      String tt = get_temp();
      String tt1 = get_temp();
      String gg = get_temp();
      debug(tt + " = load i32*, i32** " + arr + "\n");
      debug(tt1 + " = getelementptr i32, i32* " + tt + ", i32 0");
      debug(gg +  " = load i32, i32* " + tt1);
      return gg;
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
      //debug(set_class + "");
      String caller = n.f0.accept(this);
      set_class = false;

      String store_class = class_name;
      
      n.f1.accept(this);
      String f_name = n.f2.f0.tokenImage;
      n.f3.accept(this);
      String fn_origin;
      String full_class;

      if(caller.equals("TEMP 0"))
      {

        fn_origin = get_class();
        full_class = get_class();

      }
      else
      {
        //debug(class_name + "lalalla");
        //debug(store_class + "ll");
        String scope1 = Setup.get_scope(store_class);
        //debug(caller + "###" + class_name);
        String gg = scope1 + "_"+ store_class;
        //debug(Setup.all_var.get(gg).type);
        fn_origin = Setup.all_var.get(gg).type;
        //debug( " DONE MESSED UP " + gg);
        //debug(fn_origin);
        full_class = fn_origin;
      }

      String fn_full;
      if(caller.equals("TEMP 0") )
      {
        fn_full = get_class()+ "_" + f_name;
        Vector v = (Vector )Setup.Vtable.get(fn_origin);
        for(int i = 0;i<v.size();i++)
        {
          String nn = (String)v.get(i);
          String [] sp = nn.split("_");
          int ff = sp.length;
          if(sp[ff-1].equals(f_name))
          {
            fn_full = sp[0] + "_" + f_name;
          }
        }
        caller = "%this";
      }
      else
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
      //debug(fn_origin + " $$$ " + fn_full);

      String t1 = get_temp();
      String t2 = get_temp();
      String t3 = get_temp();
      String t4 = get_temp();
      String t5 = get_temp();
      debug(t1 + " = bitcast i8* " + caller + " to i8***");
      debug(t2 + " = load i8**, i8*** " + t1);
      int fn_index ;
      fn_index =  Setup.Vtable.get(fn_origin).indexOf(fn_full);
      debug(t3 + " = getelementptr i8*,i8** " +t2 + ", i32 "+fn_index);
      debug(t4 + " = load i8*, i8** " + t3);
      Vector v = AddAll.func_arg.get(fn_full);
      String tot = add_v(v);
      String ret_type = AddAll.func_ret_type.get(fn_full);
      debug(t5 + " = bitcast i8* "+t4 + " to " + ret_type + "(" + tot + ")*");
      //String args =" ";
      args="";  
      args = n.f4.accept(this);
      String [] params = args.split(","); 
      //debug("Hello   " + args);
      
      

      String res = "call " + ret_type + " " +  t5 + "(";
      res += (String)v.get(0) + " " + caller;
      for(int i=1;i<v.size();i++)
      {
        if(params[i-1].contains("TEMP 0"))
          res+=", " + (String)v.get(i)  + " " + "%this";
        else
        res += ", " + (String)v.get(i) + " " + params[i-1];
      }
      res += ")";
      String tt = get_temp();
      debug(tt + "=" + res); 

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
      //debug(set_class + "44");
      String name = n.f0.accept(this);
      name = n.f0.tokenImage;
      if(set_class==true)
      {
        //debug("yo ww");
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
         if(type.equals("int") || type.equals("int []") || type.equals("boolean"))
          {
            type = type_map.get(type);
          }
          else
          {
            type = "i8*";
          }
        if(id_cur_scope.equals("function_arg"))
        {
          String tt = get_temp();
          
          debug(tt + " = " + "load " + type + "," + type + "* " + "%." +name);
          _ret = tt;
        }
        else
        {
          if(id_cur_scope.equals("function_scope"))
          {
            String tt = get_temp();
            if(type.equals("[100 x i32]"))
            {
              type = "i32*";
              _ret = "%"+name;
              return _ret;
            }
            else
            { 
              if(type.equals("i32")  || type.equals("i1"))
                ;
              else
                type = "i8*";
              debug(tt + " = " + "load " + type + "," + type + "* " + "%" +name);

            }
            _ret = tt;
          }
          else
          {
            _ret = "lookup";
            
            //String t1 = new_temp();
            String cur_class = get_class();

            ////debug(id_scope + " ___ "+ cur_class + "_________" + name);
            if(id_scope.equals(cur_class))
            {
              int gg = (Setup.Vars_Vtable.get(cur_class).indexOf(full_name));
              gg++;
              String nt = get_temp();
              String t1 = get_temp();
              debug(nt + " = " + "getelementptr i8,i8* %this ,i32  " + gg*8);
              if(type.equals("[100 x i32]"))
              {
                debug(t1 + " = bitcast i8* " + nt + " to i32** ");
              }
              else if(type.equals("i1"))
              {
                  debug(t1 + " = bitcast i8* " + nt + " to i1* ");
              }
              else if(type.equals("i32"))
              {debug(t1 + " = bitcast i8* " + nt + " to i32* ");}
              else
              {
                type = "i8*";
                debug(t1 + " = bitcast i8* " + nt + " to i8** ");
              }
              String tt = t1;
              if(type.equals("[100 x i32]") || lhs_flag)
              {
                ;
              }
              else
              {
              tt = get_temp();
              debug(tt + " = load " + type + ", " + type + "* " + t1 );
              }
              _ret = tt;
            }
            else
            {
               int gg = Setup.Vars_Vtable.get(cur_class).indexOf(cur_class+"_"+full_name);
               gg++;
              String nt = get_temp();
              String t1 = get_temp();
              debug(nt + " = " + "getelementptr i8,i8* %this ,i32  " + gg*8);
              if(type.equals("[100 x i32]"))
              {
                debug(t1 + " = bitcast i8* " + nt + " to i32** ");
              }
              else if(type.equals("i1"))
              {
                  debug(t1 + " = bitcast i8* " + nt + " to i1* ");
              }
              else if(type.equals("i32"))

              {debug(t1 + " = bitcast i8* " + nt + " to i32* ");}
              else
              {
                type = "i8*";
                debug(t1 + " = bitcast i8* " + nt + " to i8** ");
              }
              String tt = t1;
              if(type.equals("[100 x i32]") || lhs_flag)
              {
                ;
              }
              else
              {
              tt = get_temp();
              debug(tt + " = load " + type + ",   " + type + "* " + t1 );
              }
              _ret = tt;
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
      String ptr = get_temp();
      String t1 = get_temp();
      String t2 = get_temp();
      String t3 = get_temp();
      debug(ptr + " = " + "call i8* @calloc(i32 4, i32 1000)");
      debug(t1 + " = " + "bitcast i8* " + ptr + " to i32*");
      debug("store i32 " + size  + ", i32* " + t1);
      return t1;
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
      try{
      int vtable_size = 1 + Setup.Vars_Vtable.get(cl_name).size();
      int zero_size = Setup.Vtable.get(cl_name).size();
      String t1 = get_temp();
      String t2 = get_temp();
      String t3 = get_temp();
      String t4 = get_temp();
      debug(t1 + " = call i8* @calloc(i32 1, i32 2000)");
      debug(t2 + " = bitcast i8* " + t1 + " to i8***");
      String con = " ["+ zero_size + " x i8*]";
      debug(t3 + " = getelementptr "+ con + ", " + con + "* @."+cl_name+"_vtable, i32 0, i32 0");
      debug("store i8 ** " + t3 + ", i8*** "+ t2);
      return t1;
      }
      catch(Exception e)
      {
        String t1 = get_temp();
        debug(t1 + " = call i8* @calloc(i32 1, i32 1000)");
        return t1;
      }
      
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
      String tt = get_temp();
      debug(tt + " = " + " sub i1 1," + e1);
      return tt;
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
