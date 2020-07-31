
package visitor;
import syntaxtree.*;
import java.util.*;

class Var{
  String scope;
  int ind;
  String type;
}
public class Setup implements GJNoArguVisitor<String> {
   //
   // User-generated visitor methods below
   //

  // Checklist 
  // 1. Generate all variables - Vtable
  // 2. Generate address for class variables
  // 3.
  public static HashMap<String,Vector> function_params =  new HashMap<String,Vector> ();

  public static HashMap<String,String> parent_map =  new HashMap<String,String>();     // Parent of every class / function 
  public static HashMap<String,Vector> all_scope_vars = new HashMap<String,Vector> (); // every variable in current scope

  public static HashMap<String,Vector> class_var = new HashMap<String,Vector> ();
  public static HashMap<String,Vector> func_var = new HashMap<String,Vector> ();
  public static HashMap<String,Var> all_var = new HashMap<String,Var> ();

  public static Set<String> all_class = new HashSet<String>();
  public static HashMap <String, Vector> Vtable = new HashMap<String,Vector> ();
  public static HashMap <String, Vector> Vars_Vtable = new HashMap<String, Vector> ();
  public static HashMap <String, Boolean> Visited = new HashMap<String, Boolean> ();

  public static boolean func_scope = false;
  public static int func_loc = 500;
  public static int class_vars = 0;
  public static int func_arg = 0;   // flag set index
  public static void debug(String str)
  {
    System.out.println(str);
  }
  public static Vector stack = new Vector();
  public static String top ()
  {
    return (String)stack.lastElement();
  }
  public static void addStack(String str)
  {
      stack.add(str);
  }
  public static void addAllVar(String name,String type,int ind,String scope)
  {
      Var v1 = new Var();
      v1.type = type;
      v1.ind = ind;
      v1.scope = scope;
      all_var.put(name,v1);
  }
  public static void addToClassVar(String type, int ind,String scope)
  {
      String cur_scope=(String)stack.lastElement();
      Var v1 = new Var();
      v1.type = type;
      v1.ind = ind;
      v1.scope = scope;
      class_var.get(cur_scope).add(v1);

  }
  public static void addToFuncVar(String type, int ind,String scope)
  {
      String cur_scope=(String)stack.lastElement();
      Var v1 = new Var();
      v1.type = type;
      v1.ind = ind;
      v1.scope = scope;
      func_var.get(cur_scope).add(v1);
  }
  public static void addToFunc(String str)
   {
      String cur_scope=(String)stack.lastElement();
      function_params.get(cur_scope).add(str);

   }
  public static void remStack()
  {
    if(stack.size()>0)
    {
      stack.remove(stack.size() - 1);
    }
  }
  public void addToScope(String str)
   {
      String cur_scope=(String)stack.lastElement();
      Vector gg = (Vector)all_scope_vars.get(cur_scope);
      all_scope_vars.get(cur_scope).add(str);
   }
  public static String fullStack()
   {
      String full_name="";
      String old_name="";
      Iterator i = stack.iterator();
      while(i.hasNext())
      {
        full_name+=i.next();
        old_name=full_name;

        full_name+="_";
      }
      return old_name;
   }
   public static String get_scope(String str)
   {
      String scope = top();
      int ctr = 0;
      while(true)
      {
          ctr++;
          if(ctr>100)
            return scope;
          Vector v = all_scope_vars.get(scope);
          for (int  i = 0;i<v.size() ;i++)
          {
            if(v.get(i).equals(str))
            {
              return scope;
            }
          }
          scope=parent_map.get(scope);
      }
   }
   public static String find_func(String scope1,String str)
   {
        String scope = scope1;
        //debug(scope1 + "lala")   ;
        int ctr = 0;
          while(true)
          {
              ctr++;
              if(ctr>100)
                return scope1;
              Vector v = all_scope_vars.get(scope);
              for (int  i = 0;i<v.size() ;i++)
              {
                if(v.get(i).equals(str))
                {
                  return scope;
                }
              }
              scope=parent_map.get(scope);
          }
   }
   public static void Sort_Me(String str)
   {
        if(parent_map.get(str).equals(str))
        {
            Vars_Vtable.put(str,new Vector());
            Vtable.put(str,new Vector());
            Vector v = all_scope_vars.get(str);
            for(int i=0;i<v.size();i++)
            {
               
                if(!all_var.get(str+"_"+v.get(i)).type.equals("function") && !all_var.get(str+"_"+v.get(i)).type.equals("class"))
                {
                    Vars_Vtable.get(str).add(str+"_"+v.get(i));
                }
                else if(all_var.get(str+"_"+v.get(i)).type.equals("function"))
                {
                    Vtable.get(str).add(str+"_"+v.get(i));
                }
                
            
            }
            Visited.put(str,true);
        }
        else
        {
            if(!Visited.get(parent_map.get(str)))
            {
                Sort_Me(parent_map.get(str));
            }
            Vector parent_vars = Vars_Vtable.get(parent_map.get(str));
            Vector parent_vtable = Vtable.get(parent_map.get(str));
            Vars_Vtable.put(str,new Vector());
            Vtable.put(str,new Vector());
            Vector v = all_scope_vars.get(str);
            for(int i=0;i<parent_vars.size();i++)
            {
                Vars_Vtable.get(str).add(str+"_"+parent_vars.get(i));
            }
            for(int i=0;i<parent_vtable.size();i++)
            {
                Vtable.get(str).add(parent_vtable.get(i));
            }
            //add current variables
            for(int i=0;i<v.size();i++)
            {
                if(!all_var.get(str+"_"+v.get(i)).type.equals("function") && !all_var.get(str+"_"+v.get(i)).type.equals("class"))
                {
                    Vars_Vtable.get(str).add(str+"_"+v.get(i));
                }
                else if(all_var.get(str+"_"+v.get(i)).type.equals("function"))
                {
                    String gg = (String)(str+"_"+v.get(i));
                    String[] arrOfStr = gg.split("_", 2);
                    boolean flag=false;
                    for(int j=0;j<Vtable.get(str).size();j++)
                    {
                        String gg1 = (String)Vtable.get(str).get(j);
                        String [] arrOfStr1 = gg1.split("_",2);
                        
                        if(arrOfStr1[1].equals(arrOfStr[1]))
                        {
                            
                            Vtable.get(str).set(j,str+"_"+v.get(i));
                            flag=true;
                        }
                        //debug(arrOfStr1[1] + " _________ " + arrOfStr[1]);
                        
                        
                    }
                    if(flag==false)
                    {
                        Vtable.get(str).add(str+"_"+v.get(i));
                    }
                }
            }
            Visited.put(str,true);

        }
   }
   public static void Sort_Classes()
   {
      for(String classes: all_class)
      {
        if(Visited.get(classes))
        {
          continue;
        }
        else
        {
          Sort_Me(classes);
        }
        Vector v = Vars_Vtable.get(classes);
        for(int i = 0;i<v.size();i++)
        {
          String gg = (String)v.get(i);
          //debug(gg);

        }
      }
      // for(String classes:all_class)
      // {
      //       Vector vtab = Vtable.get(classes);
      //       Vector tab_vars = Vars_Vtable.get(classes);
      //       for(int i=0;i<vtab.size();i++)
      //       {
      //           debug(" Class " + classes + " Contains " + vtab.get(i) +" as a function");
      //       }
      //       for(int i=0;i<tab_vars.size();i++)
      //       {
      //           debug(" Class " + classes + " Contains " + tab_vars.get(i) +" as a field");
      //       }
      // }

      //debug("Tables set up, have fun ####################################");
      //Vector  v = all_scope_vars.get("BT_Start");
      //debug(v.size() + "\n");
   }
    //
   // Auto class visitors--probably don't need to be overridden.
   //
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      Sort_Classes();
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
      
      n.f0.accept(this);
      String str = n.f1.accept(this);
      //debug(str);
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
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);

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
      n.f0.accept(this);
      String cl_name = n.f1.accept(this);
      parent_map.put(cl_name,cl_name);
      addStack(cl_name);
      all_scope_vars.put(cl_name,new Vector());
      n.f2.accept(this);

      addAllVar(cl_name,"class",0,"class");
      Visited.put(cl_name,false);
      all_class.add(cl_name);
      class_var.put(cl_name,new Vector());
      //start = 0;
      class_vars=0;
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      remStack();
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
      addStack(cl_name);
      all_class.add(cl_name);
      Visited.put(cl_name,false);
      all_scope_vars.put(cl_name,new Vector());
      n.f2.accept(this);
      String parent_cl = n.f3.accept(this);
      parent_map.put(cl_name,parent_cl);
      //start  = 0;

      addAllVar(cl_name,"class",0,"class");
      class_var.put(cl_name,new Vector());


      class_vars = 0;
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      remStack();
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n) {
      String _ret=null;
      String type = n.f0.accept(this);
      String name = n.f1.accept(this);
      addToScope(name);
      String full_name = top() + "_" + name;
      parent_map.put(name,top());
      Var v1 = new Var();
      v1.type = type;
      
      //var_defn.put(full_name,false);
      if(func_scope)
      {
        //var_no.put(full_name,start);
        addToFuncVar(type,func_loc,"function_scope");
        addAllVar(full_name,type,func_loc,"function_scope");
        func_loc++;
      }
      else
      {
        //class_var_no.put(full_name,class_vars);
        addToClassVar(type,class_vars,"class_scope");
        addAllVar(full_name,type,class_vars,"class_scope");
        class_vars++;
      }
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
   public String visit(MethodDeclaration n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String method_name = n.f2.accept(this);
      String old_name=fullStack();
      String full_name=old_name+"_";
      full_name+=method_name;
      function_params.put(full_name,new Vector());
      parent_map.put(full_name,top());
      addToScope(method_name);
      all_scope_vars.put(full_name,new Vector());
      addStack(full_name);
      func_loc=500;
      addAllVar(full_name,"function",class_vars,"class_scope");
      class_vars++;
      func_var.put(full_name,new Vector());
      func_arg = 0;
      func_scope = true;
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
      func_scope = false;
      remStack();
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public String visit(FormalParameterList n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n) {
      String _ret=null;
      String t = n.f0.accept(this);

      String var_name = n.f1.accept(this);
      func_arg++;

      String full_name = top();
      full_name += "_" + var_name;
      parent_map.put(full_name,top());
      addToScope(var_name);
      //function_param_index.put(full_name,func_arg);
      //debug( t + "##### " + var_name + "######" + full_name);
      addAllVar(full_name,t,func_arg,"function_arg");
      //debug( "Type is " + all_var.get(full_name).type );
      addToFuncVar(t,func_arg,"function_arg");
      //var_defn.put(full_name,true);
      addToFunc(t);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterRest n) {
      String _ret=null;
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
   public String visit(Type n) {
      String _ret=null;
      _ret = n.f0.accept(this);
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
      n.f0.accept(this);
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
      n.f1.accept(this);
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public String visit(IfStatement n) {
      String _ret=null;
      n.f0.accept(this);
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
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
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
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
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
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
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
      
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public String visit(OrExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public String visit(neqExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
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
      _ret = "PLUS " + r1 +" " + r3;
      return _ret;
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
      _ret = "MINUS " + r1 +" " + r3;
      return _ret;
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
      _ret = "TIMES " + r1 +" " + r3;
      return _ret;
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
      _ret = "DIV " + r1 +" " + r3;
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
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
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public String visit(ExpressionList n) {
      String _ret = n.f0.accept(this) + " ";

      _ret += n.f1.accept(this);


      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionRest n) {
      
      n.f0.accept(this);
      String _ret=n.f1.accept(this);

      return _ret;
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
      _ret = n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n) {
      String _ret=null;
     
      return n.f0.tokenImage;
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n) {
      String _ret=null;
      n.f0.accept(this);
      return "true";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n) {
      String _ret=null;
      n.f0.accept(this);
      return "false";
   }

   /**
    * f0 -> <IDENTIFIER>  
    */
   public String visit(Identifier n) {
      String _ret=null;
      n.f0.accept(this);
      _ret = n.f0.tokenImage;
      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n) {
      String _ret=null;
      n.f0.accept(this);
      return _ret;
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
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
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
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public String visit(NotExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n) {
      String _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
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
