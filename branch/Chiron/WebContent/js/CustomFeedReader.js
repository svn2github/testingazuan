/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/* ************************************************************************

#resource(custom.image:image)

// List all static resources that should be copied into the build version,
// if the resource filter option is enabled (default: disabled)
#embed(qx.icontheme/32/status/dialog-information.png)
#embed(custom.image/test.png)

************************************************************************ */

/**
 * Your custom application
 */
qx.Class.define("spagobi.test.TestApplication",
{
  extend : qx.application.Gui,

  /*
  *****************************************************************************
     SETTINGS
  *****************************************************************************
  */

  settings : {
    "spagobi.imageUri" : "../img"
  },


  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    form: null,
    
    main : function()
    {
		 this.base(arguments);
      
      // .......................... FUNCTIONS START ...............................
        // Function to add Label on page
        
        function_label = function(name,obj,topp,lleft)
        {
          if (name == undefined)
             name = "";
          
          if (topp == undefined)// || topp.isNaN()
            topp = 0;
          
          if (lleft == undefined)// || topp.isNaN()
            lleft = 0;
                    
          if (obj!=undefined)
            {
            obj.set({top: topp, left: lleft });
            return obj;
            }
            
          else 
            {
              var test_label = new qx.ui.basic.Label(name);
              test_label.set({ top: topp, left: lleft });
              return test_label;
            }
        }
        
        
        // Function to add Text Field on page 
        function_textfield = function(obj,topp,leftt,max_length,dimension1,dimension2)
        {
          if (topp == undefined)
              topp = 0;
          if (leftt == undefined)
              leftt = 0;
          if (max_length == undefined)    // to be set to largest possible number
              max_length = 50;
          if (dimension1 == undefined)
              dimension1 = 0;
          if (dimension2 == undefined)
              dimension2 = 0;
          if (obj!=undefined)
            {
              obj.set({top: topp, left: leftt });
              //obj.setMaxLength(max_length);
              //obj.setDimension(dimension1,dimension2);
              return obj;
            }
            
          else 
          {
        
            var test_textfield = new qx.ui.form.TextField();
            test_textfield.set({ top: topp, left: leftt });
            test_textfield.setMaxLength(max_length);
            test_textfield.setDimension(dimension1,dimension2);
            return test_textfield;
          }
        }
        
        // Function to add Combo box
        function_combo = function(obj, topp, lleft, arr )
        {
          if(topp == undefined)
            topp = 0;
          
          if(lleft == undefined)
            lleft = 0;
          
          if(arr == undefined)
            arr = null;
          
          /*  
          if(obj)
            {
              alert("Combo Box is already created");
            }
          else
          */
          {
            var combo_box = new qx.ui.form.ComboBox;
            combo_box.set({ top: topp, left: lleft });
          
            var array1 = arr;
            
            for(var i=0; i<array1.length; i++)
              {
              item1 = new qx.ui.form.ListItem(array1[i]);
              combo_box.add(item1);
              }
            combo_box.setSelected(combo_box.getList().getFirstChild());
            return combo_box;
          }
        }
        
        // Function to add a check box
        function_checkbox = function(obj, txt, top1, left1, checked1)
        {
          if(txt == undefined)
            checked = "";
          
          if(top1 == undefined)
            top1 = 0;
          
          if(left1 == undefined)
            left1 = 0;
          
          if(checked1 == undefined)
            checked1 = false;
          
          /*  
          if(obj)
          {
             alert("Check Box is already created");
          }
          else
          */
            {
              var check_box = new qx.ui.form.CheckBox(txt);
              with(check_box)
                  {
                    setTop(top1);
                    setLeft(left1);
                    setChecked(checked1);
                  };
              return check_box;
            }
        }
       
       function page_display(arg_page)
       {
        if(arg_page == "page1")
        {
            bs.getBar().setDisplay(false);
            false_button2.setChecked(true);
        }
        
        else if(arg_page == "page2")
            {
              bs.getBar().setDisplay(true);
              false_button.setChecked(true);
              
            }
       }; 
        
        
        // ................... FUNCTIONS END ....................................
        
        
        // Current document on screen
        var d = qx.ui.core.ClientDocument.getInstance(); 
       
        // ButtonView to hold Bar(for buttons) and Panes (for Pages)
        var bs = new qx.ui.pageview.buttonview.ButtonView;
        bs.setEdge(20);
        
        
        // ............................. PAGE 1 STARTS ..............................
        //  contains a dummy button, a tool bar, a table and an execute button
        
        // Dummy button      
        var false_button2 = new qx.ui.pageview.buttonview.Button("", "");
        false_button2.setLeft(790);
        false_button2.setChecked(true);
        false_button2.setDisplay(false);
        
        var page1 = new qx.ui.pageview.buttonview.Page(false_button2);
        
        // Tool bar above Table
        // Tool Bar
        var tool_bar1 = new qx.ui.toolbar.ToolBar;
        tool_bar1.setTop(-10);
        tool_bar1.setRight(-10);
        tool_bar1.setLeft(-10);
        tool_bar1.setBackgroundColor("#CCCCCC");
        //tool_bar1.add(button_first_pg,button_back_pg,Tool_Lab1,Tool_combo1,Tool_Lab2,Tool_combo2,Tool_combo3,Tool_txt_field,button_next_pg,button_last_pg);
        
        page1.add(tool_bar1);
        
        
        
        // Button 1 ... Go to 1st page
        var button_first_pg = new qx.ui.form.Button("", "icon/16/actions/go-previous.png");
        button_first_pg.setLeft(10);
        //button_first_pg.setTop(-4);
        var Tooltip_first_pg = new qx.ui.popup.ToolTip("Go to 1st page","");
        button_first_pg.setToolTip(Tooltip_first_pg);
        Tooltip_first_pg.setShowInterval(10);
        
        tool_bar1.add(button_first_pg);
        
        
        // Button 2 ... Go to back page
        var button_back_pg = new qx.ui.form.Button("", "icon/16/actions/go-left.png");
        button_back_pg.setLeft(20);
       // button_back_pg.setTop(-4);
        var Tooltip_prev_pg = new qx.ui.popup.ToolTip("Go to previous page","");
        button_back_pg.setToolTip(Tooltip_prev_pg);
        Tooltip_prev_pg.setShowInterval(10);
        
        tool_bar1.add(button_back_pg);
        
        
        // Label 1 in the tool bar
        var Tool_Lab1 = function_label("The value of the column ",undefined,5,70);
        //Tool_Lab1.setTop(5);
        
        tool_bar1.add(Tool_Lab1);
        
        // Combo box1 in tool bar
        var Tool_array1 = ["LABEL","NAME","DESCRIPTION"];
        var Tool_combo1 = function_combo(Tool_combo1,2,74,Tool_array1);
        Tool_combo1.setWidth(100);
        //Tool_combo1.setTop(2);
        
        tool_bar1.add(Tool_combo1);
        
        
        // Label 2 in tool bar
        var Tool_Lab2 = function_label("as a ",undefined,5,76);
        //Tool_Lab2.setTop(5);
        
        tool_bar1.add(Tool_Lab2);
        
        
        // Combo box2 in tool bar
        var Tool_array2 = ["string","number","date"];
        var Tool_combo2 = function_combo(Tool_combo2,2,81,Tool_array2);
        Tool_combo2.setWidth(70);
        //Tool_combo2.setTop(2);
        
        tool_bar1.add(Tool_combo2);
        
        // Combo box3 in tool bar 
        var Tool_array3 = ["starts with","<",">"];
        var Tool_combo3 = function_combo(Tool_combo3,2,92,Tool_array3);
        Tool_combo3.setWidth(80);
        //Tool_combo3.setTop(2);
        
        tool_bar1.add(Tool_combo3);
        
        // Text field in tool bar
        var Tool_txt_field = function_textfield(Tool_txt_field,2,100,undefined,100,20);
        //Tool_txt_field.setTop(2);
        
        tool_bar1.add(Tool_txt_field);
        
        // Button 3 ... Go to next page
        var button_next_pg = new qx.ui.form.Button("", "icon/16/actions/go-right.png");
        button_next_pg.setLeft(358);
        //button_next_pg.setTop(-4);
        var Tooltip_next_pg = new qx.ui.popup.ToolTip("Go to 1st page","");
        button_next_pg.setToolTip(Tooltip_next_pg);
        Tooltip_next_pg.setShowInterval(10);
        
        tool_bar1.add(button_next_pg);
        
        // Button 4 ... Go to last page
        var button_last_pg = new qx.ui.form.Button("", "icon/16/actions/go-next.png");
        button_last_pg.setLeft(368);
        //button_last_pg.setTop(-4);
        var Tooltip_last_pg = new qx.ui.popup.ToolTip("Go to previous page","");
        button_last_pg.setToolTip(Tooltip_last_pg);
        Tooltip_last_pg.setShowInterval(10);
        
        tool_bar1.add(button_last_pg);
        
        //page1.add(tool_bar1);
        // ........ Tool Bar Ends
        
        
        // ........ Table Starts
        var tabm1 = new qx.ui.table.model.Simple();
        tabm1.setColumns(["Label","Name", "Description", "Expand", "Delete"]);
        //tabm1.setColumnEditable(3, true); 
        
        
        var table1 = new qx.ui.table.Table(tabm1);
        with (table1) 
        {
          set({ left:-8, top:20,  width:970, height:350, border:"inset-thin" });
          setBackgroundColor("white");
          setStatusBarVisible(false);
          setColumnWidth(0,250);
          setColumnWidth(1,250);
          setColumnWidth(2,250);
          setColumnWidth(4,125);
        }
        
        // Vertical scroll bar
        
        var scroll_table = new qx.ui.table.pane.Scroller(table1);
        scroll_table.setVerticalScrollBarVisible(true); 
        
        var arr_label = ["Birt","ChartEngine","DashBoardInt","io","ui","i","kj","i","43","5","7","9","k","e","y","y","h","a","e","e","d","d","c","b"];
        var arr_name =  ["Birt","ChartEngine","DashBoardInt"];
        var arr_desc =  ["Birt","Chart Engine","DashBoard Internal Engine"];
        
        var rowdata = [];
        for(var i=0; i<arr_label.length;i++)
        {
          rowdata.push([ arr_label[i], arr_name[i], arr_desc[i]]);
        }
        tabm1.addRows(rowdata); 
        
        page1.add(table1);
        //bs.getPane().add(table1);
        // ....... Table Ends
        
        
        // Go button  
        var go_button = new qx.ui.form.Button("Go to Page 2");
        go_button.setLeft(750);
        go_button.setTop(300);
        var Tooltip_go = new qx.ui.popup.ToolTip("Go","");
        go_button.setToolTip(Tooltip_go);
        Tooltip_go.setShowInterval(10);
        
        function go_to_next()
        {
          page_display("page2");
          
        }
        
        go_button.addEventListener("execute", go_to_next);
        
        
        page1.add(go_button);
        // .... Button ends
        
        // ....Bottom Tool bar starts with a dummy button inside
        /*
         var go_button1 = new qx.ui.form.Button("Go to Page 2");
        //go_button.setLeft(750);
        //go_button.setTop(300);
        var Tooltip_go1 = new qx.ui.popup.ToolTip("Go1","");
        go_button1.setToolTip(Tooltip_go1);
        Tooltip_go.setShowInterval(10);
        function go_to_next1()
        {
          //page_display("page2");
          alert("amit is going to roma!")
          
        }
        go_button1.addEventListener("execute", go_to_next1);
        */
        
        var tool_bar2 = new qx.ui.toolbar.ToolBar;
        tool_bar2.setTop(371);
        tool_bar2.setRight(-10);
        tool_bar2.setLeft(-10);
        tool_bar2.setBackgroundColor("#CCCCCC");
       
        // Button 1 ... Go to 1st page
        var button_first_pg2 = new qx.ui.form.Button("", "icon/16/actions/go-previous.png");
        button_first_pg2.setLeft(370);
        var Tooltip_first_pg2 = new qx.ui.popup.ToolTip("Go to 1st page","");
        button_first_pg2.setToolTip(Tooltip_first_pg2);
        Tooltip_first_pg2.setShowInterval(10);
	      tool_bar2.add(button_first_pg2);

	     // Button 2 ... Go to back page
        var button_back_pg2 = new qx.ui.form.Button("", "icon/16/actions/go-left.png");
        button_back_pg2.setLeft(375);
       	var Tooltip_prev_pg2 = new qx.ui.popup.ToolTip("Go to previous page","");
        button_back_pg2.setToolTip(Tooltip_prev_pg2);
        Tooltip_prev_pg2.setShowInterval(10);
	      tool_bar2.add(button_back_pg2);

 	      // Button 3 ... Go to next page
        var button_next_pg2 = new qx.ui.form.Button("", "icon/16/actions/go-right.png");
        button_next_pg2.setLeft(420);
        var Tooltip_next_pg2 = new qx.ui.popup.ToolTip("Go to 1st page","");
        button_next_pg2.setToolTip(Tooltip_next_pg2);
        Tooltip_next_pg2.setShowInterval(10);
	      tool_bar2.add(button_next_pg2);


	      // Button 4 ... Go to last page
        var button_last_pg2 = new qx.ui.form.Button("", "icon/16/actions/go-next.png");
        button_last_pg2.setLeft(425);
        var Tooltip_last_pg2 = new qx.ui.popup.ToolTip("Go to previous page","");
        button_last_pg2.setToolTip(Tooltip_last_pg2);
        Tooltip_last_pg2.setShowInterval(10);
        tool_bar2.add(button_last_pg2);
        
        
        
        //tool_bar2.add(go_button1);
        page1.add(tool_bar2);
        //page1.setDisplay(true);
        // .....Bottom Toolbar ends
        
        // .............................. PAGE 1 ENDS ...............................
        
        
        
        
        
        
        
        
       // ......................... PAGE 2 STARTS ...............................        
        
        var position_top = -20, position_left =0;     // global variable to handle widgets .. 
                                                     // .. position on page
        
        // Label on Tool Bar            
        var label1 = function_label("Engine Details");
        
        // To show the default page on load      
        var false_button = new qx.ui.pageview.buttonview.Button("", "");
        false_button.setLeft(790);
        false_button.setDisplay(false);         //false_button.setChecked(true);
        
        
        // Save button  
        var button_save = new qx.ui.form.Button("", "icon/22/devices/video-display.png");
        button_save.setLeft(800);
        var Tooltip_save = new qx.ui.popup.ToolTip("Save","");
        button_save.setToolTip(Tooltip_save);
        Tooltip_save.setShowInterval(10);
        
        
        // Event Listener for Button ... check mandatory fields
        function check_Field()
        {
          if(txt1.getValue() == "")     // Label text field
            alert('Label is a Mandatory field !!');
          
                   
          if(txt2.getValue() == "")     // Name
            alert('Name is a Mandatory field !!');
            
          
          if(txt4.getDisplay()== true)  // Class text field
            { 
              if(txt4.getValue() == "")     
                  alert('Class is a Mandatory field !!');
            }
            
          else 
              if(txt5.getDisplay()== true)  // Optional condition to check
                {  
                  if(txt5.getValue() == "")     // Url  text field
                    alert('Url is a Mandatory field !!');
                
                  if(txt6.getValue() == "")     // Label text field
                    alert('Driver name is a Mandatory field !!');  
                }
                
        }
      
        button_save.addEventListener("execute", check_Field);
        
        
        // Back Button
        var button_back = new qx.ui.form.Button("", "icon/22/apps/graphics-snapshot.png");
        button_back.setLeft(810);
        var Tooltip_back = new qx.ui.popup.ToolTip("Back","");
        button_back.setToolTip(Tooltip_back);
        Tooltip_back.setShowInterval(10);
        
        
        function go_back()
        {
          page_display("page1");
          /* page2.setDisplay(false);
           label1.setDisplay(false);
           button_save.setDisplay(false);
           button_back.setDisplay(false);    
           false_button2.setChecked(true);     
           */
        }
        
        button_back.addEventListener("execute", go_back);
        
        // Default page mapped to the dummy false_button
        var page2 = new qx.ui.pageview.buttonview.Page(false_button);
        
        
      
        //tool_bar2.add(button_first_pg2,button_back_pg2,button_next_pg2,button_last_pg2);
        //tool_bar2.add(button_first_pg,button_back_pg,button_next_pg,button_last_pg);
        //Adding bar to buttonview
        
        bs.getBar().add(label1,false_button,button_save, button_back,false_button2);
        //bs.getPane().add(tool_bar,tool_bar2);
       
       
        
        page_display("page1");
        
        var lab6, lab61, txt4, lab7, lab71, txt5,lab8, lab81, txt6,lab9,combo3;
          
        // 1st textfield
        var lab1 = function_label("Label",undefined,20+position_top,10+position_left);
        var txt1 = function_textfield(undefined,20+position_top,100,20,200,20);
        var lab11 = function_label("*",undefined,20+position_top,302+position_left);
        
              
        // 2nd textfield
        var lab2 = function_label("Name",undefined,50+position_top,10);
        var txt2 = function_textfield(undefined,50+position_top,100,undefined,200,20);
        var lab21 = function_label("*",undefined,50+position_top,302);
      
      
        // 3rd textfield
        var lab3 = function_label("Description",undefined,80+position_top,10);
        var txt3 = function_textfield(undefined,80+position_top,100,undefined,200,20);
        
        
        // 1st Combo Box
        var lab4 = function_label("Document type",undefined,110+position_top,10);
        var array1 = ["Report","Map"];
        var combo1 = function_combo(combo1,110+position_top,100,array1);
        
        // 2nd Combo Box
        var lab5 = function_label("Engine type",undefined,140+position_top,10);
        var array2 = ["Internal","External"];
        var combo2 = function_combo(combo2,140+position_top,100,array2);
        
        // 3rd Combo box
        lab9 = function_label("Data Source",undefined,230+position_top,10);
        var array3 = ["  ","Foodmart","SpagoBI","GeoData"];
        combo3 = function_combo(combo3,230+position_top,100,array3);
        combo3.setDisplay(false);
        
        // 4th textfield
        lab6 = function_label("Class");
        txt4 = function_textfield(undefined,undefined,undefined,undefined,200,20);
        lab61 = function_label("*");
        
        
        // Function when "External" value selected in Combo box2    
        function_Int = function(dsp)
        {
        lab6 = function_label(undefined,lab6,230+dsp+position_top,10);
        txt4 = function_textfield(txt4,230+dsp+position_top,100,undefined,undefined,undefined);
        lab61 = function_label(undefined,lab61,230+dsp+position_top,302);
        
        lab6.setDisplay(true);
        txt4.setDisplay(true);
        lab61.setDisplay(true);
        
        page2.add(lab6,txt4,lab61);
        };
      
        // 5th textfield
        lab7= function_label("Url");
        txt5 = function_textfield(undefined,undefined,undefined,undefined,200,20);
        lab71 = function_label("*");
        
        
        // 6th textfield
        lab8= function_label("Driver Name");
        txt6 = function_textfield(undefined,undefined,undefined,undefined,200,20);
        lab81 = function_label("*");
        
        // Function when "External" value selected in Combo box2
        function_Ext = function (disp)
        {
          lab7 = function_label(undefined,lab7,230+disp+position_top,10);
          txt5 = function_textfield(txt5,230+disp+position_top,100,undefined,undefined,undefined);
          lab71 = function_label(undefined,lab71,230+disp+position_top,302);
         
          lab8 = function_label(undefined,lab8,260+disp+position_top,10);
          txt6 = function_textfield(txt6,260+disp+position_top,100,undefined,undefined,undefined);
          lab81 = function_label(undefined,lab81,260+disp+position_top,302);
          
          lab7.setDisplay(true);
          txt5.setDisplay(true);
          lab71.setDisplay(true);
          lab8.setDisplay(true);
          txt6.setDisplay(true);
          lab81.setDisplay(true);
          
          page2.add(lab7,txt5,lab71,lab8,txt6,lab81);
        };
      
        // varialble used for positioning of various fields
        var ddsp = 0;
        
        // Initial Call when the page loads
        function_Int(ddsp);
        
        // Position of Combo box 3 when event listener of combo box 2 is executed
        function_Int_Ext = function(dsp,e)
        {
          if (e.getValue()=="Internal")
                 {
                    if(lab7.getDisplay() == true)
                      {
                        lab7.setDisplay(false);
                        txt5.setDisplay(false);
                        lab71.setDisplay(false);
                        lab8.setDisplay(false);
                        txt6.setDisplay(false);
                        lab81.setDisplay(false);
                      }
                    function_Int(dsp);
                  }
              else 
                  if (e.getValue()=="External")
                     {
                       if(lab6.getDisplay() == true)
                        {
                          lab6.setDisplay(false);
                          txt4.setDisplay(false);
                          lab61.setDisplay(false);
                        }
                        function_Ext(dsp);
                      }
          };
      
      
        //Combo box event listener for Internal/External
        combo2.addEventListener("changeValue", function(e) 
        {
           var dsp;
          
           if(combo3.getDisplay()== true)
            {
              dsp = 30; 
              function_Int_Ext(dsp,e);
            }
            else
            {
              dsp = 0;
              function_Int_Ext(dsp,e);
            }
        });
      
            
        // 1st check box
        var check1 = function_checkbox(check1,"Use Data Set",170+position_top,10,true);
                          
        // 2nd check box  
        var check2 = function_checkbox(check2,"Use Data Source",200+position_top,10);
        
        // Check box Event Listener   
        check2.addEventListener("changeChecked", function(e)
        {
          if(e.getValue()== true)       // Checkbox checked
          {    
            lab9.setDisplay(true);
            combo3.setDisplay(true);
            page2.add(lab9,combo3);
            var disp = 30;
            
            // Move Class field 1 step down because new Combo box is added above them
            if(lab6.getDisplay() == true)   // class
            {
              lab6 = function_label(undefined,lab6,230+disp+position_top,10);
              txt4 = function_textfield(txt4,230+disp+position_top,100,undefined,undefined,undefined);
              lab61 = function_label(undefined,lab61,230+disp+position_top,302);
              
            }
            
            // Move URL and Driver fields 1 step down because new Combo box is added above them
            else if(lab7.getDisplay() == true)
                  {
                    lab7 = function_label(undefined,lab7,230+disp+position_top,10);
                    txt5 = function_textfield(txt5,230+disp+position_top,100,undefined,undefined,undefined);
                    lab71 = function_label(undefined,lab71,230+disp+position_top,302);
                    
                    lab8 = function_label(undefined,lab8,260+disp+position_top,10);
                    txt6 = function_textfield(txt6,260+disp+position_top,100,undefined,undefined,undefined);
                    lab81 = function_label(undefined,lab81,260+disp+position_top,302);
                    
                  }
          }
        
          else            // checkbox unchecked
          {
            lab9.setDisplay(false);
            combo3.setDisplay(false);
            disp = 0;
            if(lab6.getDisplay() == true)
             {
               lab6 = function_label(undefined,lab6,230+disp+position_top,10);
               txt4 = function_textfield(txt4,230+disp+position_top,100,undefined,undefined,undefined);
               lab61 = function_label(undefined,lab61,230+disp+position_top,302);
               
             } 
           else if(lab7.getDisplay() == true)
                {
                   lab7 = function_label(undefined,lab7,230+disp+position_top,10);
                   txt5 = function_textfield(txt5,230+disp+position_top,100,undefined,undefined,undefined);
                   lab71 = function_label(undefined,lab71,230+disp+position_top,302);
                   
                   lab8 = function_label(undefined,lab8,260+disp+position_top,10);
                   txt6 = function_textfield(txt6,260+disp+position_top,100,undefined,undefined,undefined);
                   lab81 = function_label(undefined,lab81,260+disp+position_top,302);
                }
          }     
        });
        
        // Adding label, combo box, textfield and checkbox to Page
        page2.add(lab1,txt1,lab11,lab2,txt2,lab21,lab3,txt3,lab4,combo1,lab5,combo2,check1,check2);
        
        // ......................... PAGE 2 ENDS ................................. 
        
        // Adding Page to Pane   
        bs.getPane().add(page1,page2);
        
        // Adding Pane to Document    
        d.add(bs);
    },
    
    


    /**
     * TODOC
     *
     * @type member
     */
    close : function()
    {
      this.base(arguments);

      // Prompt user
      // return "Do you really want to close the application?";
    },


    /**
     * TODOC
     *
     * @type member
     */
    terminate : function() {
      this.base(arguments);
    }
  }




  
});